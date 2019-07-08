package net.htmlunitstarter.schedule;

import net.htmlunitstarter.utl.LoggingUtl;

public class TimerTask implements Runnable {
   private Integer secondsInterval;
   private Integer secondsCount = 0;

   public TimerTask( Integer secondsInterval ) {
      this.secondsInterval = secondsInterval;
   }

   @Override
   public void run() {
      secondsCount += secondsInterval;

      long longVal = secondsCount.longValue();
      int hours = ( int )longVal / 3600;
      int remainder = ( int )longVal - hours * 3600;
      int mins = remainder / 60;
      remainder = remainder - mins * 60;
      int secs = remainder;

      LoggingUtl.log( "TIMER: "
            + String.valueOf( hours ) + " hr, "
            + String.valueOf( mins ) + " min, "
            + String.valueOf( secs ) + " sec" );
   }
}
