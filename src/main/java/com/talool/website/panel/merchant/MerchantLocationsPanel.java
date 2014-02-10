package com.talool.website.panel.merchant;

import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.talool.core.MerchantMedia;
import com.talool.website.component.StaticImage;
import com.talool.website.models.MerchantLocationListModel;
import com.talool.website.models.MerchantModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.wizard.MerchantWizard;
import com.talool.website.panel.merchant.wizard.MerchantWizard.MerchantWizardMode;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantLocationsPanel extends BaseTabPanel
{
	private static final long serialVersionUID = 3634980968241854373L;
	private UUID _merchantId;
	private MerchantWizard wizard;

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
		
		final WebMarkupContainer container = new WebMarkupContainer("list");
		container.setOutputMarkupId(true);
		add(container);
		
		final ListView<MerchantLocation> locations = new ListView<MerchantLocation>(
				"locationRptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<MerchantLocation> item)
			{
				final MerchantLocation managedLocation = item.getModelObject();

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
				
				String websiteUrl = managedLocation.getWebsiteUrl();
				if (websiteUrl != null && !websiteUrl.isEmpty())
				{
					ExternalLink webpage = new ExternalLink("websiteLink", websiteUrl, websiteUrl);
					item.add(webpage);
				}
				else
				{
					websiteUrl = "";
					Label webpage = new Label("websiteLink", websiteUrl);
					item.add(webpage);
				}

				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = 8817599057544892359L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						
						Merchant merchant = new MerchantModel(_merchantId, true).getObject();
						merchant.setCurrentLocation(managedLocation);
						wizard.setModelObject(merchant);
						
						wizard.open(target);
					}
				});
			}

		};
		container.add(locations);
		
		// override the action button
		final BasePage page = (BasePage) this.getPage();
		AjaxLink<Void> actionLink = new AjaxLink<Void>("actionLink")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				
				Merchant merchant = new MerchantModel(_merchantId, true).getObject();
				
				// create a new location and add it to the merchant
				MerchantLocation location = domainFactory.newMerchantLocation();
				MerchantMedia merchLogo = merchant.getCurrentLocation().getLogo();
				if (merchLogo != null)
				{
					location.setLogo(merchLogo);
				}
				MerchantMedia merchImage = merchant.getCurrentLocation().getMerchantImage();
				if (merchImage != null)
				{
					location.setMerchantImage(merchImage);
				}

				location.setCreatedByMerchantAccount(SessionUtils.getSession().getMerchantAccount());
				merchant.addLocation(location);
				merchant.setCurrentLocation(location);
				
				wizard.setModelObject(merchant);
				wizard.open(target);
			}
		};
		page.setActionLink(actionLink);
		Label actionLabel = new Label("actionLabel", getActionLabel());
		actionLabel.setOutputMarkupId(true);
		actionLink.add(actionLabel);
		actionLink.setOutputMarkupId(true);

		// Wizard
		wizard = new MerchantWizard("merchantWiz", "Merchant Wizard", MerchantWizardMode.MERCHANT_LOCATION)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target)
			{
				super.onFinish(target);
				// refresh the list after a deal is edited
				target.add(container);
			}
		};
		addOrReplace(wizard.setOutputMarkupId(true));
	}

	@Override
	public String getActionLabel()
	{
		return "Create New Location";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return null;
	}

}
