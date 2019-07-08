package net.htmlunitstarter.nav;

import java.net.URL;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WaitingRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

import net.htmlunitstarter.config.HtmlUnitStarterFileCache;
import net.htmlunitstarter.utl.LoggingUtl;
import net.htmlunitstarter.utl.ValidateUtl;

public class WebsiteNavigator implements AutoCloseable {

   private final WebClient webClient;
   private HtmlPage currentPage;

   private String username;
   private String password;

   public WebsiteNavigator( String username, String password ) {
      this( username, password, BrowserVersion.FIREFOX_52 );
   }

   public WebsiteNavigator( String username, String password, BrowserVersion browserVersion ) {
      this.username = username;
      this.password = password;
      webClient = createWebClient( browserVersion );
   }

   @Override
   public void close() {
      webClient.close();
   }

   public HtmlPage currentPage() {
      return currentPage;
   }

   private WebClient createWebClient( BrowserVersion browserVersion ) {
      final WebClient webClient = new WebClient( browserVersion );
      webClient.getOptions().setThrowExceptionOnScriptError( false );
      webClient.getOptions().setThrowExceptionOnFailingStatusCode( false );
      webClient.getOptions().setPrintContentOnFailingStatusCode( false );
      webClient.getOptions().setAppletEnabled( true );
      webClient.getOptions().setCssEnabled( false );
      webClient.addRequestHeader( "Access-Control-Allow-Origin", "*" );
      webClient.setHTMLParserListener( new CustomHtmlParserListener() );
      webClient.setRefreshHandler( new WaitingRefreshHandler( 5 ) );
      return webClient;
   }

   private boolean isLoggedIn() {
      try {
         loadPage( WebsitePage.Splash );
         return isSplashPage();
      }
      catch( Exception e ) {
         return false;
      }
   }

   public WebsiteNavigator login() {
      if( isLoggedIn() ) {
         return this;
      }
      try {
         loadPage( WebsitePage.Login );
         LoggingUtl.log( "Login page loaded: " + currentPage.getUrl() );
         LoggingUtl.log( currentPage.asXml() );
         if( loginFormExists() ) {
            LoggingUtl.log( "Successfully loaded Fanball Login Page with NEW token " + getCurrentLoginToken() );
            HtmlUnitStarterFileCache.setLastLoginToken( getCurrentLoginToken() );
         }
         else {
            loadPage( WebsitePage.LoginWithToken, HtmlUnitStarterFileCache.getLastLoginToken() );
            if( loginFormExists() ) {
               LoggingUtl.log( "Successfully loaded Fanball Login Page with EXISTING token " + getCurrentLoginToken() );
            }
            else {
               ValidateUtl.fail( "Failed to load the Fanball Login Page with token " + getCurrentLoginToken() );
            }
         }
         setInputById( WebsiteElement.ID_Username, username );
         setInputById( WebsiteElement.ID_Password, password );
         clickButtonByValue( "Sign In" );
         boolean loggedIn = false;
         for( int checks = 1; checks <= 10; checks++ ) {
            loggedIn = loggedIn || waitAndCheckIfLoggedIn( checks );
            if( loggedIn )
               break;
         }
         if( !loggedIn ) {
            LoggingUtl.log( "This is not the splash page.  This is: " + currentPage.getUrl() );
            LoggingUtl.log( currentPage.asXml() );
            ValidateUtl.fail( "Failed to Log In! (Current Page: " + currentPage.getUrl() + ")" );
         }
         return this;
      }
      catch( Exception e ) {
         close();
         ValidateUtl.fail( e );
         return this;
      }
   }

   private boolean waitAndCheckIfLoggedIn( int checks ) throws InterruptedException {
      Thread.sleep( 3000 );
      if( isLoggedIn() ) {
         LoggingUtl.log( "Successful login for " + username + " (check #" + checks + ")" );
         return true;
      }
      else {
         LoggingUtl.log( "Still not logged in after check #" + checks + " ..." );
      }
      return false;
   }

   private boolean loginFormExists() throws InterruptedException {
      Thread.sleep( 10000 );
      return currentPage().getElementById( WebsiteElement.ID_Username ) != null;
   }

   private String getCurrentLoginToken() {
      final int paramIndex = currentPage.getBaseURI().indexOf( "?signin=" );
      return paramIndex > 0 ? currentPage.getBaseURI().substring( paramIndex + 8 ) : null;
   }

   private boolean isSplashPage() {
      return currentPage.getUrl().toString().contains( "splash" );
   }

   public WebsiteNavigator loadPage( WebsitePage page ) {
      return loadPage( page, "" );
   }

   public WebsiteNavigator loadPage( WebsitePage page, String appendix ) {
      boolean pageLoaded = false;
      int tryCount = 0;
      final int maxTries = 3;
      while( !pageLoaded && tryCount <= maxTries ) {
         try {
            doLoadPage( page, appendix );
            pageLoaded = true;
         }
         catch( ValidateUtl.PageLoadException pageLoadException ) {
            tryCount++;
            if( tryCount > maxTries ) {
               ValidateUtl.pageLoadFail( "Tried and failed to load page " + maxTries + " times.", pageLoadException );
            }
         }
      }
      return this;
   }

