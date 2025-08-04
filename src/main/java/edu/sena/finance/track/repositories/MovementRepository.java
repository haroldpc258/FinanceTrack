package edu.sena.finance.track.repositories;

import edu.sena.finance.track.entities.Movement;
import edu.sena.finance.track.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    @Query("SELECT COUNT(m) FROM Movement m WHERE m.createdOn >= :date")
    long countByCreatedOnAfter(@Param("date") LocalDateTime oneWeekAgo);

    @Query("SELECT COUNT(m) FROM Movement m WHERE m.createdOn >= :start AND m.createdOn < :end")
    long countByCreatedOnBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT m FROM Movement m ORDER BY m.createdOn DESC LIMIT 5")
    List<Movement> findTop5ByOrderByCreatedOnDesc();

    @Query("SELECT COUNT(m) FROM  Movement m WHERE m.createdBy.company.id = :companyId AND m.createdOn >= :startDate")
    long countByCompanyIdAndCreatedOnAfter(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDateTime startDate
    );

    @Query("SELECT COALESCE(SUM(m.amount), 0) FROM Movement m WHERE m.type = :type AND m.createdBy.company.id = :companyId AND m.createdOn >= :startDate")
    double sumAmountByTypeAndCompanyIdAndCreatedOnAfter(
            @Param("type") Movement.Type type,
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDateTime startDate);

    @Query("SELECT COALESCE(SUM(m.amount), 0) FROM Movement m WHERE m.type = :type AND m.createdBy.company.id = :companyId AND m.createdOn >= :startDate AND m.createdOn <= :endDate")
    double sumAmountByTypeAndCompanyIdAndCreatedOnBetween(
            @Param("type") Movement.Type type,
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT m FROM Movement m WHERE m.createdBy.company.id = :companyId AND m.createdOn >= :startDate ORDER BY m.createdOn DESC")
    List<Movement> findByCompanyIdAndCreatedOnAfterOrderByCreatedOnDesc(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDateTime startDate);

    void deleteMovementsByCreatedBy_Id(Long userId);
    List<Movement> findMovementsByCreatedBy_Id(Long userId);

    /*void deleteAllByCreatedBy(User user);*/
}
