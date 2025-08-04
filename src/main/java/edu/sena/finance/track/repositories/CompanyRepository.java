package edu.sena.finance.track.repositories;

import edu.sena.finance.track.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT COUNT(c) FROM Company c WHERE c.createdOn >= :date")
    long countByCreatedOnAfter(@Param("date") LocalDateTime date);

    @Query("SELECT c FROM Company c ORDER BY c.createdOn DESC LIMIT 5")
    List<Company> findTop5ByOrderByCreatedOnDesc();
}
