package com.talool.website.panel.merchant;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.DealAcquire;
import com.talool.core.service.ServiceException;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.MerchantAccountPanel;

/**
 * 
 * @author clintz
 * 
 */
public class RedemptionCodeLookupPanel extends BaseTabPanel
{
	private static final Logger L = Logger.getLogger(RedemptionCodeLookupPanel.class);
	private static final long serialVersionUID = 3634980968241854373L;

	private UUID merchantId;
	private String redemptionCode;

	public RedemptionCodeLookupPanel(String id, PageParameters parameters)
	{
		super(id);
		merchantId = UUID.fromString(parameters.get("id").toString());
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		Form<Void> form = new Form<Void>("searchForm")
		{

			@Override
			protected void onSubmit()
			{
				super.onSubmit();

				try
				{
					List<DealAcquire> dacs = taloolService.getRedeemedDealAcquires(merchantId, redemptionCode);
					RedemptionCodeLookupPanel.this.get("redeemedRptr").setDefaultModel(Model.of(dacs));
					RedemptionCodeLookupPanel.this.get("redeemedRptr").setVisible(true);
				}
				catch (ServiceException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			private static final long serialVersionUID = 773304007969659116L;

		};

		form.add(new TextField<String>("redemptionCode", new PropertyModel<String>(this, "redemptionCode")).setRequired(true));
		add(form);

		add(new ListView<DealAcquire>("redeemedRptr")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<DealAcquire> item)
			{
				DealAcquire dac = item.getModelObject();
				item.add(new Label("redemptionDate", dac.getRedemptionDate()));
				if (dac.getRedeemedAtGeometry() != null)
				{
					item.add(new Label("redeemedLocation", "longitude: " + dac.getRedeemedAtGeometry().getCoordinate().x + " latitude:"
							+ dac.getRedeemedAtGeometry().getCoordinate().x));
				}
				else
				{
					item.add(new Label("redeemedLocation", "Unknown"));
				}

				item.add(new Label("dealTitle", dac.getDeal().getTitle()));
				item.add(new Label("customerName", dac.getCustomer().getFirstName() + " " + dac.getCustomer().getLastName()));
				item.add(new Label("customerEmail", dac.getCustomer().getEmail()));
			}

		}.setVisible(false));

	}

	@Override
	public String getActionLabel()
	{
		return "Create Account";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return new MerchantAccountPanel(contentId, merchantId, callback);
	}

}