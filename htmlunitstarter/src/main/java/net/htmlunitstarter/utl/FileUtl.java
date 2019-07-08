package net.htmlunitstarter.utl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class FileUtl {

   public static void appendStringToFile( String data, String fileName ) {
      try (final PrintWriter out = new PrintWriter( new BufferedWriter( new FileWriter( fileName, true ) ) )) {
         out.print( data );
      }
      catch( IOException e ) {
         throw new RuntimeException( e );
      }
   }

   public static void printPageToFile( HtmlPage page, String fileName ) {
      clearFile( fileName );
      try (final PrintWriter out = new PrintWriter( new BufferedWriter( new FileWriter( fileName, true ) ) )) {
         out.print( page.asXml() );
      }
      catch( IOException e ) {
         throw new RuntimeException( e );
      }
   }

   public static void clearFile( String fileName ) {
      try (PrintWriter writer = new PrintWriter( fileName )) {
         writer.print( "" );
      }
      catch( FileNotFoundException e ) {
         e.printStackTrace();
         throw new RuntimeException( e );
      }
   }

   public static void copyFile( String fileSourceName, String fileDestinationName ) {
      clearFile( fileDestinationName );
      try (final PrintWriter out = new PrintWriter( new BufferedWriter( new FileWriter( fileDestinationName, true ) ) )) {
         try (BufferedReader br = new BufferedReader( new FileReader( fileSourceName ) )) {
            String sCurrentLine;
            while( ( sCurrentLine = br.readLine() ) != null ) {
               out.println( sCurrentLine );
            }
         }
         catch( IOException ioe1 ) {
            ioe1.printStackTrace();
            throw new RuntimeException( ioe1 );
         }
      }
      catch( IOException ioe2 ) {
         ioe2.printStackTrace();
         throw new RuntimeException( ioe2 );
      }
   }

   public static Boolean compareFiles( String fileName1, String fileName2 ) {
      try (BufferedReader br1 = new BufferedReader( new FileReader( fileName1 ) )) {
         try (BufferedReader br2 = new BufferedReader( new FileReader( fileName2 ) )) {
            int maxLines = 10000000;
            String currentLine1;
            String currentLine2;
            //Compare each line
            int lineIndex = 1;
            while( true ) {
               currentLine1 = br1.readLine();
               currentLine2 = br2.readLine();
               if( currentLine1 == null && currentLine2 == null ) {
                  //Both files are complete and they match
                  return true;
               }
               else if( ( currentLine1 == null && currentLine2 != null ) || ( currentLine1 != null && currentLine2 == null ) || !currentLine1.equals( currentLine2 ) ) {
                  //This line doesn't match
                  return false;
               }
               else if( lineIndex++ > maxLines ) {
                  //This is taking too long!
                  throw new RuntimeException( "Too many lines to compare!" );
               }
               //Not done, but matching so far....continue
            }
         }
         catch( IOException ioe1 ) {
            ioe1.printStackTrace();
            throw new RuntimeException( ioe1 );
         }
      }
      catch( IOException ioe2 ) {
         ioe2.printStackTrace();
         throw new RuntimeException( ioe2 );
      }
   }

   public static InputStream convertToInputStream( Object obj ) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream( baos );
      oos.writeObject( obj );
      oos.flush();
      oos.close();
      return new ByteArrayInputStream( baos.toByteArray() );
   }

   public static void sendDummyFileToBrowser( HttpServletResponse response, String message ) {
      response.setContentType( "text/plain" );
      response.setHeader( "Content-Disposition", "attachment; filename=message.txt" );
      response.setHeader( "Set-Cookie", "fileDownload=true; path=/" );
      try (OutputStream os = response.getOutputStream()) {
         os.write( message.getBytes() );
      }
      catch( IOException ex ) {
         ValidateUtl.fail( ex );
      }
   }

   public static void sendFileToBrowser( HttpServletResponse response, File file ) throws IOException {
      response.setContentType( "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
      response.setHeader( "Content-Disposition", "attachment; filename=" + file.getName() );
      response.setHeader( "Set-Cookie", "fileDownload=true; path=/" );
      try (OutputStream os = response.getOutputStream(); InputStream is = new FileInputStream( file );) {
         IOUtils.copy( is, os );
      }
      catch( IOException ex ) {
         ValidateUtl.fail( ex );
      }
      finally {
         response.flushBuffer();
      }
   }
}
