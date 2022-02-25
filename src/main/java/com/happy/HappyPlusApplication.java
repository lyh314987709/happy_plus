package com.happy;

import com.alibaba.fastjson.JSONObject;
import com.happy.domain.SharesDaily;
import com.happy.service.SharesDailyService;
import com.happy.syn.SynchronizationService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@MapperScan("com.happy.dao")
@EnableCaching
@SpringBootApplication
public class HappyPlusApplication {

	public static void main(String[] args) {
		
		ConfigurableApplicationContext run = SpringApplication.run(HappyPlusApplication.class, args);
		SynchronizationService synchronizationService = run.getBean(SynchronizationService.class);
		//synchronizationService.synchronizationShares();
		//synchronizationService.relation();

		SharesDailyService sharesDailyService = run.getBean(SharesDailyService.class);

		List<SharesDaily> synchronization = sharesDailyService.synchronization(SharesDailyService.SharesDailyReq.builder()
				.trade_date("20220223")
				.build());

		System.out.println(JSONObject.toJSONString(synchronization));

	}
}
