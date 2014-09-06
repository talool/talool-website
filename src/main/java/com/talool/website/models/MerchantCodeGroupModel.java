package com.talool.website.models;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.MerchantCodeGroup;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantCodeGroupModel extends LoadableDetachableModel<MerchantCodeGroup>
{
	private static final long serialVersionUID = -6956910878696402522L;

	private static final Logger LOG = LoggerFactory.getLogger(MerchantCodeGroupModel.class);

	private String code;

	public MerchantCodeGroupModel(final String code)
	{
		this.code = code;
	}

	@Override
	protected MerchantCodeGroup load()
	{

		MerchantCodeGroup cg = null;

		try
		{
			cg = ServiceFactory.get().getTaloolService().getMerchantCodeGroupForCode(code);
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading MerchantCodeGroup", e);
		}

		return cg;
	}

}
