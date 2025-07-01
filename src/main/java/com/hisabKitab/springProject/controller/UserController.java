package com.hisabKitab.springProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
import com.hisabKitab.springProject.dto.TokenRefreshRequest;
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
import com.hisabKitab.springProject.utils.ResponseBuilder;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

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

	@Autowired
	private HttpServletRequest httpServletRequest;
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
			System.out.println(" userid = " + userDetails.getUser().getUserId());

			String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUser().getUserId()).getToken();
			System.out.println("Refresh Token: " + refreshToken);
			var response = new LoginResponseDto(userDetails.getUser(), accessToken, refreshToken);
			System.out.println("Response: " + response);

			return ResponseBuilder.success(HttpStatus.OK, "Login Successful", response);
		} catch (BadCredentialsException e) {
			throw new UnAuthorizedException("Invalid email or password");
		}
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<CommonResponseDto<String>> refreshtoken(@RequestBody TokenRefreshRequest request) {
		String requestRefreshToken = request.getRefreshToken();
		// var refreshToken = refreshTokenService.findByToken(requestRefreshToken)
		// .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Invalid
		// refresh Token"));

		return refreshTokenService.findByToken(requestRefreshToken)
				.map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser)
				.map(user -> {
					String token = jwtUtil.generateTokenByIdAndRole(user.getUserId(), user.getRole());
					return ResponseBuilder.success(HttpStatus.OK, "Token refreshed successfully", token);
				})
				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
						"Refresh token is not in database!"));
	}

	// Signup endpoint
	@PostMapping("/signup")
	public ResponseEntity<CommonResponseDto<String>> signup(@RequestBody SignUpUserDto newUser) {
		var createdUser = userService.signup(newUser);
		if (createdUser != null) {
			return ResponseBuilder.success(HttpStatus.OK, "User registered successfully!", null);
		} else {
			return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "User already exists");
		}
	}

	@DeleteMapping("/signout")
	public ResponseEntity<CommonResponseDto<String>> logoutUser() {
		var user = userService.getUserFromToken();
		Long userId = user.getUserId();
		refreshTokenService.deleteByUserId(userId);
		return ResponseBuilder.success(HttpStatus.OK, "Logout Successful", null);
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
	public ResponseEntity<CommonResponseDto<String>> sendInviteEmail(@RequestParam("email") String recipientEmail) {
		UserEntity user = userService.getUserFromToken();
		var isUserExist = userService.userExistByEmail(recipientEmail);

		if (!isUserExist) {
			if (emailNotificationService.sendInviteNotification(recipientEmail, user.getFullName())) {
				return ResponseBuilder.success(HttpStatus.OK, "Invite Sent Successfully", null);
			}
			return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "Invite failed");
		}
		return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "User already exist with the given email.");
	}

	@PostMapping("/sendOTP")
	public ResponseEntity<CommonResponseDto<String>> sendOTPMail(@RequestParam("email") String recipientEmail,
			@RequestParam("type") String type) {
		boolean isUserExist = userService.userExistByEmail(recipientEmail);

		if ((type.equals("forget-password") && isUserExist) || (type.equals("sign-up") && !isUserExist)) {
			var otp = emailNotificationService.sendOtpNotification(recipientEmail);
			if (otp != null) {
				return ResponseBuilder.success(HttpStatus.OK, "OTP sent successfully", otp);
			}
			return ResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "OTP does not sent due to error");
		} else if ((type.equals("forget-password") && !isUserExist)) {
			return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "User does not exist with the given email");
		} else if ((type.equals("sign-up") && isUserExist)) {
			return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "User already exist with given email");
		}
		return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "OTP does not sent due to error");
	}

	@DeleteMapping("/friends/{friendId}")
	public ResponseEntity<CommonResponseDto<String>> removeFriend(@PathVariable Long friendId) {
		UserEntity user = userService.getUserFromToken();
		userService.removeFriend(user, friendId);
		return ResponseBuilder.success(HttpStatus.OK, "Friend removed successfully.", null);
	}

	@PutMapping("/update-password")
	public ResponseEntity<CommonResponseDto<String>> updatePassword(
			@RequestBody UpdatePasswordRequestDto updatePasswordRequestDto) {
		try {
			var response = userService.updatePassword(updatePasswordRequestDto.getEmail(),
					updatePasswordRequestDto.getPassWord());
			return ResponseBuilder.success(HttpStatus.OK, "Password updated successfully", response);
		} catch (EntityNotFoundException e) {
			return ResponseBuilder.failure(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (Exception e) {
			return ResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR,
					"An error occurred while updating the password.");
		}
	}

	@GetMapping("/getAllFriendList")
	// @PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<CommonResponseDto<GetFriendListDto>> getAllFriends() throws UnAuthorizedException {
		System.out.println("friend list called");
		UserEntity user = userService.getUserFromToken();
		var friendList = userService.getAllFriendList(user.getUserId());
		var gfl = userService.getAllFriendListWithDetails(user.getUserId(), friendList);

		if (friendList == null) {
			gfl.setMessage("User not Existed by Id = " + user.getUserId());
			return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, gfl.getMessage(), gfl);
		} else if (friendList.isEmpty()) {
			gfl.setMessage("No friends are there in the List");
			return ResponseBuilder.success(HttpStatus.OK, gfl.getMessage(), gfl);
		}
		gfl.setMessage("Friend List founded");
		System.out.println("friend list completed");
		return ResponseBuilder.success(HttpStatus.OK, gfl.getMessage(), gfl);
	}

	// Get User by refresh Token
	@GetMapping("")
	public ResponseEntity<CommonResponseDto<LoginResponseDto>> getUserByRefreshToken() {
		UserEntity user = userService.getUserFromToken();
		// Now implement to get the token and refresh token both

		// Extract access token from Authorization header
		String accessToken = null;
		String authHeader = httpServletRequest.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			accessToken = authHeader.substring(7);
		}

		// Get refresh token from DB/service (example)
		String refreshToken = null;
		if (user != null) {
			RefreshToken tokenEntity = refreshTokenService.findByUserId(user.getUserId());
			if (tokenEntity != null) {
				refreshToken = tokenEntity.getToken();
			}
		}
		var data = new LoginResponseDto(user, accessToken, refreshToken);
		return ResponseBuilder.success(HttpStatus.OK, "User fetched successfully", data);
	}
}
