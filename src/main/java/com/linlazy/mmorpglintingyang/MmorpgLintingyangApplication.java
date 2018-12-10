package com.linlazy.mmorpglintingyang;

import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MmorpgLintingyangApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext app = SpringApplication.run(MmorpgLintingyangApplication.class, args);
		SpringContextUtil.setApplicationContext(app);

	}
}
