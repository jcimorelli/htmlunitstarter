package net.wistools.nav.hd;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Workbook;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.collect.Range;

import net.wistools.config.WistoolsProperties;
import net.wistools.data.HdRecruitInterest;
import net.wistools.data.HdTeam;
import net.wistools.nav.IWistoolsExcelCreator;
import net.wistools.nav.WisPage;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.utl.DateUtl;
import net.wistools.utl.EmailUtl;
import net.wistools.utl.ExcelUtl;
import net.wistools.utl.FormatUtl;
import net.wistools.utl.LoggingUtl;
import net.wistools.utl.ValidateUtl;

public class HdTeamRecruitingReporter extends AbstractHdRecruitSearcher implements IWistoolsExcelCreator {

   private HdWorld world;
   private List<String> teamNames = new ArrayList<>();
   private Map<String, List<HdRecruitInterest>> recruitMap = new HashMap<>();
   private List<HdTeam> teamList = new ArrayList<>();
   private Workbook workbook;
   private String username;

   public HdTeamRecruitingReporter( String worldName, String... teamNames ) {
      this.world = HdWorld.getByWorldName( worldName );
      for( String teamName : teamNames ) {
         if( teamName != null && !teamName.trim().isEmpty() ) {
            this.teamNames.add( teamName.trim() );
         }
      }
      ValidateUtl.emptyCheck( this.teamNames, "No Teams Listed!" );
   }

   public HdTeamRecruitingReporter( HdWorld world, List<HdTeam> teamList ) {
      ValidateUtl.emptyCheck( teamList, "No Teams Provided!" );
      this.world = world;
      this.teamList = teamList;
      for( HdTeam team : teamList ) {
         teamNames.add( team.getTeamName() );
      }
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
         buildTeamList( nav );
         buildRecruitMap( nav );
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
      sortData();
      generateWorkbook();
   }

   private void buildTeamList( WistoolsNavigator nav ) {
      if( teamList.isEmpty() ) {//Could have been provided to the constructor
         //Populate Team List
         for( String teamName : teamNames ) {
            teamList.add( new HdTeam( teamName ) );
         }
         //Load Data for each team
         populateTeamDetails( nav );
      }
      populateEEs( nav );
      teamList.forEach( team -> team.doFinalCalculations() );
   }

   private void populateTeamDetails( WistoolsNavigator nav ) {
      new HdTeamDetailsFinder( world, teamList ).populateTeamDetails( nav, true, false, false );
   }

   private void populateEEs( WistoolsNavigator nav ) {
      populateExpectedEEs( nav );
      populateDataForActualEEs( nav );
   }

   private void populateExpectedEEs( WistoolsNavigator nav ) {
      final HtmlPage draftPage = nav.loadPage( WisPage.HD_DraftBigBoard ).currentPage();
      for( HtmlAnchor anchor : draftPage.getAnchors() ) {
         if( "Open Team Profile".equals( anchor.getAttribute( "title" ) ) ) {
            for( HdTeam team : teamList ) {
               if( team.getTeamName().equals( anchor.getTextContent() ) ) {
                  if( ( ( DomElement )anchor.getParentNode() ).getNextElementSibling() != null && ( ( DomElement )anchor.getParentNode() ).getNextElementSibling().getNextElementSibling() != null ) {//This only happens when including your own team
                     final DomElement outlookElement = ( ( HtmlElement )anchor.getParentNode() ).getNextElementSibling().getNextElementSibling().getNextElementSibling();
                     if( "Likely going".equals( outlookElement.getTextContent().trim() ) ) {
                        team.setLikelyEarlyEntries( team.getLikelyEarlyEntries() + 1 );
                     }
                  }
               }
            }
         }
      }
   }

   private void populateDataForActualEEs( WistoolsNavigator nav ) {
      final HtmlPage recruitClassPage = nav.loadPage( WisPage.HD_RecruitClassRanks_D1 ).currentPage();
      final Map<String, Integer> signingsMap = new HashMap<>();
      final Map<String, Integer> openingsMap = new HashMap<>();
      for( HtmlAnchor anchor : recruitClassPage.getAnchors() ) {
         if( "Open Team Profile".equals( anchor.getAttribute( "title" ) ) ) {
            final DomElement teamTd = ( DomElement )anchor.getParentNode();
            if( teamTd != null && "left".equals( teamTd.getAttribute( "class" ) ) ) {
               final String teamName = anchor.getTextContent();
               final DomElement signingsTd = teamTd.getNextElementSibling().getNextElementSibling();
               final DomElement openingsTd = signingsTd.getNextElementSibling();
               signingsMap.put( teamName, Integer.parseInt( signingsTd.getTextContent() ) );
               openingsMap.put( teamName, Integer.parseInt( openingsTd.getTextContent() ) );
            }
         }
      }
      for( HdTeam team : teamList ) {
         team.setSignings( signingsMap.get( team.getTeamName() ) );
         team.setCurrentOpenings( openingsMap.get( team.getTeamName() ) );
      }
   }

   private void buildRecruitMap( WistoolsNavigator nav ) {
      //Populate Recruit Map
      for( String teamName : teamNames ) {
         recruitMap.put( teamName, new ArrayList<HdRecruitInterest>() );
      }
      //Load and parse each recruit in pool
      parseRecruitPages( nav );
   }

   private void parseRecruitPages( WistoolsNavigator nav ) {
      final Range<Integer> recruitIdRange = getRecruitIdRange( nav );
      for( Integer recruitId = recruitIdRange.lowerEndpoint(); recruitId <= recruitIdRange.upperEndpoint(); recruitId++ ) {
         parseRecruitPage( nav, recruitId );
      }
   }

