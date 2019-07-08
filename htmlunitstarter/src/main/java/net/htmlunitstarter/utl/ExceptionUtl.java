package net.htmlunitstarter.utl;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtl {

   public static String getStackTraceAsString( Exception exception ) {
      StringWriter errors = new StringWriter();
      exception.printStackTrace( new PrintWriter( errors ) );
      return errors.toString();
   }

}
