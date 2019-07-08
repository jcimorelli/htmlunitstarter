package net.wistools.nav.hbd;

import net.wistools.nav.WisElement;
import net.wistools.nav.WistoolsNavigator;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HbdPlayerSearcher {

   public HtmlPage performSearch( WistoolsNavigator nav, String position, String franchiseName ) {
      return nav.setComboByText( WisElement.ID_PositionCombo, position )
            .setComboByText( WisElement.ID_LevelCombo, "All" )
            .setComboByText( WisElement.ID_FranchiseCombo, franchiseName )
            .clickElementById( WisElement.ID_GoButton )
            .clickElementByXpath( WisElement.XPATH_PlayerNameColumnHeader ).currentPage();
   }
}
