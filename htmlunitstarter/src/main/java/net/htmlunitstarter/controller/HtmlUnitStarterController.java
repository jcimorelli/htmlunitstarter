package net.htmlunitstarter.controller;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.htmlunitstarter.nav.process.ProcessorExample;
import net.htmlunitstarter.utl.EmailUtl;

@Controller
@RequestMapping("/")
public class HtmlUnitStarterController {

   @RequestMapping(method = RequestMethod.GET)
   public ModelAndView open( HttpServletRequest request, ModelMap modelMap ) throws IOException {
      final ModelAndView model = new ModelAndView( "htmlunitstarter" );
      model.addObject( "someProperty", 1 );
      model.addObject( "someOtherProperty", "blah" );
      return model;
   }

   @RequestMapping(value = { "/someService" }, method = RequestMethod.GET)
   @ResponseBody
   public void someService( HttpServletRequest request, HttpServletResponse response ) throws Exception {
	   new ProcessorExample().process();
       EmailUtl.sendEmail( "Email Title", "Email Body", "Email Recipient", false );
   }

   private String getParam( HttpServletRequest request, String paramName ) {
      return request.getParameterMap().containsKey( paramName ) ? request.getParameterMap().get( paramName )[0] : null;
   }

   private String[] getParams( HttpServletRequest request, String paramName ) {
      return request.getParameterMap().containsKey( paramName ) ? request.getParameterMap().get( paramName ) : null;
   }

   private Integer getIntegerParam( HttpServletRequest request, String paramName ) {
      return Integer.parseInt( getParam( request, paramName ).trim() );
   }

   private Boolean getBooleanParam( HttpServletRequest request, String paramName ) {
      final String paramStr = getParam( request, paramName );
      return paramStr != null && Arrays.asList( "true", "on", "yes" ).contains( paramStr.toLowerCase() );
   }
}
