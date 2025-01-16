package com.hisabKitab.springProject.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hisabKitab.springProject.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	@Query(value = "SELECT * FROM transactions WHERE " + "(from_user_id = ?1 AND to_user_id = ?2) "
			+ "OR (from_user_id = ?2 AND to_user_id = ?1) " + "ORDER BY trans_date", nativeQuery = true)
	List<Transaction> findAllTransactionWithFriend(Long userId, Long friendId);
	
	// Custom query to find transactions by userId, friendId, and date range
	 @Query("SELECT t FROM Transaction t WHERE " +
	           "(t.fromUserId = :userId AND t.toUserId = :friendId OR " +
	           "t.fromUserId = :friendId AND t.toUserId = :userId) " +
	           "AND t.transDate BETWEEN :fromDate AND :toDate " +
	           "ORDER BY t.transDate ASC")
	    List<Transaction> findTransactionsBetweenUsersAndDateRange(
	            @Param("userId") Long userId,
	            @Param("friendId") Long friendId,
	            @Param("fromDate") LocalDate fromDate,
	            @Param("toDate") LocalDate toDate);
	
    @Query(value = "SELECT * FROM transactions WHERE "
            + "(from_user_id = ?1 AND to_user_id = ?2) "
            + "OR (from_user_id = ?2 AND to_user_id = ?1) "
            + "AND trans_date < ?3 ORDER BY trans_date", nativeQuery = true)
    List<Transaction> findAllTransactionsBeforeDate(Long userId, Long friendId, LocalDate beforeDate);



}
