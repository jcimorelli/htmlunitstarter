package net.wistools.nav.hd;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.wistools.data.IHdRecruit;
import net.wistools.nav.WisElement;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.utl.MathUtl;
import net.wistools.utl.ValidateUtl;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.google.common.collect.Range;

public abstract class AbstractHdRecruitSearcher {

   protected Range<Integer> getRecruitIdRange( WistoolsNavigator nav ) {
      final Set<Integer> allKnownRecruitIds = getAllRecruitIdsInRecruitPool( nav );
      final Set<Integer> trimmedRecruitIds = excludePriorSeasonLeftovers( allKnownRecruitIds );
      final Range<Integer> knownRange = Range.closed( Collections.min( trimmedRecruitIds ), Collections.max( trimmedRecruitIds ) );
      final Range<Integer> trueRange = getTrueRecruitIdRange( nav, knownRange );
      return trueRange;
   }

   protected Set<Integer> getAllRecruitIdsInRecruitPool( WistoolsNavigator nav ) {
      final Set<Integer> recruitIds = new HashSet<>();
      final HdRecruitPoolSearcher searcher = new HdRecruitPoolSearcher();
      searcher.performSearch( nav );
      getRecruitIdsFromPage( nav.currentPage(), recruitIds );
      while( searcher.hasNextPage( nav ) ) {
         searcher.nextPage( nav );
         getRecruitIdsFromPage( nav.currentPage(), recruitIds );
      }
      return recruitIds;
   }

   private void getRecruitIdsFromPage( HtmlPage page, final Set<Integer> recruitIds ) {
      for( HtmlAnchor anchor : page.getAnchors() ) {
         if( anchor.getAttribute( "title" ).equals( "Open Recruit Profile" ) ) {
            recruitIds.add( Integer.parseInt( anchor.getHrefAttribute().substring( 36 ) ) );
         }
      }
   }

   private Set<Integer> excludePriorSeasonLeftovers( Set<Integer> allKnownRecruitIds ) {
      final Set<Integer> recruitIds = new HashSet<Integer>();
      final Integer median = MathUtl.median( allKnownRecruitIds );
      for( Integer recruitId : allKnownRecruitIds ) {
         if( median - recruitId <= 4000 ) {//Throw out any recruitIds sufficiently under the median
            recruitIds.add( recruitId );
         }
      }
      return recruitIds;
   }

   private Range<Integer> getTrueRecruitIdRange( WistoolsNavigator nav, Range<Integer> knownRange ) {
      // Return a new range (not known range) with true values...IE:
      //    try lower recruit Id pages until find one that's nonexistent
      //    try higher recruit Id pages until find one that's nonexistent
      // This might be problematic because we may end up pulling in recruits meant for other worlds...so implement this with extreme caution
      return knownRange;
   }

   protected void parseRecruitData( WistoolsNavigator nav, IHdRecruit recruit ) {
      final DomElement recruitNameElement = nav.currentPage().getElementById( WisElement.ID_RecruitBadges ).getPreviousElementSibling();
      final DomElement positionElement = nav.currentPage().getElementById( WisElement.ID_Position );
      final DomElement projectedDivisionElement = nav.currentPage().getElementById( WisElement.ID_ProjectedDivision );
      final DomElement cityElement = getCityElement( nav, recruit.getRecruitId() );
      final DomElement signedWithElement = nav.currentPage().getElementById( WisElement.ID_SignedWith );
      final DomElement eligibilityElement = nav.currentPage().getElementById( WisElement.ID_RecruitBadges ).getFirstElementChild().getNextElementSibling();
      final DomElement positionRankElement = getPositionRankElement( nav );
      final DomElement overallRankElement = getOverallRankElement( nav );
      final DomElement yearsElement = nav.currentPage().getElementById( WisElement.ID_EligibilityYears ).getFirstElementChild().getNextElementSibling();
      recruit.setRecruitName( recruitNameElement.getTextContent() );
      recruit.setCity( cityElement.getTextContent().substring( cityElement.getTextContent().indexOf( "|" ) + 1 ).trim() );
      recruit.setPosition( positionElement.getTextContent() );
      recruit.setProjectedDiv( projectedDivisionElement.getTextContent().substring( 10 ) );
      recruit.setSignedWith( signedWithElement != null ? signedWithElement.getTextContent().substring( 29 ) : "" );
      recruit.setEligibility( eligibilityElement.getTextContent() );
      recruit.setYears( yearsElement.getTextContent().substring( 0, 1 ) );
      recruit.setPositionRank( positionRankElement != null ? positionRankElement.getTextContent().trim().substring( 1, positionRankElement.getTextContent().trim().indexOf( " " ) ) : "" );
      recruit.setOverallRank( overallRankElement != null ? overallRankElement.getTextContent().trim().substring( 1, overallRankElement.getTextContent().trim().indexOf( " " ) ) : "" );
      recruit.setColorCategory( getColorCategory( nav.currentPage() ) );
   }

