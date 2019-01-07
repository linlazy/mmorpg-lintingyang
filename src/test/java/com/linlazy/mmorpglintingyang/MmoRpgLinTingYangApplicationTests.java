package com.linlazy.mmorpglintingyang;

import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MmoRpgLinTingYangApplicationTests {


	@Test
	public void contextLoads() {
		ConfigurableApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
		assert  applicationContext != null;
	}

}
