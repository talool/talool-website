package com.talool.website.pages.payment.braintree;

import org.apache.wicket.ajax.json.JSONStringer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.ValidationError;

abstract public class BraintreePage extends WebPage {
	
	private static final long serialVersionUID = 1L;
	private static final String merchantId = "mkf3rwysqz6w9x44";
	private static final String publicKey = "ck6f7kcdq8jwq5b8";
	private static final String privateKey = "ac3b232be33cce4cf3ce108106d0a93e";
	protected BraintreeGateway gateway;
	
	private static final Logger LOG = LoggerFactory.getLogger(BraintreePage.class);

	public BraintreePage()
	{
		super();
		initGateway();
	}

	public BraintreePage(PageParameters parameters)
	{
		super(parameters);
		initGateway();
	}
	
	private void initGateway()
	{
		gateway = new BraintreeGateway(
	            Environment.SANDBOX,
	            merchantId,
	            publicKey,
	            privateKey
	        );
	}
	
	public String createJsonResponse(Result<Transaction> result)
	{
		JSONStringer jb = new JSONStringer();
		String json;
		try {
			if (result.isSuccess()) {
	            Transaction transaction = result.getTarget();
	            jb.object()
	         			.key("success")
	         			.value(1)
	         			.key("transactionId")
	         			.value(transaction.getId())
	         		.endObject();
	        } else if (result.getTransaction() != null) {
	        	Transaction transaction = result.getTransaction();
	            jb.object()
	         			.key("message")
	         			.value(result.getMessage())
	         			.key("status")
	         			.value(transaction.getStatus())
	         			.key("code")
	         			.value(transaction.getProcessorResponseCode())
	         			.key("text")
	         			.value(transaction.getProcessorResponseText())
	         		.endObject();
	        } else {
	            jb.object()
	         			.key("message")
	         			.value(result.getMessage())
	         			.key("errors")
	         			.array();
	            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
	            	jb.object()
	            			.key("attribute")
	            			.value(error.getAttribute())
	            			.key("code")
	            			.value(error.getCode())
	            			.key("message")
	            			.value(error.getMessage())
	            		.endObject();
	            }
	            jb.endArray().endObject();
	        }
			json = jb.toString();
		} catch (Exception e) {
			json = "{\"success\":0, \"message\":\"failed to create response\"}";
			LOG.debug("Failed to create a json payment response: ",e);
		}
		
		LOG.debug("json response: "+json);
		
		return json;
	}
}
