package net.htmlunitstarter.nav;

public enum WebsitePage {
   Login("https://www.website.com/login"),
   LoginWithToken("https://www.website.com/login?signin="),
   Splash("https://www.website.com/splash"),
   SomeOtherPage("https://www.website.com/someOtherPage", false);

   private String url;
   private boolean requiresJavascript;

   private WebsitePage( String url ) {
      this( url, true );
   }

   private WebsitePage( String url, boolean requiresJavascript ) {
      this.url = url;
      this.requiresJavascript = requiresJavascript;
   }

   public String url() {
      return url;
   }

   public boolean requiresJavascript() {
      return requiresJavascript;
   }
}
