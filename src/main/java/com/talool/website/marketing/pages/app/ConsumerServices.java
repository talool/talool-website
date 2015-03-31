package com.talool.website.marketing.pages.app;

import com.talool.core.Merchant;
import com.talool.website.marketing.panel.CustomerPanel;
import com.talool.website.models.MerchantModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import java.util.UUID;


public class ConsumerServices extends BaseAppPage {

	private String merchantName;

	public ConsumerServices(PageParameters params) {
		super(params);

		StringValue whiteLableId = params.get("wlid");
		if (!whiteLableId.isNull() && !whiteLableId.isEmpty())
		{
			// Different merchants may want different content.  For now just swap out the name
			MerchantModel model = new MerchantModel(UUID.fromString(whiteLableId.toString()),false);
			Merchant m = model.getObject();
			if (m!=null)
			{
				merchantName = m.getName();
			}

		}

		if (merchantName==null) merchantName = "Talool";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new CustomerPanel("benefits", merchantName));
	}

}
