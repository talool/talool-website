package com.talool.website.panel.message;

import java.io.Serializable;
import java.util.Date;

import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.core.Merchant;
import com.talool.domain.CustomerCriteria;

public class MessageJobPojo implements Serializable {

	private static final long serialVersionUID = 7850987146885189914L;
	
	private Deal deal;
	private DealOffer offer;
	private CustomerCriteria criteria;
	private Merchant merchant;
	private String title;
	private String notes;
	private String message;
	private Date startDate;
	
	private MessageJobType jobType;
	
	public enum MessageJobType
	{
		MerchantGiftJob("Gift"), DealOfferPurchaseJob("Book");

		private String name;

		private MessageJobType(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}
	}
	
	public MessageJobPojo(Merchant merchant) {
		super();
		this.merchant = merchant;
		this.criteria = new CustomerCriteria();
	}
	public Deal getDeal() {
		return deal;
	}
	public void setDeal(Deal deal) {
		this.deal = deal;
	}
	public CustomerCriteria getCriteria() {
		return criteria;
	}
	public void setCriteria(CustomerCriteria criteria) {
		this.criteria = criteria;
	}
	public Merchant getMerchant()
	{
		return merchant;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getStartDate() {
		if (this.startDate==null) startDate = new Date();
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public DealOffer getOffer() {
		return offer;
	}
	public void setOffer(DealOffer offer) {
		this.offer = offer;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public MessageJobType getJobType() {
		return jobType;
	}
	public void setJobType(MessageJobType jobType) {
		this.jobType = jobType;
	}
	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
	

	
	
	
}
