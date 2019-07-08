package net.htmlunitstarter.config;

import net.htmlunitstarter.utl.ValidateUtl;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.env.Environment;

@Configurable
public class HtmlUnitStarterFileCache {

   @Autowired
   private Environment environment;

   private static final HtmlUnitStarterFileCache instance = new HtmlUnitStarterFileCache();

   public static String getLastLoginToken() {
      return instance.environment.getProperty( "lastLoginToken" );
   }

   public static void setLastLoginToken( String token ) {
      setValue( "lastLoginToken", token );
   }

   public static void setValue( String key, String value ) {
      try {
         final PropertiesConfiguration fileCache = new PropertiesConfiguration( HtmlUnitStarterFileCache.class.getResource( "/filecache.properties" ) );
         fileCache.setProperty( key, value );
         fileCache.save();
      }
      catch( ConfigurationException e ) {
         ValidateUtl.fail( e );
      }
   }
}