package com.zcb.billno.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.zcb.billno.service.dao.BillnoAppidDAO;
import com.zcb.billno.service.dao.model.BillnoAppidDO;
import com.zcb.billno.service.dto.BillNo;
import com.zcb.billno.service.dto.BillnoInput;
import com.zcb.billno.service.exception.BillnoError;
import com.zcb.billno.service.exception.BillnoSvcException;
import com.zcb.billno.service.facade.BillnoFacade;
import com.zcb.billno.service.type.ListidPattern;

//import dbox.slite.config.AppConfig;

/**
 * 生成billno实现类
 * 
 * @author zhl
 */
@Service
public class BillnoFacadeImpl implements BillnoFacade, InitializingBean {
    /**
     * 自增序号为10位，前面部分为机器编号，后面为自增的序列值
     * 最大值小于5亿，因此给前面留的机器编号的数量为50个（第10位为0～9），第9位为5~9
     */
    private static final int MAX_SEQ_NO = 500000000;
    private static int BUFFER_SIZE = 1000;

    /***
     * 如果正式使用，请开启配置文件
     */
    /*@Resource
    private AppConfig appConfig;*/

    @Resource
    private BillnoAppidDAO billnoAppidDAO;

    private static Map<String, Integer> curSeqNoMap;
    private static Map<String, Integer> maxSeqNoMap;
    private static Map<String, Integer> startNoMap;
    private static String macNo;
    private static final String SEQ_DATE_FORMAT = "yyyyMMdd";

    private static Map<String, Lock> appLock = new HashMap<String, Lock>();// appid锁

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 生成billno
     */
    public BillNo genBillNo(BillnoInput billInfo) {
        BillNo result = new BillNo();
        try {
            // 检查参数
            checkParam(billInfo);

            // 获取当前10位序号
            String seqNo = getSeqNo(billInfo.getAppid());

            result.setSeqNo(seqNo);
            result.setMacNo(macNo);
            // 按规则生成单号
            String billno = formatBillNo(billInfo, result);
            result.setBillNo(billno);
            logger.debug("appid:" + billInfo.getAppid() + ",billno:" + billno);

            return result;
        } catch (ErrorCodeException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Throwable t) {
            logger.error(t.getMessage());
            throw new BillnoSvcException(BillnoError.SYSTEM_ERROR, t);
        }
    }
    
    /**
     * 格式化订单号
     * @param billInfo
     * @param result
     * @return
     * @throws Exception
     */
    private String formatBillNo(BillnoInput billInfo, BillNo result) throws Exception {
        StringBuilder sb = new StringBuilder(32);
        if (StringUtils.isNotBlank(billInfo.getPrefix()))
            sb.append(billInfo.getPrefix());

        switch (billInfo.getPattern()) {
        case ListidPattern.TYPE_SPID_DATE_SEQNO:
            sb.append(billInfo.getListType());
            sb.append(billInfo.getSpid());
            String curDate = DataFormatter.formatDate(new Date(), StringUtils.isBlank(billInfo
                    .getDateFormat()) ? SEQ_DATE_FORMAT : billInfo.getDateFormat());
            sb.append(curDate);
            break;
        case ListidPattern.SPID_DATE_SEQNO:
            sb.append(billInfo.getSpid());
            curDate = DataFormatter.formatDate(new Date(), StringUtils.isBlank(billInfo
                    .getDateFormat()) ? SEQ_DATE_FORMAT : billInfo.getDateFormat());
            sb.append(curDate);
            break;
        case ListidPattern.TYPE_DATE_SEQNO:
            sb.append(billInfo.getListType());
            curDate = DataFormatter.formatDate(new Date(), StringUtils.isBlank(billInfo
                    .getDateFormat()) ? SEQ_DATE_FORMAT : billInfo.getDateFormat());
            sb.append(curDate);
            break;
        case ListidPattern.DATE_SEQNO:
            curDate = DataFormatter.formatDate(new Date(), StringUtils.isBlank(billInfo
                    .getDateFormat()) ? SEQ_DATE_FORMAT : billInfo.getDateFormat());
            sb.append(curDate);
            break;
        case ListidPattern.TYPE_SEQNO:
            sb.append(billInfo.getListType());
            break;
        case ListidPattern.SPID_SEQNO:
            sb.append(billInfo.getSpid());
            break;
        case ListidPattern.SEQNO:
            break;
        default:
            break;
        }

        sb.append(result.getMacNo());

        if (StringUtils.isNotBlank(billInfo.getSuffix())) {
            if (billInfo.isPlaceSeqno()) {
                int suffixLen = billInfo.getSuffix().length();
                sb.append(result.getSeqNo().substring(suffixLen - 1));
            } else {
                sb.append(result.getSeqNo());
            }
            sb.append(billInfo.getSuffix());
        } else {
            sb.append(result.getSeqNo());
        }

        return sb.toString();
    }

