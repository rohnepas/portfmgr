package portfmgr.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import portfmgr.portfmgrApplication;
import portfmgr.model.OnlineCourseQuery;
import portfmgr.model.Portfolio;
import portfmgr.model.PortfolioCalculator;
import portfmgr.model.PortfolioRepository;
import portfmgr.model.Transaction;
import portfmgr.model.TransactionRepository;

/**
 * This class represents one of the main controllers and manage the detail view about the portfolios.
 * 
 * @author Marc Steiner & pascal.rohner
 */

@Controller
public class PortfolioDetailViewController implements Initializable {

	private portfmgrApplication mainApp;
	private Portfolio portfolio;
	private JSONObject onlineDataJSON;
	private List<String> currencyList;
	private List<String> cryptocurrencyList;
	private static String coinlistPath = "src/main/java/coinlist/coinlist.json";
	
	@Autowired
	PortfolioRepository portRepo;
	
	@Autowired
	TransactionRepository transRepo;
	
	@FXML
	private TableView<Transaction> transactionTable;
	@FXML
	private TableColumn<Transaction, LocalDate> transactionDateColumn;
	@FXML
	private TableColumn<Transaction, String> transactionTypeColumn;
	@FXML
	private TableColumn<Transaction, String> transactionCurrencyColumn;
	@FXML
	private TableColumn<Transaction, Double> transactionPriceColumn;
	@FXML
	private TableColumn<Transaction, Double> transactionAmountColumn;
	@FXML
	private TableColumn<Transaction, Double> transactionFeesColumn;
	@FXML
	private TableColumn<Transaction, Double> transactionTotalColumn;
	@FXML
	private TableColumn<?, String> overviewCurrencyColumn;
	@FXML
	private Label portfolioName;
	@FXML
	private Label portfolioCurrency;
	@FXML
	private Label profitOrLoss;
	@FXML
	private Label profitOrLossPercentage;
	@FXML
	private Label totalPortoflioValue;
	@FXML
	private ImageView logo;
	@FXML
	private Button addTransaction;
	@FXML
	private Button deleteTransaction;
	

	/**
	 * Calls method from mainApp to open the portfolioView
	 */
	public void openPortfolioView() {
		mainApp.openPortfolioView();

	}
	
	public void setMainApp(portfmgrApplication mainApp) {
		this.mainApp = mainApp;
		setCurrencyList();
		checkAndSetPortfolioSettings();
		updatePortfolio();
	}
	
	public void setCurrencyList() {
		currencyList = Arrays.asList("CHF", "EUR", "USD");
	}
	
	/*
	 * Checks if the choosen portolio has a proper name and currency set. If not opens updateView pop-up
	 */
	public void checkAndSetPortfolioSettings() {

		String tempPortfolioName = portfolio.getPortfolioName();
		String tempPortfolioCurrency = portfolio.getPortfolioCurrency();
		
		boolean currencyExists = currencyList.stream().anyMatch(str -> str.trim().equals(tempPortfolioCurrency));
		
		if (tempPortfolioName == "leeres Portfolio" || tempPortfolioName == "" || !currencyExists) {
			
			mainApp.openPortfolioUpdateView(portfolio, currencyList);
			
		}
		else {
			portfolioName.setText(portfolio.getPortfolioName());
			portfolioCurrency.setText(portfolio.getPortfolioCurrency());
			
		}
	}
	
	/*
	 * Refreshes the portfolio name and currency.
	 */
	public void refreshPortfolio() {

		portfolioName.setText(portfolio.getPortfolioName());
		portfolioCurrency.setText(portfolio.getPortfolioCurrency());
		
		// TO DO: UPDATE DATA
		profitOrLoss.setText("GEWINN VERLUST CHF");
		profitOrLossPercentage.setText("GEWINN VERLUST %");
		totalPortoflioValue.setText("WERT PORTFOLIO");
	}
	
	/*
	 * Method called if refresh button is clicked. It finds all symbols of crypto currencies in this portfolio
	 * and calls the portfolio calculate class
	 */
	public void updatePortfolio() {
		 
		setCryptocurrencyList();
		onlineDataJSON = onlineCourseQuery();
		PortfolioCalculator calculator = new PortfolioCalculator(portfolio, onlineDataJSON, cryptocurrencyList, currencyList, coinlistPath);
		calculator.calculatePortfolio();
		refreshPortfolio();
		
	}
	
	public void setCryptocurrencyList() {
		/*
		 * TO DO:
		 * Finde alle CryptoCurrencies die in diesem Portfolio vorkommen
		 * aktuell eine Testliste mit verschiedenen Kryptos implementiert
		*/
		cryptocurrencyList = Arrays.asList("BTC", "ETH", "LTC", "XRP", "TRX", "IOT");
	}
	
