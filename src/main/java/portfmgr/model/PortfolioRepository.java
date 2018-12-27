package portfmgr.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


/*
 * documentation:
 * https://docs.spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html/repositories.html
 *
 *@author: Pascal Rohner
 */
public interface PortfolioRepository extends CrudRepository<Portfolio, Long> {
	
	List<Portfolio> findByPortfolioCurrency(String portfolioCurrency);
	//Portfolio findByPortfolioId(Long Id);
}
