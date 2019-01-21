package portfmgr.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * documentation:
 * https://docs.spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html/repositories.html
 * https://www.w3schools.com/sql/
 *@author: Pascal Rohners und Marc Steiner
 */
@Repository("portfmgr.model.TransactionRepository")
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

	List<Transaction> findByCryptoCurrency(String cryptoCurrency);

	List<Transaction> findByPortfolio(Portfolio portfolio);
	

	/**
	 * SQL query which sum up the number of coins for one specific crypto currency
	 * 
	 * @param symbol (crypto currency)
	 * @return sum of number of coins
	 * @author Marc Steiner
	 */
	@Query("SELECT SUM(total) FROM Transaction WHERE cryptoCurrency = :symbol")
	Double sumCryptoCurrencyTotal(@Param("symbol") String symbol);
	
	/**
	 * SQL query which search all unique crypto currency symbols are available
	 * 
	 * @param id (portfolio id needed for separate portfolio data)
	 * @return distinct list of available crypto currency symbols
	 * @author Marc Steiner
	 */
	@Query("SELECT DISTINCT cryptoCurrency FROM Transaction WHERE portfolio_id = :id")
	List<String> findDistinctCryptoCurrency(@Param("id") Long id);
	
	/**
	 * SQL query which groups all transactions by crypto currency and sum the total number of this crypto currency.
	 *  
	 * @param numberOfCoins (=total amount of coins per transaction referred to Transaction.java)
	 * @param currency (=crypto currency referred to Transaction.java)
	 * @param id (portfolio id needed for separate portfolio data)
	 * @return SQL query return list of objects <Object[]>
	 * @author Marc Steiner
	 */
	@Query("SELECT sum(numberOfCoins) as nbr, cryptoCurrency FROM Transaction WHERE portfolio_id = :id GROUP BY cryptoCurrency")
	List<Object[]> sumAndGroupTotalNumberOfCoins(@Param("id") Long id);
		
	/**
	 * SQL query which groups all transactions by crypto currency and sum the total number of  cost for this crypto currency.
	 *  
	 * @param total (=total amount spent per crypo currency referred to Transaction.java)
	 * @param currency (=crypto currency referred to Transaction.java)
	 * @param id (portfolio id needed for separate portfolio data)
	 * @return SQL query return list of objects <Object[]>
	 * @author Marc Steiner
	 */
	@Query("SELECT sum(total) as nbr, cryptoCurrency FROM Transaction WHERE portfolio_id = :id GROUP BY cryptoCurrency")
	List<Object[]> sumAndGroupTotalSpent(@Param("id") Long id);
	
	
	/**
	 * SQL query which sum the total of all transactions in the specific portfolio.
	 *  
	 * @param total (=total amount spent per crypo currency referred to Transaction.java)
	 * @param id (portfolio id needed for separate portfolio data)
	 * @return SQL query return Double value
	 * @author Marc Steiner
	 */
	@Query("SELECT sum(total)FROM Transaction WHERE portfolio_id = :id")
	Double sumTotalSpent(@Param("id") Long id);
	
	/**
	 * SQL query to delete all transactions from a specific portfolio.
	 *  
	 * @param id (portfolio id needed for separate portfolio data)
	 * @return SQL query return Double value
	 * @author Marc Steiner
	 */
	@Transactional
	@Modifying
	@Query("DELETE FROM Transaction WHERE portfolio_id = :id")
	void deleteAllTransactions(@Param("id") Long id);
	
	/**
	 * SQL query which sums up the number of coins for a specific cryptoCurrency in a specific portfolio
	 *  
	 * @param currency (=crypto currency referred to Transaction.java)
	 * @param id (portfolio id needed for separate portfolio data)
	 * @return SQL query returns a Double
	 * @author Pascal Rohner
	 */
	@Query("SELECT sum(numberOfCoins) as nbr FROM Transaction WHERE portfolio_id = :id AND cryptoCurrency = :cryptoCurrency")
	Double sumUpNumberOfCoinsForCryptoCurrency(@Param("id") Long id, @Param("cryptoCurrency") String cryptoCurrency);
	
	/**
	 * SQL query which sums up the number of coins for a specific cryptoCurrency in a specific portfolio
	 *  
	 * @param currency (=crypto currency referred to Transaction.java)
	 * @param id (portfolio id needed for separate portfolio data)
	 * @return SQL query returns a Double
	 * @author Pascal Rohner
	 */
	@Query("SELECT sum(numberOfCoins) as nbr FROM Transaction WHERE type = 'Kauf' AND portfolio_id = :id AND cryptoCurrency = :cryptoCurrency")
	Double sumUpNumberOfCoinsForCryptoCurrencyTypeKauf(@Param("id") Long id, @Param("cryptoCurrency") String cryptoCurrency);
	
	/**
	 * SQL query which sums up the total amount for a specific cryptoCurrency in a specific portfolio
	 *  
	 * @param id (portfolio id needed for separate portfolio data)
	 * @param currency (=crypto currency referred to Transaction.java)
	 * @return SQL query return a Double
	 * @author Pascal Rohner
	 */
	@Query("SELECT sum(total) FROM Transaction WHERE type = 'Kauf' AND portfolio_id = :id AND cryptoCurrency = :cryptoCurrency")
	Double sumUpTotalForCryptoCurrency(@Param("id") Long id, @Param("cryptoCurrency") String cryptoCurrency);

}
