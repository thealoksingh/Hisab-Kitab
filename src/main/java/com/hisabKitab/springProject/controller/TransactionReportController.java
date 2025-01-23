package com.hisabKitab.springProject.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hisabKitab.springProject.entity.Balance;
import com.hisabKitab.springProject.entity.Transaction;
import com.hisabKitab.springProject.repository.TransactionRepository;
import com.hisabKitab.springProject.service.TransactionReportService;
import com.hisabKitab.springProject.service.TransactionService;
import com.hisabKitab.springProject.service.UserService;

@RestController
//@RequestMapping("/report")
@CrossOrigin(origins = "*")
public class TransactionReportController {

	
	@Autowired
    private  TransactionReportService reportService;
	@Autowired
	private UserService userService;
	@Autowired
    private  TransactionService transactionService; // Fetch transactions from DB
	@Autowired
	private TransactionRepository transactionRepository;

    public TransactionReportController(TransactionReportService reportService, TransactionService transactionService) {
        this.reportService = reportService;
        this.transactionService = transactionService;
    }

	/*
	 * @GetMapping("/api/reports/friend-transaction") public ResponseEntity<byte[]>
	 * generateFriendTransactionReport(@RequestParam("friendId") Long
	 * friendId, @RequestParam("userId") Long userId) { List<Transaction>
	 * transactions = transactionService.getAllTransactionWithFriend(userId,
	 * friendId); var friend = userService.getUserById(friendId); byte[] pdfData =
	 * reportService.generateFriendTransactionReport(transactions,
	 * friend.getFullName());
	 * 
	 * return ResponseEntity.ok() .header(HttpHeaders.CONTENT_DISPOSITION,
	 * "attachment; filename= "+friend.getFullName()+"-transaction-report.pdf")
	 * .contentType(MediaType.APPLICATION_PDF) .body(pdfData); }
	 */
    
    @GetMapping("/api/reports/friend-transaction")
    public ResponseEntity<byte[]> generateFriendTransactionReport(
        @RequestParam("friendId") Long friendId, 
        @RequestParam("userId") Long userId,
        @RequestParam("fromDate") String fromDate,  // fromDate as String
        @RequestParam("toDate") String toDate       // toDate as String
    ) throws IOException {
        // Convert String to LocalDate
    	System.out.println(fromDate+" -> "+ toDate);
        LocalDate startDate = LocalDate.parse(fromDate);
        LocalDate endDate = LocalDate.parse(toDate);

        // Get all transactions for the given user and friend within the specified date range
        List<Transaction> transactions = transactionService.getTransactionsByDateRange(userId, friendId, startDate, endDate);

        // Get friend details
        var friend = userService.getUserById(friendId);
        
//        Get Report data
        
        var reportData = getRunningBalanceBeforeDate(userId, friendId, endDate);

        // Generate the report
        byte[] pdfData = reportService.generateFriendTransactionReport(transactions, friend, fromDate, toDate, reportData.getRunningBalance(), reportData.getTotaDebit(), reportData.getTotalCredit());

        // Return the generated PDF as an attachment
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + friend.getFullName() + "-transaction-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfData);
    }
    
    public ReportData getRunningBalanceBeforeDate(long userId, long friendId, LocalDate startDate) {
    	var pastTransaction = transactionRepository.findAllTransactionsBeforeDate(userId, friendId, startDate);
    	var runningBalance = 0.0d;
    	var totalDebit = 0.0d;
    	var totalCredit = 0.0d;
    	
    	for (Transaction t:pastTransaction) {
    		if(t.getFromUserId()==userId) {
    			runningBalance += t.getAmount();
    			totalDebit += t.getAmount();
    		}else {
    			
    			runningBalance -= t.getAmount();
    			totalCredit += t.getAmount();
    		}
    		
    		
    		
    	}
    	return new ReportData(runningBalance, totalDebit, totalCredit);
    }
    
    
    @GetMapping("/api/reports/whole-transaction")
    public ResponseEntity<byte[]> generateWholeTransactionReport( @RequestParam ("userId") Long userId){
    	
    	
    	  // Get user details
        var user = userService.getUserById(userId);
	     var friendList = userService.getAllFriendList(userId);
    	
    	 var gfl = userService.getAllFriendListWithDetails(userId, friendList);
    
    	 
//        List<Balance> Balance = transactionService.getTransactionsByUserId(userId);
    	
        
    	 byte[] pdfData = reportService.generateWholeTransactionReport(gfl ,user.getFullName());

         // Return the generated PDF as an attachment
         return ResponseEntity.ok()
                 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + user.getFullName() + "-transaction-report.pdf")
                 .contentType(MediaType.APPLICATION_PDF)
                 .body(pdfData);
    
    }
}



class ReportData {
	private double runningBalance;
	private double totaDebit;
	private double totalCredit;
	public ReportData() {
	}
	public ReportData(double runningBalance, double totaDebit, double totalCredit) {
		this.runningBalance = runningBalance;
		this.totaDebit = totaDebit;
		this.totalCredit = totalCredit;
	}
	public double getRunningBalance() {
		return runningBalance;
	}
	public void setRunningBalance(double runningBalance) {
		this.runningBalance = runningBalance;
	}
	public double getTotaDebit() {
		return totaDebit;
	}
	public void setTotaDebit(double totaDebit) {
		this.totaDebit = totaDebit;
	}
	public double getTotalCredit() {
		return totalCredit;
	}
	public void setTotalCredit(double totalCredit) {
		this.totalCredit = totalCredit;
	}
	@Override
	public String toString() {
		return "ReportData [runningBalance=" + runningBalance + ", totaDebit=" + totaDebit + ", totalCredit="
				+ totalCredit + "]";
	}
	
	
}

