package net.wistools.nav.hd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.wistools.data.HdTeam;
import net.wistools.nav.WisElement;
import net.wistools.nav.WisPage;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.utl.FormatUtl;
import net.wistools.utl.LoggingUtl;
import net.wistools.utl.ValidateUtl;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HdTeamDetailsFinder {

   private HdWorld world;
   private List<HdTeam> teamList;

   //This will find details for ALL teams in "my" division
   public HdTeamDetailsFinder( HdWorld world ) {
      this.world = world;
   }

   public HdTeamDetailsFinder( HdWorld world, List<HdTeam> teamList ) {
      this.world = world;
      this.teamList = teamList;
   }

   public List<HdTeam> getTeamList() {
      return teamList;
   }

   public void populateTeamDetails( WistoolsNavigator nav, boolean calcPrefs, boolean includeSchedule, boolean excludeSims ) {
      nav.loadHdWorld( world );
      buildTeamList( nav );
      new TeamPageParser( teamList, excludeSims ).parseTeamPages( nav );
      if( includeSchedule )
         new TeamScheduleParser( teamList ).parseScheduleInfo( nav );
      if( calcPrefs )
         new TeamPrefsCalculator( teamList ).calcPrefs( nav );
   }

   private void buildTeamList( WistoolsNavigator nav ) {
      if( teamList == null ) {
         getAllTeamsForMyDiv( nav );
      }
      else {
         getSpecifiedTeams( nav );
      }
   }

   public List<HdTeam> getAllTeamsForMyDiv( WistoolsNavigator nav ) {
      teamList = new ArrayList<>();
      nav.loadPage( WisPage.HD_TeamRecords );
      for( HtmlAnchor anchor : nav.currentPage().getAnchors() ) {
         if( "Open Team Profile".equals( anchor.getAttribute( "title" ) ) ) {
            final String teamId = anchor.getHrefAttribute().substring( anchor.getHrefAttribute().indexOf( "tid=" ) + 4 );
            final HdTeam team = new HdTeam( Integer.parseInt( teamId ) );
            team.setTeamName( anchor.getTextContent() );
            if( !teamList.contains( team ) ) {
               teamList.add( team );
            }
         }
      }
      return teamList;
   }

   private void getSpecifiedTeams( final WistoolsNavigator nav ) {
      final List<HdTeam> teamsStillNeeded = new ArrayList<>( teamList );
      //Search Division 1
      parsePageForTeamIds( nav, WisPage.HD_ProjectionReport_Div1, teamsStillNeeded );
      //Search Division 2
      if( !teamsStillNeeded.isEmpty() ) {
         parsePageForTeamIds( nav, WisPage.HD_ProjectionReport_Div2, teamsStillNeeded );
      }
      //Search Division 3
      if( !teamsStillNeeded.isEmpty() ) {
         parsePageForTeamIds( nav, WisPage.HD_ProjectionReport_Div3, teamsStillNeeded );
      }
      ValidateUtl.check( !teamsStillNeeded.isEmpty(), "Unknown Team(s): " + FormatUtl.listToDelimitedString( teamsStillNeeded, ", " ) );
   }

   private void parsePageForTeamIds( final WistoolsNavigator nav, final WisPage page, final List<HdTeam> teamsStillNeeded ) {
      nav.loadPage( page );
      ValidateUtl.check( nav.currentPage().asXml().contains( "Check back after the non-conference portion of the schedule for a report on post-season prospects" ),
            "Cannot run Recruiting Report during non-conference schedule." );
      final List<HdTeam> teamsForLoop = new ArrayList<>( teamsStillNeeded );
      for( HtmlAnchor anchor : nav.currentPage().getAnchors() ) {
         for( HdTeam team : teamsForLoop ) {
            if( team.getTeamName().equals( anchor.getTextContent() ) ) {
               team.setTeamId( Integer.valueOf( anchor.getHrefAttribute().substring( 33 ) ) );
               teamsStillNeeded.remove( team );
               break;
            }
         }
         if( teamsStillNeeded.isEmpty() ) {
            break;
         }
      }
   }

   private static class TeamPageParser {
      private List<HdTeam> teamList;
      private List<HdTeam> excludedSimTeams = new ArrayList<>();
      private boolean excludeSims;

      public TeamPageParser( List<HdTeam> teamList, boolean excludeSims ) {
         this.teamList = teamList;
         this.excludeSims = excludeSims;
      }

      public void parseTeamPages( WistoolsNavigator nav ) {
         for( HdTeam team : teamList ) {
            parseTeamPage( nav, team );
         }
         for( HdTeam excludedSimTeam : excludedSimTeams ) {
            teamList.remove( excludedSimTeam );
         }
      }

      private void parseTeamPage( final WistoolsNavigator nav, final HdTeam team ) {
         LoggingUtl.log( "Parsing Team Page for " + team.getTeamName() );
         final HtmlPage teamPage = nav.loadPage( WisPage.HD_TeamProfile_Roster, String.valueOf( team.getTeamId() ) ).currentPage();
         final DomElement rankElement = teamPage.getElementById( WisElement.ID_HoopsLinkContainer ).getNextElementSibling();
         team.setRank( rankElement.getTextContent() );
         final DomElement recordSpan = ( ( HtmlElement )rankElement.getParentNode() ).getNextElementSibling().getFirstElementChild();
         team.setRecord( recordSpan.getFirstElementChild().getNextElementSibling().getTextContent() );
         final DomElement conferenceSpan = recordSpan.getNextElementSibling();
         team.setConference( conferenceSpan.getFirstElementChild().getNextElementSibling().getTextContent() );
         final DomElement coachSpan = conferenceSpan.getNextElementSibling();
         final DomElement coachElement = coachSpan.getFirstElementChild().getNextElementSibling();
         if( excludeSims && coachElement == null ) {
            excludedSimTeams.add( team );
            return;
         }
         if( coachElement != null ) {
            team.setCoach( coachElement.getTextContent() );
         }
         final DomElement prestigeSpan = coachSpan.getNextElementSibling();
         team.setPrestige( prestigeSpan.getFirstElementChild().getTextContent() );
         final DomElement rpiSpan = prestigeSpan.getNextElementSibling();
         team.setRpiRank( rpiSpan.getFirstElementChild().getTextContent() );
         final DomElement sosSpan = rpiSpan.getNextElementSibling();
         team.setSosRank( sosSpan.getFirstElementChild().getTextContent() );
         final String divisionAndCity = teamPage.getElementById( WisElement.ID_DivisionAndCity ).getTextContent();
         final int hyphenIndex = divisionAndCity.indexOf( "-" );
         team.setDivision( divisionAndCity.substring( 0, hyphenIndex - 1 ) );
         team.setCity( divisionAndCity.substring( hyphenIndex + 2 ) );
         final DomElement homecourtElement = teamPage.getElementById( WisElement.ID_Homecourt );
         team.setHomecourt( homecourtElement.getTextContent().substring( 11 ) );

         parsePlayerInfo( nav, team, teamPage );
      }

      private void parsePlayerInfo( final WistoolsNavigator nav, final HdTeam team, final HtmlPage teamPage ) {
         LoggingUtl.log( "Parsing Player Info for " + team.getTeamName() );
         int playerCount = 0;
         for( HtmlAnchor playerAnchor : teamPage.getAnchors() ) {
            if( "Open Player Profile".equals( playerAnchor.getAttribute( "title" ) ) ) {
               playerCount++;
               final DomElement playerTd = ( DomElement )playerAnchor.getParentNode();
               final String playerClass = playerTd.getNextElementSibling().getFirstElementChild().getTextContent();
               if( "Sr/5".equals( playerClass ) ) {
                  team.setDepartingSeniors( team.getDepartingSeniors() + 1 );
               }
               else if( "Sr.".equals( playerClass ) ) {
                  final String pid = playerAnchor.getHrefAttribute().substring( playerAnchor.getHrefAttribute().indexOf( "pid=" ) + 4 );
                  nav.loadPage( WisPage.HD_PlayerProfile_Stats, pid );
                  if( !nav.currentPage().asText().contains( "Academic Non-Qualifier" ) ) {
                     team.setDepartingSeniors( team.getDepartingSeniors() + 1 );
                  }
               }
               final String scholarshipStatus = playerTd.getNextElementSibling().getNextElementSibling().getNextElementSibling().getFirstElementChild().getTextContent();
               if( "W".equals( scholarshipStatus ) ) {
                  team.setWalkons( team.getWalkons() + 1 );
               }
            }
         }
         team.setPlayersCut( 12 - playerCount );
      }
   }

   private static class TeamScheduleParser {
      private List<HdTeam> teamList;

      public TeamScheduleParser( List<HdTeam> teamList ) {
         this.teamList = teamList;
      }

      public void parseScheduleInfo( WistoolsNavigator nav ) {
         for( HdTeam team : teamList ) {
            LoggingUtl.log( "Parsing schedule for " + team.getTeamName() );
            nav.loadPage( WisPage.HD_TeamProfile_Schedule, team.getTeamId().toString() );
            final DomElement nextSeasonScheduleElement = nav.currentPage().getElementById( WisElement.ID_TeamNextSeasonSchedule );
            final List<String> takenSlots = new ArrayList<>();
            if( nextSeasonScheduleElement != null ) {
               final DomElement table = nextSeasonScheduleElement.getFirstElementChild().getNextElementSibling();
               DomElement nextScheduledGame = table.getFirstElementChild().getFirstElementChild().getNextElementSibling();
               while( nextScheduledGame != null ) {
                  final String takenSlot = nextScheduledGame.getFirstElementChild().getTextContent().substring( 1 );
                  takenSlots.add( takenSlot );
                  nextScheduledGame = nextScheduledGame.getNextElementSibling();
               }
            }
            final List<String> availableSlots = new ArrayList<>( Arrays.asList( "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" ) );
            availableSlots.removeAll( takenSlots );
            team.setAvailableScheduleSpots( availableSlots );
         }
      }
   }

   private static class TeamPrefsCalculator {
      private List<HdTeam> teamList;

      public TeamPrefsCalculator( List<HdTeam> teamList ) {
         this.teamList = teamList;
      }

      public void calcPrefs( WistoolsNavigator nav ) {
         for( HdTeam team : teamList ) {
            LoggingUtl.log( "Calculating Prefs for " + team.getTeamName() );
            // FIXME jason
         }
      }
   }
}
