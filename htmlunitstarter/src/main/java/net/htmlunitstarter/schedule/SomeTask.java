package net.htmlunitstarter.schedule;

import net.htmlunitstarter.utl.LoggingUtl;

public class SomeTask implements Runnable {

   @Override
   public void run() {
	  LoggingUtl.log("\"Some Task\" is processing...");
      //do something
   }
}
