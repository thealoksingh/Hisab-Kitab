package com.hisabKitab.springProject.dto;

import java.time.LocalDate;

public class UpdateTransactionDto {

	       private Long transId;

	    private Long fromUserId;
		private Long toUserId;

	     private Double amount;

	    private LocalDate transDate;

	    private String description;

	     private Long createdBy;
	     
	     private String transType;
	     
	     
	     @Override
		public String toString() {
			return "UpdateTransactionDto [transId=" + transId + ", fromUserId=" + fromUserId + ", toUserId=" + toUserId
					+ ", amount=" + amount + ", transDate=" + transDate + ", description=" + description
					+ ", createdBy=" + createdBy + ", transType=" + transType + "]";
		}

		public UpdateTransactionDto() {
			super();
			// TODO Auto-generated constructor stub
		}

		public UpdateTransactionDto(Long transId, Long fromUserId, Long toUserId, Double amount, LocalDate transDate,
				String description, Long createdBy, String transType) {
			super();
			this.transId = transId;
			this.fromUserId = fromUserId;
			this.toUserId = toUserId;
			this.amount = amount;
			this.transDate = transDate;
			this.description = description;
			this.createdBy = createdBy;
			this.transType = transType;
		}

		public Long getTransId() {
			return transId;
		}

		public void setTransId(Long transId) {
			this.transId = transId;
		}

		public Long getFromUserId() {
			return fromUserId;
		}

		public void setFromUserId(Long fromUserId) {
			this.fromUserId = fromUserId;
		}

		public Long getToUserId() {
			return toUserId;
		}

		public void setToUserId(Long toUserId) {
			this.toUserId = toUserId;
		}

		public Double getAmount() {
			return amount;
		}

		public void setAmount(Double amount) {
			this.amount = amount;
		}

		public LocalDate getTransDate() {
			return transDate;
		}

		public void setTransDate(LocalDate transDate) {
			this.transDate = transDate;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Long getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(Long createdBy) {
			this.createdBy = createdBy;
		}

		public String getTransType() {
			return transType;
		}

		public void setTransType(String transType) {
			this.transType = transType;
		}

	
	
}
