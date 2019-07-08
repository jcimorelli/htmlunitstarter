package net.wistools.test;

import net.wistools.config.WistoolsConfig;
import net.wistools.config.WistoolsProperties;
import net.wistools.nav.hd.HdRecruitColorCoder;
import net.wistools.nav.hd.HdRecruitSearchCriteria;

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
public class TestRecruitColorCoding {

   @Test
   public void testRecruitColorCoding() {
      final HdRecruitSearchCriteria criteria = new HdRecruitSearchCriteria();
      criteria.setProjectedLevel( "Division I" );
      criteria.setSortBy( "Overall Rank" );
      criteria.setSortDirection( "Ascending" );
      final HdRecruitColorCoder coder = new HdRecruitColorCoder( "Allen" );
      coder.setSearchCriteria( criteria );
      coder.setUpdateColor( "Yellow" );
      coder.colorCodeRecruits( WistoolsProperties.getWisUsername(), WistoolsProperties.getWisPassword() );
   }
}
