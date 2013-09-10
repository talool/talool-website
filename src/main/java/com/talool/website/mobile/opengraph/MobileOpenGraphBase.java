package com.talool.website.mobile.opengraph;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.service.CustomerService;
import com.talool.core.service.TaloolService;
import com.talool.website.mobile.MobilePage;


public abstract class MobileOpenGraphBase extends MobilePage {

	private static final long serialVersionUID = 1L;
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	
	protected transient static final CustomerService customerService = FactoryManager.get()
			.getServiceFactory().getCustomerService();

	protected transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();
	
	public MobileOpenGraphBase(PageParameters parameters)
	{
		super(parameters);
	}
	
}
