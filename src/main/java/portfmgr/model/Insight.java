package portfmgr.model;

import org.springframework.beans.factory.annotation.Autowired;

public class Insight {

	@Autowired
	TransactionRepository transRepo;

	String cryptoCurrency;
	Double numberOfCoins;
	Double total;
	Double averagePrice;
	Double changePercent;
	Double changeFiat;
	Double spotPrice;

	public String getCryptoCurrency() {
		return cryptoCurrency;
	}

	public void setCryptoCurrency(String cryptoCurrency) {
		this.cryptoCurrency = cryptoCurrency;
	}

	public Double getNumberOfCoins() {
		return numberOfCoins;
	}

	public void setNumberOfCoins(Double numberOfCoins) {
		this.numberOfCoins = numberOfCoins;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(Double averagePrice) {
		this.averagePrice = averagePrice;
	}

	public Double getChangePercent() {
		return changePercent;
	}

	public void setChangePercent(Double changePercent) {
		this.changePercent = changePercent;
	}

	public Double getChangeFiat() {
		return changeFiat;
	}

	public void setChangeFiat(Double changeFiat) {
		this.changeFiat = changeFiat;
	}

	public Double getSpotPrice() {
		return spotPrice;
	}

	public void setSpotPrice(Double spotPrice) {
		this.spotPrice = spotPrice;
	}

}
