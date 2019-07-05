package com.gold.common;

import com.gold.common.zookeepers.ZkClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling

public class SnowflakeApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SnowflakeApplication.class, args);
		//加载zookeeper
		ZkClient client =context.getBean(ZkClient.class);
		client.register();
	}

}
