package net.wistools.utl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.wistools.data.IDataObject;
import net.wistools.data.ReportTransient;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class ExcelUtl {

   public static <T extends IDataObject> Workbook buildWorkbookFromDataMap( Map<String, List<T>> dataMap, Class<? extends IDataObject> dataType ) {
      return buildWorkbookFromDataMap( dataMap, dataType, 0 );
   }

   public static <T extends IDataObject> Workbook buildWorkbookFromDataMap( Map<String, List<T>> dataMap, Class<? extends IDataObject> dataType, int headerRowNum ) {
      try {
         final Workbook workbook = new XSSFWorkbook();
         addSheetsToWorkbookFromDataMap( dataMap, dataType, headerRowNum, workbook );
         return workbook;
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
         return null;
      }
   }

   public static <T extends IDataObject> void addSheetsToWorkbookFromDataMap( Map<String, List<T>> dataMap, Class<? extends IDataObject> dataType, int headerRowNum, final Workbook workbook ) {
      try {
         if( dataMap != null && !dataMap.isEmpty() ) {
            final List<ReportField> reportFields = getReportFields( dataType );
            for( String key : dataMap.keySet() ) {
               final Sheet sheet = workbook.createSheet( key );
               if( dataMap.get( key ) != null && !dataMap.get( key ).isEmpty() ) {
                  addHeaders( workbook, sheet, dataType, headerRowNum, reportFields );
                  addDataToSheet( sheet, dataMap.get( key ), headerRowNum, reportFields );
                  formatColumns( sheet, dataMap, dataType, reportFields );
               }
            }
         }
      }
      catch( Exception e ) {
         ValidateUtl.fail( e );
      }
   }

   private static List<ReportField> getReportFields( Class<? extends IDataObject> clazz ) {
      final List<ReportField> reportFields = new ArrayList<>();
      for( Field field : clazz.getDeclaredFields() ) {
         if( !field.isAnnotationPresent( ReportTransient.class ) ) {
            if( field.isAnnotationPresent( net.wistools.data.ReportField.class ) ) {
               reportFields.add( new ReportField( FormatUtl.firstLetterCaps( field.getName() ), field.getAnnotation( net.wistools.data.ReportField.class ).name() ) );
            }
            else {
               reportFields.add( new ReportField( FormatUtl.firstLetterCaps( field.getName() ) ) );
            }
         }
      }
      return reportFields;
   }

   public static void addHeaders( Workbook workbook, Sheet sheet, Class<? extends IDataObject> clazz, int headerRowNum, List<ReportField> reportFields ) {
      final Row headerRow = sheet.createRow( headerRowNum );
      final CellStyle headerStyle = getHeaderStyle( workbook );
      int cellCol = 0;
      for( ReportField reportField : reportFields ) {
         addHeaderCell( headerRow, cellCol++, reportField, headerStyle );
      }
   }

   private static int addHeaderCell( final Row row, int cellCol, ReportField reportField, CellStyle headerStyle ) {
      final Cell headerCell = row.createCell( cellCol );
      headerCell.setCellValue( reportField.columnName );
      headerCell.setCellStyle( headerStyle );
      return cellCol;
   }

   private static CellStyle getHeaderStyle( Workbook workbook ) {
      final Font font = workbook.createFont();
      font.setBoldweight( Font.BOLDWEIGHT_BOLD );
      font.setColor( IndexedColors.WHITE.getIndex() );
      font.setFontHeightInPoints( ( short )13 );

      final CellStyle headerStyle = workbook.createCellStyle();
      headerStyle.setFillForegroundColor( HSSFColor.DARK_BLUE.index );
      headerStyle.setFillPattern( CellStyle.SOLID_FOREGROUND );
      headerStyle.setFont( font );
      return headerStyle;
   }

   public static <T extends IDataObject> void addDataToSheet( Sheet sheet, List<T> dataObjects, int headerRowNum, List<ReportField> reportFields ) throws Exception {
      int rownum = headerRowNum + 1;
      for( IDataObject dataObject : dataObjects ) {
         final Row dataRow = sheet.createRow( rownum++ );
         int columnIndex = 0;
         for( ReportField reportField : reportFields ) {
            final Cell dataCell = dataRow.createCell( columnIndex++ );
            final Object dataValue = dataObject.getClass().getMethod( "get" + reportField.fieldName ).invoke( dataObject );
            dataCell.setCellValue( format( dataValue ) );
         }
      }
   }

   //At some point we may want to utilize excel cell datatypes rather than storing everything as text
   private static String format( Object dataValue ) {
      if( dataValue == null ) {
         return "NULL";
      }
      if( dataValue instanceof BigDecimal ) {
         return new DecimalFormat( "#,###" ).format( dataValue );
      }
      if( dataValue instanceof Double ) {
         dataValue = MathUtl.round2( ( Double )dataValue );
      }
      return dataValue.toString();
   }

   private static <T extends IDataObject> void formatColumns( Sheet sheet, Map<String, List<T>> dataMap, Class<? extends IDataObject> dataType, List<ReportField> reportFields ) {
      final int columns = reportFields.size();
      for( int columnIndex = 0; columnIndex < columns; columnIndex++ ) {
         sheet.autoSizeColumn( columnIndex );
      }
   }

   public static void formatColumns( Workbook workbook, int numberOfColumns ) {
      for( int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++ ) {
         final Sheet sheet = workbook.getSheetAt( sheetIndex );
         for( int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++ ) {
            sheet.autoSizeColumn( columnIndex );
         }
      }
   }

   public static void addIndividualDataField( Workbook workbook, String sheetName, int row, int column, String fieldName, Object fieldValue ) {
      final Sheet sheet = workbook.getSheet( sheetName );
      final Row fieldNameRow = sheet.getRow( row ) != null ? sheet.getRow( row ) : sheet.createRow( row );
      final Row fieldValueRow = sheet.getRow( row + 1 ) != null ? sheet.getRow( row + 1 ) : sheet.createRow( row + 1 );
      final Cell fieldNameCell = fieldNameRow.createCell( column );
      final Cell fieldValueCell = fieldValueRow.createCell( column );
      fieldNameCell.setCellStyle( getHeaderStyle( workbook ) );
      fieldNameCell.setCellValue( fieldName );
      fieldValueCell.setCellValue( fieldValue != null ? fieldValue.toString() : "NULL" );
   }

   private static class ReportField {
      String fieldName;
      String columnName;

      public ReportField( String fieldName ) {
         this.fieldName = fieldName;
         this.columnName = fieldName;
      }

      public ReportField( String fieldName, String columnName ) {
         this.fieldName = fieldName;
         this.columnName = columnName;
      }
   }
}
