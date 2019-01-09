package com.linlazy.mmorpglintingyang;

import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
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
//		UserEntity user = new UserEntity();
//		user.setOperatorType(EntityOperatorType.INSERT);
//		user.setActorId(4194306);
//		user.setUsername("linlazy");
//		UserEntityDao userEntityDao = app.getBean(UserEntityDao.class);
//		userEntityDao.updateQueue(user);

	}
}
