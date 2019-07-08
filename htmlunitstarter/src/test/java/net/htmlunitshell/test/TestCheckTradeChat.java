package net.wistools.test;

import net.wistools.config.WistoolsConfig;
import net.wistools.schedule.TradeChatCheckTask;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WistoolsConfig.class)
@Configurable
public class TestCheckTradeChat {

   @Test
   public void testCheckTradeChat() {
      new TradeChatCheckTask().run();
   }

}