	/*
	 * Calls the Web API and query the data
	 * @return JSON Object with crypto currency data
	 */
	public JSONObject onlineCourseQuery() {
		
		OnlineCourseQuery query = new OnlineCourseQuery();
		JSONObject data;
		
		query.setSymbols(getCryptoCurrencyList());
		query.setCurrencies(getCurrencyList());
		
		try {
			data = query.getOnlineCourseData();
			return data;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	
	public List<String> getCryptoCurrencyList(){
		return cryptocurrencyList;
	}
	
	public List<String> getCurrencyList(){
		return currencyList;
	}
	
	
	/*
	 * Sends actual portfolio to the main app, which handles starts the UpdateViewController.
	 * If something has changed in the update view, the data will be saved directly to the database
	 */
	public void editPortfolio() {
		
		mainApp.openPortfolioUpdateView(portfolio, currencyList);
		refreshPortfolio();
	
	}
	
	public void deletePortfolio() {
		System.out.println("Portfolio DELETE");
		/*
		 * TO DO: Delete Portfolio
		 */
	}
	
	/*
	 * Export the portfolio into a Excel Sheet with Apache POI
	 * Code snippets from: https://www.tutorialspoint.com/apache_poi/apache_poi_spreadsheets.htm
	 */
	public void exportPortfolio() throws IOException {
		String[] columns = {"Name", "Anzahl", "Preis pro Stk.", "Total"};
		
		//Define output folder path
		String path = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "CryptoPortfolios";
		
	    //Define file name with actual date in Format 2019/01/01
		String fileName = Calendar.getInstance().get(Calendar.YEAR) + "_CryptoPortfolio.xlsx";
		
		//Create Workbook - XSSF: Used for dealing with files excel 2007 or later(.xlsx)
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("CryptoPortfolio");
		
		// Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        
        // Create a Font for styling of normal cells
        Font cellsFont = workbook.createFont();
        cellsFont.setBold(false);
        cellsFont.setFontHeightInPoints((short) 11);
        cellsFont.setColor(IndexedColors.BLACK.getIndex());

        // Create a CellStyle with the font for header
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // Create a CellStyle with the font for normal cells
        CellStyle normalCellStyle = workbook.createCellStyle();
        normalCellStyle.setFont(cellsFont);
        
        // Create header row and write header
        Row headerRow = sheet.createRow(0);        
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
        
        /*
         * This data needs to be written (Object[]). 
         * The number "1", "2"... defines the order in a TreeMap, therefore the
         * order which data will be shown first in the Excel sheet
         * 
         * TO DO: Portfolio Daten abfüllen inkl. Währung übergeben
         * 
         */
        Map<String, Object[]> empinfo = new TreeMap<String, Object[]>();
        empinfo.put( "1", new Object[] { "Bitcoin", "1", "4000", "4000" });
        empinfo.put( "2", new Object[] { "Ethereum", "10", "200", "2000" });
        empinfo.put( "3", new Object[] { "Litecoin", "100", "50", "5000" });

        //Iterate over data and write to sheet.rowid = 1, because the row 0 is the header row info
        Set<String> keyid = empinfo.keySet();
        int rowid = 1;
        String currency = " CHF";

        for (String key : keyid) {
           Row row = sheet.createRow(rowid++);
           Object [] objectArr = empinfo.get(key);
           int cellid = 0;

           for (Object obj : objectArr) {
              Cell cell = row.createCell(cellid++);
              
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
	 * Opens the transaction dialog to add a transaction
	 * 
	 * @author pascal.rohner
	 */
	
	public void addTransaction() {
		mainApp.openTransactionViewAdd(coinlistPath);
		
	}
	
	
	/**
	 * Deletes a transaction from the transaction table
	 * 
	 * @author pascal.rohner
	 */
	public void deleteTransaction() {
		
		// Creation of a observable list for selected transaction and all transaction
		ObservableList<Transaction> selectedTransactions, allTransactions;
		
		allTransactions = transactionTable.getItems();
		
		// gives the selected rows
		selectedTransactions = transactionTable.getSelectionModel().getSelectedItems();
		
		// loops over the selected rows and removes them from the transaction table
		for (Transaction transaction : selectedTransactions) {
			allTransactions.remove(transaction);
		}
		
		// ToDo: Beim Lï¿½schen muss die Transation noch aus der Datenbank gelï¿½scht werden!
		
	}
	
	
	
	public void setActualPortoflio(Portfolio portfolio) {
		this.portfolio = portfolio;	
	}
	
	
	
	/**
	 * Returns an ObservableList of Transaction objects
	 * @return ObversableList of transactions
	 * @author pascal.rohner 
	 */
	public ObservableList<Transaction> getTransaction(){	
		ObservableList<Transaction> transaction = FXCollections.observableArrayList();
		
		// vorerst werden nur mal BTC-Positionen angezeigt. Es kï¿½nnen aktuell auch nur BTC Positionen erfasst werden.
		transaction.addAll(transRepo.findByCurrency("BTC"));
		
		return transaction;
		}
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/**
		 * Sets up the columns for the transaction table
		 * @author pascal.rohner
		 */
		transactionDateColumn.setCellValueFactory(new PropertyValueFactory<Transaction, LocalDate>("transactionDate"));
		transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("type"));
		transactionCurrencyColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("currency"));
		transactionPriceColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("priceUSD"));
		transactionPriceColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("priceBTC"));
		transactionPriceColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("priceEUR"));
		transactionPriceColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("priceCHF"));
		transactionAmountColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("numberOfCoins"));
		transactionFeesColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("feesUSD"));
		transactionFeesColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("feesBTC"));
		transactionFeesColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("feesEUR"));
		transactionFeesColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("feesCHF"));
		transactionTotalColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("total"));

		/**
		 * Gets all the transactions which need to been shown in the transaction table
		 * 
		 * @author pascal.rohner
		 */
				
		transactionTable.setItems(getTransaction());
		
		/**
		 * Allows the user to just select a single rows in the transaction table
		 * 
		 * @author pascal.rohner
		 */
		
		transactionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	
					
	}
	
}


	
	