   private void parseRecruitPage( WistoolsNavigator nav, Integer recruitId ) {
      nav.loadPage( WisPage.HD_RecruitProfile_Considering, recruitId.toString() );

      //Parse and add recruit for each team
      for( String teamName : teamNames ) {
         final String htmlTeamName = StringEscapeUtils.escapeHtml( teamName );
         if( nav.currentPage().asXml().contains( htmlTeamName ) ) {//This could return false positive (like UMass, Lowell instead of UMass), which is why we need the teamAnchor null check below too
            HtmlAnchor teamAnchor = null;
            for( HtmlAnchor anchor : nav.currentPage().getAnchors() ) {
               if( anchor.getTextContent().equals( teamName ) ) {
                  teamAnchor = anchor;
                  break;
               }
            }
            if( teamAnchor != null ) {
               final HdRecruitInterest recruit = new HdRecruitInterest( recruitId );
               parseRecruitData( nav, recruit );
               final DomElement interestLevelElement = ( ( HtmlElement )teamAnchor.getParentNode() ).getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling();
               final DomElement scholarshipOfferElement = interestLevelElement.getNextElementSibling();
               recruit.setInterestLevel( interestLevelElement.getTextContent() );
               recruit.setScholarshipOffer( scholarshipOfferElement.getTextContent() );
               recruitMap.get( teamName ).add( recruit );
               LoggingUtl.log( recruit.getRecruitName() + " is considering " + teamName );
            }
         }
      }
   }

   private void generateWorkbook() {
      workbook = ExcelUtl.buildWorkbookFromDataMap( recruitMap, HdRecruitInterest.class, 3 );
      for( HdTeam team : teamList ) {
         ExcelUtl.addIndividualDataField( workbook, team.getTeamName(), 0, 0, "Coach", FormatUtl.defaultValue( team.getCoach(), "Sim AI" ) );
         ExcelUtl.addIndividualDataField( workbook, team.getTeamName(), 0, 1, "Prestige", team.getPrestige() );
         ExcelUtl.addIndividualDataField( workbook, team.getTeamName(), 0, 2, "Departing Seniors", team.getDepartingSeniors() );
         ExcelUtl.addIndividualDataField( workbook, team.getTeamName(), 0, 3, "Walkons", team.getWalkons() );
         ExcelUtl.addIndividualDataField( workbook, team.getTeamName(), 0, 4, "Likely EEs", team.getLikelyEarlyEntries() );
         ExcelUtl.addIndividualDataField( workbook, team.getTeamName(), 0, 5, "Actual EEs", team.getActualEarlyEntries() );
         ExcelUtl.addIndividualDataField( workbook, team.getTeamName(), 0, 6, "Cuts", team.getPlayersCut() );
      }
      ExcelUtl.formatColumns( workbook, 6 );//Reformat columns after adding individual fields
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
      return "HdRecruitingReports";
   }

   public String getReportName() {
      return "RecruitingSummary_" + EmailUtl.getNamePart( username ) + "_" + FormatUtl.stripNonAlphaNumeric( world.getWorldName() ) + "_" + DateUtl.formatTimestampForFileName( new Date() ) + ".xlsx";
   }

   public void sortData() {
      // Sort Recruits
      final Map<String, Integer> interestLevelMap = new HashMap<>();
      interestLevelMap.put( "Very High", 5 );
      interestLevelMap.put( "High", 4 );
      interestLevelMap.put( "Moderate", 3 );
      interestLevelMap.put( "Low", 2 );
      interestLevelMap.put( "Very Low", 1 );
      final Map<String, Integer> positionMap = new HashMap<>();
      positionMap.put( "PG", 1 );
      positionMap.put( "SG", 2 );
      positionMap.put( "SF", 3 );
      positionMap.put( "PF", 4 );
      positionMap.put( "C", 5 );
      for( List<HdRecruitInterest> recruitList : recruitMap.values() ) {
         recruitList.sort( new Comparator<HdRecruitInterest>() {
            @Override
            public int compare( HdRecruitInterest r1, HdRecruitInterest r2 ) {
               // -1=r1 before, 0=even, 1=r1 after
               if( "Yes".equals( r1.getScholarshipOffer() ) && "No".equals( r2.getScholarshipOffer() ) ) {
                  return -1;
               }
               if( "No".equals( r1.getScholarshipOffer() ) && "Yes".equals( r2.getScholarshipOffer() ) ) {
                  return 1;
               }
               if( interestLevelMap.get( r1.getInterestLevel() ) > interestLevelMap.get( r2.getInterestLevel() ) ) {
                  return -1;
               }
               if( interestLevelMap.get( r1.getInterestLevel() ) < interestLevelMap.get( r2.getInterestLevel() ) ) {
                  return 1;
               }
               if( positionMap.get( r1.getPosition() ) < positionMap.get( r2.getPosition() ) ) {
                  return -1;
               }
               if( positionMap.get( r1.getPosition() ) > positionMap.get( r2.getPosition() ) ) {
                  return 1;
               }
               return 0;
            }
         } );
      }

      // Sort Excel Tabs
      teamNames.sort( new Comparator<String>() {

         @Override
         public int compare( String t1, String t2 ) {
            return t1.compareTo( t2 );
         }
      } );
      final Map<String, List<HdRecruitInterest>> orderedRecruitMap = new LinkedHashMap<>();
      for( String name : teamNames ) {
         orderedRecruitMap.put( name, recruitMap.get( name ) );
      }
      recruitMap = orderedRecruitMap;
   }
}
