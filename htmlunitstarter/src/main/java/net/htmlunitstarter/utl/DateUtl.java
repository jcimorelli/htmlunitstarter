package net.htmlunitstarter.utl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.util.Assert;

public class DateUtl {
   public static final String Format_Date = "yyyy-MM-dd";
   public static final String Format_Timestamp = "yyyy-MM-dd HH:mm:ss.S";
   public static final String Format_UrlTimestamp = "yyyyMMdd_HHmmssS";
   public static final String Format_FileNameTimestamp = "yyyy.MM.dd_HH.mm.ss.S";
   public static final String Format_JavaDateToString = "EEE MMM dd HH:mm:ss zzz yyyy";

   public static Date addDays( Date date, int days ) {
      Calendar calendar = getCalendar( date );
      calendar.add( Calendar.DATE, days );
      return calendar.getTime();
   }

   public static Date addMonths( Date date, int months ) {
      Calendar calendar = getCalendar( date );
      calendar.add( Calendar.MONTH, months );
      return calendar.getTime();
   }

   public static Calendar addYears( Calendar calendar, int years ) {
      calendar.add( Calendar.YEAR, years );
      return calendar;
   }

   public static Date addYears( Date date, int years ) {
      Calendar calendar = getCalendar( date );
      calendar.add( Calendar.YEAR, years );
      return calendar.getTime();
   }

   public static Date adjustByDayOfMonth( Date dateToBeAdjusted, int dayOfMonth ) {
      int dayOfTheMonthToBeAdjusted = DateUtl.getDay( dateToBeAdjusted );
      Calendar calendarToBeAdjusted = getCalendar( dateToBeAdjusted );
      getBeginingOfTheMonth( calendarToBeAdjusted );

      //if the day to be adjusted to is before the current day, push to the next month
      if( dayOfTheMonthToBeAdjusted > dayOfMonth ) {
         calendarToBeAdjusted.add( Calendar.MONTH, 1 );
      }

      //if the day does not exist in the month, use the month end day      
      int endOfMonthDay = getEndDayOfMonth( calendarToBeAdjusted.getTime() );
      int dayOfMonthForAdjustment = endOfMonthDay < dayOfMonth ? endOfMonthDay : dayOfMonth;
      calendarToBeAdjusted.set( Calendar.DAY_OF_MONTH, dayOfMonthForAdjustment );

      return calendarToBeAdjusted.getTime();
   }

   public static GregorianCalendar asCalendar( Date date ) {
      GregorianCalendar result = null;
      if( date != null ) {
         result = new GregorianCalendar();
         result.setTime( date );
      }
      return result;
   }

   public static int daysDifference( Date first, Date second ) {
      Calendar firstCalendar = getCalendar( first );
      Calendar secondCalendar = getCalendar( second );
      if( second.before( first ) ) {
         firstCalendar = getCalendar( second );
         secondCalendar = getCalendar( first );
      }
      int firstYear = firstCalendar.get( Calendar.YEAR );
      int secondYear = secondCalendar.get( Calendar.YEAR );
      if( firstYear == secondYear ) {
         return secondCalendar.get( Calendar.DAY_OF_YEAR ) - firstCalendar.get( Calendar.DAY_OF_YEAR );
      }
      Calendar firstYearEnd = getCalendar( getYearEnd( firstCalendar.getTime() ) );
      Calendar secondYearBeginning = getCalendar( getYearBeginning( secondCalendar.getTime() ) );

      int firstYearDiff = firstYearEnd.get( Calendar.DAY_OF_YEAR ) - firstCalendar.get( Calendar.DAY_OF_YEAR );
      int secondYearDiff = secondCalendar.get( Calendar.DAY_OF_YEAR ) - secondYearBeginning.get( Calendar.DAY_OF_YEAR );

      int middleDiff = 0;
      int startYear = firstYearEnd.get( Calendar.YEAR ) + 1;
      int endYear = secondCalendar.get( Calendar.YEAR );
      for( int year = startYear; year < endYear; year++ ) {
         Calendar yearBegin = Calendar.getInstance();
         yearBegin.set( year, Calendar.JANUARY, 1 );
         Calendar yearEnd = Calendar.getInstance();
         yearEnd.set( year, Calendar.DECEMBER, 31 );
         int daysInYear = yearEnd.get( Calendar.DAY_OF_YEAR ) - yearBegin.get( Calendar.DAY_OF_YEAR );
         middleDiff += daysInYear + 1;
      }
      int daysDiff = firstYearDiff + middleDiff + secondYearDiff + 1;
      return daysDiff;
   }

