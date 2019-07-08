package net.htmlunitstarter.utl;

import java.util.Collection;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author cimorelli
 */
public abstract class ValidateUtl {

   public static void check( boolean expression ) {
      if( expression ) {
         fail( "Default Check Error" );
      }
   }

   public static void check( boolean expression, String errorIfTrue ) {
      if( expression ) {
         fail( errorIfTrue );
      }
   }

   public static void nullCheck( Object obj, String errorIfNull ) {
      if( obj == null ) {
         fail( errorIfNull );
      }
   }

   public static void emptyCheck( Collection<? extends Object> collection, String errorIfEmpty ) {
      if( collection == null || collection.size() == 0 ) {
         fail( errorIfEmpty );
      }
   }

   public static void emptyCheck( Map<String, Object> map, String errorIfEmpty ) {
      if( map == null || map.size() == 0 ) {
         fail( errorIfEmpty );
      }
   }

   public static void blankCheck( String string, String errorIfBlank ) {
      if( string == null || string.equals( "" ) ) {
         fail( errorIfBlank );
      }
   }

   public static void fail( String error ) {
      throw new ValidateException( error );
   }

   public static void fail( Exception exception ) {
      exception.printStackTrace();
      throw new ValidateException( exception );
   }

   public static void fail( String error, Exception exception ) {
      exception.printStackTrace();
      throw new ValidateException( error, exception );
   }

   // Used to determine if the exception was thrown by this class or not
   public static class ValidateException extends RuntimeException {
      private static final long serialVersionUID = -105085098789065782L;

      public ValidateException( String error ) {
         super( LoggingUtl.toLogFormat( error ) );
      }

      public ValidateException( Exception exception ) {
         super( exception );
      }

      public ValidateException( String error, Exception exception ) {
         super( LoggingUtl.toLogFormat( error ), exception );
      }
   }

   public static class PageLoadException extends ValidateException {
      private static final long serialVersionUID = 8746672536725151239L;

      public PageLoadException( HtmlPage currentPage, String url ) {
         super( LoggingUtl.toLogFormat( "There was an error returned by the website while attempting to load the page: " + url ) );
         LoggingUtl.log( currentPage.asXml() );
      }

      public PageLoadException( String error, PageLoadException pageLoadException ) {
         super( error, pageLoadException );
      }
   }

   public static void pageLoadFail( String error, PageLoadException pageLoadException ) {
      throw new PageLoadException( error, pageLoadException );
   }

   public static void pageLoadFail( HtmlPage currentPage, String url ) {
      throw new PageLoadException( currentPage, url );
   }
}
