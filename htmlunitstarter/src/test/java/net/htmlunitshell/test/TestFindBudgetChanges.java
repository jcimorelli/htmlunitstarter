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

import com.gargoylesoftware.htmlunit.html.HtmlPage;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WistoolsConfig.class)
@Configurable
public class TestFindBudgetChanges {

   private static final String DEBUG_FILE_PATH = "src/test/resources/";

   @Test
   public void testFindBudgetChanges() {
      try (final WistoolsNavigator nav = new WistoolsNavigator( WistoolsProperties.getWisUsername(), WistoolsProperties.getWisPassword() )) {
         final HtmlPage hbdBudgetAnalysisPage = nav.login().loadHbdWorld( HbdWorld.CLEMENTE ).loadPage( WisPage.HBD_BudgetAnalysis ).currentPage();

         final String oldFileName = DEBUG_FILE_PATH + "hbd-budget-analysis-debug_OLD.html";
         final String newFileName = DEBUG_FILE_PATH + "hbd-budget-analysis-debug_NEW.html";

         FileUtl.copyFile( newFileName, oldFileName );
         FileUtl.printPageToFile( hbdBudgetAnalysisPage, newFileName );
         @SuppressWarnings("unused")
         final Boolean filesMatch = FileUtl.compareFiles( oldFileName, newFileName );
         "".length();
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
   }

}
