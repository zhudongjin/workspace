create database if not exists com_db;
use com_db;

CREATE TABLE t_billno_appid_config(	
	Fappid			varchar(10)		COMMENT '应用id',
	Fstart_no 		bigint				COMMENT '开始自增序号',
	Fmemo varchar(64)  	COMMENT '备注',
  PRIMARY KEY (`Fappid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
