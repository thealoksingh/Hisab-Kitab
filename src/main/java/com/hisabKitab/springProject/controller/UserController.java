package com.hisabKitab.springProject.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hisabKitab.springProject.dto.GetFriendListDto;
import com.hisabKitab.springProject.dto.SignUpUserDto;
import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.security.JwtTokenService;
import com.hisabKitab.springProject.service.EmailNotificationService;
import com.hisabKitab.springProject.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailNotificationService emailNotificationService;

	@Autowired
	private JwtTokenService jwtTokenService; // Inject JwtTokenService

	// Login endpoint
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestParam String email, @RequestParam String password) {

		System.out.println("login api called");
		UserEntity user = userService.login(email, password);

//        Map<String, String> response = new HashMap<>();

		if (user != null) {
			System.out.println(user.toString());

			// Manually create an Authentication object for token generation
			Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null,
					Collections.emptyList());

			// Generate JWT Token
			String token = jwtTokenService.generateToken(authentication);
			System.out.println(token);
			// Construct response
			Map<String, Object> response = new HashMap<>();
			response.put("status", HttpStatus.OK.value());
			response.put("message", "Login successful");
			response.put("token", token);
			response.put("user", user);

			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Collections.singletonMap("message", "Invalid email or password"));
		}
	}

	// Signup endpoint
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody SignUpUserDto newUser) {
		String result = userService.signup(newUser);
		if (result.equals("User registered successfully!")) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.status(400).body(result); // If user already exists
		}
	}

	@GetMapping("/addfriend/{userId}")
	public ResponseEntity<String> addFriend(@PathVariable("userId") Long userId,
			@RequestParam("contactNo") String contactNo) {

		var friend = userService.checkUserExistByContactNumber(userId, contactNo);

		if (friend != null) {
			return ResponseEntity.ok("Friend Added Successfully");
		}
		return ResponseEntity.status(400).body("User not existed with the contact no = " + contactNo); // If user not
																										// exists
	}

	@PostMapping("/sendInvite")
	public ResponseEntity<String> sendInviteEmail(@RequestParam("email") String recipientEmail,
			@RequestParam("senderName") String senderName) {
		var isUserExist = userService.userExistByEmail(recipientEmail);

		if (!isUserExist) {
			if (emailNotificationService.sendInviteNotification(recipientEmail, senderName)) {
				return ResponseEntity.ok("Invite Sent Successfully");
			}
			return ResponseEntity.badRequest().body("Invite failed");
		}
		return ResponseEntity.badRequest().body("User already exist with the given email.");
	}

	@PostMapping("/sendOTP")
	public ResponseEntity<String> sendOTPMail(@RequestParam("email") String recipientEmail,
			@RequestParam("type") String type) {
		boolean isUserExist = userService.userExistByEmail(recipientEmail);

		if ((type.equals("forget-password") && isUserExist) || (type.equals("sign-up") && !isUserExist)) {

			var otp = emailNotificationService.sendOtpNotification(recipientEmail);

			if (otp != null) {
				return ResponseEntity.ok(otp);
			}
		} else if ((type.equals("forget-password") && !isUserExist)) {

			return ResponseEntity.badRequest().body("User does not exist with the given email");
		} else if ((type.equals("sign-up") && isUserExist)) {

			return ResponseEntity.badRequest().body("User already exist with given email");
		}
		return ResponseEntity.badRequest().body("OTP does not sent due to error");

	}

	@DeleteMapping("/{userId}/friends/{friendId}")
	public ResponseEntity<String> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
		userService.removeFriend(userId, friendId);
		return ResponseEntity.ok("Friend removed successfully.");
	}

	@PostMapping("/update-password")
	public ResponseEntity<String> updatePassword(@RequestParam("email") String email,
			@RequestParam("newPassword") String newPassword) {
		try {
			var response = userService.updatePassword(email, newPassword);
			return ResponseEntity.ok(response);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the password.");
		}
	}

	@GetMapping("/getAllFriendList/{userId}")
	public ResponseEntity<GetFriendListDto> getAllFriends(@PathVariable("userId") Long userId) {
		var friendList = userService.getAllFriendList(userId);

		var gfl = userService.getAllFriendListWithDetails(userId, friendList);

//    	GetFriendListDto gfl = new GetFriendListDto();

		if (friendList == null) {
			gfl.setMessage("User not Existed by Id = " + userId);
			return ResponseEntity.status(400).body(gfl);
		} else if (friendList.isEmpty()) {
			gfl.setMessage("No friends are there in the List");
//    		gfl.setFriendList(friendList);
			return ResponseEntity.status(400).body(gfl);
		}
		gfl.setMessage("Friend List founded");
//    	gfl.setFriendList(friendList);
		return ResponseEntity.ok(gfl);
	}

}
