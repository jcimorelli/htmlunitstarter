package net.htmlunitstarter.schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HtmlUnitStarterScheduler {

   private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 1 );

   public HtmlUnitStarterScheduler() {
      scheduleAtFixedRate();
      scheduleAtFixedTimes();
   }

   private void scheduleAtFixedRate() {
	   scheduler.scheduleAtFixedRate( new SomeTask(), 10, 30, TimeUnit.MINUTES );
   }

   private void scheduleAtFixedTimes() {
      final LocalDateTime localNow = LocalDateTime.now();
      final ZonedDateTime now = ZonedDateTime.of( localNow, ZoneId.of( "America/New_York" ) );
      final ZonedDateTime today5AM = now.withHour( 5 ).withMinute( 05 ).withSecond( 0 );
      final ZonedDateTime today11AM = now.withHour( 11 ).withMinute( 05 ).withSecond( 0 );
      final ZonedDateTime today5PM = now.withHour( 17 ).withMinute( 05 ).withSecond( 0 );
      final ZonedDateTime today11PM = now.withHour( 23 ).withMinute( 05 ).withSecond( 0 );
      ZonedDateTime startTime;
      if( now.compareTo( today11PM ) > 0 ) {
         startTime = today5AM.plusDays( 1 );
      }
      else if( now.compareTo( today5PM ) > 0 ) {
         startTime = today11PM;
      }
      else if( now.compareTo( today11AM ) > 0 ) {
         startTime = today5PM;
      }
      else if( now.compareTo( today5AM ) > 0 ) {
         startTime = today11AM;
      }
      else {
         startTime = today5AM;
      }
      final Duration durationTilStart = Duration.between( now, startTime );
      final long initalDelay = durationTilStart.getSeconds();
      scheduler.scheduleAtFixedRate( new SomeTask(), initalDelay, 6 * 60 * 60, TimeUnit.SECONDS );
   }
}
