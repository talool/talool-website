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

public class VenmoUseCardPage extends BraintreePage
{

	private static final long serialVersionUID = 1L;

	public VenmoUseCardPage()
	{
		super();
		processPost();
	}

	public VenmoUseCardPage(PageParameters parameters)
	{
		super(parameters);
		processPost();
	}

	private void processPost()
	{
		IRequestParameters params = getRequest().getPostParameters();

		String card = params.getParameterValue("venmo_sdk_payment_method_code").toString();

		BigDecimal amt = new BigDecimal("9.99");

		if (!StringUtils.isEmpty(card))
		{
			TransactionRequest request = new TransactionRequest()
					.amount(amt)
					.venmoSdkPaymentMethodCode(card)
					.options()
					.submitForSettlement(true)
					.done();

			Result<Transaction> result = gateway.transaction().sale(request);
			TextRequestHandler textRequestHandler = new TextRequestHandler("application/json", "UTF-8", createJsonResponse(result));
			RequestCycle.get().scheduleRequestHandlerAfterCurrent(textRequestHandler);
		}
	}

}
