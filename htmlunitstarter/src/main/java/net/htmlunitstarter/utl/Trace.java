package net.htmlunitstarter.utl;

import org.apache.commons.lang.StringEscapeUtils;

public class Trace {
   private StringBuilder trace = new StringBuilder();
   private StringBuilder debug = new StringBuilder( " ******* START DEBUG ******* \n" );

   public void write( String text ) {
      write( text, 0 );
   }

   public void write( String text, int depth ) {
      for( int i = 0; i < depth; i++ ) {
         trace.append( "\t" );
      }
      trace.append( text ).append( "\n" );
   }

   public void debug( String text ) {
      debug( text, 0 );
   }

   public void debug( String text, int depth ) {
      for( int i = 0; i < depth; i++ ) {
         debug.append( "\t" );
      }
      debug.append( text ).append( "\n" );
   }

   @Override
   public String toString() {
      return trace.toString();
   }

   public String debug() {
      debug.append( " ******** END DEBUG ******** " );
      return debug.toString();
   }

   public Object getJSEscaped() {
      return StringEscapeUtils.escapeJavaScript( trace.toString() );
   }
}
