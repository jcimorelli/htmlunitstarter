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
import net.wistools.data.HdPlayerRatings;
import net.wistools.data.HdTeam;
import net.wistools.nav.IWistoolsExcelCreator;
import net.wistools.nav.WisElement;
import net.wistools.nav.WisPage;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.utl.DateUtl;
import net.wistools.utl.EmailUtl;
import net.wistools.utl.ExcelUtl;
import net.wistools.utl.FormatUtl;
import net.wistools.utl.ValidateUtl;

import org.apache.poi.ss.usermodel.Workbook;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;

public class HdTransferSearcher implements IWistoolsExcelCreator {

   private HdWorld world;
   private List<HdPlayerRatings> potentialTransfers = new ArrayList<>();
   private Workbook workbook;
   private String username;

   public HdTransferSearcher( HdWorld world ) {
      this.world = world;
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
         final List<HdTeam> allTeams = new HdTeamDetailsFinder( world ).getAllTeamsForMyDiv( nav );
         for( HdTeam team : allTeams ) {
            searchTeam( nav, team );
         }
         potentialTransfers.forEach( pt -> pt.populateCurrentRatings( nav ) );
         potentialTransfers = potentialTransfers.stream().filter( pt -> !pt.isGraduating() ).collect( Collectors.toList() );
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
      sortData();
      generateWorkbook();
   }

   private void searchTeam( WistoolsNavigator nav, HdTeam team ) {
      nav.loadPage( WisPage.HD_TeamProfile_Ratings, String.valueOf( team.getTeamId() ) );
      final DomElement ratingsChangeHeaderRow = ( DomElement )nav.currentPage().getElementById( WisElement.ID_RatingsChangeTableTotalHeader ).getParentNode();
      DomElement nextSibling = ratingsChangeHeaderRow.getNextElementSibling();
      while( !"highlight sortbottom".equals( nextSibling.getAttribute( "class" ) ) ) {
         checkPlayerRow( nav, nextSibling );
         nextSibling = nextSibling.getNextElementSibling();
      }
   }

   private void checkPlayerRow( WistoolsNavigator nav, DomElement playerRow ) {
      final DomElement workEthicChangeTd = ( DomElement )playerRow.getFirstElementChild()
            .getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling()
            .getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling();
      final boolean isPlayerUpset = workEthicChangeTd.getTextContent().trim().contains( "-" );
      if( isPlayerUpset ) {
         final HtmlAnchor playerAnchor = ( HtmlAnchor )playerRow.getFirstElementChild().getFirstElementChild();
         final String pid = playerAnchor.getHrefAttribute().substring( playerAnchor.getHrefAttribute().indexOf( "pid=" ) + 4 );
         final HdPlayerRatings pt = new HdPlayerRatings( Integer.parseInt( pid ) );
         final String teamName = nav.currentPage().getElementById( WisElement.ID_TeamLinkSpan ).getFirstElementChild().getTextContent().trim();
         pt.setTeam( teamName );
         final String divisionAndCity = nav.currentPage().getElementById( WisElement.ID_DivisionAndCity ).getTextContent().trim();
         pt.setDivisionAndCity( divisionAndCity );

         pt.setWorkEthicChange( Integer.parseInt( workEthicChangeTd.getTextContent().trim() ) );

         final DomElement staminaChangeTd = workEthicChangeTd.getNextElementSibling();
         pt.setStaminaChange( Integer.parseInt( staminaChangeTd.getTextContent().trim() ) );
         final DomElement durabilityChangeTd = staminaChangeTd.getNextElementSibling();
         pt.setDurabilityChange( Integer.parseInt( durabilityChangeTd.getTextContent().trim() ) );
         final DomElement overallChangeTd = durabilityChangeTd.getNextElementSibling().getNextElementSibling();
         pt.setOverallChange( Integer.parseInt( overallChangeTd.getTextContent().trim() ) );

         final DomElement passingChangeTd = workEthicChangeTd.getPreviousElementSibling();
         pt.setPassingChange( Integer.parseInt( passingChangeTd.getTextContent().trim() ) );
         final DomElement ballHandlingChangeTd = passingChangeTd.getPreviousElementSibling();
         pt.setBallHandlingChange( Integer.parseInt( ballHandlingChangeTd.getTextContent().trim() ) );
         final DomElement perimeterChangeTd = ballHandlingChangeTd.getPreviousElementSibling();
         pt.setPerimeterChange( Integer.parseInt( perimeterChangeTd.getTextContent().trim() ) );
         final DomElement lowPostChangeTd = perimeterChangeTd.getPreviousElementSibling();
         pt.setLowPostChange( Integer.parseInt( lowPostChangeTd.getTextContent().trim() ) );
         final DomElement shotBlockingChangeTd = lowPostChangeTd.getPreviousElementSibling();
         pt.setShotBlockingChange( Integer.parseInt( shotBlockingChangeTd.getTextContent().trim() ) );
         final DomElement defenseChangeTd = shotBlockingChangeTd.getPreviousElementSibling();
         pt.setDefenseChange( Integer.parseInt( defenseChangeTd.getTextContent().trim() ) );
         final DomElement reboundingChangeTd = defenseChangeTd.getPreviousElementSibling();
         pt.setReboundingChange( Integer.parseInt( reboundingChangeTd.getTextContent().trim() ) );
         final DomElement speedChangeTd = reboundingChangeTd.getPreviousElementSibling();
         pt.setSpeedChange( Integer.parseInt( speedChangeTd.getTextContent().trim() ) );
         final DomElement athleticismChangeTd = speedChangeTd.getPreviousElementSibling();
         pt.setAthleticismChange( Integer.parseInt( athleticismChangeTd.getTextContent().trim() ) );

         potentialTransfers.add( pt );
      }
   }

   private void sortData() {
      potentialTransfers.sort( new Comparator<HdPlayerRatings>() {
         @Override
         public int compare( HdPlayerRatings p1, HdPlayerRatings p2 ) {
            return p2.compareTo( p1 );
         }
      } );
   }

   private void generateWorkbook() {
      final Map<String, List<HdPlayerRatings>> dataMap = new LinkedHashMap<>();
      dataMap.put( "Potential Transfers", potentialTransfers );
      workbook = ExcelUtl.buildWorkbookFromDataMap( dataMap, HdPlayerRatings.class, 0 );
   }

   @Override
   public Workbook getWorkbook() {
      return workbook;
   }

   @Override
   public String getServerFolderName() {
      return "TransferReport";
   }

   @Override
   public String getReportName() {
      return "TransferReport_" + EmailUtl.getNamePart( username ) + "_" + FormatUtl.stripNonAlphaNumeric( world.getWorldName() ) + "_" + DateUtl.formatTimestampForFileName( new Date() ) + ".xlsx";
   }

   private void saveReport( String fileLocation ) {
      try (OutputStream os = new FileOutputStream( fileLocation )) {
         workbook.write( os );
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
   }

}
