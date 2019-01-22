package portfmgr.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Entity class for persistence of insights in an embedded database
 *
 * @author: Pascal Rohner
 */

public class Insight {

	@Autowired
	TransactionRepository transRepo;
	
	/*
	 * Id serves as unique key and is automatically generated when
	 * adding a insight to the database.
	 */
	@Id
	@Column(unique = true)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String cryptoCurrency;
	private Double numberOfCoins;
	private Double total;
	private Double averagePrice;
	private Double changePercent;
	private Double changeFiat;
	private Double spotPrice;

	/*
	 * Method to query the primary key
	 * 
	 * @return Auto-generated primary key as a Long
	 */
	public Long getId() {
		return this.id;
		
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
	 * Method to query the number of coins
	 * 
	 * @return number of coins as a Double
	 */
	public Double getNumberOfCoins() {
		return numberOfCoins;
	}

	/*
	 * Method to set the number of coins
	 * 
	 * @param number of coins as a Double
	 */
	public void setNumberOfCoins(Double numberOfCoins) {
		this.numberOfCoins = numberOfCoins;
	}

	/*
	 * Method to query the total
	 * 
	 * @return number total as a Double
	 */
	public Double getTotal() {
		return total;
	}

	/*
	 * Method to set the total
	 * 
	 * @param total as a Double
	 */
	public void setTotal(Double total) {
		this.total = total;
	}

	/*
	 * Method to query the avarage price
	 * 
	 * @return avarage price as a Double
	 */
	public Double getAveragePrice() {
		return averagePrice;
	}
	
	/*
	 * Method to set the avarage price
	 * 
	 * @param avarage price as a Double
	 */
	public void setAveragePrice(Double averagePrice) {
		this.averagePrice = averagePrice;
	}
	
	/*
	 * Method to query the change in percent
	 * 
	 * @return the change in percent as a Double
	 */
	public Double getChangePercent() {
		return changePercent;
	}
	
	/*
	 * Method to set the change in percent
	 * 
	 * @param the change in percent as a Double
	 */
	public void setChangePercent(Double changePercent) {
		this.changePercent = changePercent;
	}
	
	/*
	 * Method to query the change in fiat
	 * 
	 * @return the change in fiat as a Double
	 */
	public Double getChangeFiat() {
		return changeFiat;
	}

	/*
	 * Method to set the change in fiat
	 * 
	 * @param the change in fiat as a Double
	 */
	public void setChangeFiat(Double changeFiat) {
		this.changeFiat = changeFiat;
	}
	
	/*
	 * Method to get the spot price 
	 * 
	 * @return spot price as a Double
	 */
	public Double getSpotPrice() {
		return spotPrice;
	}

	/*
	 * Method to set the spot price 
	 * 
	 * @param spot price as a Double
	 */
	public void setSpotPrice(Double spotPrice) {
		this.spotPrice = spotPrice;
	}

}
