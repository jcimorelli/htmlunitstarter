package net.htmlunitstarter.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class HtmlUnitStarterInitializer implements WebApplicationInitializer {

   private static Logger logger = Logger.getLogger( HtmlUnitStarterInitializer.class );

   //Called first when the application starts loading.
   public void onStartup( ServletContext servletContext ) throws ServletException {
      logger.info( "Initializing Spring Context..." );

      //Registering the class that incorporates the annotated DispatcherServlet configuration of spring
      AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
      rootContext.register( HtmlUnitStarterConfig.class );

      //Adding the listener for the rootContext
      servletContext.addListener( new ContextLoaderListener( rootContext ) );

      //Registering the dispatcher servlet mappings.
      ServletRegistration.Dynamic dispatcher = servletContext.addServlet( "dispatcher", new DispatcherServlet( rootContext ) );
      dispatcher.setLoadOnStartup( 1 );
      dispatcher.addMapping( "/" );

   }

}