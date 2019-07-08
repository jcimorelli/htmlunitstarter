package net.wistools.nav.hd;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.wistools.config.WistoolsProperties;
import net.wistools.data.HdRecruitRatings;
import net.wistools.data.HdRecruitRatings.CustomOverallWeights;
import net.wistools.nav.IWistoolsExcelCreator;
import net.wistools.nav.WisElement;
import net.wistools.nav.WisPage;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.utl.DateUtl;
import net.wistools.utl.EmailUtl;
import net.wistools.utl.ExcelUtl;
import net.wistools.utl.FormatUtl;
import net.wistools.utl.ValidateUtl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Workbook;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.google.common.collect.Range;

public class HdRecruitPoolExporter extends AbstractHdRecruitSearcher implements IWistoolsExcelCreator {
   private HdWorld world;
   private List<HdRecruitRatings> recruits = new ArrayList<>();
   private Map<String, List<HdRecruitRatings>> recruitMap = new LinkedHashMap<>();
   private Workbook workbook;
   private String username;
   private CustomOverallWeights overallWeights;
   private boolean includeUnknownRecruits = true;
   private String decisionStatusFilter;
   private String divisionFilter;

   public HdRecruitPoolExporter( String worldName, boolean includeUnknownRecruits, String decisionStatusFilter, String divisionFilter, CustomOverallWeights overallWeights ) {
      this.world = HdWorld.getByWorldName( worldName );
      this.overallWeights = overallWeights;
      this.includeUnknownRecruits = includeUnknownRecruits;
      this.decisionStatusFilter = decisionStatusFilter;
      this.divisionFilter = divisionFilter;
   }

   public void reportAndSave( String fileLocation ) {
      reportAndSave( fileLocation, WistoolsProperties.getWisUsername(), WistoolsProperties.getWisPassword() );
   }

   public void reportAndSave( String fileLocation, String username, String password ) {
      report( username, password );
      saveReport( fileLocation );
   }

