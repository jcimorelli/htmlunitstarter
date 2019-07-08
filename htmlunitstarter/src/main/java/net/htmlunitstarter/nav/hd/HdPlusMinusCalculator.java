package net.wistools.nav.hd;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.wistools.data.HdLineup;
import net.wistools.data.HdPlayerStats;
import net.wistools.nav.IWistoolsExcelCreator;
import net.wistools.nav.WisElement;
import net.wistools.nav.WisPage;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.utl.DateUtl;
import net.wistools.utl.EmailUtl;
import net.wistools.utl.ExcelUtl;
import net.wistools.utl.FormatUtl;
import net.wistools.utl.LoggingUtl;
import net.wistools.utl.MathUtl;
import net.wistools.utl.ValidateUtl;

import org.apache.poi.ss.usermodel.Workbook;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;

public class HdPlusMinusCalculator implements IWistoolsExcelCreator {

   //Input
   private HdWorld world;
   private String username;

   //Intermediate
   private String teamName;
   private Map<String, HdLineup> lineups = new LinkedHashMap<>();
   private Map<String, HdPlayerStats> players = new LinkedHashMap<>();

   //Output
   private Workbook workbook;

   public HdPlusMinusCalculator( HdWorld world ) {
      this.world = world;
   }

   public HdPlusMinusCalculator( String world ) {
      this.world = HdWorld.getByWorldName( world );
   }

   public void calculateAndSave( String fileLocation, String username, String password ) {
      calculate( username, password );
      saveReport( fileLocation );
   }

