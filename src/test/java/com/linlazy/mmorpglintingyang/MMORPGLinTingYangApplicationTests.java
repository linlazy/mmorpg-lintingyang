package com.linlazy.mmorpglintingyang;

import com.google.common.eventbus.EventBus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MMORPGLinTingYangApplicationTests {

	@Test
	public void contextLoads() {

		EventBus eventBus = new EventBus("sync");
		
	}

}