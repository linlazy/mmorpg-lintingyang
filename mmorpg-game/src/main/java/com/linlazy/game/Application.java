package com.linlazy.game;

import com.linlazy.game.server.ServerBoot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class Application  implements ApplicationListener {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		ServerBoot.start();
	}
}
