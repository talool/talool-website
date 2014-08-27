package com.talool.website.panel.message;

import java.io.Serializable;
import java.util.Date;

import com.talool.core.Deal;
import com.talool.core.Merchant;
import com.talool.domain.CustomerCriteria;

public class MerchantGift implements Serializable {

	private static final long serialVersionUID = 7850987146885189914L;
	
	private Deal deal;
	private CustomerCriteria criteria;
	private Merchant merchant;
	private String title;
	private Date startDate;
	
	public MerchantGift(Merchant merchant) {
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
	

	
	
	
}
