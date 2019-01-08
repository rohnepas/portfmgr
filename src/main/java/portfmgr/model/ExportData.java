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
	
	//Create Workbook - XSSF: Used for dealing with files excel 2007 or later(.xlsx)
	private Workbook workbook = new XSSFWorkbook();
	private Sheet sheet = workbook.createSheet("CryptoPortfolio");
	
	// Define columen structur of Excel sheet
	private String[] columns = {"Name", "Anzahl", "Preis pro Stk.", "Total"}; 
	
	//Define output folder path
	private String path = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "CryptoPortfolios";
	
	//Define file name with actual date in Format 2019/01/01
	private String fileName = Calendar.getInstance().get(Calendar.YEAR) + "_CryptoPortfolio.xlsx";
	
	private Font headerFont = workbook.createFont();
	private Font cellsFont = workbook.createFont();
	private CellStyle headerCellStyle = workbook.createCellStyle();
	private CellStyle normalCellStyle = workbook.createCellStyle();
	private Map<String, Object[]> data = new TreeMap<String, Object[]>();
	
	
	public ExportData(Portfolio portfolio) throws IOException {
		this.portfolio = portfolio;
		setup();
		exportData();
	}
	
	/**
	 * Set styling of the Excel sheet and add the header row information 
	 */
	public void setup() {
		// Create a Font for styling header cells
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        
        // Create a Font for styling of normal cells
        cellsFont.setBold(false);
        cellsFont.setFontHeightInPoints((short) 11);
        cellsFont.setColor(IndexedColors.BLACK.getIndex());

        // Create a CellStyle with the font for header
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // Create a CellStyle with the font for normal cells
        normalCellStyle.setFont(cellsFont);

		// Create header row and write header
        Row headerRow = sheet.createRow(0);        
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
	}
       
	/**
	 * Call the method to extract the data from the portolio and write data into the Excel sheet
	 */
	public void exportData() throws IOException {

        int rowid = 1;
        String currency = portfolio.getPortfolioCurrency();
        extractData();
            
        //Iterate over data and write to sheet.rowid = 1, because the row 0 is the header row info
        Set<String> keyid = data.keySet();
        
        for (String key : keyid) {
           Row row = sheet.createRow(rowid++);
           Object [] objectArr = data.get(key);
           int cellid = 0;

           for (Object obj : objectArr) {
              Cell cell = row.createCell(cellid++);
              
              // Add portfolio currency to the "money cells"
              if(cellid > 2) {
            	  cell.setCellValue((String)obj + currency);
              }
              
              else {
            	  cell.setCellValue((String)obj);
              }
              
              cell.setCellStyle(normalCellStyle);
           }
        }
        
        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
                
        // Create folder if not exists     
        File fileDir = new File(path);
        
        if (fileDir.exists()) {
            System.out.println(fileDir + " already exists");
        } else if (fileDir.mkdirs()) {
            System.out.println(fileDir + " was created");
        } else {
            System.out.println(fileDir + " was not created");
        }
        
        // Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(path + File.separator + fileName);
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
