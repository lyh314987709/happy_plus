package com.happy;

import com.happy.util.LocalDateTimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class HappyPlusApplicationTests {

	public static void main(String[] args) {
		String text = "20220228";

		System.out.println(LocalDateTimeUtil.parseToLocalDateToLocalDateTime(text));
	}

}
