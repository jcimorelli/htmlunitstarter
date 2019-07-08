package net.htmlunitstarter.utl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class FormatUtl {

   public static String toProper( String string ) {
      return string.substring( 0, 1 ).toUpperCase().concat( string.substring( 1 ).toLowerCase() );
   }

   public static String toHash( String string ) {
      MessageDigest digest;
      try {
         digest = MessageDigest.getInstance( "SHA-1" );
         digest.reset();
         return new String( digest.digest( string.getBytes( "UTF-8" ) ) );
      }
      catch( NoSuchAlgorithmException e ) {
         e.printStackTrace();
      }
      catch( UnsupportedEncodingException e ) {
         e.printStackTrace();
      }
      return null;
   }

   public static String deformatPhoneNumber( String phoneNumber ) {
      StringBuilder deformattedPhoneNumber = new StringBuilder();
      if( phoneNumber != null ) {
         char[] charArray = phoneNumber.toCharArray();
         for( int i = 0; i < phoneNumber.length(); i++ ) {
            char c = charArray[i];
            if( c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' ) {
               deformattedPhoneNumber.append( c );
            }
         }
      }
      return deformattedPhoneNumber.toString();
   }

   public static String formatPhoneNumber( String phoneNumber ) {
      if( phoneNumber != null ) {
         StringBuilder formattedPhoneNumber = new StringBuilder( phoneNumber );
         if( phoneNumber.length() == 10 ) {
            formattedPhoneNumber.insert( 3, "." );
            formattedPhoneNumber.insert( 7, "." );
         }
         else if( phoneNumber.length() == 11 && phoneNumber.startsWith( "1" ) ) {
            formattedPhoneNumber.insert( 1, "." );
            formattedPhoneNumber.insert( 5, "." );
            formattedPhoneNumber.insert( 9, "." );
         }
         else if( phoneNumber.length() == 7 ) {
            formattedPhoneNumber.insert( 3, "." );
         }
         return formattedPhoneNumber.toString();
      }
      return null;
   }

   public static <T> String listToDelimitedString( List<T> list, String delimiter, String prefix, String suffix ) {
      final List<String> stringList = list.stream().map( item -> item.toString() ).collect( Collectors.toList() );
      StringBuilder result = new StringBuilder();
      Iterator<String> iter = stringList.iterator();
      result.append( prefix );
      result.append( iter.next() );
      result.append( suffix );
      while( iter.hasNext() ) {
         result.append( delimiter );
         result.append( prefix );
         result.append( iter.next() );
         result.append( suffix );
      }
      return result.toString();
   }

   public static String padRight( String text, int targetLength, char padChar ) {
      final StringBuilder sb = new StringBuilder( text );
      while( sb.length() < targetLength ) {
         sb.append( padChar );
      }
      return sb.toString();
   }

   public static String padLeft( String text, int targetLength, char padChar ) {
      final StringBuilder sb = new StringBuilder( text );
      while( sb.length() < targetLength ) {
         sb.insert( 0, padChar );
      }
      return sb.toString();
   }

   public static <T> String listToDelimitedString( List<T> list, String delimiter ) {
      final List<String> stringList = list.stream().map( item -> item.toString() ).collect( Collectors.toList() );
      if( list == null || list.isEmpty() ) {
         return "";
      }
      final StringBuilder sb = new StringBuilder();
      final Iterator<String> iter = stringList.iterator();
      while( iter.hasNext() ) {
         sb.append( iter.next() );
         if( iter.hasNext() ) {
            sb.append( delimiter );
         }
      }
      return sb.toString();
   }

   public static List<String> delimitedStringToList( String string, String delimiter ) {
      if( string == null ) {
         return new LinkedList<String>();
      }
      final List<String> list = new LinkedList<String>();
      while( string.indexOf( delimiter ) != -1 ) {
         String item = string.substring( 0, string.indexOf( delimiter ) );
         list.add( item );
         string = string.substring( item.length() + delimiter.length() );
      }
      list.add( string );
      return list;
   }

   public static String generateRandomAlphanumeric( int length ) {
      final String chars = "0123456789abcdefghijklmnopqrstuvwxyz";
      final Random rnd = new Random();
      StringBuilder sb = new StringBuilder( length );
      for( int i = 0; i < length; i++ ) {
         sb.append( chars.charAt( rnd.nextInt( chars.length() ) ) );
      }
      return sb.toString();
   }

   public static String readInput( InputStream is ) throws IOException {
      BufferedReader reader = new BufferedReader( new InputStreamReader( is ) );
      StringBuilder sb = new StringBuilder();
      String line;
      while( ( line = reader.readLine() ) != null ) {
         sb.append( line );
      }
      reader.close();
      return sb.toString();
   }

   public static <T extends Object> T defaultValue( T obj, T defaultValue ) {
      if( obj != null ) {
         return obj;
      }
      return defaultValue;
   }

   public static String firstLetterCaps( String string ) {
      if( string != null ) {
         return Character.toString( string.charAt( 0 ) ).toUpperCase() + string.substring( 1 );
      }
      return null;
   }

   public static String stripNonAlphaNumeric( String string ) {
      return string.replaceAll( "[^a-zA-Z0-9]", "" );
   }
}