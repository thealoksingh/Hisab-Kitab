package com.hisabKitab.springProject.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hisabKitab.springProject.dto.GetFriendListDto;
import com.hisabKitab.springProject.dto.SignUpUserDto;
import com.hisabKitab.springProject.dto.UsersFriendEntityDto;
import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.exception.EntityAlreadyExistException;
import com.hisabKitab.springProject.exception.UnAuthorizedException;
import com.hisabKitab.springProject.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserService {

	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BalanceService balanceService;

	@Autowired
	private FriendRequestCountService friendRequestCountService;

//	method for finding Authenticated user from token

	public UserEntity getUserFromToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = Long.parseLong(authentication.getName());
		return findUserById(userId);
	}

	// Method to log in user by checking email and password
	public UserEntity login(String email, String password) throws UnAuthorizedException {
		UserEntity user = getUserByEmail(email);
		System.out.println(user);
		if (passwordEncoder.matches(password, user.getPassword())) {
			return user;
		}

		throw new UnAuthorizedException("Wrong password entered");
	}

	// Method to sign up a new user
	public String signup(SignUpUserDto newUser) throws EntityAlreadyExistException {
		UserEntity existingUser = userRepository.findByEmail(newUser.getEmail());

		if (existingUser != null) {
			throw new EntityAlreadyExistException("User already exists with this email!");
			
		}

		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		userRepository.save(new UserEntity(newUser.getFullName(), newUser.getEmail(), newUser.getPassword(),
				newUser.getRole(), newUser.getContactNo(), getRandomColor()));
		return "User registered successfully!";
	}

	// Find if userEmail already exist
	public boolean userExistByEmail(String email) {
		System.out.println(userRepository.findByEmail(email));
		return userRepository.findByEmail(email) != null;

	}

	public String getRandomColor() {
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

		user.setPassword(passwordEncoder.encode(newPassword));
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
	public void removeFriend(UserEntity user, Long friendId) {
		
		UserEntity friend = userRepository.findById(friendId)
				.orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + friendId));

		if(!user.removeFriend(friend)){
			throw new EntityNotFoundException("Friend is not found in your list.."); // Remove friend from user's friend list
		} 
			
		userRepository.save(user); // Persist changes

		// Optional: If you want to remove the bidirectional friendship completely
		friend.removeFriend(user);
		userRepository.save(friend);
	}

	public List<UserEntity> getAllFriendList(Long userId) throws UnAuthorizedException {
		var user = userRepository.findById(userId).orElseThrow(()-> new UnAuthorizedException("Un Authorized request"));

		return user.getFriends();
	}

	public void deleteUserById(Long userId) {

		if (userRepository.existsById(userId)) {
			userRepository.deleteById(userId);
			return;
		}
		throw new EntityNotFoundException("User not exist");
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

		var friendRequestCount = friendRequestCountService.friendRequestCount(userId);
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

	public UserEntity getUserByEmail(String email) {
		return userRepository.getByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User not exist for this email : " + email));
	}

}
