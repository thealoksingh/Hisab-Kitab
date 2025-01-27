package com.hisabKitab.springProject.service;


import java.util.Properties;
import java.util.Random;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;


@Service
public class EmailNotificationService {
	
	 private final String fromEmail = "business.hisab.kitab@gmail.com"; // Sender's email
     private final String appPassword = "txjh mtdg kamn tfbs";  // App password

//    public static void main(String[] args) {
//        // Call the method with the email address
//        sendOTP("your-mail-id@gmail.com");
//    }
    // Send Invitation message to the provided email address
    public  boolean sendInviteNotification(String recipientEmail, String senderName) {
       
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");


        // Set up the session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(senderName+" Invited you to to join us on Hisab-Kitab!");
            String invitationTemplate = """
            		Hi ,

            		I’d love for you to join me on Hisab Kitab to simplify tracking and sharing expenses!

            		👉 [Sign up here](%s)

            		Let’s make managing expenses easier together.

            		Cheers,
            		%s
            		""";

            		
            	
            		String signupUrl = "https://hisab-kitab-business.netlify.app/"; // Replace with actual sign-up URL

            		String invitationMessage = String.format(invitationTemplate,  signupUrl, senderName);

            		System.out.println(invitationMessage);

            
            message.setText(invitationMessage);

            // Send the email
            Transport.send(message);

            // Log success
            System.out.println("Invitation sent to: " + recipientEmail);
            
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send Invitation to: " + recipientEmail);
            return false;
        }
    }

    
    public boolean sendAndAcceptFriendRequestNotification(String recipientEmail, String subjectText, String emailBodyMessage) {
    	
    	Properties properties = new Properties();
    	properties.put("mail.smtp.host", "smtp.gmail.com");
    	properties.put("mail.smtp.port", "587");
    	properties.put("mail.smtp.auth", "true");
    	properties.put("mail.smtp.starttls.enable", "true");
    	
    	
    	// Set up the session
    	Session session = Session.getInstance(properties, new Authenticator() {
    		@Override
    		protected PasswordAuthentication getPasswordAuthentication() {
    			return new PasswordAuthentication(fromEmail, appPassword);
    		}
    	});
    	
    	try {
    		// Create a message
    		Message message = new MimeMessage(session);
    		message.setFrom(new InternetAddress(fromEmail));
    		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
    		message.setSubject(subjectText);

    		System.out.println(emailBodyMessage);
    		
    		
    		message.setText(emailBodyMessage);
    		
    		// Send the email
    		Transport.send(message);
    		
    		// Log success
    		System.out.println("Friend Request sent to: " + recipientEmail);
    		
    		return true;
    	} catch (MessagingException e) {
    		e.printStackTrace();
    		System.out.println("Failed to send Friend Request to: " + recipientEmail);
    		return false;
    	}
    }

    
    public String sendOtpNotification(String recipientEmail) {
        // SMTP server configuration
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Generate OTP
        String otp = generateOtp();

        // Set up the session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(" Your 6-digit OTP(One time Password) to join Hisab-Kitab!");

            // Create the email body
            String emailBody = """
                Hi,

                Your One-Time Password (OTP) for joining Hisab-Kitab is: %s

                Use this OTP to complete your signup process.

                Cheers,
                %s
                """;

            String formattedMessage = String.format(emailBody, otp, "Hisab-Kitab");
            message.setText(formattedMessage);

            // Send the email
            Transport.send(message);

            // Log success
            System.out.println("OTP sent to: " + recipientEmail);

            // Return the generated OTP
            return otp;
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send OTP to: " + recipientEmail);
            return null;
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate 6-digit OTP
        return String.valueOf(otp);
    }

	
}

//App Password Steps :-
//App password will created from manage your google account->security->2-Step Verification->App Password-> Create Specific App-> Name="JavaMail App"->Create->Copy the 16-character password-> Paste in final String appPassword.
//you are good to go then.