    /**
     * 获取当前10位序号,1位机器号+9位自增序号,线程锁定保证准确性
     * 
     * @param appid
     * @return
     * @throws Exception
     */
    private String getSeqNo(String appid) throws Exception {
        lock(appid);
        try {
            // 先从缓冲列表中获取序号
            Integer seqNo = getSeqNoFromBuffer(appid);

            if (seqNo == null || seqNo < 0) {
                seqNo = preBatchGetSeqNo(appid);
            }
            
            //超过最大值时，重置序号
            if (seqNo >= MAX_SEQ_NO) {
                seqNo = resetSeqNo(appid);
            }
            
            //此处如果还超过最大值则无法处理了
            if (seqNo >= MAX_SEQ_NO) {
                throw new BillnoSvcException(BillnoError.SEQ_LIMIT_ERROR, "最大序号不能大于等于5亿");
            }

            return formatSeqNo(seqNo);
        } catch (Exception e) {
            throw new BillnoSvcException(BillnoError.SYSTEM_ERROR, "生成单号服务发生系统错误：" + e.getMessage());
        } finally {
            unlock(appid);
        }
    }

    /**
     * 从当前缓冲列表中获取seq no
     * 
     * @param appid
     * @return <0表示无法从当前缓冲列表中获取，>0表示获取的seq no值
     */
    private int getSeqNoFromBuffer(String appid) {
        if (null != curSeqNoMap && null != maxSeqNoMap) {
            Integer curSeqNo = curSeqNoMap.get(appid);
            Integer maxSeqNo = maxSeqNoMap.get(appid);

            // 当前应用序号已经生成 如果大于最大序号则重新生成
            if (null != curSeqNo && null != maxSeqNo) {
                curSeqNo += 1;
                if (curSeqNo <= maxSeqNo) {
                    curSeqNoMap.put(appid, curSeqNo);
                    return curSeqNo.intValue();
                }
            }
        }

        return -1;
    }

    /**
     * 更新文件最大序号
     * 
     * @param appId
     * @throws IOException
     */
    private int preBatchGetSeqNo(String appId) throws IOException {
        // 如果缓存列表不存在，则创建
        if (curSeqNoMap == null || maxSeqNoMap == null) {
            curSeqNoMap = new HashMap<String, Integer>();
            maxSeqNoMap = new HashMap<String, Integer>();
        }

        String appIdFilePath ="/usr/local/tinnfy/dev/tomcat7.0/appiddata";// appConfig.get("billno.dataPath");// 从配置中取文件路径
        String appIdFileName = new StringBuffer(appIdFilePath).append("/appid.").append(appId)
                .append(".seq").toString();

        File appIdFile = new File(appIdFileName);

        int maxSeqNo = 0;
        int curSeqNo = 0;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            if (!appIdFile.exists()) {
                // 文件不存在，表示第一次运行
                File fileDir = appIdFile.getParentFile();
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                // 先从配置的appid源获得初始的序号
                curSeqNo = startNoMap.get(appId);
                // 当前可用的最大序号数目为BUFFER_SIZE，因此减1保证数目为BUFFER_SIZE
                // 例如初始序号为1,BUFFER_SIZE=100，则当前最大序号为100
                maxSeqNo = startNoMap.get(appId) + BUFFER_SIZE - 1;
            } else {
                // 从文件中获取上次使用的最大序号值
                br = new BufferedReader(new FileReader(appIdFile));
                // 当前序号为上次使用的序号+1
                curSeqNo = Integer.valueOf(StringUtils.trimToEmpty(br.readLine())) + 1;
                maxSeqNo = curSeqNo + BUFFER_SIZE - 1;
            }
            
            //当序号超过最大值时重新开计数
            if (curSeqNo >= MAX_SEQ_NO) {
                curSeqNo = 0;
                maxSeqNo = curSeqNo + BUFFER_SIZE - 1;
            }
            
            //当当前最大SeqNo超过最大值时，只能取最大值
            if (maxSeqNo >= MAX_SEQ_NO) {
                maxSeqNo = MAX_SEQ_NO-1;
            }
            
            // 保存当前缓冲的最大序号到文件
            bw = new BufferedWriter(new FileWriter(appIdFile));
            bw.write(String.valueOf(maxSeqNo));
        } catch (IOException e) {
            throw new BillnoSvcException(BillnoError.SYSTEM_ERROR, "获取单号失败");
        } finally {
            if (br != null) {
                br.close();
            }
            if (bw != null) {
                bw.close();
            }
        }

        curSeqNoMap.put(appId, curSeqNo);
        maxSeqNoMap.put(appId, maxSeqNo);

