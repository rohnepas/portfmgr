package portfmgr.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Entity class for persistence of portfolio information in an embedded database
 *
 * @author: Pascal Rohner
 */

@Entity
public class Portfolio {

	/*
	 * Id serves as unique key, it is unique and is automatically generated when
	 * adding a portfolio to the database.
	 */
	@Id
	@Column(unique = true)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String portfolioName;
	private String portfolioFiatCurrency;
	private String profitOrLoss = "-";
	private String totalPortfolioValue = "-";
	

	@OneToMany(mappedBy = "portfolio")
	private List<Transaction> transactions;
	/*
	 * Method to query the primary key
	 * 
	 * @return Auto-generated primary key as a String
	 */
	public Long getId() {
		return id;
	}

	/*
	 * Method to query the portfolio name
	 * 
	 * @return name of the portfolio as a String
	 */
	public String getPortfolioName() {
		return portfolioName;
	}

	/*
	 * Method to set the portfolio name
	 * 
	 * @param name of the portfolio as a String
	 */
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	/*
	 * Method to query the portfolio fiat currency (for exmaple CHF)
	 * 
	 * @return curreny of the portfolio as a String
	 */
	public String getPortfolioFiatCurrency() {
		return portfolioFiatCurrency;
	}

	/*
	 * Method to set the portfolio currency
	 * 
	 * @param currency of the portfolio as a String
	 */
	public void setPortfolioFiatCurrency(String portfolioFiatCurrency) {
		this.portfolioFiatCurrency = portfolioFiatCurrency;
	}

	/*
	 * Method overrides the toString-Method
	 * 
	 * @return id, name and currency of a portfolio
	 */
	@Override
	public String toString() {
		return "Portfolio [id=" + id + ", portfolioName=" + portfolioName + ", portfolioFiatCurrency=" + portfolioFiatCurrency
				+ "]";
	}

	public String getProfitOrLoss() {
		return profitOrLoss;
	}

	public void setProfitOrLoss(String profitOrLoss) {
		this.profitOrLoss = profitOrLoss;
	}

	public String getTotalPortfolioValue() {
		return totalPortfolioValue;
	}

	public void setTotalPortfolioValue(String totalPortfolioValue) {
		this.totalPortfolioValue = totalPortfolioValue;
	}

}
