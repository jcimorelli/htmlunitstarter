package net.htmlunitstarter.utl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

public abstract class ClasspathUtl {

   private static Logger logger = Logger.getLogger( ClasspathUtl.class );

   public static String readTextFromResource( String fileName ) {
      InputStream input = FormatUtl.class.getResourceAsStream( "/" + fileName );
      Scanner scanner = new Scanner( input );
      final String text = scanner.useDelimiter( "\\A" ).next();
      scanner.close();
      return text;
   }

   public static String readTextFromFile( String filePath ) {
      String text = null;
      try {
         Scanner scanner = new Scanner( new File( filePath ) );
         text = scanner.useDelimiter( "\\A" ).next();
         scanner.close();
      }
      catch( FileNotFoundException e ) {
         e.printStackTrace();
      }
      return text;
   }

   //This method from http://stackoverflow.com/questions/1456930/how-do-i-read-all-classes-from-a-java-package-in-the-classpath
   public static ArrayList<String> getClassNamesFromPackage( String packageName ) throws IOException, URISyntaxException {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      URL packageURL;
      ArrayList<String> names = new ArrayList<>();;

      packageName = packageName.replace( ".", "/" );
      packageURL = classLoader.getResource( packageName );

      if( packageURL.getProtocol().equals( "jar" ) ) {
         String jarFileName;
         JarFile jf;
         Enumeration<JarEntry> jarEntries;
         String entryName;

         // build jar file name, then loop through zipped entries
         jarFileName = URLDecoder.decode( packageURL.getFile(), "UTF-8" );
         jarFileName = jarFileName.substring( 5, jarFileName.indexOf( "!" ) );
         logger.info( ">" + jarFileName );
         jf = new JarFile( jarFileName );
         jarEntries = jf.entries();
         while( jarEntries.hasMoreElements() ) {
            entryName = jarEntries.nextElement().getName();
            if( entryName.startsWith( packageName ) && entryName.length() > packageName.length() + 5 ) {
               entryName = entryName.substring( packageName.length(), entryName.lastIndexOf( '.' ) );
               names.add( entryName );
            }
         }
         jf.close();
         // loop through files in classpath
      }
      else {
         URI uri = new URI( packageURL.toString() );
         File folder = new File( uri.getPath() );
         // won't work with path which contains blank (%20)
         // File folder = new File(packageURL.getFile()); 
         File[] contenuti = folder.listFiles();
         String entryName;
         for( File actual : contenuti ) {
            entryName = actual.getName();
            entryName = entryName.substring( 0, entryName.lastIndexOf( '.' ) );
            names.add( entryName );
         }
      }
      return names;
   }
}
