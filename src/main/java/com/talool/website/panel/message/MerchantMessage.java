package com.talool.website.panel.message;

import java.io.Serializable;

/*
 * Temporary POJO for designing Messages
 */
public class MerchantMessage implements Serializable {

	private static final long serialVersionUID = 7850987146885189914L;
	
	private String message;
	private String criteria;
	private int radius;
	private Boolean isActive;
	private String expiresOn;
	private String deliveryStats;
	private String createdBy;
	private String editedBy;
	
	private String messageId;
	private String merchantId;
	
	public MerchantMessage(String merchantId, String message, int radius) {
		super();
		this.message = message;
		this.radius = radius;
		this.merchantId = merchantId;
		this.isActive = true;
		this.createdBy = "Doug";
		this.editedBy = "Chris";
		this.expiresOn = "Friday";
		this.deliveryStats = "0 of 100";
		this.criteria = "All Customers";
		this.messageId = "abc123";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getDeliveryStats() {
		return deliveryStats;
	}

	public void setDeliveryStats(String deliveryStats) {
		this.deliveryStats = deliveryStats;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getEditedBy() {
		return editedBy;
	}

	public void setEditedBy(String editedBy) {
		this.editedBy = editedBy;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getExpiresOn() {
		return expiresOn;
	}

	public void setExpiresOn(String expiresOn) {
		this.expiresOn = expiresOn;
	}
	
	
	
}
