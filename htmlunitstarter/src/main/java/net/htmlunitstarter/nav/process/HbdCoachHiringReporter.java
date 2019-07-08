package net.wistools.nav.hbd;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.wistools.config.WistoolsProperties;
import net.wistools.data.HbdCoach;
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

public class HbdCoachHiringReporter implements IWistoolsExcelCreator {

   private HbdWorld world;
   private List<HbdCoach> coachList = new ArrayList<>();
   private Map<String, List<HbdCoach>> coachReport = new LinkedHashMap<>();
   private String username;
   private Workbook workbook;

   private static final int PATIENCE_PER_IQ_POINT = 7;

   public HbdCoachHiringReporter( String worldName ) {
      world = HbdWorld.getByWorldName( worldName );
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
         nav.login().loadHbdWorld( world );
         gatherCoachData( nav );
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
      doCalculations();
      sortDataIntoReport();
      generateWorkbook();
   }

   private void gatherCoachData( WistoolsNavigator nav ) {
      nav.loadPage( WisPage.HBD_HireCoaches )
            .setComboByText( WisElement.ID_CoachTypeCombo, "Available Coaches" )
            .setComboByText( WisElement.ID_CoachRoleCombo, "All" );
      addCoachesToList( nav, "ML" );
      addCoachesToList( nav, "AAA" );
      addCoachesToList( nav, "AA" );
      addCoachesToList( nav, "High A" );
      addCoachesToList( nav, "Low A" );
      addCoachesToList( nav, "Rookie" );
   }

   private void addCoachesToList( WistoolsNavigator nav, String level ) {
      nav.setComboByText( WisElement.ID_CoachLevelCombo, level ).clickElementById( WisElement.ID_GoButton2 );
      final DomElement availableCoachesTable = nav.currentPage().getElementById( WisElement.ID_AvailableCoachesTable );
      for( DomElement tr : availableCoachesTable.getFirstElementChild().getChildElements() ) {
         if( !"header".equals( tr.getAttribute( "class" ) ) ) {
            coachList.add( parseCoachRow( tr ) );
         }
      }
   }

   private HbdCoach parseCoachRow( DomElement tr ) {
      final DomElement coachNameTd = tr.getFirstElementChild();
      final DomElement coachAnchor = coachNameTd.getFirstElementChild();
      final String href = coachAnchor.getAttribute( "href" );
      final String coachIdStr = href.substring( href.indexOf( "cid=" ) + 4 );
      final HbdCoach coach = new HbdCoach( Integer.parseInt( coachIdStr ) );
      coach.setCoachName( coachAnchor.getTextContent().replaceAll( "\\*", "" ).trim() );
      final DomElement ageTd = coachNameTd.getNextElementSibling().getNextElementSibling();
      coach.setAge( parseIntegerElement( ageTd ) );
      final DomElement experienceTd = ageTd.getNextElementSibling();
      coach.setExperience( parseIntegerElement( experienceTd ) );
      final DomElement levelTd = experienceTd.getNextElementSibling().getNextElementSibling();
      coach.setLevelDemand( levelTd.getTextContent() );
      final DomElement roleTd = levelTd.getNextElementSibling();
      coach.setRoleDemand( roleTd.getTextContent() );
      final DomElement salaryTd = roleTd.getNextElementSibling();
      coach.setSalaryDemand( parseNumberElement( salaryTd ) );
      final DomElement hittingIqTd = salaryTd.getNextElementSibling();
      coach.setHittingIq( parseIntegerElement( hittingIqTd ) );
      final DomElement pitchingIqTd = hittingIqTd.getNextElementSibling();
      coach.setPitchingIq( parseIntegerElement( pitchingIqTd ) );
      final DomElement fieldingIqTd = pitchingIqTd.getNextElementSibling();
      coach.setFieldingIq( parseIntegerElement( fieldingIqTd ) );
      final DomElement baserunnningIqTd = fieldingIqTd.getNextElementSibling();
      coach.setBaserunningIq( parseIntegerElement( baserunnningIqTd ) );
      final DomElement patienceTd = baserunnningIqTd.getNextElementSibling();
      coach.setPatience( parseIntegerElement( patienceTd ) );
      final DomElement strategyTd = patienceTd.getNextElementSibling();
      coach.setStrategy( parseIntegerElement( strategyTd ) );
      final DomElement disciplineTd = strategyTd.getNextElementSibling();
      coach.setDiscipline( parseIntegerElement( disciplineTd ) );
      final DomElement loyaltyTd = disciplineTd.getNextElementSibling();
      coach.setLoyalty( parseIntegerElement( loyaltyTd ) );
      final DomElement interestTd = loyaltyTd.getNextElementSibling();
      coach.setInterestedFranchises( interestTd.getTextContent() );
      return coach;
   }

