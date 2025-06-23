package com.hisabKitab.springProject.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;

    private String role;

    @Column(unique = true)
    private String contactNo;

    @ManyToMany
    @JoinTable(name = "friendship",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @JsonIgnore
    private List<UserEntity> friends = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FriendRequestEntity> sentRequests = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FriendRequestEntity> receivedRequests = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TransactionComment> comments;
    
    @Column(nullable = false)
    private String colorHexValue;

    public UserEntity() {
    }

    public UserEntity(String fullName, String email, String password, String role, String contactNo) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.contactNo = contactNo;
    }
    
    
    
    
 public UserEntity(String fullName, String email, String password, String role, String contactNo,
			String colorHexValue) {
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.role = role;
		this.contactNo = contactNo;
		this.colorHexValue = colorHexValue;
	}

// Convenience method to add a friend (and ensure bidirectional friendship)
 	public void addFriend(UserEntity friend) {
 		this.friends.add(friend);
 		friend.getFriends().add(this); // Ensure mutual friendship
 	}

 	// Convenience method to remove a friend
 	public boolean removeFriend(UserEntity friend) {
 		var res1 = this.friends.remove(friend);
       
        var res2 = friend.getFriends().remove(this); // Ensure mutual removal
       
        if (!res2 || !res1) {
            return false;
            
        }
 		return res1==res2;
 	}
    //Convience method to add check user is friend or not
    public boolean isFriend(UserEntity friend) {
        return this.friends.contains(friend);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public List<UserEntity> getFriends() {
        return friends;
    }

    public void setFriends(List<UserEntity> friends) {
        this.friends = friends;
    }

    public List<FriendRequestEntity> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(List<FriendRequestEntity> sentRequests) {
        this.sentRequests = sentRequests;
    }

    public List<FriendRequestEntity> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(List<FriendRequestEntity> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public List<TransactionComment> getComments() {
        return comments;
    }

    public void setComments(List<TransactionComment> comments) {
        this.comments = comments;
    }

    
    public String getColorHexValue() {
		return colorHexValue;
	}

	public void setColorHexValue(String colorHexValue) {
		this.colorHexValue = colorHexValue;
	}

	@Override
    public String toString() {
        return "UserEntity [userId=" + userId + ", fullName=" + fullName + ", email=" + email + ", password=" + password
                + ", role=" + role + ", contactNo=" + contactNo + "]";
    }
}

