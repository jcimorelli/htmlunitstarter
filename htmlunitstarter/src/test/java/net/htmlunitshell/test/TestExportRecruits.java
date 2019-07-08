package net.wistools.test;

import net.wistools.config.WistoolsConfig;
import net.wistools.config.WistoolsProperties;
import net.wistools.data.HdRecruitRatings.CustomOverallWeights;
import net.wistools.nav.hd.HdRecruitPoolExporter;

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
public class TestExportRecruits {

   @Test
   public void test() {
      final CustomOverallWeights weights = new CustomOverallWeights( 10, 10, 9, 10, 7, 7, 7, 9, 9, 5, 0, 5, 5 );
      final HdRecruitPoolExporter exporter = new HdRecruitPoolExporter( "Smith", true, "Signed", "DI", weights );
      exporter.reportAndSave( "C:\\Wistools\\" + exporter.getServerFolderName() + "\\" + exporter.getReportName(), WistoolsProperties.getWisUsername(), WistoolsProperties.getWisPassword() );
   }
}
