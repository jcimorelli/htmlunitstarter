package net.wistools.test;

import net.wistools.config.WistoolsConfig;

import org.apache.commons.configuration.PropertiesConfiguration;
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
public class TestProperties {

   @Test
   public void testProperties() throws Exception {
      @SuppressWarnings("unused")
      final PropertiesConfiguration fileCache = new PropertiesConfiguration( TestProperties.class.getResource( "/filecache.properties" ) );
      "".length();
   }
}