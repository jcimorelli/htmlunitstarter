package net.htmlunitstarter.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import net.htmlunitstarter.schedule.HtmlUnitStarterScheduler;

@Configuration
@EnableWebMvc
@EnableSpringConfigured
@ComponentScan({ "net.htmlunitstarter.config", "net.htmlunitstarter.data", "net.htmlunitstarter.controller", "net.htmlunitstarter.utl" })
@PropertySource(value = { "classpath:htmlunitstarter.properties", "classpath:filecache.properties" })
@EnableTransactionManagement
public class HtmlUnitStarterConfig extends WebMvcConfigurerAdapter {

   //net.htmlunitstarter.properties
   @Value("${dbHost}")
   private String dbHost;
   @Value("${dbUser}")
   private String dbUser;
   @Value("${dbPassword}")
   private String dbPassword;

   @Value("${siteUsername}")
   private String siteUsername;
   @Value("${sitePassword}")
   private String sitePassword;

   @Value("${smtpUsername}")
   private String smtpUsername;
   @Value("${smtpPassword}")
   private String smtpPassword;
   @Value("${smtpHost}")
   private String smtpHost;
   @Value("${smtpPort}")
   private String smtpPort;
   @Value("${adminEmail}")
   private String adminEmail;

   //filecache.properties
   @Value("${lastLoginToken}")
   private String lastLoginToken;

   @Bean
   public static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {
      return new PropertySourcesPlaceholderConfigurer();
   }

   @Override
   public void addResourceHandlers( ResourceHandlerRegistry registry ) {
      registry.addResourceHandler( "/resources/**" ).addResourceLocations( "/resources/" );
   }

   @Override
   public void configureDefaultServletHandling( DefaultServletHandlerConfigurer configurer ) {
      configurer.enable();
   }

   @Bean
   public ViewResolver setupViewResolver() {
      InternalResourceViewResolver resolver = new InternalResourceViewResolver();
      resolver.setPrefix( "/WEB-INF/pages/" );
      resolver.setSuffix( ".jsp" );
      resolver.setViewClass( JstlView.class );
      return resolver;
   }

   @Bean
   public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
      LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
      em.setDataSource( dataSource() );
      em.setPackagesToScan( new String[]{ "net.htmlunitstarter.db", "net.htmlunitstarter.request" } );
      em.setJpaVendorAdapter( getEclipseLinkJpaVendorAdapter() );
      em.setJpaProperties( getEclipseLinkProperties() );
      return em;
   }

   @Bean
   public DataSource dataSource() {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName( "com.mysql.jdbc.Driver" );
      dataSource.setUrl( "jdbc:mysql://" + dbHost + ":3306/net.htmlunitstarter" );
      dataSource.setUsername( dbUser );
      dataSource.setPassword( dbPassword );
      return dataSource;
   }

   @Bean
   public PlatformTransactionManager transactionManager( EntityManagerFactory emf ) {
      JpaTransactionManager transactionManager = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory( emf );
      return transactionManager;
   }

   @Bean
   public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
      return new PersistenceExceptionTranslationPostProcessor();
   }

   private EclipseLinkJpaVendorAdapter getEclipseLinkJpaVendorAdapter() {
      EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
      vendorAdapter.setDatabasePlatform( "org.eclipse.persistence.platform.database.MySQLPlatform" );
      vendorAdapter.setGenerateDdl( false );
      vendorAdapter.setShowSql( false );
      return vendorAdapter;
   }

   private Properties getEclipseLinkProperties() {
      Properties properties = new Properties();
      //properties.put( "eclipselink.deploy-on-startup", "true" );
      properties.put( "eclipselink.weaving", "false" );
      //properties.put( "eclipselink.weaving.lazy", "true" );
      //properties.put( "eclipselink.weaving.internal", "true" );
      //properties.put( "eclipselink.logging.level", "SEVERE" );
      //properties.put( "eclipselink.query-results-cache.type", "WEAK" );
      //properties.put( "eclipselink.jdbc.batch-writing", "JDBC" );
      //properties.put( "eclipselink.jdbc.batch-writing.size", "1000" );
      properties.put( "eclipselink.cache.shared.default", "false" );

      return properties;
   }

   @Bean
   public HtmlUnitStarterScheduler getHtmlUnitStarterScheduler() {
      return new HtmlUnitStarterScheduler();
   }

}