   public void calculate( String username, String password ) {
      this.username = username;
      try (final WistoolsNavigator nav = new WistoolsNavigator( username, password )) {
         nav.login().loadHdWorld( world );
         parseGameData( nav );
         aggregateIndividuals();
         parsePlayerStats( nav );
         calculatePer25();
         calculateScoringEfficiency();
         sortData();
         generateWorkbook();
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
   }

   private void parseGameData( final WistoolsNavigator nav ) {
      teamName = nav.currentPage().getElementById( WisElement.ID_TeamLinkSpan ).getFirstElementChild().getTextContent();
      for( String gameId : getGameIds( nav ) ) {
         new GameParser( gameId ).parseGame( nav );
         //Store individual game data?
      }
   }

   private void parsePlayerStats( WistoolsNavigator nav ) {
      final DomElement statsTable = nav.loadPage( WisPage.HD_PlayerGamePlan ).currentPage().getElementById( WisElement.ID_StatsTable );
      for( DomElement tr : statsTable.getFirstElementChild().getChildElements() ) {
         final DomElement nameTd = tr.getFirstElementChild();
         if( nameTd instanceof HtmlTableDataCell ) {
            final String name = nameTd == null ? null : nameTd.getTextContent().trim();
            if( name != null && name.length() > 0 ) {
               final HdPlayerStats player = players.get( name );
               if( player != null ) {
                  final DomElement ppgTd = nameTd.getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling();
                  player.setPpg( Double.parseDouble( ppgTd.getTextContent() ) );
                  final DomElement offPctTd = ppgTd.getNextElementSibling().getNextElementSibling().getNextElementSibling();
                  player.setOffPct( Double.parseDouble( offPctTd.getTextContent() ) );
               }
            }
         }
      }
   }

   private List<String> getGameIds( WistoolsNavigator nav ) {
      nav.loadPage( WisPage.HD_Schedule );
      final List<String> gameIds = new ArrayList<>();
      for( HtmlAnchor anchor : nav.currentPage().getAnchors() ) {
         if( anchor.getAttribute( "title" ) != null && anchor.getAttribute( "title" ).equals( "Open Boxscore" ) ) {
            final String href = anchor.getHrefAttribute();
            final int idx1 = href.indexOf( "?gid=" );
            final int idx2 = href.indexOf( "&tab=" );
            gameIds.add( href.substring( idx1 + 5, idx2 ) );
         }
      }
      return gameIds;
   }

   private void aggregateIndividuals() {
      for( HdLineup lineup : lineups.values() ) {
         for( String playerName : lineup.getPlayers() ) {
            HdPlayerStats player = players.get( playerName );
            if( player == null ) {
               player = new HdPlayerStats( playerName );
               players.put( playerName, player );
            }
            player.updatePlusMinus( lineup.getPlusMinus() );
            player.updateMinutes( lineup.getMinutes() );
         }
      }
   }

   private void calculatePer25() {
      for( HdLineup lineup : lineups.values() ) {
         lineup.setPlusMinusPer25( MathUtl.divide( BigDecimal.valueOf( lineup.getPlusMinus() ), BigDecimal.valueOf( lineup.getMinutes() ) ).multiply( BigDecimal.valueOf( 25 ) ).doubleValue() );
      }
      for( HdPlayerStats player : players.values() ) {
         player.setPlusMinusPer25( MathUtl.divide( BigDecimal.valueOf( player.getPlusMinus() ), BigDecimal.valueOf( player.getMinutes() ) ).multiply( BigDecimal.valueOf( 25 ) ).doubleValue() );
      }
   }

   private void calculateScoringEfficiency() {
      for( HdPlayerStats player : players.values() ) {
         if( player.getOffPct() > 0 ) {
            player.setScoringEfficiency( MathUtl.divide( BigDecimal.valueOf( player.getPpg() ), BigDecimal.valueOf( player.getOffPct() ) ).doubleValue() );
         }
      }
   }

   private void sortData() {
      final List<Map.Entry<String, HdLineup>> lineupEntries = new ArrayList<>( lineups.entrySet() );
      Collections.sort( lineupEntries, new Comparator<Map.Entry<String, HdLineup>>() {
         @Override
         public int compare( Map.Entry<String, HdLineup> t1, Map.Entry<String, HdLineup> t2 ) {
            return new Double( t2.getValue().getMinutes() ).compareTo( new Double( t1.getValue().getMinutes() ) );
         }
      } );
      lineups.clear();
      for( Map.Entry<String, HdLineup> e : lineupEntries ) {
         lineups.put( e.getKey(), e.getValue() );
      }
      final List<Map.Entry<String, HdPlayerStats>> playerEntries = new ArrayList<>( players.entrySet() );
      Collections.sort( playerEntries, new Comparator<Map.Entry<String, HdPlayerStats>>() {
         @Override
         public int compare( Map.Entry<String, HdPlayerStats> t1, Map.Entry<String, HdPlayerStats> t2 ) {
            return new Double( t2.getValue().getPlusMinusPer25() ).compareTo( new Double( t1.getValue().getPlusMinusPer25() ) );
         }
      } );
      players.clear();
      for( Map.Entry<String, HdPlayerStats> e : playerEntries ) {
         players.put( e.getKey(), e.getValue() );
      }
   }

   private void generateWorkbook() {
      final HashMap<String, List<HdLineup>> lineupDataMap = new HashMap<>();
      lineupDataMap.put( "Lineups", new ArrayList<HdLineup>( lineups.values() ) );
      workbook = ExcelUtl.buildWorkbookFromDataMap( lineupDataMap, HdLineup.class, 0 );
      final HashMap<String, List<HdPlayerStats>> playerDataMap = new HashMap<>();
      playerDataMap.put( "Players", new ArrayList<HdPlayerStats>( players.values() ) );
      ExcelUtl.addSheetsToWorkbookFromDataMap( playerDataMap, HdPlayerStats.class, 0, workbook );
   }

   @Override
   public Workbook getWorkbook() {
      return workbook;
   }

   @Override
   public String getServerFolderName() {
      return "HdPlusMinus";
   }

   @Override
   public String getReportName() {
      return "HdPlusMinus_" + EmailUtl.getNamePart( username ) + "_" + FormatUtl.stripNonAlphaNumeric( world.getWorldName() ) + "_" + DateUtl.formatTimestampForFileName( new Date() ) + ".xlsx";
   }

   private void saveReport( String fileLocation ) {
      try (OutputStream os = new FileOutputStream( fileLocation )) {
         workbook.write( os );
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
   }
   private enum Period {
      FirstHalf(20d),
      SecondHalf(20d),
      OT(5d);

      double timeOfPeriod;

      Period( double timeOfPeriod ) {
         this.timeOfPeriod = timeOfPeriod;
      }

      Period nextPeriod() {
         if( this.equals( FirstHalf ) ) {
            return SecondHalf;
         }
         else {
            return OT;
         }
      }
   }

   private class GameParser {
      private String gameId;
      private boolean homeGame = false;
      private boolean nextLineupIsSub = true;//@ Beginning of game, 2nd half, OT
      private int scoreDifferential = 0;//My team's score minus other team's score
      private HdLineup currentLineup = null;
      private Period currentPeriod = Period.FirstHalf;
      private double timeOfLastPlay = Period.FirstHalf.timeOfPeriod;
      private double timeOfLastSub = Period.FirstHalf.timeOfPeriod;

      public GameParser( String gameId ) {
         this.gameId = gameId;
      }

      public void parseGame( final WistoolsNavigator nav ) {
         nav.loadPage( WisPage.HD_BoxScore, gameId );
         determineHomeOrAway( nav );
         for( DomElement tr : nav.currentPage().getElementsByTagName( "tr" ) ) {
            if( nextLineupIsSub
                  && tr.getAttribute( "class" ) != null && tr.getAttribute( "class" ).startsWith( "lineup" )
                  && teamName.equals( tr.getFirstElementChild().getNextElementSibling().getTextContent() ) ) {
               parsePeriodStartLineup( tr );
            }
            else if( ( "even".equals( tr.getAttribute( "class" ) ) || "odd".equals( tr.getAttribute( "class" ) ) )
                  && "time".equals( tr.getFirstElementChild().getAttribute( "class" ) ) ) {
               parsePlay( tr, homeGame );
            }
            else if( "subs".equals( tr.getAttribute( "class" ) ) ) {
               parseLineupChange( tr );
            }
         }
         LoggingUtl.log( "Final (" + MathUtl.round2( timeOfLastPlay ) + ") = " + currentLineup.toString() + " (" + currentLineup.getPlusMinus() + ")" );
      }

      private void determineHomeOrAway( WistoolsNavigator nav ) {
         final String title = nav.currentPage().getElementsByTagName( "title" ).get( 0 ).getTextContent();
         final int vsIndex = title.indexOf( "vs." );
         final int myTeamIndex = title.indexOf( teamName );
         ValidateUtl.check( vsIndex == -1, "Didn't find 'vs.' in '" + title + "'" );
         ValidateUtl.check( myTeamIndex == -1, "Didn't find '" + teamName + "' in '" + title + "'" );
         homeGame = myTeamIndex < vsIndex;
      }

      private void parsePeriodStartLineup( DomElement tr ) {
         final DomElement pgStarterDiv = tr.getFirstElementChild().getNextElementSibling().getNextElementSibling().getFirstElementChild();
         final DomElement sgStarterDiv = pgStarterDiv.getNextElementSibling();
         final DomElement sfStarterDiv = sgStarterDiv.getNextElementSibling();
         final DomElement pfStarterDiv = sfStarterDiv.getNextElementSibling();
         final DomElement cStarterDiv = pfStarterDiv.getNextElementSibling();
         final String pgStarterStr = pgStarterDiv.getFirstElementChild().getNextElementSibling().getNextElementSibling().getTextContent();
         final String sgStarterStr = sgStarterDiv.getFirstElementChild().getNextElementSibling().getNextElementSibling().getTextContent();
         final String sfStarterStr = sfStarterDiv.getFirstElementChild().getNextElementSibling().getNextElementSibling().getTextContent();
         final String pfStarterStr = pfStarterDiv.getFirstElementChild().getNextElementSibling().getNextElementSibling().getTextContent();
         final String cStarterStr = cStarterDiv.getFirstElementChild().getNextElementSibling().getNextElementSibling().getTextContent();
         final HdLineup startingLineup = new HdLineup();
         startingLineup.addPlayer( pgStarterStr.substring( 0, pgStarterStr.indexOf( "(" ) - 1 ) );
         startingLineup.addPlayer( sgStarterStr.substring( 0, sgStarterStr.indexOf( "(" ) - 1 ) );
         startingLineup.addPlayer( sfStarterStr.substring( 0, sfStarterStr.indexOf( "(" ) - 1 ) );
         startingLineup.addPlayer( pfStarterStr.substring( 0, pfStarterStr.indexOf( "(" ) - 1 ) );
         startingLineup.addPlayer( cStarterStr.substring( 0, cStarterStr.indexOf( "(" ) - 1 ) );
         swapInNewLineup( startingLineup, null );
         nextLineupIsSub = false;
      }

      private void parsePlay( DomElement tr, boolean homeGame ) {
         final DomElement scoreElement = tr.getFirstElementChild().getNextElementSibling().getNextElementSibling().getNextElementSibling();
         if( "score changed".equals( scoreElement.getAttribute( "class" ) ) ) {
            final String score = scoreElement.getTextContent();
            final String awayScore = score.substring( 0, score.indexOf( "-" ) );
            final String homeScore = score.substring( score.indexOf( "-" ) + 1 );
            final Integer newScoreDifferential = homeGame ? Integer.parseInt( homeScore ) - Integer.parseInt( awayScore ) : Integer.parseInt( awayScore ) - Integer.parseInt( homeScore );
            final Integer scoreDelta = newScoreDifferential - scoreDifferential;
            currentLineup.updatePlusMinus( scoreDelta );
            LoggingUtl.log( "ScoreUpdate (" + MathUtl.round2( timeOfLastPlay ) + ") = " + currentLineup.toString() + " (" + scoreDelta + ")" );
            scoreDifferential = newScoreDifferential;
         }
         updateTime( tr );
      }

      private void updateTime( DomElement tr ) {
         timeOfLastPlay = parseTime( tr );
         if( timeOfLastPlay == 0d ) {
            if( currentPeriod.equals( Period.FirstHalf ) || scoreDifferential == 0 ) {
               currentPeriod = currentPeriod.nextPeriod();
               nextLineupIsSub = true;
            }
            else {//End of Game
               updateMinutes( null );
            }
         }
      }

      private Double parseTime( DomElement tr ) {
         final String time = tr.getFirstElementChild().getTextContent();
         final Double minutes = Double.parseDouble( time.substring( 0, time.indexOf( ":" ) ) );
         final Double seconds = Double.parseDouble( time.substring( time.indexOf( ":" ) + 1 ) );
         return minutes + ( seconds / 60d );
      }

      private void parseLineupChange( DomElement tr ) {
         if( teamName.equals( tr.getFirstElementChild().getNextElementSibling().getTextContent() ) ) {
            final DomElement playTd = tr.getFirstElementChild().getNextElementSibling().getNextElementSibling();
            if( playTd.getFirstElementChild() != null ) {//Otherwise there were no actual subs
               final HdLineup newLineup = determineNewLineup( playTd );
               swapInNewLineup( newLineup, tr );
            }
         }
      }

      private HdLineup determineNewLineup( DomElement playTd ) {
         final HdLineup newLineup = currentLineup.clone();
         final String subsInText = playTd.getFirstElementChild().getTextContent();
         for( String subIn : FormatUtl.delimitedStringToList( subsInText, ", " ) ) {
            newLineup.addPlayer( subIn.substring( 0, subIn.indexOf( "(" ) - 1 ) );
         }
         final String subsOutText = playTd.getFirstElementChild().getNextElementSibling().getTextContent();
         for( String subOut : FormatUtl.delimitedStringToList( subsOutText, ", " ) ) {
            newLineup.removePlayer( subOut );
         }
         ValidateUtl.check( newLineup.getPlayers().size() != 5, "There are not 5 players in this lineup " + newLineup.toString() );
         return newLineup;
      }

      private void swapInNewLineup( HdLineup newLineup, DomElement tr ) {
         updateMinutes( tr );
         if( currentLineup != null ) {//This would be null if beginning of game
            LoggingUtl.log( " Outgoing (" + MathUtl.round2( timeOfLastPlay ) + ") = " + currentLineup.toString() + " (" + currentLineup.getPlusMinus() + ")" );
         }
         final HdLineup existingLineup = lineups.get( newLineup.toString() );
         if( existingLineup != null ) {
            currentLineup = existingLineup;
         }
         else {
            lineups.put( newLineup.toString(), newLineup );
            currentLineup = newLineup;
         }
         LoggingUtl.log( "Incoming (" + MathUtl.round2( timeOfLastPlay ) + ") = " + currentLineup.toString() + " (" + currentLineup.getPlusMinus() + ")" );
      }

      private void updateMinutes( DomElement tr ) {
         if( currentLineup != null ) {//This would be null if beginning of game
            final boolean isNewPeriod = tr == null;
            final double timeElapsedSinceLastSub = isNewPeriod ? timeOfLastSub : timeOfLastSub - timeOfLastPlay;
            currentLineup.updateMinutes( timeElapsedSinceLastSub );
            timeOfLastSub = isNewPeriod ? currentPeriod.timeOfPeriod : timeOfLastPlay;
         }
      }
   }

}
