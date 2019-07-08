package net.wistools.test;

import net.wistools.config.WistoolsConfig;
import net.wistools.config.WistoolsProperties;
import net.wistools.nav.WisPage;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.nav.hbd.HbdWorld;
import net.wistools.utl.FileUtl;
import net.wistools.utl.ValidateUtl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gargoylesoftware.htmlunit.html.DomElement;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WistoolsConfig.class)
@Configurable
public class TestIfaSigningsPageParsing {

   private static final String ELEMENT_ID_GoButton = "ctl00_ctl00_ctl00_Main_PageOptionsPlaceHolder_PageOptionsPlaceHolder_GoButton_InternalGoButton";
   private static final String ELEMENT_ID_FirstIfaProspectRow = "ctl00_ctl00_ctl00_Main_mcPH_mcPH_IntSignRepeater_ctl01_DataRow";
   private static final String ELEMENT_ID_SeasonCombo = "ctl00_ctl00_ctl00_Main_PageOptionsPlaceHolder_PageOptionsPlaceHolder_SeasonDropDown_SeasonDropDown";
   private static final String ELEMENT_ID_FranchiseCombo = "ctl00_ctl00_ctl00_Main_PageOptionsPlaceHolder_PageOptionsPlaceHolder_FranchiseDropDown_FranchiseDropDown";

   @Test
   public void testIfa() {
      try (final WistoolsNavigator nav = new WistoolsNavigator( WistoolsProperties.getWisUsername(), WistoolsProperties.getWisPassword() )) {
         nav.login().loadHbdWorld( HbdWorld.CLEMENTE )
               .loadPage( WisPage.HBD_InternationalSignings )
               .setComboByText( ELEMENT_ID_SeasonCombo, "45" )
               .setComboByText( ELEMENT_ID_FranchiseCombo, "Portland Jackals" )
               .clickElementById( ELEMENT_ID_GoButton );
         FileUtl.printPageToFile( nav.currentPage(), "src/test/resources/test-ifa.html" );
         final DomElement firstIfaRow = nav.currentPage().getElementById( ELEMENT_ID_FirstIfaProspectRow );
         ValidateUtl.nullCheck( firstIfaRow, "Didn't see any signed ifa prospects" );
         "".length();
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
   }
}