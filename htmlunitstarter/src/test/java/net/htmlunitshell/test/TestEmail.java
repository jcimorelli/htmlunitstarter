package net.htmlunitstarter.test;

import net.htmlunitstarter.config.HtmlUnitStarterConfig;
import net.htmlunitstarter.schedule.EmailTask;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HtmlUnitStarterConfig.class)
@Configurable
@SuppressWarnings("deprecation")
public class TestEmail {

   @Test
   public void testEmail() {
      new EmailTask( "TEST Subject", "TEST Body", "person@email.com" ).run();
   }
}