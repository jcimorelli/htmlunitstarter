package net.wistools.nav;

import org.apache.poi.ss.usermodel.Workbook;

public interface IWistoolsExcelCreator {

   public Workbook getWorkbook();

   public String getServerFolderName();

   public String getReportName();
}