   private Integer parseIntegerElement( DomElement intElement ) {
      return Integer.parseInt( intElement.getTextContent() );
   }

   private BigDecimal parseNumberElement( DomElement numberElement ) {
      String numStr = numberElement.getTextContent().replace( "$", "" ).replace( ",", "" );
      BigDecimal multiplier = BigDecimal.ONE;
      multiplier = numStr.contains( "M" ) ? BigDecimal.valueOf( 1000000 ) : multiplier;
      multiplier = numStr.contains( "K" ) ? BigDecimal.valueOf( 1000 ) : multiplier;
      numStr = numStr.replace( "M", "" ).replace( "K", "" );
      return BigDecimal.valueOf( Double.parseDouble( numStr ) ).multiply( multiplier );
   }

   private void doCalculations() {
      for( HbdCoach coach : coachList ) {
         coach.setHittingCoachAbility( coach.getHittingIq() + coach.getPatience() / PATIENCE_PER_IQ_POINT );
         coach.setPitchingCoachAbility( coach.getPitchingIq() + coach.getPatience() / PATIENCE_PER_IQ_POINT );
         coach.setFieldingCoachAbility( coach.getFieldingIq() + coach.getPatience() / PATIENCE_PER_IQ_POINT );
         coach.setBenchCoachAbility(
               ( coach.getHittingIq() * 3 +
                     coach.getPitchingIq() * 3 +
                     coach.getBaserunningIq() +
                     coach.getFieldingIq() ) / 8 + coach.getPatience() / PATIENCE_PER_IQ_POINT );
         coach.setBaseCoachAbility(
               ( coach.getHittingIq() * 2 + coach.getBaserunningIq() ) / 3 + coach.getPatience() / PATIENCE_PER_IQ_POINT );
      }
   }

   //Coach Sort Exclusion
   private static class CSE {
      String level;
      String role;

      public CSE( String level, String role ) {
         this.level = level;
         this.role = role;
      }
   }

   private void sortDataIntoReport() {
      coachReport.put( "ML HC", sortForHitting( coachList ) );
      coachReport.put( "ML PC", sortForPitching( coachList ) );
      coachReport.put( "ML BC", sortForBench( filterCoaches( new CSE( "ML", "HC" ), new CSE( "ML", "PC" ) ) ) );
      coachReport.put( "ML FC", sortForFielding( filterCoaches( new CSE( "ML", "HC" ), new CSE( "ML", "PC" ) ) ) );
      coachReport.put( "ML BU", sortForPitching( filterCoaches( new CSE( "ML", "PC" ), new CSE( "ML", "HC" ), new CSE( "ML", "BC" ) ) ) );
      coachReport.put( "ML 1B", sortForBaseCoach( filterCoaches( new CSE( "ML", "PC" ), new CSE( "ML", "HC" ), new CSE( "ML", "BC" ), new CSE( "ML", "3B" ) ) ) );
      coachReport.put( "ML 3B", sortForBaseCoach( filterCoaches( new CSE( "ML", "PC" ), new CSE( "ML", "HC" ), new CSE( "ML", "BC" ) ) ) );
      coachReport.put( "AAA HC", sortForHitting( filterCoaches( new CSE( "ML", "*" ) ) ) );
      coachReport.put( "AAA PC", sortForPitching( filterCoaches( new CSE( "ML", "*" ) ) ) );
      coachReport.put( "AAA BC", sortForBench( filterCoaches( new CSE( "ML", "*" ) ) ) );
      coachReport.put( "AA HC", sortForHitting( filterCoaches( new CSE( "ML", "*" ), new CSE( "AAA", "*" ) ) ) );
      coachReport.put( "AA PC", sortForPitching( filterCoaches( new CSE( "ML", "*" ), new CSE( "AAA", "*" ) ) ) );
      coachReport.put( "AA BC", sortForBench( filterCoaches( new CSE( "ML", "*" ), new CSE( "AAA", "*" ) ) ) );
      coachReport.put( "HiA HC", sortForHitting( filterCoaches( new CSE( "ML", "*" ), new CSE( "AAA", "*" ), new CSE( "AA", "*" ) ) ) );
      coachReport.put( "HiA PC", sortForPitching( filterCoaches( new CSE( "ML", "*" ), new CSE( "AAA", "*" ), new CSE( "AA", "*" ) ) ) );
      coachReport.put( "HiA BC", sortForBench( filterCoaches( new CSE( "ML", "*" ), new CSE( "AAA", "*" ), new CSE( "AA", "*" ) ) ) );
      coachReport.put( "LoA HC", sortForHitting( filterCoaches( new CSE( "ML", "*" ), new CSE( "AAA", "*" ), new CSE( "AA", "*" ), new CSE( "HiA", "*" ) ) ) );
      coachReport.put( "LoA PC", sortForPitching( filterCoaches( new CSE( "ML", "*" ), new CSE( "AAA", "*" ), new CSE( "AA", "*" ), new CSE( "HiA", "*" ) ) ) );
      coachReport.put( "LoA BC", sortForBench( filterCoaches( new CSE( "ML", "*" ), new CSE( "AAA", "*" ), new CSE( "AA", "*" ), new CSE( "HiA", "*" ) ) ) );
      coachReport.put( "RL HC", sortForHitting( filterCoaches( new CSE( "ML", "*" ), new CSE( "AAA", "*" ), new CSE( "AA", "*" ), new CSE( "HiA", "*" ), new CSE( "LoA", "*" ) ) ) );
      coachReport.put( "RL PC", sortForPitching( filterCoaches( new CSE( "ML", "*" ), new CSE( "AAA", "*" ), new CSE( "AA", "*" ), new CSE( "HiA", "*" ), new CSE( "LoA", "*" ) ) ) );
      coachReport.put( "RL BC", sortForBench( filterCoaches( new CSE( "ML", "*" ), new CSE( "AAA", "*" ), new CSE( "AA", "*" ), new CSE( "HiA", "*" ), new CSE( "LoA", "*" ) ) ) );
   }

