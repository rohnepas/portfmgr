package portfmgr.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class handles the Excel sheet export
 * @author Marc Steiner
 */
public class ExportData {
	
	private Portfolio portfolio;
	private File file;
	private int indexTitleRow = 0;
	private int indexHeaderRow = 2;
	private int indexDataRow = 3;
	private Map<String, Object[]> data = new TreeMap<String, Object[]>();
	
	//Create Workbook - XSSF: Used for dealing with files excel 2007 or later(.xlsx)
	private Workbook workbook = new XSSFWorkbook();
	private Sheet sheet = workbook.createSheet("CryptoPortfolio");
	
	// Define columen structur of Excel sheet
	private String[] columns = {"Name", "Anzahl", "Preis pro Stk.", "Total"}; 

	//Define file name with actual date in Format 2019/01/01
	private int calendarYear = Calendar.getInstance().get(Calendar.YEAR);
	
	private Font headerFont = workbook.createFont();
	private Font cellsFont = workbook.createFont();
	private Font titleFont = workbook.createFont();
	private CellStyle headerCellStyle = workbook.createCellStyle();
	private CellStyle normalCellStyle = workbook.createCellStyle();
	private CellStyle titleCellStyle = workbook.createCellStyle();

	
	public ExportData(Portfolio portfolio, File file) throws IOException {
		this.portfolio = portfolio;
		this.file = file;
		setup();
		exportData();
	}
	
	/**
	 * Set styling of the Excel sheet and add the header row information 
	 */
	public void setup() {
		// Create a font for styling title cells
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 20);
        titleFont.setColor(IndexedColors.BLACK.getIndex());
		
		// Create a Font for styling header cells
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        
        // Create a Font for styling of normal cells
        cellsFont.setBold(false);
        cellsFont.setFontHeightInPoints((short) 11);
        cellsFont.setColor(IndexedColors.BLACK.getIndex());

        // Create a CellStyle with the font for title
        titleCellStyle.setFont(titleFont);
        //titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //titleCellStyle.setFillBackgroundColor(IndexedColors.RED.getIndex());
       
        // Create a CellStyle with the font for header
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //headerCellStyle.setFillBackgroundColor(IndexedColors.RED.getIndex());
        
        // Create a CellStyle with the font for normal cells
        normalCellStyle.setFont(cellsFont);

        // Create and write title row
        Row titleRow = sheet.createRow(indexTitleRow);        
        Cell titleCell = titleRow.createCell(indexTitleRow);
        titleCell.setCellValue(calendarYear + " PORTFOLIO: " + portfolio.getPortfolioName());
        titleCell.setCellStyle(titleCellStyle);
                
		// Create and write header row
        Row headerRow = sheet.createRow(indexHeaderRow);        
        for(int i = 0; i < columns.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(columns[i]);
            headerCell.setCellStyle(headerCellStyle);
        }
	}
       
	/**
	 * Call the method to extract the data from the portfolio and write data into the Excel sheet
	 * @throws IOException if there is a problem with writing or closing of the excel sheet
	 */
	public void exportData() throws IOException {

        String currency = portfolio.getPortfolioCurrency();
        extractData();
            
        //Iterate over data and write to sheet.
        Set<String> keyid = data.keySet();
        
        for (String key : keyid) {
           Row row = sheet.createRow(indexDataRow++);
           Object [] objectArr = data.get(key);
           int cellid = 0;

           for (Object obj : objectArr) {
              Cell cell = row.createCell(cellid++);
              
              // Add portfolio currency to the "money cells"
              if(cellid > 2) {
            	  cell.setCellValue((String)obj + currency);
              } else {
            	  cell.setCellValue((String)obj);
              }
              cell.setCellStyle(normalCellStyle);
           }
        }
        
        // Resize all columns (without title column = 0) to fit the content size
        for(int i = 1; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(file);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
	}
	
	
	 /**
     * This data needs to be written (Object[]). 
     * The number "1", "2"... defines the order in a TreeMap, therefore the
     * order which data will be shown first in the Excel sheet
     * 
     * @return data
     * 
     */
	public void extractData() {
		
		// TO DO: Portfolio Daten abfüllen inkl. Währung übergeben
        data.put( "1", new Object[] { "Bitcoin", "1", "4000", "4000" });
        data.put( "2", new Object[] { "Ethereum", "10", "200", "2000" });
        data.put( "3", new Object[] { "Litecoin", "100", "50", "5000" });

	}
		
}