   public void report( String username, String password ) {
      this.username = username;
      try (final WistoolsNavigator nav = new WistoolsNavigator( username, password )) {
         nav.login().loadHdWorld( world );
         buildRecruitList( nav );
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
      doFinalCalculations();
      sortData();
      generateWorkbook();
   }

   private void buildRecruitList( WistoolsNavigator nav ) {
      if( includeUnknownRecruits ) {
         final Range<Integer> recruitIdRange = getRecruitIdRange( nav );
         for( Integer recruitId = recruitIdRange.lowerEndpoint(); recruitId <= recruitIdRange.upperEndpoint(); recruitId++ ) {
            parseRecruitPage( nav, recruitId );
         }
      }
      else {
         for( Integer recruitId : getAllRecruitIdsInRecruitPool( nav ) ) {
            parseRecruitPage( nav, recruitId );
         }
      }
   }

   private void parseRecruitPage( WistoolsNavigator nav, Integer recruitId ) {
      nav.loadPage( WisPage.HD_RecruitProfile_Ratings, recruitId.toString() );
      final HdRecruitRatings recruit = new HdRecruitRatings( recruitId );
      parseRecruitData( nav, recruit );
      if( isFilterExclude( recruit ) ) {
         return;
      }
      parseScoutingLevel( nav, recruit );
      parseOffenseDefense( nav, recruit );
      if( !"0".equals( recruit.getScoutingLevel() ) && !"1".equals( recruit.getScoutingLevel() ) ) {
         parseRecruitRatings( nav, recruit );
         parseRecruitPrefs( nav, recruit );
      }
      parseConsidering( nav, recruit );
      recruits.add( recruit );
   }

   private boolean isFilterExclude( HdRecruitRatings recruit ) {
      final boolean isSigned = !recruit.getSignedWith().isEmpty();
      if( ( "Unsigned".equals( decisionStatusFilter ) && isSigned ) || ( "Signed".equals( decisionStatusFilter ) && !isSigned ) ) {
         return true;
      }
      final String div = recruit.getProjectedDiv();
      if( !"All".equals( divisionFilter ) && div != null && !div.equals( divisionFilter ) ) {
         return true;
      }
      return false;
   }

   private void parseScoutingLevel( WistoolsNavigator nav, HdRecruitRatings recruit ) {
      final DomElement scoutingLevelElement = nav.currentPage().getElementById( WisElement.ID_ScoutingLevel );
      if( scoutingLevelElement != null ) {
         final String scoutingLevel = scoutingLevelElement.getTextContent().replaceAll( "[^0-9]", "" );
         recruit.setScoutingLevel( scoutingLevel );
      }
      else {
         recruit.setScoutingLevel( "0" );//Haven't seen this recruit
      }
   }

   private void parseOffenseDefense( WistoolsNavigator nav, HdRecruitRatings recruit ) {
      final DomElement offenseDefenseElement = nav.currentPage().getElementById( WisElement.ID_PrimaryOffenseDefense );
      if( offenseDefenseElement != null ) {
         final DomElement offenseElement = offenseDefenseElement.getFirstElementChild().getNextElementSibling();
         final DomElement defenseElement = offenseElement.getNextElementSibling().getNextElementSibling();
         recruit.setOffenseSet( offenseElement.getTextContent() );
         recruit.setDefenseSet( defenseElement.getTextContent() );
      }
   }

   private void parseRecruitRatings( WistoolsNavigator nav, HdRecruitRatings recruit ) {
      final DomElement ratingsTabBoxDiv = nav.currentPage().getElementById( WisElement.ID_RecruitProfileNav ).getNextElementSibling();
      final DomElement ratingsTable = ratingsTabBoxDiv.getFirstElementChild().getFirstElementChild().getFirstElementChild();
      final DomElement athleticismRow = ratingsTable.getFirstElementChild().getFirstElementChild();
      recruit.setAthleticism( parseRatingFromRow( athleticismRow, recruit ) );
      recruit.setAthleticism_potential( parsePotentialFromRow( athleticismRow ) );
      final DomElement speedRow = athleticismRow.getNextElementSibling();
      recruit.setSpeed( parseRatingFromRow( speedRow, recruit ) );
      recruit.setSpeed_potential( parsePotentialFromRow( speedRow ) );
      final DomElement reboundingRow = speedRow.getNextElementSibling();
      recruit.setRebounding( parseRatingFromRow( reboundingRow, recruit ) );
      recruit.setRebounding_potential( parsePotentialFromRow( reboundingRow ) );
      final DomElement defenseRow = reboundingRow.getNextElementSibling();
      recruit.setDefense( parseRatingFromRow( defenseRow, recruit ) );
      recruit.setDefense_potential( parsePotentialFromRow( defenseRow ) );
      final DomElement shotBlockingRow = defenseRow.getNextElementSibling();
      recruit.setShotBlocking( parseRatingFromRow( shotBlockingRow, recruit ) );
      recruit.setShotBlocking_potential( parsePotentialFromRow( shotBlockingRow ) );
      final DomElement lowPostRow = shotBlockingRow.getNextElementSibling();
      recruit.setLowPost( parseRatingFromRow( lowPostRow, recruit ) );
      recruit.setLowPost_potential( parsePotentialFromRow( lowPostRow ) );
      final DomElement perimeterRow = lowPostRow.getNextElementSibling();
      recruit.setPerimeter( parseRatingFromRow( perimeterRow, recruit ) );
      recruit.setPerimeter_potential( parsePotentialFromRow( perimeterRow ) );
      final DomElement ballHandlingRow = perimeterRow.getNextElementSibling();
      recruit.setBallHandling( parseRatingFromRow( ballHandlingRow, recruit ) );
      recruit.setBallHandling_potential( parsePotentialFromRow( ballHandlingRow ) );
      final DomElement passingRow = ballHandlingRow.getNextElementSibling();
      recruit.setPassing( parseRatingFromRow( passingRow, recruit ) );
      recruit.setPassing_potential( parsePotentialFromRow( passingRow ) );
      final DomElement workEthicRow = passingRow.getNextElementSibling();
      recruit.setWorkEthic( parseRatingFromRow( workEthicRow, recruit ) );
      final DomElement staminaRow = workEthicRow.getNextElementSibling();
      recruit.setStamina( parseRatingFromRow( staminaRow, recruit ) );
      recruit.setStamina_potential( parsePotentialFromRow( staminaRow ) );
      final DomElement durabilityRow = staminaRow.getNextElementSibling();
      recruit.setDurability( parseRatingFromRow( durabilityRow, recruit ) );
      recruit.setDurability_potential( parsePotentialFromRow( durabilityRow ) );
      final DomElement ftShootingRow = durabilityRow.getNextElementSibling();
      recruit.setFtShooting( parseRatingFromRow( ftShootingRow, recruit ) );
      recruit.setFtShooting_potential( parsePotentialFromRow( ftShootingRow ) );
   }

   private Integer parseRatingFromRow( DomElement row, HdRecruitRatings recruit ) {
      final DomElement ratingTd = row.getFirstElementChild().getNextElementSibling();
      final String ratingStr = ratingTd.getFirstElementChild().getTextContent();
      try {
         return Integer.parseInt( ratingStr );
      }
      catch( NumberFormatException e ) {}
      //For scouting level 2&3, and FT Shooting
      switch( ratingStr ) {
      case "A+":
         return 88;
      case "A":
         return 82;
      case "A-":
         return 76;
      case "B+":
         return 71;
      case "B":
         return 66;
      case "B-":
         return 60;
      case "C+":
         return 55;
      case "C":
         return 50;
      case "C-":
         return 44;
      case "D+":
         return 39;
      case "D":
         return 34;
      case "D-":
         return 28;
      case "F+":
         return 23;
      case "F":
         return 18;
      case "F-":
         return 8;
      default:
         return null;
      }
   }

   private String parsePotentialFromRow( DomElement row ) {
      final DomElement ratingTd = row.getFirstElementChild().getNextElementSibling();
      return ratingTd.getFirstElementChild().getAttribute( "title" );
   }

   private void parseRecruitPrefs( WistoolsNavigator nav, HdRecruitRatings recruit ) {
      nav.loadPage( WisPage.HD_RecruitProfile_Preferences, recruit.getRecruitId().toString() );
      recruit.setDistance( Integer.parseInt( nav.currentPage().getElementById( WisElement.ID_RecruitDistance ).getTextContent().replaceAll( "[^0-9]", "" ) ) );
      final String playingTimePref = nav.currentPage().getElementById( WisElement.ID_PlayingTimePref ).getTextContent();
      recruit.setPlayingTimePref( "No Preference".equals( playingTimePref ) ? "-" : playingTimePref );
      final String distancePref = nav.currentPage().getElementById( WisElement.ID_DistancePref ).getTextContent();
      recruit.setDistancePref( distancePref.substring( 0, distancePref.indexOf( " (" ) ) );
      recruit.setSuccessPref( nav.currentPage().getElementById( WisElement.ID_SuccessPref ).getTextContent() );
      recruit.setPlayStylePref( nav.currentPage().getElementById( WisElement.ID_PlayStylePref ).getTextContent() );
      recruit.setOffensePref( nav.currentPage().getElementById( WisElement.ID_OffensePref ).getTextContent() );
      recruit.setDefensePref( nav.currentPage().getElementById( WisElement.ID_DefensePref ).getTextContent() );
      recruit.setConfStrengthPref( nav.currentPage().getElementById( WisElement.ID_ConferenceStrengthPref ).getTextContent() );
      recruit.setCoachLongevityPref( nav.currentPage().getElementById( WisElement.ID_CoachLongevityPref ).getTextContent() );
      recruit.setSigningPref( nav.currentPage().getElementById( WisElement.ID_SigningPref ).getTextContent() );
   }

   private void parseConsidering( WistoolsNavigator nav, HdRecruitRatings recruit ) {
      nav.loadPage( WisPage.HD_RecruitProfile_Considering, recruit.getRecruitId().toString() );
      StringBuilder consideringText = new StringBuilder();
      for( HtmlAnchor anchor : nav.currentPage().getAnchors() ) {
         if( "Open Team Profile".equals( anchor.getAttribute( "title" ) )
               && anchor.getParentNode() != null && anchor.getParentNode() instanceof HtmlTableDataCell ) {
            final String team = anchor.getTextContent();
            final DomElement coachTd = ( ( HtmlTableDataCell )anchor.getParentNode() ).getNextElementSibling();
            final String coach = coachTd.getFirstElementChild() != null ? coachTd.getFirstElementChild().getTextContent() : coachTd.getTextContent().trim();
            final DomElement divTd = coachTd.getNextElementSibling();
            final String div = divTd.getTextContent();
            final DomElement prestigeTd = divTd.getNextElementSibling();
            final String prestige = prestigeTd.getTextContent();
            final DomElement interestTd = prestigeTd.getNextElementSibling();
            final String interest = interestTd.getTextContent();
            final DomElement scholarshipTd = interestTd.getNextElementSibling();
            final String scholarship = scholarshipTd.getTextContent();
            if( "Very High".equals( interest ) || "High".equals( interest ) || "Moderate".equals( interest ) ) {
               consideringText.append( "[" + team + "|" + coach + "|" + div + "|" + prestige + "|" + interest + "|" + scholarship + "], " );
            }
         }
      }
      if( consideringText.length() > 0 ) {
         recruit.setConsidering( consideringText.substring( 0, consideringText.length() - 2 ) );
      }
   }

   private void doFinalCalculations() {
      for( HdRecruitRatings recruit : recruits ) {
         if( !"0".equals( recruit.getScoutingLevel() ) && !"1".equals( recruit.getScoutingLevel() ) ) {
            recruit.doFinalCalculations( overallWeights );
         }
      }
   }

   public void sortData() {
      recruits.sort( new Comparator<HdRecruitRatings>() {
         @Override
         public int compare( HdRecruitRatings r1, HdRecruitRatings r2 ) {
            // -1=r1 before, 0=even, 1=r1 after

            //Projected Overall sort
            final int overallCompare = ObjectUtils.compare( r2.getCustomOverall(), r1.getCustomOverall() );
            if( overallCompare != 0 ) {
               return overallCompare;
            }

            //Position Rank sort
            final Integer r1PositionRank = r1.getPositionRank() != null && !r1.getPositionRank().isEmpty() ? Integer.parseInt( r1.getPositionRank() ) : null;
            final Integer r2PositionRank = r2.getPositionRank() != null && !r2.getPositionRank().isEmpty() ? Integer.parseInt( r2.getPositionRank() ) : null;

            if( r1PositionRank != null && r2PositionRank == null ) {
               return -1;
            }
            else if( r1PositionRank == null && r2PositionRank != null ) {
               return 1;
            }
            else {
               final int positionRankCompare = ObjectUtils.compare( r1PositionRank, r2PositionRank );
               if( positionRankCompare != 0 ) {
                  return positionRankCompare;
               }
            }

            //Division sort
            final int divisionCompare = ObjectUtils.compare( r1.getProjectedDiv(), r2.getProjectedDiv() );
            if( divisionCompare != 0 ) {
               return divisionCompare;
            }

            //Add other sorts here if necessary
            return 0;
         }
      } );

      recruitMap.put( "Scouted 2+", recruits.stream()
            .filter( r -> "2".equals( r.getScoutingLevel() )
                  || "3".equals( r.getScoutingLevel() )
                  || "4".equals( r.getScoutingLevel() ) )
            .collect( Collectors.toList() ) );
      recruitMap.put( "Level 4", recruits.stream().filter( r -> "4".equals( r.getScoutingLevel() ) ).collect( Collectors.toList() ) );
      recruitMap.put( "Level 3", recruits.stream().filter( r -> "3".equals( r.getScoutingLevel() ) ).collect( Collectors.toList() ) );
      recruitMap.put( "Level 2", recruits.stream().filter( r -> "2".equals( r.getScoutingLevel() ) ).collect( Collectors.toList() ) );
      recruitMap.put( "Level 1", recruits.stream().filter( r -> "1".equals( r.getScoutingLevel() ) ).collect( Collectors.toList() ) );
      recruitMap.put( "Level 0", recruits.stream().filter( r -> "0".equals( r.getScoutingLevel() ) ).collect( Collectors.toList() ) );
   }

   private void generateWorkbook() {
      workbook = ExcelUtl.buildWorkbookFromDataMap( recruitMap, HdRecruitRatings.class, 0 );
   }

   private void saveReport( String fileLocation ) {
      try (OutputStream os = new FileOutputStream( fileLocation )) {
         workbook.write( os );
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
   }

   @Override
   public Workbook getWorkbook() {
      return workbook;
   }

   @Override
   public String getServerFolderName() {
      return "HdRecruitExport";
   }

   public String getReportName() {
      return "RecruitExport_" + EmailUtl.getNamePart( username ) + "_" + FormatUtl.stripNonAlphaNumeric( world.getWorldName() ) + "_" + DateUtl.formatTimestampForFileName( new Date() ) + ".xlsx";
   }

}
