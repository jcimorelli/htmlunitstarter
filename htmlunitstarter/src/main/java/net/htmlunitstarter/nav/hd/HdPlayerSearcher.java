package net.wistools.nav.hd;

import java.util.List;

import net.wistools.data.HdTeam;
import net.wistools.nav.WisPage;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.utl.LoggingUtl;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HdPlayerSearcher {

   private HdWorld world;
   private String playerNameSearch;
   private StringBuilder resultsText = new StringBuilder();

   public HdPlayerSearcher( HdWorld world ) {
      this.world = world;
   }

   public void searchForPlayerByName( WistoolsNavigator nav, String playerName ) {
      this.playerNameSearch = playerName;
      nav.login().loadHdWorld( world );
      final List<HdTeam> allTeams = new HdTeamDetailsFinder( world ).getAllTeamsForMyDiv( nav );
      for( HdTeam team : allTeams ) {
         searchTeam( nav, team );
      }
   }

   private void searchTeam( WistoolsNavigator nav, HdTeam team ) {
      final HtmlPage teamPage = nav.loadPage( WisPage.HD_TeamProfile_Roster, String.valueOf( team.getTeamId() ) ).currentPage();
      for( HtmlAnchor playerAnchor : teamPage.getAnchors() ) {
         if( "Open Player Profile".equals( playerAnchor.getAttribute( "title" ) ) ) {
            final String playerName = playerAnchor.getTextContent();
            if( playerName != null && playerName.toLowerCase().contains( playerNameSearch.toLowerCase() ) ) {
               final String playerUrl = WisPage.Root.url() + playerAnchor.getHrefAttribute();
               final String foundPlayerMessage = "Potential match for '" + playerNameSearch + "' found: " + playerName + " is on " + team.getTeamName() + " (" + playerUrl + ")";
               LoggingUtl.log( foundPlayerMessage );
               resultsText.append( foundPlayerMessage ).append( "\n" );
            }
         }
      }
   }

   public String getResultsText() {
      return resultsText.toString();
   }
}