   public static String formatDate( Date date ) {
      return formatDate( date, null );
   }

   public static String formatDate( Date date, String format ) {
      return date == null ? "" : createDateFormatter( format ).format( date );
   }

   public static String formatTimestamp( Date timestamp ) {
      return formatTimestamp( timestamp, null );
   }

   public static String formatTimestamp( Date timestamp, String format ) {
      return timestamp == null ? "" : createTimestampFormatter( format ).format( timestamp );
   }

   public static String formatTimestampForFileName( Date timestamp ) {
      return formatTimestamp( timestamp, Format_FileNameTimestamp );
   }

   public static String formatTimestampForUrl( Date timestamp ) {
      return formatTimestamp( timestamp, Format_UrlTimestamp );
   }

   public static int getAge( Date birthDate, Date effectiveDate ) {
      int age = yearsDifference( birthDate, effectiveDate );
      if( age > 0 && effectiveDate.before( addYears( birthDate, age ) ) ) {
         age--;
      }
      return age;
   }

   public static Date getBeginingOfTheMonth( Calendar calendar ) {
      calendar.set( Calendar.DAY_OF_MONTH, 1 );
      return calendar.getTime();
   }

   public static Date getBeginingOfTheMonth( Date date ) {
      Calendar calendar = getCalendar( date );
      return getBeginingOfTheMonth( calendar );
   }

   public static Calendar getCalendar() {
      return Calendar.getInstance();
   }

   public static Calendar getCalendar( Date date ) {
      Calendar calendar = getCalendar();
      calendar.setTime( date );
      return calendar;
   }

   public static Date getCurrentYearsDate( Date otherDate, Date currentDate ) {
      int currentYear = getYear( currentDate );
      Date currentAnniversaryDate = getTime( currentYear, getMonth( otherDate ), getDay( otherDate ) );
      return currentAnniversaryDate;
   }

   public static Date getDate( Object value ) {
      Date result = null;
      if( value != null ) {
         if( value instanceof Date ) {
            result = ( Date )value;
         }
         else if( value instanceof String ) {
            result = DateUtl.parseDate( ( String )value );
         }
         else {
            throw new RuntimeException( "Cannot get value: " + value + " of type " + value.getClass() + " as Date" );
         }
      }
      return result;
   }

   public static List<Date> getDatesListBeforeEndDate( Date startDate, Date endDate ) {
      Validate.notNull( startDate, "Start Date must be specified" );
      Validate.notNull( endDate, "End Date must be specified" );
      final List<Date> datesList = new ArrayList<Date>();
      while( true ) {
         datesList.add( startDate );
         Date nextDay = DateUtl.addDays( startDate, 1 );
         if( DateUtl.isAfterOrEqual( nextDay, endDate ) ) {
            break;
         }
         startDate = nextDay;
      }
      return datesList;
   }

   public static int getDay( Date date ) {
      return getCalendar( date ).get( Calendar.DAY_OF_MONTH );
   }

   public static int getDayOfMonth( Calendar calendar ) {
      return calendar.get( Calendar.DAY_OF_MONTH );
   }

   public static int getDayOfMonth( Date date ) {
      Calendar calendar = getCalendar( date );
      return getDayOfMonth( calendar );
   }

   public static int getDayOfWeek( Calendar calendar ) {
      return calendar.get( Calendar.DAY_OF_WEEK );
   }

   public static int getDayOfWeek( Date date ) {
      return getDayOfWeek( getCalendar( date ) );
   }

   public static String getDayOfWeekName( Date date ) {
      final DateFormat dayOfWeekFormat = new SimpleDateFormat( "EEEE" );
      return dayOfWeekFormat.format( date );
   }

