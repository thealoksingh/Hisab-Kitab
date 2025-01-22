package com.hisabKitab.springProject.service;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisabKitab.springProject.dto.GetFriendListDto;
import com.hisabKitab.springProject.dto.SignUpUserDto;
import com.hisabKitab.springProject.dto.UsersFriendEntityDto;
import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.lang.String;
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BalanceService balanceService;
	
	@Autowired
	private FriendRequestService friendRequestService;

	// Method to log in user by checking email and password
	public UserEntity login(String email, String password) {
		UserEntity user = userRepository.findByEmail(email);

		if (user != null) {
			if (user.getPassword().equals(password)) {
				return user;
			}
		}
		return null;
	}

	// Method to sign up a new user
	public String signup(SignUpUserDto newUser) {
		UserEntity existingUser = userRepository.findByEmail(newUser.getEmail());

		if (existingUser != null) {

			return "User already exists with this email!";
		}

		userRepository.save(new UserEntity(newUser.getFullName(), newUser.getEmail(), newUser.getPassword(),
				newUser.getRole(), newUser.getContactNo(), getRandomColor()));
		return "User registered successfully!";
	}
	
	 public  String getRandomColor() {
	        Random random = new Random();
	        StringBuilder color = new StringBuilder("#");
	        
	        for (int i = 0; i < 6; i++) {
	            // Restrict to darker shades by using lower hex values (0-8)
	            int value = random.nextInt(8); // Generate a random number between 0 and 7
	            color.append(Integer.toHexString(value));
	        }
	        
	        return color.toString();
	    }

	public UserEntity checkUserExistByContactNumber(Long userId, String contactNo) {
		UserEntity friend = userRepository.findByContactNo(contactNo);

		if (friend != null) {
			var user = userRepository.findById(userId).get();
			user.getFriends().add(friend);
			friend.getFriends().add(user);
			userRepository.save(user);
			userRepository.save(friend);
			return friend;

		}

		return friend;

	}
	@Transactional
	public String updatePassword(String email, String newPassword) {
	    UserEntity user = userRepository.findByEmail(email);

	    if (user == null) {
	        throw new EntityNotFoundException("User with email " + email + " does not exist");
	    }

	    user.setPassword(newPassword);
	    userRepository.save(user);

	    return "Password updated successfully!";
	}

	public UserEntity addFriend(UserEntity user, UserEntity friend) {
		
		user.getFriends().add(friend);
		friend.getFriends().add(user);
		userRepository.save(user);
		userRepository.save(friend);
		return friend;

	}

	@Transactional
	public void removeFriend(Long userId, Long friendId) {
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
		UserEntity friend = userRepository.findById(friendId)
				.orElseThrow(() -> new RuntimeException("Friend not found with ID: " + friendId));

		user.removeFriend(friend); // Remove friend from user's friend list
		userRepository.save(user); // Persist changes

		// Optional: If you want to remove the bidirectional friendship completely
		friend.removeFriend(user);
		userRepository.save(friend);
	}

	public List<UserEntity> getAllFriendList(Long userId) {
		var user = userRepository.findById(userId).orElse(null);

//		return (user != null) ? new ArrayList<>(user.getFriends()): null;
		return (user != null) ? user.getFriends() : null;
	}

	public void deleteUserById(Long userId) {

		if (userRepository.existsById(userId)) {
			userRepository.deleteById(userId);
		}
	}

	public List<UserEntity> getAllUser() {
		return userRepository.findAll();
	}

	public GetFriendListDto getAllFriendListWithDetails(Long userId, List<UserEntity> friendList) {
		List<UsersFriendEntityDto> userFriendEntityList = new ArrayList<>();

		for (UserEntity f : friendList) {
			var friendClosingBalance = balanceService.getNetBalanceDetail(userId, f.getUserId());
			var lastTransactionDate = balanceService.getLastTransactionDateDetail(userId, f.getUserId());
			userFriendEntityList.add(new UsersFriendEntityDto(f, friendClosingBalance, lastTransactionDate));
		}

		userFriendEntityList.sort(Comparator.comparing(UsersFriendEntityDto::getLastTransactionDate,
				Comparator.nullsLast(Comparator.reverseOrder())));

//		userFriendEntityList.sort((a, b) -> {
//            if (a.getLastTransactionDate() == null) return 1; // null dates go to the end
//            if (b.getLastTransactionDate() == null) return -1; // null dates go to the end
//            return b.getLastTransactionDate().compareTo(a.getLastTransactionDate()); // Descending order
//        });

		System.out.println(userFriendEntityList);

		var friendRequestCount = friendRequestService.getAllPendingRequests(userId).size() + friendRequestService.getAllSentRequests(userId).size();
		return new GetFriendListDto("Friend List founded", userFriendEntityList, friendRequestCount);
	}

	public UserEntity getUserById(Long friendId) {
		var friend = userRepository.findById(friendId);
		if (friend.isPresent()) {
			return friend.get();
		}
		return null;
		
	}

	public UserEntity findUserById(Long senderId) {
		return userRepository.findById(senderId)
				.orElseThrow(() -> new EntityNotFoundException("User with ID " + senderId + " not found"));
	}

	public UserEntity findUserByContactNo(String recieverContactNo) {
		
		return userRepository.findByContactNo(recieverContactNo);
	}

}
