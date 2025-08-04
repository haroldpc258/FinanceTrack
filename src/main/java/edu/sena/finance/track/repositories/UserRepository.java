package edu.sena.finance.track.repositories;

import edu.sena.finance.track.entities.User;
import edu.sena.finance.track.entities.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(@Param("role") User.Role role);

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdOn >= :date")
    long countByCreatedOnAfter(@Param("date") LocalDateTime date);

    @Query("SELECT COUNT(u) FROM User u WHERE u.company.id = :companyId AND u.status = :status")
    long countByCompanyIdAndStatus(@Param("companyId") Long companyId, @Param("status") Status status);

    @Query("SELECT COUNT(u) FROM User u WHERE u.company.id = :companyId AND u.status = 'ACTIVE' AND u.createdOn < :date")
    long countActiveEmployeesBeforeDate(@Param("companyId") Long companyId, @Param("date") LocalDateTime date);
}
