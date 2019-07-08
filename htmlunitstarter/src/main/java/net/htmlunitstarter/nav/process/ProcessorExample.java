package net.htmlunitstarter.nav.process;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import net.htmlunitstarter.config.HtmlUnitStarterProperties;
import net.htmlunitstarter.nav.WebsiteNavigator;
import net.htmlunitstarter.nav.WebsitePage;
import net.htmlunitstarter.utl.ValidateUtl;

public class ProcessorExample {

   public void process() {
      try (final WebsiteNavigator nav = new WebsiteNavigator( HtmlUnitStarterProperties.getSiteUsername(), HtmlUnitStarterProperties.getSitePassword() )) {
         final HtmlPage page = nav.login().loadPage( WebsitePage.SomeOtherPage ).currentPage();
         //scrape page and do stuff
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
   }


}
