package com.talool.website.pages;

import java.util.UUID;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.panel.SubmitCallBack;


public class BaseDashboard extends BasePage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(BasePage.class);
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	protected transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();

	public FeedbackPanel feedback;
	
	protected UUID merchantId;
	protected Merchant merchant;

	public BaseDashboard()
	{
		super();
		
		// debugging "4ab9ed13-1f39-4f8a-932a-dec4abc9f621" "01f6be1e-322d-4710-9baa-c6ba1374dd38"
		merchantId = UUID.fromString("63b9b22f-478d-462c-9340-7cf6a1836081");
		
		initMerchant();
	}
	
	public BaseDashboard(PageParameters parameters)
	{
		super(parameters);
		
		String id = parameters.get("id").toString();
		merchantId = UUID.fromString(id);
		
		initMerchant();
	}
	
	public void initMerchant()
	{
		try
		{
			merchant = taloolService.getMerchantById(merchantId);
		}
		catch (ServiceException se)
		{
			LOG.error("oops",se);
		}
	}
	
	
	public FeedbackPanel getFeedback()
	{
		return feedback;
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNewDefinitionPanelTitle() {
		// TODO Auto-generated method stub
		return null;
	}
}
