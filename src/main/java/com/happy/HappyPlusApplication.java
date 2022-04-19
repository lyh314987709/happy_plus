package com.happy;

import com.happy.service.SharesDailyService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@MapperScan("com.happy.dao")
@EnableCaching
@SpringBootApplication
public class HappyPlusApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(HappyPlusApplication.class, args);
		//SynchronizationService synchronizationService = run.getBean(SynchronizationService.class);
		//synchronizationService.synchronizationShares();
		//synchronizationService.relation();

		SharesDailyService sharesDailyService = run.getBean(SharesDailyService.class);

		sharesDailyService.synchronization(SharesDailyService.SharesDailyReq.builder().trade_date("20220418").build());

		//sharesDailyService.isStop(LocalDateTime.of(2022, 02, 25, 23, 59, 59));
		//System.out.println("isStop出来完成");

		//sharesDailyService.onceStop(LocalDateTime.of(2022, 02, 25, 23, 59, 59));
		//System.out.println("onceStop处理完成");
	}
}
