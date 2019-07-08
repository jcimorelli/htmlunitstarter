package net.wistools.test;

import net.wistools.config.WistoolsConfig;
import net.wistools.nav.hd.HdTeamRecruitingReporter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WistoolsConfig.class)
@Configurable
public class TestGetRecruitingData {

   @Test
   public void testRecruitSearch() {
      final HdTeamRecruitingReporter reporter = new HdTeamRecruitingReporter( "Smith",
            "St. Joseph's",
            "Temple",
            "Dayton",
            "Saint Louis",
            "Georgia");
      reporter.reportAndSave( reporter.getReportName() );
   }
}