   private DomElement getPositionRankElement( WistoolsNavigator nav ) {
      final DomElement legendDiv = ( DomElement )nav.currentPage().getElementById( WisElement.ID_EligibilityYears ).getParentNode();
      final Iterator<DomElement> iter = legendDiv.getChildElements().iterator();
      while( iter.hasNext() ) {
         final DomElement element = iter.next();
         if( element.getFirstElementChild() != null && "../Images/icons/status/RankedPosition.gif".equals( element.getFirstElementChild().getAttribute( "src" ) ) ) {
            return element;
         }
      }
      return null;
   }

   private DomElement getOverallRankElement( WistoolsNavigator nav ) {
      final DomElement legendDiv = ( DomElement )nav.currentPage().getElementById( WisElement.ID_EligibilityYears ).getParentNode();
      final Iterator<DomElement> iter = legendDiv.getChildElements().iterator();
      while( iter.hasNext() ) {
         final DomElement element = iter.next();
         if( element.getFirstElementChild() != null && "../Images/icons/status/RankedOverall.gif".equals( element.getFirstElementChild().getAttribute( "src" ) ) ) {
            return element;
         }
      }
      return null;
   }

   private DomElement getCityElement( WistoolsNavigator nav, Integer recruitId ) {
      DomElement bioInfoDiv = null;
      for( DomElement div : nav.currentPage().getElementsByTagName( "div" ) ) {
         if( "RecruitProfile_BioInfo".equals( div.getAttribute( "class" ) ) ) {
            bioInfoDiv = div;
            break;
         }
      }
      ValidateUtl.nullCheck( bioInfoDiv, "Couldn't find recruit's bio info (recruitId=" + recruitId + ")" );
      Iterator<DomElement> iter = bioInfoDiv.getChildElements().iterator();
      DomElement lastChild = null;
      while( iter.hasNext() ) {
         lastChild = iter.next();
      }
      return lastChild;
   }

   private String getColorCategory( HtmlPage recruitPage ) {
      final HtmlRadioButtonInput white = ( HtmlRadioButtonInput )recruitPage.getElementById( WisElement.ID_HdColorCriteriaWhite2 );
      if( white != null && white.isChecked() ) {
         return "-";
      }
      final HtmlRadioButtonInput blue = ( HtmlRadioButtonInput )recruitPage.getElementById( WisElement.ID_HdColorCriteriaBlue2 );
      if( blue != null && blue.isChecked() ) {
         return "Blue";
      }
      final HtmlRadioButtonInput green = ( HtmlRadioButtonInput )recruitPage.getElementById( WisElement.ID_HdColorCriteriaGreen2 );
      if( green != null && green.isChecked() ) {
         return "Green";
      }
      final HtmlRadioButtonInput yellow = ( HtmlRadioButtonInput )recruitPage.getElementById( WisElement.ID_HdColorCriteriaYellow2 );
      if( yellow != null && yellow.isChecked() ) {
         return "Yellow";
      }
      final HtmlRadioButtonInput red = ( HtmlRadioButtonInput )recruitPage.getElementById( WisElement.ID_HdColorCriteriaRed2 );
      if( red != null && red.isChecked() ) {
         return "Red";
      }
      return null;
   }
}