   public static int getDaysInYear( Date date ) {
      return getCalendar( date ).getActualMaximum( Calendar.DAY_OF_YEAR );
   }

   public static Integer getDurationMonths( Date startDate, Date currentDate ) {
      Integer durationMonths = DateUtl.monthsDifference( startDate, currentDate );
      final Date currentStartDate = DateUtl.addMonths( startDate, durationMonths );
      if( currentDate.compareTo( currentStartDate ) < 0 && durationMonths > 0 ) {
         durationMonths--;
      }
      return durationMonths;
   }

   public static Integer getDurationYears( Date startDate, Date currentDate ) {
      return getDurationYears( startDate, currentDate, false );
   }

   public static Integer getDurationYears( Date startDate, Date currentDate, boolean countAnniversaryAsEndOfYear ) {
      Integer durationYears = DateUtl.yearsDifference( startDate, currentDate );
      final Date currentStartDate = DateUtl.getCurrentYearsDate( startDate, currentDate );
      boolean afterFirstYear = durationYears > 0;
      boolean beforeAnniversary = ( countAnniversaryAsEndOfYear && currentDate.compareTo( currentStartDate ) <= 0 ) || ( !countAnniversaryAsEndOfYear && currentDate.compareTo( currentStartDate ) < 0 );
      if( afterFirstYear && beforeAnniversary ) {
         durationYears--;
      }
      return durationYears;
   }

   public static Date getEarliestDate( Date... dates ) {
      final Iterator<Date> iter = Arrays.asList( dates ).iterator();
      Date earliestDate = iter.next();
      while( iter.hasNext() ) {
         final Date nextDate = iter.next();
         if( nextDate.before( earliestDate ) ) {
            earliestDate = nextDate;
         }
      }
      return earliestDate;
   }

   public static int getEndDayOfMonth( Calendar calendar ) {
      return calendar.getActualMaximum( Calendar.DAY_OF_MONTH );
   }

   public static int getEndDayOfMonth( Date date ) {
      Calendar calendar = getCalendar( date );
      return getEndDayOfMonth( calendar );
   }

   public static Date getLastAnniversaryDate( Date otherAnniversaryDate, Date currentDate ) {
      Date anniversaryDate = DateUtl.getCurrentYearsDate( otherAnniversaryDate, currentDate );
      if( anniversaryDate.after( currentDate ) ) {
         anniversaryDate = DateUtl.addYears( anniversaryDate, -1 );
      }
      return anniversaryDate;
   }

   public static Date getLatestDate( Date... dates ) {
      final Iterator<Date> iter = Arrays.asList( dates ).iterator();
      Date latestDate = iter.next();
      while( iter.hasNext() ) {
         final Date nextDate = iter.next();
         if( nextDate.after( latestDate ) ) {
            latestDate = nextDate;
         }
      }
      return latestDate;
   }

   public static int getMonth( Calendar calendar ) {
      return calendar.get( Calendar.MONTH );
   }

   public static int getMonth( Date date ) {
      return getCalendar( date ).get( Calendar.MONTH );
   }

   public static Date getMonthBeginning( Date date ) {
      Calendar monthBeginning = getCalendar( date );
      monthBeginning.set( Calendar.DAY_OF_MONTH, 1 );
      return monthBeginning.getTime();
   }

   public static Date getMonthEnd( Date date ) {
      Calendar monthEnd = getCalendar( date );
      monthEnd.set( Calendar.DAY_OF_MONTH, monthEnd.getActualMaximum( Calendar.DAY_OF_MONTH ) );
      return monthEnd.getTime();
   }

   public static Date getNearestFridayDate( Date date ) {
      Date result = null;
      Calendar calendar = getCalendar( date );
      int dayOfWeek = calendar.get( Calendar.DAY_OF_WEEK );
      if( dayOfWeek == Calendar.FRIDAY ) {
         result = date;
      }
      else {
         int daysDifference = Calendar.FRIDAY - dayOfWeek;
         if( daysDifference < 4 ) {
            //Includes Saturday(-1), Tuesday, Wednesday, Thursday
            calendar.add( Calendar.DATE, daysDifference );
         }
         else {
            //Includes Sunday and Monday
            int daysToFallBack = -1 * ( 7 - daysDifference );
            calendar.add( Calendar.DATE, daysToFallBack );
         }
         result = calendar.getTime();
      }
      return result;
   }

