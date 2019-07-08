package net.wistools.test;

import net.wistools.config.WistoolsConfig;
import net.wistools.config.WistoolsProperties;
import net.wistools.nav.hd.HdPlusMinusCalculator;
import net.wistools.nav.hd.HdWorld;

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
public class TestPlayByPlayParse {

   @Test
   public void test() {
      final HdPlusMinusCalculator calc = new HdPlusMinusCalculator( HdWorld.SMITH );
      calc.calculateAndSave( "C:\\Wistools\\" + calc.getServerFolderName() + "\\" + calc.getReportName(), WistoolsProperties.getWisUsername(), WistoolsProperties.getWisPassword() );
   }
}
