package com.hisabKitab.springProject.controller;

import java.util.Collections;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hisabKitab.springProject.dto.CommonResponseDto;
import com.hisabKitab.springProject.dto.GetFriendListDto;
import com.hisabKitab.springProject.dto.LoginRequestDto;
import com.hisabKitab.springProject.dto.LoginResponseDto;
import com.hisabKitab.springProject.dto.SignUpUserDto;
import com.hisabKitab.springProject.dto.UpdatePasswordRequestDto;
import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.exception.EntityAlreadyExistException;
import com.hisabKitab.springProject.exception.UnAuthorizedException;
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
	public ResponseEntity<CommonResponseDto<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto)
			throws UnAuthorizedException {

		System.out.println("login api called");
		UserEntity user = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

//        Map<String, String> response = new HashMap<>();

		System.out.println(user.toString());

		// Manually create an Authentication object for token generation
		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null,
				Collections.emptyList());

		// Generate JWT Token
		String token = jwtTokenService.generateToken(authentication);
		System.out.println(token);
		// Construct response

		var response = new CommonResponseDto<>(HttpStatus.OK, "Login Successfull",
				new LoginResponseDto(user.getFullName(), user.getContactNo(), token));

		return ResponseEntity.ok(response);

	}

	// Signup endpoint
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody SignUpUserDto newUser) throws EntityAlreadyExistException {
		String result = userService.signup(newUser);

		return ResponseEntity.ok(result);
	}

	/*
	 * @GetMapping("/addfriend") public ResponseEntity<String>
	 * addFriend(@RequestParam("contactNo") String contactNo) { UserEntity user =
	 * userService.getUserFromToken(); var friend =
	 * userService.checkUserExistByContactNumber(user.getUserId(), contactNo); if
	 * (friend != null) { return ResponseEntity.ok("Friend Added Successfully"); }
	 * return
	 * ResponseEntity.status(400).body("User not existed with the contact no = " +
	 * contactNo); // If user not // exists }
	 */

	@PostMapping("/sendInvite")
	public ResponseEntity<String> sendInviteEmail(@RequestParam("email") String recipientEmail) {

		UserEntity user = userService.getUserFromToken();
		var isUserExist = userService.userExistByEmail(recipientEmail);

		if (!isUserExist) {
			if (emailNotificationService.sendInviteNotification(recipientEmail, user.getFullName())) {
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

	@DeleteMapping("/friends/{friendId}")
	public ResponseEntity<String> removeFriend(@PathVariable Long friendId) {
		UserEntity user = userService.getUserFromToken();
		userService.removeFriend(user, friendId);
		return ResponseEntity.ok("Friend removed successfully.");
	}

	@PutMapping("/update-password")
	public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequestDto updatePasswordRequestDto) {
		try {
			var user = userService.getUserFromToken();
			var response = userService.updatePassword(user.getEmail(), updatePasswordRequestDto.getPassWord());
			return ResponseEntity.ok(response);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the password.");
		}
	}

	@GetMapping("/getAllFriendList")
	public ResponseEntity<GetFriendListDto> getAllFriends() throws UnAuthorizedException {
		UserEntity user = userService.getUserFromToken();
		var friendList = userService.getAllFriendList(user.getUserId());
		var gfl = userService.getAllFriendListWithDetails(user.getUserId(), friendList);

//    	GetFriendListDto gfl = new GetFriendListDto(); 

		if (friendList == null) {
			gfl.setMessage("User not Existed by Id = " + user.getUserId());
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
