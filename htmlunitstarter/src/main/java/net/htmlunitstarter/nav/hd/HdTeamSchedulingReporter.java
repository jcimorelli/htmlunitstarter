package net.wistools.nav.hd;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.wistools.config.WistoolsProperties;
import net.wistools.data.HdTeam;
import net.wistools.nav.IWistoolsExcelCreator;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.utl.DateUtl;
import net.wistools.utl.EmailUtl;
import net.wistools.utl.ExcelUtl;
import net.wistools.utl.FormatUtl;
import net.wistools.utl.ValidateUtl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Workbook;

public class HdTeamSchedulingReporter implements IWistoolsExcelCreator {

   private HdWorld world;
   private String username;
   private List<HdTeam> teams;
   private Workbook workbook;

   public HdTeamSchedulingReporter( HdWorld world ) {
      this.world = world;
   }

   public HdTeamSchedulingReporter( String worldName ) {
      this.world = HdWorld.getByWorldName( worldName );
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
         nav.login();
         final HdTeamDetailsFinder teamFinder = new HdTeamDetailsFinder( world );
         teamFinder.populateTeamDetails( nav, false, true, false );
         teams = teamFinder.getTeamList();
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
      sortData();
      generateWorkbook();
   }

   private void sortData() {
      teams.sort( new Comparator<HdTeam>() {
         @Override
         public int compare( HdTeam t1, HdTeam t2 ) {
            // -1=r1 before, 0=even, 1=r1 after

            //Openings Compare
            final int departuresCompare = ObjectUtils.compare( t2.getTotalDepartures(), t1.getTotalDepartures() );
            if( departuresCompare != 0 ) {
               return departuresCompare;
            }

            //Wins Compare
            final Integer t1Wins = Integer.parseInt( t1.getRecord().substring( 0, t1.getRecord().indexOf( "-" ) ) );
            final Integer t2Wins = Integer.parseInt( t2.getRecord().substring( 0, t2.getRecord().indexOf( "-" ) ) );
            final int winsCompare = ObjectUtils.compare( t2Wins, t1Wins );
            if( winsCompare != 0 ) {
               return winsCompare;
            }

            //RPI Compare
            final Integer t1Rpi = t1.getRpiRank() != null ? Integer.parseInt( t1.getRpiRank() ) : null;
            final Integer t2Rpi = t2.getRpiRank() != null ? Integer.parseInt( t2.getRpiRank() ) : null;
            final int rpiCompare = ObjectUtils.compare( t1Rpi, t2Rpi );
            if( rpiCompare != 0 ) {
               return rpiCompare;
            }

            //Add other sorts here if necessary
            return 0;
         }
      } );
   }

   private void generateWorkbook() {
      final Map<String, List<HdTeam>> dataMap = new LinkedHashMap<>();
      dataMap.put( "Teams", teams );
      workbook = ExcelUtl.buildWorkbookFromDataMap( dataMap, HdTeam.class, 0 );
   }

   @Override
   public Workbook getWorkbook() {
      return workbook;
   }

   @Override
   public String getServerFolderName() {
      return "ScheduleReport";
   }

   @Override
   public String getReportName() {
      return "ScheduleReport_" + EmailUtl.getNamePart( username ) + "_" + FormatUtl.stripNonAlphaNumeric( world.getWorldName() ) + "_" + DateUtl.formatTimestampForFileName( new Date() ) + ".xlsx";
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
