package portfmgr.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * documentation:
 * https://docs.spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html/repositories.html
 *
 *@author: Pascal Rohner
 */
@Repository("portfmgr.model.PortfolioRepository")
public interface PortfolioRepository extends CrudRepository<Portfolio, Long> {
	
	List<Portfolio> findByPortfolioFiatCurrency(String portfolioFiatCurrency);
}
