package com.linlazy.mmorpg;

import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author linlazy
 */
@SpringBootApplication
public class MmoRpgLinTingYangApplication {


	public static void main(String[] args) {
		ConfigurableApplicationContext app = SpringApplication.run(MmoRpgLinTingYangApplication.class, args);
		SpringContextUtil.setApplicationContext(app);

	}
}
