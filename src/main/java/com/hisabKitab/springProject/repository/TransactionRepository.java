package com.hisabKitab.springProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hisabKitab.springProject.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	@Query(value = "SELECT * FROM transactions WHERE " + "(from_user_id = ?1 AND to_user_id = ?2) "
			+ "OR (from_user_id = ?2 AND to_user_id = ?1) " + "ORDER BY trans_date", nativeQuery = true)
	List<Transaction> findAllTransactionWithFriend(Long userId, Long friendId);

}
