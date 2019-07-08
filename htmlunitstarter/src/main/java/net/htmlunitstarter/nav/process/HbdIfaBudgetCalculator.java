package net.wistools.nav.hbd;

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

import net.wistools.config.WistoolsProperties;
import net.wistools.data.HbdTeamIfaBudget;
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
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HbdIfaBudgetCalculator implements IWistoolsExcelCreator {

   private HbdWorld world;
   private Map<String, HbdTeamIfaBudget> teamBudgetMap = new LinkedHashMap<>();
   private String username;
   private Workbook workbook;

   public HbdIfaBudgetCalculator( String worldName ) {
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
         gatherTeamBudgetData( nav );
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
      doCalculations();
      sortData();
      generateWorkbook();
   }

   private void gatherTeamBudgetData( WistoolsNavigator nav ) {
      gatherFromBudgetPage( nav );//Build team list; Get coach budget and prospect budget
      gatherFromTradeProposalPage( nav );//Get player budget and player spending
      gatherFromCoachesPage( nav );//Get coaching spending
      gatherFromDraftResults( nav );//Get draft spending
      gatherFromIfaSignings( nav );//Get IFA spending
   }

   private void gatherFromBudgetPage( WistoolsNavigator nav ) {
      final HtmlPage budgetPage = nav.loadPage( WisPage.HBD_BudgetAnalysis ).currentPage();
      for( HtmlAnchor teamAnchor : budgetPage.getAnchors() ) {
         if( "Open Franchise Profile".equals( teamAnchor.getAttribute( "title" ) ) ) {
            final Integer teamId = Integer.parseInt( teamAnchor.getAttribute( "href" ).substring( 44 ) );
            final HbdTeamIfaBudget teamBudget = new HbdTeamIfaBudget( teamId );
            teamBudget.setFullTeamName( teamAnchor.getTextContent() );
            final DomElement teamTd = ( DomElement )teamAnchor.getParentNode();
            final DomElement coachTd = teamTd.getNextElementSibling();
            teamBudget.setCoach( coachTd.getFirstElementChild().getTextContent() );
            final DomElement prospectBudgetTd = coachTd.getNextElementSibling().getNextElementSibling();
            final DomElement coachBudgetTd = prospectBudgetTd.getNextElementSibling();
            teamBudget.setProspectBudget( parseBudgetItemTd( prospectBudgetTd ) );
            teamBudget.setCoachBudget( parseBudgetItemTd( coachBudgetTd ) );
            teamBudget.setMyTeam( username.equals( teamBudget.getCoach() ) );
            teamBudgetMap.put( teamBudget.getFullTeamName(), teamBudget );
         }
      }
   }

   private BigDecimal parseBudgetItemTd( DomElement budgetItemTd ) {
      final String budgetItemStr = budgetItemTd.getTextContent();
      String budgetAmtStr = budgetItemStr;
      final int openParenthIdx = budgetItemStr.indexOf( "(" );
      if( openParenthIdx > 0 ) {
         final int closeParenthIdx = budgetItemStr.indexOf( ")" );
         budgetAmtStr = budgetItemStr.substring( openParenthIdx + 1, closeParenthIdx );
      }
      return BigDecimal.valueOf( Integer.parseInt( budgetAmtStr ) ).multiply( BigDecimal.valueOf( 1000000 ) );
   }

   private void gatherFromTradeProposalPage( WistoolsNavigator nav ) {
      for( HbdTeamIfaBudget teamBudget : teamBudgetMap.values() ) {
         nav.loadPage( WisPage.HBD_TradeProposalsBuild, teamBudget.getTeamId().toString() );
         final String playerBudgetElementId = teamBudget.isMyTeam() ? WisElement.ID_MyTeamPlayerBudget : WisElement.ID_OtherTeamPlayerBudget;
         final String playerSpendingElementId = teamBudget.isMyTeam() ? WisElement.ID_MyTeamPlayerSpending : WisElement.ID_OtherTeamPlayerSpending;
         final BigDecimal playerBudget = parseNumberElement( nav.currentPage().getElementById( playerBudgetElementId ) );
         final BigDecimal playerSpending = parseNumberElement( nav.currentPage().getElementById( playerSpendingElementId ) );
         teamBudget.setPlayerBudget( playerBudget );
         teamBudget.setPlayerSpending( playerSpending );
      }
   }

   private void gatherFromCoachesPage( WistoolsNavigator nav ) {
      nav.loadPage( WisPage.HBD_Coaches ).setComboByText( WisElement.ID_LevelCombo, "All" );
      for( HbdTeamIfaBudget teamBudget : teamBudgetMap.values() ) {
         nav.setComboByText( WisElement.ID_FranchiseCombo, teamBudget.getFullTeamName() ).clickElementById( WisElement.ID_GoButton );
         final DomElement coachTable = nav.currentPage().getElementById( WisElement.ID_CoachTable );
         final DomElement firstCoachRow = coachTable.getFirstElementChild().getFirstElementChild().getNextElementSibling().getNextElementSibling();
         if( firstCoachRow == null ) {
            return;
         }
         parseCoachRow( firstCoachRow, teamBudget );
         DomElement nextCoachRow = firstCoachRow.getNextElementSibling();
         while( nextCoachRow != null ) {
            parseCoachRow( nextCoachRow, teamBudget );
            nextCoachRow = nextCoachRow.getNextElementSibling();
         }
      }
   }

   private void parseCoachRow( DomElement coachRow, HbdTeamIfaBudget teamBudget ) {
      final DomElement coachCostTd = coachRow.getFirstElementChild().getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling();
      final BigDecimal coachCost = parseNumberElement( coachCostTd );
      teamBudget.addCoachSpending( coachCost );
   }

   private void gatherFromDraftResults( WistoolsNavigator nav ) {
      nav.loadPage( WisPage.HBD_DraftHistory ).setComboByText( WisElement.ID_DraftRoundsCombo, "All" );
      for( HbdTeamIfaBudget teamBudget : teamBudgetMap.values() ) {
         nav.setComboByText( WisElement.ID_FranchiseCombo, teamBudget.getFullTeamName() ).clickElementById( WisElement.ID_GoButton2 );
         final DomElement firstDraftRow = nav.currentPage().getElementById( WisElement.ID_FirstDraftProspectRow );
         if( firstDraftRow == null ) {
            return;
         }
         parseDraftRow( firstDraftRow, teamBudget );
         DomElement nextDraftRow = firstDraftRow.getNextElementSibling();
         while( nextDraftRow != null ) {
            parseDraftRow( nextDraftRow, teamBudget );
            nextDraftRow = nextDraftRow.getNextElementSibling();
         }
      }
   }

   private void parseDraftRow( DomElement draftRow, HbdTeamIfaBudget teamBudget ) {
      final DomElement draftCostTd = draftRow.getFirstElementChild().getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling()
            .getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling();
      final BigDecimal draftCost = "---".equals( draftCostTd.getTextContent() ) ? BigDecimal.ZERO : parseNumberElement( draftCostTd );
      teamBudget.addDraftSpending( draftCost );
   }

   private void gatherFromIfaSignings( WistoolsNavigator nav ) {
      final HtmlPage ifaPage = nav.loadPage( WisPage.HBD_InternationalSignings ).currentPage();
      final DomElement ifaSigningsTable = ifaPage.getElementById( WisElement.ID_IfaSigningsTable );
      final DomElement firstIfaRow = ifaSigningsTable.getFirstElementChild().getFirstElementChild();
      if( firstIfaRow == null ) {
         return;
      }
      parseIfaRow( firstIfaRow );
      DomElement nextIfaRow = firstIfaRow.getNextElementSibling();
      while( nextIfaRow != null ) {
         parseIfaRow( nextIfaRow );
         nextIfaRow = nextIfaRow.getNextElementSibling();
      }
   }

   private void parseIfaRow( DomElement ifaRow ) {
      final boolean isHeader = ifaRow.getAttribute( "class" ) != null && ifaRow.getAttribute( "class" ).contains( "makeStickyHeader" );
      if( !isHeader ) {
         final DomElement franchiseTd = ifaRow.getFirstElementChild().getNextElementSibling().getNextElementSibling().getNextElementSibling();
         final String fullTeamName = franchiseTd.getFirstElementChild().getTextContent();
         final DomElement costTd = franchiseTd.getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling().getNextElementSibling();
         final BigDecimal cost = parseNumberElement( costTd );
         teamBudgetMap.get( fullTeamName ).addIfaSpending( cost );
      }
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
      for( HbdTeamIfaBudget teamBudget : teamBudgetMap.values() ) {
         teamBudget.calculateTotals();
      }
   }

   private void sortData() {
      if( teamBudgetMap != null && !teamBudgetMap.isEmpty() ) {
         final List<Map.Entry<String, HbdTeamIfaBudget>> entries = new ArrayList<>( teamBudgetMap.entrySet() );
         Collections.sort( entries, new Comparator<Map.Entry<String, HbdTeamIfaBudget>>() {
            @Override
            public int compare( Map.Entry<String, HbdTeamIfaBudget> t1, Map.Entry<String, HbdTeamIfaBudget> t2 ) {
               return t2.getValue().getNetPotentialProspectBudget().compareTo( t1.getValue().getNetPotentialProspectBudget() );
            }
         } );
         teamBudgetMap.clear();
         for( Map.Entry<String, HbdTeamIfaBudget> e : entries ) {
            teamBudgetMap.put( e.getKey(), e.getValue() );
         }
      }
   }

   private void generateWorkbook() {
      final Map<String, List<HbdTeamIfaBudget>> dataMap = new HashMap<>();
      dataMap.put( "Budget Analysis", new ArrayList<HbdTeamIfaBudget>( teamBudgetMap.values() ) );
      workbook = ExcelUtl.buildWorkbookFromDataMap( dataMap, HbdTeamIfaBudget.class, 0 );
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
      return "HbdIfaBudgetAnalysis";
   }

   public String getReportName() {
      return "IfaBudgetAnalysis_" + EmailUtl.getNamePart( username ) + "_" + FormatUtl.stripNonAlphaNumeric( getWorld().getWorldName() ) + "_" + DateUtl.formatTimestampForFileName( new Date() ) + ".xlsx";
   }
}
