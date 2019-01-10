package portfmgr.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * documentation:
 * https://docs.spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html/repositories.html
 *
 *@author: Pascal Rohners
 */
@Repository("portfmgr.model.TransactionRepository")
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	
	List<Transaction> findByCurrency(String currency);
	
	List<Transaction> findByPortfolio(Portfolio portfolio);
	


}
