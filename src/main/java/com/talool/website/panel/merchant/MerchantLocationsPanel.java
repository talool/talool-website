package com.talool.website.panel.merchant;

import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.MerchantLocation;
import com.talool.core.MerchantMedia;
import com.talool.website.component.StaticImage;
import com.talool.website.models.MerchantLocationListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.MerchantLocationPanel;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantLocationsPanel extends BaseTabPanel
{
	private static final long serialVersionUID = 3634980968241854373L;
	private UUID _merchantId;

	public MerchantLocationsPanel(String id, PageParameters parameters)
	{
		super(id);
		_merchantId = UUID.fromString(parameters.get("id").toString());
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		MerchantLocationListModel model = new MerchantLocationListModel();
		model.setMerchantId(_merchantId);
		final ListView<MerchantLocation> locations = new ListView<MerchantLocation>(
				"locationRptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<MerchantLocation> item)
			{
				MerchantLocation managedLocation = item.getModelObject();
				final Long merchantLocationId = managedLocation.getId();

				item.setModel(new CompoundPropertyModel<MerchantLocation>(managedLocation));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				MerchantMedia media = managedLocation.getMerchantImage();
				if (media==null)
				{
					item.add(new StaticImage("myimage", false, "/img/000.png"));
				}
				else
				{
					item.add(new StaticImage("myimage", false, media.getMediaUrl()));
				}
				
				MerchantMedia logo = managedLocation.getLogo();
				if (logo==null)
				{
					item.add(new StaticImage("mylogo", false, "/img/000.png"));
				}
				else
				{
					item.add(new StaticImage("mylogo", false, logo.getMediaUrl()));
				}
				
				item.add(new Label("locationName"));
				item.add(new Label("phone"));
				item.add(new Label("address1"));
				item.add(new Label("address2"));
				item.add(new Label("city"));
				item.add(new Label("stateProvinceCounty"));
				item.add(new Label("zip"));
				
				String websiteUrl = new String();
				try
				{
					websiteUrl = managedLocation.getWebsiteUrl();
				}
				catch (Exception e)
				{
					websiteUrl = "empty";
				}
				ExternalLink webpage = new ExternalLink("website", websiteUrl, websiteUrl);
				item.add(webpage);

				BasePage page = (BasePage) this.getPage();
				final AdminModalWindow modal = page.getModal();
				final SubmitCallBack callback = page.getCallback(modal);
				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = 8817599057544892359L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						MerchantLocationPanel panel = new MerchantLocationPanel(modal.getContentId(), callback,
								merchantLocationId);
						modal.setContent(panel);
						modal.setTitle("Edit Merchant Location");
						modal.show(target);
					}
				});
			}

		};

		add(locations);
	}

	@Override
	public String getActionLabel()
	{
		return "Create Location";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return new MerchantLocationPanel(contentId, _merchantId, callback);
	}

}
