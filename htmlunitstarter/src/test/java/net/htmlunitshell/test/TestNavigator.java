package net.wistools.test;

import net.wistools.config.WistoolsConfig;
import net.wistools.config.WistoolsProperties;
import net.wistools.nav.WisPage;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.nav.hd.HdWorld;
import net.wistools.utl.FileUtl;
import net.wistools.utl.ValidateUtl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WistoolsConfig.class)
@Configurable
public class TestNavigator {

   @Test
   public void testNavigator() {
      try (final WistoolsNavigator nav = new WistoolsNavigator( WistoolsProperties.getWisUsername(), WistoolsProperties.getWisPassword() )) {
         nav.login().loadHdWorld( HdWorld.SMITH ).loadPage( WisPage.HD_RecruitingHome );
         FileUtl.printPageToFile( nav.currentPage(), "src/test/resources/test-navigator.html" );
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
   }
}
