package portfmgr.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import javafx.collections.ObservableList;

/**
 * This class handles the Excel sheet export
 * @author Marc Steiner
 */
public class ExportData {
	
	private Portfolio portfolio;
	private File file;
	private int indexTitleRow = 0;
	private int indexDateRow = 1;
	private int indexHeaderRow = 3;
	private int indexDataRow = 4;
	private Map<String, Object[]> data = new TreeMap<String, Object[]>();
	private ObservableList<Insight> insights;
	
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

	
	public ExportData(Portfolio portfolio, File file, ObservableList<Insight> insights) throws IOException {
		this.portfolio = portfolio;
		this.file = file;
		this.insights = insights;
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
        Cell titleCell = titleRow.createCell(0);        
        titleCell.setCellValue(calendarYear + " PORTFOLIO: " + portfolio.getPortfolioName());
        titleCell.setCellStyle(titleCellStyle);
        
        // Create and write date row
        Row dateRow = sheet.createRow(indexDateRow);        
        Cell dateCell = dateRow.createCell(0);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy - HH:mm"); 
        Date currentTime = new Date(); 
        dateCell.setCellValue("Export erstellt am: " + formatter.format(currentTime) + "Uhr");
        dateCell.setCellStyle(normalCellStyle);
        
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

        String fiatCurrency = portfolio.getPortfolioFiatCurrency();
        data = extractData();
            
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
            	  cell.setCellValue((String)obj + " " + fiatCurrency);
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
	 * @return 
     * 
     * @return data
     * 
     */
	public Map<String, Object[]> extractData() {
		
		int index = 1;
		
		for (Insight element: insights) {
			
			data.put(String.valueOf(index), new Object[] {
					element.getCryptoCurrency(), 
					String.valueOf(element.getNumberOfCoins()), 
					String.valueOf(element.getSpotPrice()), 
					String.valueOf(element.getTotal())
					});
			
			index++;			
		}
        
        return data;
	}
}
