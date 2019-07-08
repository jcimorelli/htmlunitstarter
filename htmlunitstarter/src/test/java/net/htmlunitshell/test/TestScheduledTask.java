package net.wistools.test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.wistools.config.WistoolsConfig;
import net.wistools.utl.LoggingUtl;

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
public class TestScheduledTask {

   @Test
   public void testScheduledTask() {
      ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 1 );
      scheduler.scheduleAtFixedRate( new CountingTask(), 5, 5, TimeUnit.SECONDS );
      try {
         Thread.sleep( 50000 );
      }
      catch( InterruptedException e ) {
         e.printStackTrace();
      }
   }

   private static class CountingTask implements Runnable {
      private static int count = 1;

      @Override
      public void run() {
         LoggingUtl.log( count++ );
      }
   }
}
