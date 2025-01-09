package com.hisabKitab.springProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hisabKitab.springProject.entity.Transaction;
import com.hisabKitab.springProject.entity.TransactionComment;

public interface TransactionCommentsRepository extends JpaRepository<TransactionComment, Long>  {

	boolean existsByTransaction(Transaction transaction);

	List<TransactionComment> findByTransaction_TransId(Long transactionId);
	
	

}