   private void doLoadPage( WebsitePage page, String appendix ) {
      if( !page.requiresJavascript() ) {
         webClient.getOptions().setJavaScriptEnabled( false );
      }
      try {
         currentPage = ( HtmlPage )webClient.getPage( page.url() + appendix );
      }
      catch( Exception e ) {
         close();
         ValidateUtl.fail( e );
      }
      finally {
         webClient.getOptions().setJavaScriptEnabled( true );
      }
      if( currentPage.getTitleText() != null && currentPage.getTitleText().contains( "Error" ) ) {
         ValidateUtl.pageLoadFail( currentPage, page.url() + appendix );
      }
   }

   public WebsiteNavigator setCheckBoxById( String elementId, boolean checked ) {
      final DomElement checkboxElement = currentPage.getElementById( elementId );
      ValidateUtl.nullCheck( checkboxElement, "Could not find checkbox by id '" + elementId + "' on page " + currentPage.getUrl() );
      if( checked ) {
         checkboxElement.setAttribute( "checked", "checked" );
      }
      else {
         checkboxElement.removeAttribute( "checked" );
      }
      return this;
   }

   public WebsiteNavigator setInputByName( HtmlForm form, String inputName, String inputValue ) {
      final HtmlInput input = form.getInputByName( inputName );
      ValidateUtl.nullCheck( input, "Could not find input by name '" + inputName + "' on page " + currentPage.getUrl() );
      input.setValueAttribute( inputValue );
      return this;
   }

   public WebsiteNavigator setInputById( String inputId, String inputValue ) {
      final HtmlInput input = ( HtmlInput )currentPage().getElementById( inputId );
      ValidateUtl.nullCheck( input, "Could not find input by id '" + inputId + "' on page " + currentPage.getUrl() );
      input.setValueAttribute( inputValue );
      return this;
   }

   public WebsiteNavigator setComboByText( String elementId, String optionText ) {
      final HtmlSelect combo = ( HtmlSelect )currentPage.getElementById( elementId );
      ValidateUtl.nullCheck( combo, "Could not find combo by id '" + elementId + "' on page " + currentPage.getUrl() );
      final HtmlOption option = combo.getOptionByText( optionText );
      combo.setSelectedAttribute( option, true );
      return this;
   }

   public WebsiteNavigator setComboByValue( String elementId, String optionValue ) {
      final HtmlSelect combo = ( HtmlSelect )currentPage.getElementById( elementId );
      ValidateUtl.nullCheck( combo, "Could not find combo by id '" + elementId + "' on page " + currentPage.getUrl() );
      final HtmlOption option = combo.getOptionByValue( optionValue );
      combo.setSelectedAttribute( option, true );
      return this;
   }

   public WebsiteNavigator clickElementById( String id ) {
      final DomElement element = currentPage.getElementById( id );
      ValidateUtl.nullCheck( element, "Could not find element by id '" + id + "' on page " + currentPage.getUrl() );
      return click( element );
   }

   public WebsiteNavigator clickElementByXpath( String xpath ) {
      final DomElement element = currentPage.getFirstByXPath( xpath );
      ValidateUtl.nullCheck( element, "Could not find element by xpath '" + xpath + "' on page " + currentPage.getUrl() );
      return click( element );
   }

   public WebsiteNavigator clickAnchorByText( String anchorText ) {
      for( HtmlAnchor anchor : currentPage().getAnchors() ) {
         if( anchorText.equals( anchor.getTextContent() ) ) {
            return click( anchor );
         }
      }
      ValidateUtl.fail( "Could not find anchor by text '" + anchorText + "' on page " + currentPage.getUrl() );
      return null;
   }

   public WebsiteNavigator click( DomElement element ) {
      return click( element, 1000 );
   }

   public WebsiteNavigator click( DomElement element, int waitMs ) {
      try {
         currentPage = element.click();
         wait( waitMs );//Wait to make sure the event has completed
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
      return this;
   }

   public WebsiteNavigator clickButtonByValue( String value ) {
      return clickButtonByValue( value, 1000 );
   }

   private WebsiteNavigator clickButtonByValue( String value, int waitMs ) {
      for( DomElement button : currentPage().getElementsByTagName( "button" ) ) {
         if( value.equalsIgnoreCase( button.getTextContent().trim() ) ) {
            click( button, waitMs );
            return this;
         }
      }
      ValidateUtl.fail( "Could not find button by value '" + value + "' on page " + currentPage().getBaseURI() );
      return null;
   }

   public WebsiteNavigator wait( int milliseconds ) {
      try {
         Thread.sleep( milliseconds );
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
      return this;
   }

   private static class CustomHtmlParserListener implements HTMLParserListener {
      @Override
      public void error( String message, URL url, String html, int line, int column, String key ) {
         // Log this elsewhere? (not in console)
      }

      @Override
      public void warning( String message, URL url, String html, int line, int column, String key ) {
         // Log this elsewhere? (not in console)         
      }
   }

}
