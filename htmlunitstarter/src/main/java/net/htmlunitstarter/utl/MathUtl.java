package net.htmlunitstarter.utl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MathUtl {

   private static BigInteger precisionLimit = new BigInteger( "999999999999999" );
   private static int SCALE = 15;

   public static boolean isInteger( String string ) {
      try {
         Integer.parseInt( string );
         return true;
      }
      catch( NumberFormatException e ) {
         return false;
      }
   }

   public static Integer median( Set<Integer> integers ) {
      Integer median = null;
      final List<Integer> list = new ArrayList<Integer>( integers );
      Collections.sort( list );
      final int length = list.size();
      // If our length is even, then we need to find the average of the two centered values
      if( length % 2 == 0 ) {
         int indexA = ( length - 1 ) / 2;
         int indexB = length / 2;

         median = ( list.get( indexA ) + list.get( indexB ) ) / 2;
      }
      // Else if our length is odd, then we simply find the value at the center index
      else {
         int index = ( length - 1 ) / 2;
         median = list.get( index );
      }
      return median;
   }

   public static final BigDecimal divide( BigDecimal dividend, BigDecimal divisor ) {
      int scale = SCALE > dividend.scale() ? SCALE : dividend.scale();
      scale = scale > divisor.scale() ? scale : divisor.scale();
      return dividend.divide( divisor, scale, BigDecimal.ROUND_HALF_EVEN );
   }

   public static double round2( Double value ) {
      return round( new BigDecimal( value ), 2 ).doubleValue();
   }

   public static final BigDecimal round( BigDecimal value, int precision ) {
      if( value == null ) {
         return value;
      }
      BigDecimal roundedValue = value;
      int scale = value.scale();
      if( exceedsPrecisionLimit( value ) && precision < scale - 1 ) {
         roundedValue = value.setScale( scale - 1, BigDecimal.ROUND_HALF_UP );
      }
      return roundedValue.setScale( precision, BigDecimal.ROUND_HALF_UP );
   }

   private static boolean exceedsPrecisionLimit( BigDecimal value ) {
      return value.unscaledValue().compareTo( precisionLimit ) > 0;
   }

   public static BigDecimal minOf( BigDecimal... values ) {
      BigDecimal min = values[0];
      for( BigDecimal value : values ) {
         if( lessThan( value, min ) ) {
            min = value;
         }
      }
      return min;
   }

   public static boolean lessThan( BigDecimal value1, BigDecimal value2 ) {
      return value1.compareTo( value2 ) < 0;
   }

   public static boolean lessThan( Integer value1, Integer value2 ) {
      return value1.compareTo( value2 ) < 0;
   }

   public static BigDecimal maxOf( BigDecimal... values ) {
      BigDecimal max = values[0];
      for( BigDecimal value : values ) {
         if( greaterThan( value, max ) ) {
            max = value;
         }
      }
      return max;
   }

   public static Integer maxOf( Integer... values ) {
      Integer max = values[0];
      for( Integer value : values ) {
         if( greaterThan( value, max ) ) {
            max = value;
         }
      }
      return max;
   }

   public static boolean greaterThan( BigDecimal value1, BigDecimal value2 ) {
      return value1.compareTo( value2 ) > 0;
   }

   public static boolean greaterThan( Integer value1, Integer value2 ) {
      return value1.compareTo( value2 ) > 0;
   }

   public static BigDecimal sum( BigDecimal... addends ) {
      BigDecimal result = BigDecimal.ZERO;
      for( BigDecimal addend : addends ) {
         result = result.add( addend );
      }
      return result;
   }
}
