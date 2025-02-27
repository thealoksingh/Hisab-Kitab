package com.hisabKitab.springProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hisabKitab.springProject.entity.FriendRequestEntity;
import com.hisabKitab.springProject.entity.UserEntity;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequestEntity, Long> {

    List<FriendRequestEntity> findByReceiverAndStatus(UserEntity receiver, String status);

    List<FriendRequestEntity> findBySenderAndStatus(UserEntity sender, String status);

	FriendRequestEntity findBySenderAndReceiver(UserEntity sender, UserEntity receiver);

	Optional<FriendRequestEntity> findByIdAndReceiver_UserId(Long requestId, Long userId);

	Optional<FriendRequestEntity> findByIdAndSender_UserId(Long requestId, Long userId);
}
