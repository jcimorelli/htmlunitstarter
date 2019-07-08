package net.htmlunitstarter.utl;

import java.util.Date;

public class LoggingUtl {

   public static final String Format_LogTimestamp = "MM-dd HH:mm:ss";
   public static final String LOG_PREFIX = "CUSTM";//Should be 5 chars to line up with "WARN ", "ERROR", "INFO ", etc.

   public static void log( Object obj ) {
      log( String.valueOf( obj ) );
   }

   public static void log( String message ) {
      System.out.println( toLogFormat( message ) );
   }

   public static String toLogFormat( String message ) {
      final String timestamp = DateUtl.formatDate( new Date(), Format_LogTimestamp );
      return LOG_PREFIX + " " + timestamp + " " + message;
   }
}
