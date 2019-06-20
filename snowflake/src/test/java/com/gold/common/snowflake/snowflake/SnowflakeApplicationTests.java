package com.gold.common.snowflake.snowflake;

import com.gold.common.snowflake.SnowflakeIdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SnowflakeApplicationTests {

	@Test
	void contextLoads() {
		System.out.println(System.currentTimeMillis());
		long startTime = System.nanoTime();
		for (int i = 0; i < 50000; i++) {
			long id = SnowflakeIdWorker.generateId();
			System.out.println(id);
		}
		System.out.println((System.nanoTime()-startTime)/1000000+"ms");

	}

}
