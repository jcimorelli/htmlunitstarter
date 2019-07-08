package net.wistools.schedule;

import java.io.File;
import java.util.List;

import net.wistools.config.WistoolsProperties;
import net.wistools.nav.WisPage;
import net.wistools.nav.WistoolsNavigator;
import net.wistools.nav.hd.HdTeamDetailsFinder;
import net.wistools.nav.hd.HdTeamRecruitingReporter;
import net.wistools.nav.hd.HdWorld;
import net.wistools.utl.EmailUtl;
import net.wistools.utl.ExceptionUtl;
import net.wistools.utl.FileUtl;

public class ScheduledRecruitingReportTask implements Runnable {

   @Override
   public void run() {
      runReport( WistoolsProperties.isAutoRecruitReport_1(),
            WistoolsProperties.getAutoRecruitReportWisUsername_1(),
            WistoolsProperties.getAutoRecruitReportWisPassword_1(),
            WistoolsProperties.getAutoRecruitReportWorld_1(),
            WistoolsProperties.getAutoRecruitReportEmails_1() );
      runReport( WistoolsProperties.isAutoRecruitReport_2(),
            WistoolsProperties.getAutoRecruitReportWisUsername_2(),
            WistoolsProperties.getAutoRecruitReportWisPassword_2(),
            WistoolsProperties.getAutoRecruitReportWorld_2(),
            WistoolsProperties.getAutoRecruitReportEmails_2() );
      runReport( WistoolsProperties.isAutoRecruitReport_3(),
            WistoolsProperties.getAutoRecruitReportWisUsername_3(),
            WistoolsProperties.getAutoRecruitReportWisPassword_3(),
            WistoolsProperties.getAutoRecruitReportWorld_3(),
            WistoolsProperties.getAutoRecruitReportEmails_3() );
      runReport( WistoolsProperties.isAutoRecruitReport_4(),
            WistoolsProperties.getAutoRecruitReportWisUsername_4(),
            WistoolsProperties.getAutoRecruitReportWisPassword_4(),
            WistoolsProperties.getAutoRecruitReportWorld_4(),
            WistoolsProperties.getAutoRecruitReportEmails_4() );
      runReport( WistoolsProperties.isAutoRecruitReport_5(),
            WistoolsProperties.getAutoRecruitReportWisUsername_5(),
            WistoolsProperties.getAutoRecruitReportWisPassword_5(),
            WistoolsProperties.getAutoRecruitReportWorld_5(),
            WistoolsProperties.getAutoRecruitReportEmails_5() );
   }

   private void runReport( Boolean isOn, String username, String password, HdWorld world, List<String> emails ) {
      if( isOn ) {
         try (final WistoolsNavigator nav = new WistoolsNavigator( username, password )) {
            nav.login();
            if( isRecruitingInSession( nav, world ) ) {
               final HdTeamDetailsFinder teamFinder = new HdTeamDetailsFinder( world );
               teamFinder.populateTeamDetails( nav, true, false, true );
               final HdTeamRecruitingReporter reporter = new HdTeamRecruitingReporter( world, teamFinder.getTeamList() );
               reporter.report( username, password );
               final File file = FileUtl.saveFileToServer( reporter );
               if( file != null ) {
                  final String divisionDisplay = teamFinder.getTeamList().isEmpty() ? "?" : teamFinder.getTeamList().get( 0 ).getDivision();
                  EmailUtl.sendEmail( "Automated Recruiting Report for World " + world + ", " + divisionDisplay,
                        "Attached is a Recruiting Report for all human-coached teams in World " + world + ", " + divisionDisplay + "." + " This will automatically run every cycle. Email Jason to opt out.",
                        emails.toArray( new String[0] ), file );
               }
            }
         }
         catch( Exception e ) {
            EmailUtl.sendEmail( "Automated Recruiting Report FAILED for World " + world.getWorldName() + ", User " + username, ExceptionUtl.getStackTraceAsString( e ), WistoolsProperties.getAdminEmail(), true );
         }
      }
   }

   private boolean isRecruitingInSession( WistoolsNavigator nav, HdWorld world ) {
      //Determine if recruiting is currently in session
      nav.loadHdWorld( world ).loadPage( WisPage.HD_RecruitingHome );
      final boolean recruitingNotStarted = nav.currentPage().asXml().contains( "Recruiting begins" );
      final boolean inbetweenSessions = nav.currentPage().asXml().contains( "Recruiting resumes" );
      return !recruitingNotStarted && !inbetweenSessions;
   }
}