        return curSeqNo;
    }
    
    private int resetSeqNo(String appId) throws IOException {
        logger.info("seq no is greater max no, reset seq no for appid:", appId);
        
        // 如果缓存列表不存在，则创建
        if (curSeqNoMap == null || maxSeqNoMap == null) {
            curSeqNoMap = new HashMap<String, Integer>();
            maxSeqNoMap = new HashMap<String, Integer>();
        }

        String appIdFilePath ="/usr/local/tinnfy/dev/tomcat7.0/appiddata"; //appConfig.get("billno.dataPath");// 从配置中取文件路径
        String appIdFileName = new StringBuffer(appIdFilePath).append("/appid.").append(appId)
                .append(".seq").toString();

        File appIdFile = new File(appIdFileName);

        int maxSeqNo = 0;
        int curSeqNo = 0;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            if (!appIdFile.exists()) {
                // 文件不存在，表示第一次运行
                File fileDir = appIdFile.getParentFile();
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }                
            } 
            
            //当序号超过最大值时重新开计数
            curSeqNo = 0;
            maxSeqNo = curSeqNo + BUFFER_SIZE - 1;
            
            //当当前最大SeqNo超过最大值时，只能取最大值
            if (maxSeqNo >= MAX_SEQ_NO) {
                maxSeqNo = MAX_SEQ_NO-1;
            }
            
            // 保存当前缓冲的最大序号到文件
            bw = new BufferedWriter(new FileWriter(appIdFile));
            bw.write(String.valueOf(maxSeqNo));
        } catch (IOException e) {
            throw new BillnoSvcException(BillnoError.SYSTEM_ERROR, "重置序号失败");
        } finally {
            if (br != null) {
                br.close();
            }
            if (bw != null) {
                bw.close();
            }
        }

        curSeqNoMap.put(appId, curSeqNo);
        maxSeqNoMap.put(appId, maxSeqNo);

        return curSeqNo;
    }

    /**
     * 检查参数
     * 
     * @param billInfo
     */
    private void checkParam(BillnoInput billInfo) {
        String listType = billInfo.getListType();
        String spId = billInfo.getSpid();
        String appId = billInfo.getAppid();

        if (StringUtils.isBlank(listType)) {
            throw new BillnoSvcException(BillnoError.PARAMETER_INVALID, "类型不能为空");
        }

        // 长度必须是3位
        if (listType.length() != 3) {
            throw new BillnoSvcException(BillnoError.PARAMETER_INVALID, "订单类型格式不合法");
        }

        if (StringUtils.isBlank(appId)) {
            throw new BillnoSvcException(BillnoError.PARAMETER_INVALID, "appid不能为空");
        }

        if (StringUtils.isBlank(spId) || spId.length() < 10) {
            throw new BillnoSvcException(BillnoError.PARAMETER_INVALID, "商户号不合法");
        }

        // 后缀长度不能超过3位
        if (StringUtils.isNotBlank(billInfo.getSuffix()) && billInfo.getSuffix().length() > 3) {
            throw new BillnoSvcException(BillnoError.PARAMETER_INVALID,
                    "suffix length greater than 3");
        }

        // 效验appid,并获取对应的appid起始自增序号
        if (startNoMap == null || !startNoMap.containsKey(appId)) {
            BillnoAppidDO appIdInfo = billnoAppidDAO.queryBillnoAppid(appId);
            if (null == appIdInfo) {
                throw new BillnoSvcException(BillnoError.PARAMETER_INVALID, "应用ID不合法");
            }

            if (null == startNoMap) {
                startNoMap = new HashMap<String, Integer>();
            }

            startNoMap.put(appId, appIdInfo.getStartNo());
        }
    }

    /**
     * 格式化9位自增序号,少于9位前面自动补0
     * 
     * @param seqNo
     * @return
     */
    private String formatSeqNo(Integer seqNo) {
        return String.format("%09d", seqNo);
    }

    public Lock getAppLock(String appid) {
        Lock lock = appLock.get(appid);
        if (null == lock) {
            lock = setAppLock(appid);
        }
        return lock;
    }

    public synchronized Lock setAppLock(String appid) {
        Lock lock = appLock.get(appid);
        if (lock == null) {
            lock = new ReentrantLock();
            appLock.put(appid, lock);
        }
        return lock;
    }

    private void lock(String appid) {
        getAppLock(appid).lock();
    }

    private void unlock(String appid) {
        getAppLock(appid).unlock();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 从配置中取1位机器号，最多支持10台机器
        if (macNo == null) {
            macNo ="1"; //appConfig.get("billno.macNo");
            if (null == macNo || macNo.length() != 1 || !"0123456789".contains(macNo)) {
                throw new BillnoSvcException(BillnoError.SYSTEM_ERROR, "机器号配置错误");
            }
        }
        
        //获取buffsize的配置
        String confBuffSize ="2000";// appConfig.get("billno.buffsize");
        if (StringUtils.isNumeric(confBuffSize)) {
            int buffsize = Integer.parseInt(confBuffSize);
            if (buffsize>500)
                BUFFER_SIZE = buffsize;
        }
    }

}