   public static Date getNearestMonthEnd( Date date ) {
      if( isMonthEnd( date ) ) {
         return date;
      }
      return getMonthEnd( addMonths( date, -1 ) );
   }

   public static Date getNextAnniversaryDate( Date otherAnniversaryDate, Date currentDate ) {
      Date anniversaryDate = DateUtl.getCurrentYearsDate( otherAnniversaryDate, currentDate );
      if( anniversaryDate.before( currentDate ) ) {
         anniversaryDate = DateUtl.addYears( anniversaryDate, 1 );
      }
      return anniversaryDate;
   }

   public static Date getTime( int year, int month, int date ) {

      Calendar calendar = getCalendar();
      calendar.clear();
      calendar.set( year, month, date );
      return calendar.getTime();
   }

   public static Timestamp getTimestamp( Date date ) {
      return new Timestamp( date.getTime() );
   }

   public static int getYear( Date d ) {

      return getCalendar( d ).get( Calendar.YEAR );
   }

   public static Date getYearBeginning( Date date ) {

      Calendar yearBeginning = getCalendar( date );
      yearBeginning.set( Calendar.MONTH, Calendar.JANUARY );
      yearBeginning.set( Calendar.DAY_OF_MONTH, 1 );
      return yearBeginning.getTime();
   }

   public static Date getYearEnd( Calendar calendar ) {
      calendar.set( Calendar.MONTH, Calendar.DECEMBER );
      calendar.set( Calendar.DAY_OF_MONTH, 31 );
      return calendar.getTime();
   }

   public static Date getYearEnd( Date date ) {
      Calendar calendar = getCalendar( date );
      return getYearEnd( calendar );
   }

   public static Date getYearEnd( Integer year ) {
      Calendar calendar = getCalendar();
      calendar.set( Calendar.YEAR, year );
      return getYearEnd( calendar );
   }

   public static boolean is366DayYear( Date startDate ) {
      boolean is366DayYear = false;
      Calendar calendar = getCalendar( startDate );
      if( isLeapYear( calendar ) ) {
         int month = calendar.get( Calendar.MONTH );
         if( month < Calendar.MARCH ) {
            is366DayYear = true;
         }
      }
      else {
         calendar = addYears( calendar, 1 );
         if( isLeapYear( calendar ) ) {
            int month = calendar.get( Calendar.MONTH );
            if( month > Calendar.FEBRUARY ) {
               is366DayYear = true;
            }
         }
      }
      return is366DayYear;
   }

   public static boolean isAfter( Date date1, Date date2 ) {
      Assert.notNull( date1, "Must specify a value for date1" );
      Assert.notNull( date2, "Must specify a value for date2" );
      return date2.compareTo( date1 ) < 0;
   }

   public static boolean isAfterOrEqual( Date date1, Date date2 ) {
      Validate.notNull( date1, "Must specify a value for date1" );
      Validate.notNull( date2, "Must specify a value for date2" );
      return date2.compareTo( date1 ) <= 0;
   }

   public static boolean isBefore( Date date1, Date date2 ) {
      Assert.notNull( date1, "Must specify a value for date1" );
      Assert.notNull( date2, "Must specify a value for date2" );
      return date1.compareTo( date2 ) < 0;
   }

   public static boolean isBeforeOrEqual( Date date1, Date date2 ) {
      Assert.notNull( date1, "Must specify a value for date1" );
      Assert.notNull( date2, "Must specify a value for date2" );
      return date1.compareTo( date2 ) <= 0;
   }

   public static boolean isBetween( Date date, Date startDate, Date endDate ) {
      Validate.notNull( date, "Must specify a value for date" );
      Validate.notNull( startDate, "Must specify a value for Start Date" );
      Validate.notNull( endDate, "Must specify a value for End Date" );
      return isAfterOrEqual( date, startDate ) && isBeforeOrEqual( date, endDate );
   }

