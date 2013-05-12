package com.talool.website.panel;

import org.apache.wicket.markup.html.panel.Panel;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.service.CustomerService;
import com.talool.core.service.TaloolService;

/**
 * 
 * @author clintz
 * 
 */
public abstract class BasePanel extends Panel
{
	private static final long serialVersionUID = -2584158965295658902L;

	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	protected transient static final CustomerService customerService = FactoryManager.get()
			.getServiceFactory().getCustomerService();

	protected transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();

	public BasePanel(String id)
	{
		super(id);

	}

}
