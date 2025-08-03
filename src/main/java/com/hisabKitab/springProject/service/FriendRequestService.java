package com.hisabKitab.springProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisabKitab.springProject.dto.FriendRequestResponse;
import com.hisabKitab.springProject.dto.FriendRequestStatus;
import com.hisabKitab.springProject.dto.NotificationRequestDto;
import com.hisabKitab.springProject.entity.FriendRequestEntity;
import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.repository.FriendRequestRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FriendRequestService {

	@Autowired
	private FriendRequestRepository friendRequestRepository;

	@Autowired
	private EmailNotificationService emailNotificationService;

	@Autowired
	private UserService userService;

	@Autowired
private NotificationService notificationService;


	@Autowired
	private FriendRequestEventProducer eventProducer;

	public FriendRequestResponse sendRequest(UserEntity sender, UserEntity receiver) {

		if (sender.getUserId().equals(receiver.getUserId())) {
			return new FriendRequestResponse(FriendRequestStatus.SELF_REQUEST_NOT_ALLOWED, null);
		}

		for (UserEntity friend : sender.getFriends()) {
			if (friend.getUserId().equals(receiver.getUserId())) {
				return new FriendRequestResponse(FriendRequestStatus.ALREADY_FRIENDS, null);
			}
		}

		var existingRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver);
		if (existingRequest != null) {
			return new FriendRequestResponse(FriendRequestStatus.REQUEST_ALREADY_SENT, existingRequest);
		}

		FriendRequestEntity friendRequest = new FriendRequestEntity();
		friendRequest.setSender(sender);
		friendRequest.setReceiver(receiver);
		friendRequest.setStatus("PENDING");

		FriendRequestEntity savedRequest = friendRequestRepository.save(friendRequest);

		// Broadcast the friend request event
		eventProducer.sendFriendRequestEvent(savedRequest);

		// Send email notification
		String subjectText = sender.getFullName() + " has sent you a friend request.";
		String emailBodyMessage = String.format("""
				      		        Hi %s,

				      Great news! You received a friend request from %s.

				Accept the friend request on Hisab Kitab to build stronger connections and simplify expense management.
				Collaborate effortlessly, stay organized, and make managing and sharing expenses a breeze.

				      Log in now to start collaborating: https://hisab-kitab-business.netlify.app/

				      Cheers,
				      The Hisab Kitab Team
				      """,
				receiver.getFullName(), sender.getFullName());

		if (emailNotificationService.sendAndAcceptFriendRequestNotification(receiver.getEmail(), subjectText,
				emailBodyMessage)) {

			// Broadcast the friend request event
			NotificationRequestDto notificationDto = new NotificationRequestDto();
			notificationDto.setUserId(receiver.getUserId());
			notificationDto.setTitle("New friend request");
			notificationDto.setDescription(sender.getFullName() + " sent you a friend request");
			notificationService.save(notificationDto);

			return new FriendRequestResponse(FriendRequestStatus.REQUEST_SENT, savedRequest);
		}
		return new FriendRequestResponse(FriendRequestStatus.REQUEST_NOT_SENT, null);

	}

	public FriendRequestEntity acceptRequest(Long userId, Long requestId) {
		FriendRequestEntity request = friendRequestRepository.findByIdAndReceiver_UserId(requestId, userId)
				.orElseThrow(() -> new EntityNotFoundException("User's Request not found"));

		var sender = request.getSender();
		var receiver = request.getReceiver();

		String subjectText = receiver.getFullName() + " has accepted your friend request.";
		String emailBodyMessage = String.format("""
						        Hi %s,

				Great news! Your friend request to %s has been accepted.

				You are now connected on Hisab Kitab, making it even easier to manage and share your expenses together.

				Log in now to start collaborating: https://hisab-kitab-business.netlify.app/

				Cheers,
				The Hisab Kitab Team
				""",
				sender.getFullName(), receiver.getFullName());

		if (emailNotificationService.sendAndAcceptFriendRequestNotification(sender.getEmail(), subjectText,
				emailBodyMessage)) {
			request.setStatus("ACCEPTED");
			userService.addFriend(sender, receiver);

			deleteRequest(userId, requestId);

			// Notify the sender about the acceptance
			// Broadcast the friend request event
			eventProducer.sendFriendRequestEvent(request);


			// Broadcast the friend request event
			NotificationRequestDto notificationDto = new NotificationRequestDto();
			notificationDto.setUserId(sender.getUserId());
			notificationDto.setTitle("Friend request accepted");
			notificationDto.setDescription(receiver.getFullName() + " accepted your friend request");
			notificationService.save(notificationDto);

			return request;
		}

		return null;
	}

	public void unsendRequest(Long userId, Long requestId) {
		var request = friendRequestRepository.findByIdAndSender_UserId(requestId, userId)
				.orElseThrow(() -> new EntityNotFoundException("User's Request not exist"));
		if (request != null) {
			friendRequestRepository.deleteById(requestId);
		}
		System.out.println("friend request unsend succesffully");

		// Broadcast the friend request event
		if (request != null) {
			request.setStatus("UNSENT");
			eventProducer.sendFriendRequestEvent(request);
		}
	}

	public void deleteRequest(Long userId, Long requestId) {
		var request = friendRequestRepository.findByIdAndReceiver_UserId(requestId, userId)
				.orElseThrow(() -> new EntityNotFoundException("User's Request not exist"));
		if (request != null) {
			friendRequestRepository.deleteById(requestId);
		}
		System.out.println("friend request deleted succesfully");

		// Broadcast the friend request event
		if (request != null) {
			request.setStatus("REJECTED");
			eventProducer.sendFriendRequestEvent(request);

			// Broadcast the friend request event
			NotificationRequestDto notificationDto = new NotificationRequestDto();
			notificationDto.setUserId(request.getSender().getUserId());
			notificationDto.setTitle("Friend request rejected");
			notificationDto.setDescription(request.getSender().getFullName() + " rejected your friend request");
			notificationService.save(notificationDto);
		}
	}

	public List<FriendRequestEntity> getAllPendingRequests(Long receiverId) {
		var user = userService.findUserById(receiverId);
		return user != null ? user.getReceivedRequests() : null;
	}

	public List<FriendRequestEntity> getAllSentRequests(Long senderId) {
		var user = userService.findUserById(senderId);
		return user != null ? user.getSentRequests() : null;
	}
}
