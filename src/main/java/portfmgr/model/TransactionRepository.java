package portfmgr.model;

import java.util.List;

import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * documentation:
 * https://docs.spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html/repositories.html
 *
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
	 * @return sum of bumber of coins
	 * @author Marc Steiner
	 */
	@Query("SELECT SUM(total) FROM Transaction WHERE cryptoCurrency = :symbol")
	Double sumCryptoCurrencyTotal(@Param("symbol") String symbol);
	
	/**
	 * SQL query which search all unique crypto currency symbols are available
	 * 
	 * @return distinct list of available crypto currency symbols
	 * @author Marc Steiner
	 */
	@Query("SELECT DISTINCT cryptoCurrency FROM Transaction")
	List<String> findDistinctCryptoCurrency();
	
	/**
	 * SQL query which groups all transactions by crypto currency and sum the total number of this crypto currencycost.
	 *  
	 * @param numberOfCoins (=total amount of coins per transaction referred to Transaction.java)
	 * @param currency (=crypto currency referred to Transaction.java)
	 * @return SQL query return list of objects <Object[]>
	 * @author Marc Steiner
	 */
	@Query("SELECT sum(numberOfCoins) as nbr, cryptoCurrency FROM Transaction GROUP BY cryptoCurrency")
	List<Object[]> sumAndGroupTotalNumberOfCoins();
	


}