   private List<HbdCoach> filterCoaches( CSE... exclusions ) {
      final List<HbdCoach> result = new ArrayList<>();
      for( HbdCoach coach : coachList ) {
         boolean include = true;
         for( CSE exclusion : exclusions ) {
            if( exclusion.level.equals( coach.getLevelDemand() ) && ( exclusion.role.equals( "*" ) || exclusion.role.equals( coach.getRoleDemand() ) ) ) {
               include = false;
               break;
            }
         }
         if( include ) {
            result.add( coach );
         }
      }
      return result;
   }

   private List<HbdCoach> sortForHitting( List<HbdCoach> coachList ) {
      final List<HbdCoach> result = new ArrayList<>( coachList );
      Collections.sort( result, new Comparator<HbdCoach>() {
         @Override
         public int compare( HbdCoach c1, HbdCoach c2 ) {
            return c2.getHittingCoachAbility().compareTo( c1.getHittingCoachAbility() );
         }
      } );
      return result;
   }

   private List<HbdCoach> sortForPitching( List<HbdCoach> coachList ) {
      final List<HbdCoach> result = new ArrayList<>( coachList );
      Collections.sort( result, new Comparator<HbdCoach>() {
         @Override
         public int compare( HbdCoach c1, HbdCoach c2 ) {
            return c2.getPitchingCoachAbility().compareTo( c1.getPitchingCoachAbility() );
         }
      } );
      return result;
   }

   private List<HbdCoach> sortForBench( List<HbdCoach> coachList ) {
      final List<HbdCoach> result = new ArrayList<>( coachList );
      Collections.sort( result, new Comparator<HbdCoach>() {
         @Override
         public int compare( HbdCoach c1, HbdCoach c2 ) {
            return c2.getBenchCoachAbility().compareTo( c1.getBenchCoachAbility() );
         }
      } );
      return result;
   }

   private List<HbdCoach> sortForFielding( List<HbdCoach> coachList ) {
      final List<HbdCoach> result = new ArrayList<>( coachList );
      Collections.sort( result, new Comparator<HbdCoach>() {
         @Override
         public int compare( HbdCoach c1, HbdCoach c2 ) {
            return c2.getFieldingCoachAbility().compareTo( c1.getFieldingCoachAbility() );
         }
      } );
      return result;
   }

   private List<HbdCoach> sortForBaseCoach( List<HbdCoach> coachList ) {
      final List<HbdCoach> result = new ArrayList<>( coachList );
      Collections.sort( result, new Comparator<HbdCoach>() {
         @Override
         public int compare( HbdCoach c1, HbdCoach c2 ) {
            return c2.getBaseCoachAbility().compareTo( c1.getBaseCoachAbility() );
         }
      } );
      return result;
   }

   private void generateWorkbook() {
      workbook = ExcelUtl.buildWorkbookFromDataMap( coachReport, HbdCoach.class, 0 );
   }

   private void saveReport( String fileLocation ) {
      try (OutputStream os = new FileOutputStream( fileLocation )) {
         workbook.write( os );
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
   }

   public HbdWorld getWorld() {
      return world;
   }

   @Override
   public Workbook getWorkbook() {
      return workbook;
   }

   @Override
   public String getServerFolderName() {
      return "HbdCoachHiringReport";
   }

   public String getReportName() {
      return "CoachHiringReport_" + EmailUtl.getNamePart( username ) + "_" + FormatUtl.stripNonAlphaNumeric( getWorld().getWorldName() ) + "_" + DateUtl.formatTimestampForFileName( new Date() ) + ".xlsx";
   }
}
