package com.talool.website.panel.message;

import java.io.Serializable;

import com.talool.core.Deal;
import com.talool.core.Merchant;
import com.talool.domain.CustomerCriteria;

public class MerchantGift implements Serializable {

	private static final long serialVersionUID = 7850987146885189914L;
	
	private Deal deal;
	private CustomerCriteria criteria;
	private Merchant merchant;
	
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
	

	
	
	
}
