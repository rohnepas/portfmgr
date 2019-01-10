package portfmgr.model;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	private String currency;
	private String moneytary;
	private Double numberOfCoins;
	private Double priceUSD;
	private Double priceBTC;
	private Double priceEUR;
	private Double priceCHF;
	private LocalDate transactionDate;
	private String type;
	private Double feesUSD;
	private Double feesBTC;
	private Double feesEUR;
	private Double feesCHF;
	private Double total;
	
	@ManyToOne
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
	
	public Transaction(String currency, String moneytary, Double numberOfCoins, Double priceCHF, LocalDate transactionDate, String type,
			Double feesCHF, Double total) {
		super();
		this.currency = currency;
		this.moneytary = moneytary;
		this.numberOfCoins = numberOfCoins;
		this.priceCHF = priceCHF;
		this.transactionDate = transactionDate;
		this.type = type;
		this.feesCHF = feesCHF;
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
	 * Method to query the currency
	 * 
	 * @return currency as a String
	 */
	public String getCurrency() {
		return currency;
	}

	/*
	 * Method to set the currency
	 * 
	 * @param currency as a String
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	/*
	 * Method to query the moneytary (= Währung)
	 * 
	 * @return moneytary as a String
	 */
	public String getMoneytary() {
		return moneytary;
	}
	
	/*
	 * Method to set the moneytary
	 * 
	 * @param moneytary as a String
	 */
	public void setMoneytary(String moneytary) {
		this.moneytary = moneytary;
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
	 * Method to query the price in USD
	 * 
	 * @return price in USD as Double
	 */
	public Double getPriceUSD() {
		return priceUSD;
	}
	
	/*
	 * Method to set the price in USD
	 * 
	 * @param price in USD as Double
	 */
	public void setPriceUSD(Double priceUSD) {
		this.priceUSD = priceUSD;
	}

	/*
	 * Method to query the price in BTC
	 * 
	 * @return price in BTC as Double
	 */
	public Double getPriceBTC() {
		return priceBTC;
	}

	/*
	 * Method to set the price in BTC
	 * 
	 * @param price in BTC as Double
	 */
	public void setPriceBTC(Double priceBTC) {
		this.priceBTC = priceBTC;
	}

	/*
	 * Method to query the price in EUR
	 * 
	 * @return price in EUR as Double
	 */
	public Double getPriceEUR() {
		return priceEUR;
	}

	/*
	 * Method to set the price in EUR
	 * 
	 * @param price in EUR as Double
	 */
	public void setPriceEUR(Double priceEUR) {
		this.priceEUR = priceEUR;
	}
	
	/*
	 * Method to query the price in CHF
	 * 
	 * @return price in CHF as Double
	 */
	public Double getPriceCHF() {
		return priceCHF;
	}

	/*
	 * Method to set the price in CHF
	 * 
	 * @param price in CHF as Double
	 */
	public void setPriceCHF(Double priceCHF) {
		this.priceCHF = priceCHF;
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
	 * Method to query the fees in USD
	 * 
	 * @return fees in USD as Double
	 */
	public Double getFeesUSD() {
		return feesUSD;
	}

	/*
	 * Method to set the fees in USD
	 * 
	 * @param fees in USD as Double
	 */
	public void setFeesUSD(Double feesUSD) {
		this.feesUSD = feesUSD;
	}

	/*
	 * Method to query the fees in BTC
	 * 
	 * @return fees in BTC as Double
	 */
	public Double getFeesBTC() {
		return feesBTC;
	}

	/*
	 * Method to set the fees in BTC
	 * 
	 * @param fees in BTC as Double
	 */
	public void setFeesBTC(Double feesBTC) {
		this.feesBTC = feesBTC;
	}

	/*
	 * Method to query the fees in EUR
	 * 
	 * @return fees in EUR as Double
	 */
	public Double getFeesEUR() {
		return feesEUR;
	}

	/*
	 * Method to set the fees in EUR
	 * 
	 * @param fees in EUR as Double
	 */
	public void setFeesEUR(Double feesEUR) {
		this.feesEUR = feesEUR;
	}

	/*
	 * Method to query the fees in CHF
	 * 
	 * @return fees in CHF as Double
	 */
	public Double getFeesCHF() {
		return feesCHF;
	}

	/*
	 * Method to set the fees in CHF
	 * 
	 * @param fees in CHF as Double
	 */
	public void setFeesCHF(Double feesCHF) {
		this.feesCHF = feesCHF;
	}
	
	/*
	 * Method to query the total in CHF
	 * 
	 * @return fees in CHF as Double
	 */
	public Double getTotal() {
		return total;
	}
	
	/*
	 * Method to set the fees in CHF
	 * 
	 * @param fees in CHF as Double
	 */
	public void setTotal(Double total) {
		this.total = total;
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
	 * Method overrides the toString-Method
	 * 
	 * @return id, currency and portfolio
	 */
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", currency=" + currency + ", portfolio=" + portfolio + "]";
	}
	
	

	
	
	
	
	
	
	
	
	
	
}
