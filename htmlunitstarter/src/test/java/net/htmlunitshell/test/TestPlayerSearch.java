package net.wistools.test;

import net.wistools.config.WistoolsConfig;
import net.wistools.config.WistoolsProperties;
import net.wistools.nav.WisPage;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.nav.hbd.HbdPlayerSearcher;
import net.wistools.nav.hbd.HbdWorld;
import net.wistools.utl.FileUtl;
import net.wistools.utl.ValidateUtl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WistoolsConfig.class)
@Configurable
public class TestPlayerSearch {

   @Test
   public void testPlayerSearch() {
      try (final WistoolsNavigator nav = new WistoolsNavigator( WistoolsProperties.getWisUsername(), WistoolsProperties.getWisPassword() )) {
         nav.login().loadHbdWorld( HbdWorld.CLEMENTE ).loadPage( WisPage.HBD_PlayerSearch );
         final HtmlPage resultPage = new HbdPlayerSearcher().performSearch( nav, "All", "Portland Jackals" );
         FileUtl.printPageToFile( resultPage, "src/test/resources/PlayerSearchResult.html" );
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
   }

}
