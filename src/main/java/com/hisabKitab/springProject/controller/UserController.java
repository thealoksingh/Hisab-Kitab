package com.hisabKitab.springProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.hisabKitab.springProject.dto.TokenRefreshRequest;
import com.hisabKitab.springProject.dto.TokenRefreshResponse;
import com.hisabKitab.springProject.dto.UpdatePasswordRequestDto;
import com.hisabKitab.springProject.entity.RefreshToken;
import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.exception.TokenRefreshException;
import com.hisabKitab.springProject.exception.UnAuthorizedException;
import com.hisabKitab.springProject.security.CustomUserDetails;
import com.hisabKitab.springProject.security.JwtUtil;
import com.hisabKitab.springProject.service.EmailNotificationService;
import com.hisabKitab.springProject.service.RefreshTokenService;
import com.hisabKitab.springProject.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailNotificationService emailNotificationService;

	@Autowired
	private RefreshTokenService refreshTokenService;
	// @Autowired
	// private JwtTokenService jwtTokenService; // Inject JwtTokenService

	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/login")
	public ResponseEntity<CommonResponseDto<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto)
			throws UnAuthorizedException {

		try {
			Authentication auth = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));

			CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal(); // Access user directly

			String accessToken = jwtUtil.generateToken(auth);
			System.out.println("Access Token: " + accessToken);
			System.out.println(" userid = "+userDetails.getUser().getUserId());

			String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUser().getUserId()).getToken();
			System.out.println("Refresh Token: " + refreshToken);
			var response = new CommonResponseDto<>(HttpStatus.OK, "Login Successfull",
					new LoginResponseDto(userDetails.getUser().getUserId() ,userDetails.getUser().getFullName(), userDetails.getUser().getContactNo(),
					accessToken, refreshToken));
			System.out.println("Response: " + response);
			return ResponseEntity.ok(response);

		} catch (BadCredentialsException e) {
			throw new UnAuthorizedException("Invalid email or password");
		}
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken( @RequestBody TokenRefreshRequest request) {
		String requestRefreshToken = request.getRefreshToken();
		// var refreshToken = refreshTokenService.findByToken(requestRefreshToken)
		// .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Invalid refresh Token"));

		return refreshTokenService.findByToken(requestRefreshToken)
				.map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser)
				.map(user -> {
					String token = jwtUtil.generateTokenByIdAndRole(user.getUserId(), user.getRole());
					return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
				})
				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
						"Refresh token is not in database!"));
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

	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser() {
		var user = userService.getUserFromToken();
		Long userId = user.getUserId();
		refreshTokenService.deleteByUserId(userId);
		return ResponseEntity.ok(new CommonResponseDto<>(HttpStatus.OK, "Logout Successful", null));
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
			
			var response = userService.updatePassword(updatePasswordRequestDto.getEmail(), updatePasswordRequestDto.getPassWord());
			return ResponseEntity.ok(response);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the password.");
		}
	}

	@GetMapping("/getAllFriendList")
	// @PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<GetFriendListDto> getAllFriends() throws UnAuthorizedException {

		UserEntity user = userService.getUserFromToken();
		var friendList = userService.getAllFriendList(user.getUserId());
		var gfl = userService.getAllFriendListWithDetails(user.getUserId(), friendList);

		// GetFriendListDto gfl = new GetFriendListDto();

		if (friendList == null) {
			gfl.setMessage("User not Existed by Id = " + user.getUserId());
			return ResponseEntity.status(400).body(gfl);
		} else if (friendList.isEmpty()) {
			gfl.setMessage("No friends are there in the List");
			// gfl.setFriendList(friendList);
			return ResponseEntity.ok(gfl);
		}
		gfl.setMessage("Friend List founded");
		// gfl.setFriendList(friendList);
		return ResponseEntity.ok(gfl);
	}

}
