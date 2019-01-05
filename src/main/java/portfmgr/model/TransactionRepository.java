package portfmgr.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("portfmgr.model.TransactionRepository")
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	
	List<Transaction> findByCurrency(String currency);
	


}