   public static final boolean isDate( String text ) {
      return isDate( text, createDateFormatter( null ) );
   }

   public static boolean isFirstHalfCalendarYear( Date date ) {
      boolean firstHalf = true;
      Calendar calendar = getCalendar( date );
      int month = calendar.get( Calendar.MONTH );
      if( month > Calendar.JUNE ) {
         firstHalf = false;
      }
      return firstHalf;
   }

   public static boolean isLeapYear( Calendar calendar ) {
      int year = calendar.get( Calendar.YEAR );
      return isLeapYear( year );
   }

   public static boolean isLeapYear( Date date ) {
      Calendar calendar = getCalendar( date );
      return isLeapYear( calendar );
   }

   public static boolean isLeapYear( int year ) {
      return ( ( year % 4 == 0 ) && ( year % 100 != 0 ) ) || ( year % 400 == 0 );
   }

   public static boolean isMonthEnd( Date date ) {
      return date != null && ( 0 == date.compareTo( getMonthEnd( date ) ) );
   }

   public static Date maxOf( Date... dates ) {
      Date result = null;
      for( Date date : dates ) {
         if( result == null ) {
            result = date;
         }
         else if( result != null && date != null ) {
            result = isAfter( date, result ) ? date : result;
         }
      }
      return result;
   }

   public static int monthsDifference( Date first, Date second ) {
      int difference = 0;
      Calendar firstCalendar = getCalendar( first );
      Calendar secondCalendar = getCalendar( second );
      while( firstCalendar.get( Calendar.YEAR ) != secondCalendar.get( Calendar.YEAR ) ) {
         int diff = 12 * ( secondCalendar.get( Calendar.YEAR ) - firstCalendar.get( Calendar.YEAR ) );
         difference += diff;
         firstCalendar.add( Calendar.MONTH, diff );
      }
      if( firstCalendar.get( Calendar.MONTH ) != secondCalendar.get( Calendar.MONTH ) ) {
         int diff = secondCalendar.get( Calendar.MONTH ) - firstCalendar.get( Calendar.MONTH );
         difference += diff;
      }
      difference = Math.abs( difference );
      int firstDay = firstCalendar.get( Calendar.DAY_OF_MONTH );
      int secondDay = secondCalendar.get( Calendar.DAY_OF_MONTH );
      if( first.before( second ) ) {
         if( firstDay > secondDay ) { // Not a whole month
            difference--;
         }
      }
      else if( first.after( second ) ) {
         if( secondDay > firstDay ) { // Not a whole month
            difference--;
         }
      }
      return difference;
   }

   public static int monthsProcessed( Date first, Date second ) {
      return monthsDifference( first, second ) + 1;
   }

   public static Date parseDate( String text ) {
      return parse( text, createDateFormatter( null ) );
   }

   public static Date parseDate( String text, String format ) {
      return parse( text, createDateFormatter( format ) );
   }

   public static Date parseTimestamp( String text ) {
      return parse( text, createTimestampFormatter( null ) );
   }

   public static int yearsDifference( Date first, Date second ) {
      int firstYear = getYear( first );
      int secondYear = getYear( second );
      return Math.abs( firstYear - secondYear );
   }

   private static DateFormat createDateFormatter( String format ) {
      if( format == null ) {
         return new SimpleDateFormat( Format_Date );
      }
      return new SimpleDateFormat( format );
   }

   private static DateFormat createTimestampFormatter( String format ) {
      if( format == null ) {
         return new SimpleDateFormat( Format_Timestamp );
      }
      return new SimpleDateFormat( format );
   }

   private static boolean isDate( String text, DateFormat format ) {
      boolean result = true;
      try {
         parse( text, format );
      }
      catch( Exception exception ) {
         result = false;
      }
      return result;
   }

   private static final Date parse( String text, DateFormat format ) {
      try {
         return new Date( format.parse( text ).getTime() );
      }
      catch( ParseException exception ) {
         throw new RuntimeException( exception );
      }
   }

   public static boolean isSameDay( Date date1, Date date2 ) {
      return org.apache.commons.lang3.time.DateUtils.isSameDay( date1, date2 );
   }

}
