package portfmgr.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Entity class for persistence of transaction information in an embedded database
 *
 * @author: Pascal Rohner
 */

@Entity
public class Transaction {
	
	/*
	 * Id serves as unique key, it is unique and is automatically generated when
	 * adding a transaction to the database.
	 */
	@Id
	@Column(unique = true)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String cryptoCurrency;
	private String fiatCurrency;
	private Double numberOfCoins;
	private Double price;
	private LocalDate transactionDate;
	private String type;
	private Double fees;
	private Double total;
	
	@ManyToOne
	@JoinColumn(name = "portfolio_id")
	private Portfolio portfolio;
 
	/*
	 * Empty constructor
	 * 
	 */
	public Transaction() {};
	
	/*
	 * Overloaded constructor with some initial data
	 * 
	 */
	
	public Transaction(String cryptoCurrency, String fiatCurrency, Double numberOfCoins, Double price, LocalDate transactionDate, String type,
			Double fees, Double total) {
		super();
		this.cryptoCurrency = cryptoCurrency;
		this.fiatCurrency = fiatCurrency;
		this.numberOfCoins = numberOfCoins;
		this.price = price;
		this.transactionDate = transactionDate;
		this.type = type;
		this.fees = fees;
		this.total = total;
	}
	
	
	/*
	 * Method to query the primary key
	 * 
	 * @return Auto-generated primary key as a String
	 */
	public Long getId() {
		return id;
	}


	/*
	 * Method to query the cryptoCurrency
	 * 
	 * @return cryptoCurrency as a String
	 */
	public String getCryptoCurrency() {
		return cryptoCurrency;
	}

	/*
	 * Method to set the cryptoCurrency
	 * 
	 * @param cryptoCurrency as a String
	 */
	public void setCryptoCurrency(String cryptoCurrency) {
		this.cryptoCurrency = cryptoCurrency;
	}
	
	/*
	 * Method to query the fiatCurrencey (for example CHF)
	 * 
	 * @return fiatCurrencey as a String
	 */
	public String getFiatCurrency() {
		return fiatCurrency;
	}
	
	/*
	 * Method to set the fiatCurrencey
	 * 
	 * @param fiatCurrencey as a String
	 */
	public void setFiatCurrency(String fiatCurrency) {
		this.fiatCurrency = fiatCurrency;
	}

	/*
	 * Method to query the number of coins
	 * 
	 * @return number of Coins as Double
	 */
	public Double getNumberOfCoins() {
		return numberOfCoins;
	}
	
	/*
	 * Method to set the number of coins
	 * 
	 * @param number of Coins as Double
	 */
	public void setNumberOfCoins(Double numberOfCoins) {
		this.numberOfCoins = numberOfCoins;
	}

	/*
	 * Method to query the price
	 * 
	 * @return price as Double
	 */
	public Double getPrice() {
		return price;
	}
	
	/*
	 * Method to set the price 
	 * 
	 * @param price as Double
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	/*
	 * Method to query the transaction date
	 * 
	 * @return date of the transaction as Date
	 */
	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	/*
	 * Method to set the transaction date
	 * 
	 * @param date of the transaction as Date
	 */
	public void setTransactionDate(LocalDate localDate) {
		this.transactionDate = localDate;
	}

	/*
	 * Method to query the type of transaction (Buy or Sell)
	 * 
	 * @return type of transaction as String
	 */
	public String getType() {
		return type;
	}

	/*
	 * Method to set the type of transaction (Buy or Sell)
	 * 
	 * @param type of transaction as String
	 */
	public void setType(String type) {
		this.type = type;
	}

	/*
	 * Method to query the fees
	 * 
	 * @return fees as Double
	 */
	public Double getFees() {
		return fees;
	}

	/*
	 * Method to set the fees 
	 * 
	 * @param fees as Double
	 */
	public void setFees(Double fees) {
		this.fees = fees;
	}

	/*
	 * Method to query the Portfolio
	 * 
	 * @return portfolio as Portfolio
	 */
	public Portfolio getPortfolio() {
		return portfolio;
	}

	/*
	 * Method to set the fees in CHF
	 * 
	 * @param portfolio as Portfolio
	 */
	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}
	
	/*
	 * Method to set the total
	 * 
	 * @param total as Double
	 */
	public void setTotal(Double total) {
		this.total = total;
	}
	
	/*
	 * Method to get the total
	 * 
	 */
	public Double getTotal() {
		return this.total;
	}
	
	/*
	 * Method overrides the toString-Method
	 * 
	 * @return id, cryptoCurrency and portfolio
	 */
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", cryptoCurrency=" + cryptoCurrency + ", portfolio=" + portfolio + "]";
	}
	
	

	
	
	
	
	
	
	
	
	
	
}
