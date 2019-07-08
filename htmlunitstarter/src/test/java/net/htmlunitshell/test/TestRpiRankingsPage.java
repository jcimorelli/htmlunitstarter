package net.wistools.test;

import net.wistools.config.WistoolsConfig;
import net.wistools.config.WistoolsProperties;
import net.wistools.nav.WisPage;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.nav.hd.HdWorld;
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
public class TestRpiRankingsPage {

   @Test
   public void testRpiPage() {
      try (final WistoolsNavigator nav = new WistoolsNavigator( WistoolsProperties.getWisUsername(), WistoolsProperties.getWisPassword() )) {
         nav.login().loadHdWorld( HdWorld.SMITH ).loadPage( WisPage.HD_RpiRankings_Div1 );
         //final HtmlAnchor showMoreAnchor = ( HtmlAnchor )nav.currentPage().getElementById( WisElement.ID_ShowMore ).getFirstElementChild();
         //nav.click( showMoreAnchor, 2000 );

         nav.setInputById( "ctl00_ctl00_ctl00_ctl00_MainContentPlaceHolder_MainContentPlaceHolder_MainContentPlaceHolder_MainContentPlaceHolder_UpdateNumRows", "All" );
         nav.clickElementById( "ctl00_ctl00_ctl00_ctl00_MainContentPlaceHolder_MainContentPlaceHolder_MainContentPlaceHolder_MainContentPlaceHolder_UpdateButton" );

         //final String pageText = ( ( HtmlPage )nav.currentPage().executeJavaScript( "ShowMore();" ).getNewPage() ).asXml();
         ValidateUtl.check( !nav.currentPage().asXml().contains( "Coppin" ) );
         //THIS STILL FAILS and i'm not sure why.  There seems to be no way to get HtmlUnit to recognize the js changes on the page.  We'll have to use the Projection Report for loading team data instead.
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }

   }
}
