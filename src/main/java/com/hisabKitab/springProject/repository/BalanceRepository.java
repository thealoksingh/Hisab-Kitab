package com.hisabKitab.springProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hisabKitab.springProject.entity.Balance;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Balance findByUserIdAndFriendId(Long userId, Long friendId);

	Balance findByFriendIdAndUserId(Long fromUserId, Long toUserId);

	Boolean existsByUserIdAndFriendId(Long userId, Long friendId);
}
