package net.wistools.nav.hd;

import net.wistools.nav.WisElement;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.utl.ValidateUtl;

import com.gargoylesoftware.htmlunit.html.DomElement;

public class HdRecruitColorCoder {

   private HdWorld world;
   private String updateColor;
   private Integer firstNRecruits;
   private HdRecruitPoolSearcher searcher;

   public HdRecruitColorCoder( String worldName ) {
      this.world = HdWorld.getByWorldName( worldName );
      this.searcher = new HdRecruitPoolSearcher();
   }

   public void setSearchCriteria( HdRecruitSearchCriteria criteria ) {
      searcher.setCriteria( criteria );
   }

   public void colorCodeRecruits( String username, String password ) {
      int firstNRecruits = this.firstNRecruits == null ? 1000000 : this.firstNRecruits;//just say first million if null (null=all)
      try (final WistoolsNavigator nav = new WistoolsNavigator( username, password )) {
         nav.login().loadHdWorld( world );
         searcher.performSearch( nav );
         int recruitUpdateCount = updateRecruitsOnCurrentPage( updateColor, firstNRecruits, nav, 0 );
         while( recruitUpdateCount < firstNRecruits && searcher.hasNextPage( nav ) ) {
            searcher.nextPage( nav );
            recruitUpdateCount = updateRecruitsOnCurrentPage( updateColor, firstNRecruits, nav, recruitUpdateCount );
         }
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
   }

   private int updateRecruitsOnCurrentPage( String color, int firstNRecruits, final WistoolsNavigator nav, int recruitUpdateCount ) {
      int currentPageRecruitUpdateCount = 0;//first 50 are recruits on the page, but there are some hidden ones after that in the source for some reason
      for( DomElement colorControlDiv : nav.currentPage().getElementsById( WisElement.ID_RecruitColorControl ) ) {
         final DomElement categoryOptionsDiv = colorControlDiv.getFirstElementChild().getNextElementSibling().getNextElementSibling();
         DomElement colorSpan = categoryOptionsDiv.getFirstElementChild();
         switch( color ) {
         case "Red"://5th child
            colorSpan = colorSpan.getNextElementSibling();
         case "Yellow"://4th child
            colorSpan = colorSpan.getNextElementSibling();
         case "Green"://3rd child
            colorSpan = colorSpan.getNextElementSibling();
         case "Blue"://2nd child
            colorSpan = colorSpan.getNextElementSibling();
         case "White"://1st child
            break;
         }
         nav.click( colorSpan, 50 );
         recruitUpdateCount++;
         currentPageRecruitUpdateCount++;
         if( recruitUpdateCount == firstNRecruits || currentPageRecruitUpdateCount == 50 ) {
            break;
         }
      }
      return recruitUpdateCount;
   }

   public void setUpdateColor( String updateColor ) {
      this.updateColor = updateColor;
   }

   public void setFirstNRecruits( Integer firstNRecruits ) {
      this.firstNRecruits = firstNRecruits;
   }

}
