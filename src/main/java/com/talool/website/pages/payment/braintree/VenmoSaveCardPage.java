package com.talool.website.pages.payment.braintree;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;

public class VenmoSaveCardPage extends BraintreePage {
	

	private static final long serialVersionUID = 1L;

	public VenmoSaveCardPage()
	{
		super();
		processPost();
	}

	public VenmoSaveCardPage(PageParameters parameters)
	{
		super(parameters);
		processPost();
	}
	
	private void processPost()
	{
		IRequestParameters params = getRequest().getPostParameters();
		
		String card = params.getParameterValue("card_number").toString();
		String exp_month = params.getParameterValue("expiration_month").toString();
		String exp_year = params.getParameterValue("expiration_year").toString();
		String customer_id = params.getParameterValue("customer_id").toString();
		String cvv = params.getParameterValue("cvv").toString();
		//String zip = params.getParameterValue("zipcode").toString();
		String session = params.getParameterValue("venmo_sdk_session").toString();

		BigDecimal amt = new BigDecimal("9.99");
		
		if (!StringUtils.isEmpty(card) && 
			!StringUtils.isEmpty(exp_month) && 
			!StringUtils.isEmpty(exp_year) &&
			!StringUtils.isEmpty(cvv) &&
			!StringUtils.isEmpty(customer_id) &&
			!StringUtils.isEmpty(session))
		{
			TransactionRequest request = new TransactionRequest()
			.amount(amt)
			.creditCard()
	        	.number(card)
	        	.expirationMonth(exp_month)
	        	.expirationYear(exp_year)
	        	.cvv("111")
	        .done()
	        .options()
	        	.venmoSdkSession(session)
	        	.submitForSettlement(true)
	        	.storeInVaultOnSuccess(true)
	        .done();

			Result<Transaction> result = gateway.transaction().sale(request);
			TextRequestHandler textRequestHandler = new TextRequestHandler("application/json", "UTF-8", createJsonResponse(result)); 
			RequestCycle.get().scheduleRequestHandlerAfterCurrent(textRequestHandler); 
		}
	}
	
}
