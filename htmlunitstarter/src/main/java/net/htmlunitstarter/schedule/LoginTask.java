package net.wistools.schedule;

import net.wistools.config.WistoolsProperties;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.utl.LoggingUtl;

//Just for test
@Deprecated
public class LoginTask implements Runnable {

   @Override
   public void run() {
      LoggingUtl.log( "about to connect..." );
      try (final WistoolsNavigator nav = new WistoolsNavigator( WistoolsProperties.getWisUsername(), WistoolsProperties.getWisPassword() )) {
         LoggingUtl.log( "logging in..." );
         nav.login();
         LoggingUtl.log( nav.currentPage().asText() );
      }
   }
}
