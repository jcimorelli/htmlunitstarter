package net.wistools.nav.hd;

import net.wistools.nav.WisElement;
import net.wistools.nav.WisPage;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.utl.ValidateUtl;

import com.gargoylesoftware.htmlunit.html.DomElement;

public class HdRecruitPoolSearcher {

   public HdRecruitSearchCriteria criteria = new HdRecruitSearchCriteria();

   public void performSearch( WistoolsNavigator nav ) {
      nav.loadPage( WisPage.HD_RecruitPool );
      ValidateUtl.check( nav.currentPage().asXml().contains( "Recruiting has ended for this season." ), "Recruiting has ended for this season.  Please run report again later." );
      nav.setComboByText( WisElement.ID_PositionCombo2, criteria.getPosition() )
            .setComboByText( WisElement.ID_MinDistanceCombo, criteria.getDistanceFrom() )
            .setComboByText( WisElement.ID_MaxDistanceCombo, criteria.getDistanceTo() )
            .setComboByText( WisElement.ID_StateCombo, criteria.getState() )
            .setComboByText( WisElement.ID_ProjectedDivisionCombo, criteria.getProjectedLevel() )
            .setComboByText( WisElement.ID_PlayingPrefCombo, criteria.getPlayingPref() )
            .setComboByText( WisElement.ID_DistancePrefCombo, criteria.getDistancePref() )
            .setComboByText( WisElement.ID_StylePrefCombo, criteria.getStylePref() )
            .setComboByText( WisElement.ID_OffensePrefCombo, criteria.getOffensePref() )
            .setComboByText( WisElement.ID_DefensePrefCombo, criteria.getDefensePref() )
            .setComboByText( WisElement.ID_EligibleYearsCombo, criteria.getEligibleYears() )
            .setComboByText( WisElement.ID_EligibilityCombo, criteria.getEligibility() )
            .setComboByText( WisElement.ID_SigningPrefCombo, criteria.getSigningPref() )
            .setComboByText( WisElement.ID_SuccessPrefCombo, criteria.getSuccessPref() )
            .setComboByText( WisElement.ID_ConferencePrefCombo, criteria.getConferencePref() )
            .setComboByText( WisElement.ID_LongevityPrefCombo, criteria.getLongevityPref() )
            .setComboByText( WisElement.ID_DecisionStatus, criteria.getDecisionStatus() )
            .setComboByText( WisElement.ID_ScoutingLevelCombo, criteria.getScoutingLevel() )
            .setComboByText( WisElement.ID_HdSortByCombo, criteria.getSortBy() )
            .setComboByText( WisElement.ID_SortDirectionCombo, criteria.getSortDirection() );
      setColorCriteria( nav );
      nav.clickElementById( WisElement.ID_SearchButton );
   }

   //TODO this criteria setting is not working properly.  fix this bug.
   private void setColorCriteria( WistoolsNavigator nav ) {
      if( criteria.getCurrentColor() != null ) {
         switch( criteria.getCurrentColor() ) {
         case "White":
            nav.setCheckBoxById( WisElement.ID_HdColorCriteriaWhite, true );
         case "Blue":
            nav.setCheckBoxById( WisElement.ID_HdColorCriteriaBlue, true );
         case "Green":
            nav.setCheckBoxById( WisElement.ID_HdColorCriteriaGreen, true );
         case "Yellow":
            nav.setCheckBoxById( WisElement.ID_HdColorCriteriaYellow, true );
         case "Red":
            nav.setCheckBoxById( WisElement.ID_HdColorCriteriaRed, true );
         case "Any"://don't check any of them
            break;
         }
      }
   }

   public boolean hasNextPage( WistoolsNavigator nav ) {
      final DomElement nextPageAnchor = nav.currentPage().getElementById( WisElement.ID_PagingPanel ).getFirstElementChild().getNextElementSibling().getFirstElementChild();
      return nextPageAnchor != null;
   }

   public void nextPage( WistoolsNavigator nav ) {
      final DomElement nextPageAnchor = nav.currentPage().getElementById( WisElement.ID_PagingPanel ).getFirstElementChild().getNextElementSibling().getFirstElementChild();
      nav.click( nextPageAnchor, 3000 );
   }

   public HdRecruitSearchCriteria getCriteria() {
      return criteria;
   }

   public void setCriteria( HdRecruitSearchCriteria criteria ) {
      this.criteria = criteria;
   }

}
