package net.wistools.test;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wistools.config.WistoolsConfig;
import net.wistools.data.HdRecruitInterest;
import net.wistools.utl.ExcelUtl;

import org.apache.poi.ss.usermodel.Workbook;
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
public class TestApachePOI {

   //@Test
   public void testApachePOI_Integrated() {
      //new HdTeamRecruitingReporter( HdWorld.ALLEN, "Georgetown", "UMass", "Louisville" );
   }

   @Test
   public void testApachePOI_Mock() throws Exception {
      final Map<String, List<HdRecruitInterest>> recruitMap = getMockedRecruitMap();
      final Workbook result = ExcelUtl.buildWorkbookFromDataMap( recruitMap, HdRecruitInterest.class, 3 );
      ExcelUtl.addIndividualDataField( result, "Team1", 0, 0, "#Seniors", 1 );
      ExcelUtl.addIndividualDataField( result, "Team1", 0, 1, "#Walkons", 0 );
      ExcelUtl.addIndividualDataField( result, "Team1", 0, 2, "#LikelyEEs", 1 );
      ExcelUtl.addIndividualDataField( result, "Team2", 0, 0, "#Seniors", 3 );
      ExcelUtl.addIndividualDataField( result, "Team2", 0, 1, "#Walkons", 1 );
      ExcelUtl.addIndividualDataField( result, "Team2", 0, 2, "#LikelyEEs", 0 );
      OutputStream os = new FileOutputStream( "src/test/resources/HdRecruitingSummaryTest.xlsx" );
      result.write( os );
      os.close();
   }

   private Map<String, List<HdRecruitInterest>> getMockedRecruitMap() {
      final Map<String, List<HdRecruitInterest>> recruitMap = new HashMap<>();

      final List<HdRecruitInterest> recruitList1 = new ArrayList<>();
      final HdRecruitInterest recruit1 = new HdRecruitInterest( 1 );
      recruit1.setRecruitName( "Bob Roberts" );
      recruit1.setInterestLevel( "Very High" );
      recruit1.setScholarshipOffer( "Yes" );
      recruitList1.add( recruit1 );
      final HdRecruitInterest recruit2 = new HdRecruitInterest( 2 );
      recruit2.setRecruitName( "Bob Robertson" );
      recruit2.setInterestLevel( "Very High" );
      recruit2.setScholarshipOffer( "No" );
      recruitList1.add( recruit2 );
      recruitMap.put( "Team1", recruitList1 );

      final List<HdRecruitInterest> recruitList2 = new ArrayList<>();
      final HdRecruitInterest recruit3 = new HdRecruitInterest( 3 );
      recruit3.setRecruitName( "adam Roberts" );
      recruit3.setInterestLevel( "Very High" );
      recruit3.setScholarshipOffer( "Yes" );
      recruitList2.add( recruit1 );
      final HdRecruitInterest recruit4 = new HdRecruitInterest( 4 );
      recruit4.setRecruitName( "Bob Robertsonton" );
      recruit4.setInterestLevel( "High" );
      recruit4.setScholarshipOffer( "No" );
      recruitList2.add( recruit4 );
      final HdRecruitInterest recruit5 = new HdRecruitInterest( 5 );
      recruit5.setRecruitName( "Chris Robertsonton" );
      recruit5.setInterestLevel( "High" );
      recruit5.setScholarshipOffer( "Yes" );
      recruitList2.add( recruit5 );
      recruitMap.put( "Team2", recruitList2 );

      return recruitMap;
   }

   /*   private Map<String, HdTeam> buildMockedTeamMap() {
         final Map<String, HdTeam> map = new HashMap<>();
         
         final HdTeam team1 = 
         
         return map;
      }*/
}
