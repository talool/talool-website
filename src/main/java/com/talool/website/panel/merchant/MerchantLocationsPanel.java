package com.talool.website.panel.merchant;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.MediaType;
import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.talool.core.MerchantMedia;
import com.talool.core.service.ServiceException;
import com.talool.domain.Properties;
import com.talool.service.ServiceFactory;
import com.talool.website.component.ConfirmationIndicatingAjaxLink;
import com.talool.website.models.MerchantLocationListModel;
import com.talool.website.models.MerchantModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.PropertiesPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.image.EditableImage;
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
	private static final Logger LOG = Logger.getLogger(MerchantLocationsPanel.class);
	private UUID _merchantId;
	private MerchantWizard wizard;

	public MerchantLocationsPanel(String id, PageParameters parameters)
	{
		super(id);
		_merchantId = UUID.fromString(parameters.get("id").toString());
	}

	public MerchantLocationsPanel(String id, UUID mid)
	{
		super(id);
		_merchantId = mid;
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final BasePage page = (BasePage) getPage();

		final AdminModalWindow modalProps = new AdminModalWindow("modalProps");
		modalProps.setInitialWidth(650);
		add(modalProps.setOutputMarkupId(true));

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

				String url = null;
				if (managedLocation.getMerchantImage() != null) 
				{
					url = managedLocation.getMerchantImage().getMediaUrl();
				}
				item.add(new EditableImage("editableImage",Model.of(url), _merchantId, MediaType.MERCHANT_IMAGE)
				{

					private static final long serialVersionUID = 1L;

					@Override
					public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media) 
					{
						try
						{
							managedLocation.setMerchantImage(media);
							taloolService.merge(managedLocation);
							target.add(this);
						}
						catch (ServiceException se)
						{
							LOG.error("Failed to save new image with managedLocation",se);
						}
						
					}
					
				});
				
				String logoUrl = (managedLocation.getLogo() != null)?managedLocation.getLogo().getMediaUrl():null;
				item.add(new EditableImage("editableLogo",Model.of(logoUrl), _merchantId, MediaType.MERCHANT_LOGO)
				{

					private static final long serialVersionUID = -7677598504543286741L;
					private int iframeWidth = 100;

					@Override
					public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media) 
					{
						try
						{
							// update the current locations
							Merchant m = taloolService.getMerchantById(_merchantId);
							m.getCurrentLocation().setLogo(media);
							taloolService.merge(m.getCurrentLocation());
							
							// check for other locations that have no logo and update them too
							for (MerchantLocation loc:m.getLocations())
							{
								if (loc.getLogo() == null)
								{
									loc.setLogo(media);
									taloolService.merge(loc);
								}
							}
							
							target.add(this);
						}
						catch (ServiceException se)
						{
							LOG.error("Failed to save new logo with merchant",se);
						}
						
					}

					@Override
					public int getIframeWidth() {
						return iframeWidth;
					}
				});

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

				item.add(new AjaxLink<Void>("editProps")
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();

						PropertiesPanel panel = new PropertiesPanel(modalProps.getContentId(), Model.of(managedLocation.getProperties()),
								MerchantLocation.class)
						{

							private static final long serialVersionUID = -6061721033345142501L;

							@Override
							public void saveEntityProperties(Properties props, AjaxRequestTarget target)
							{
								try
								{
									ServiceFactory.get().getTaloolService().merge(managedLocation);
								}
								catch (ServiceException e)
								{
									LOG.error("Failed to merge locaion after editing properties", e);
								}
								target.appendJavaScript("window.parent.Wicket.Window.current.autoSizeWindow();");
							}

						};

						StringBuilder sb = new StringBuilder();
						modalProps.setContent(panel);
						sb.setLength(0);
						sb.append("Manage '").append(managedLocation.getNiceCityState()).append("'").append(" properties");
						modalProps.setTitle(sb.toString());
						modalProps.show(target);
					}

				}.setVisible(page.isSuperUser));
				
				StringBuilder confirm = new StringBuilder();
				confirm.append("Are you sure you want to remove \"").append(managedLocation.getNiceCityState()).append("\"?");
				ConfirmationIndicatingAjaxLink<Void> deleteLink = new ConfirmationIndicatingAjaxLink<Void>("deleteLink", JavaScriptUtils.escapeQuotes(confirm.toString()).toString())
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						
						// abort if there is only one location
						Merchant merchant = new MerchantModel(_merchantId, true).getObject();
						if (merchant.getLocations().size()<2)
						{
							Session.get().error("We can't delete the last location for "+merchant.getName()+".");
							return;
						}
						
						try 
						{
							taloolService.deleteMerchantLocation(managedLocation.getId());
							target.add(container);
							Session.get().success(managedLocation.getNiceCityState() + " has been deleted.");
						} 
						catch (ServiceException se)
						{
							LOG.error("problem fetcing the talool merchant id", se);
							Session.get().error("There was a problem deleting this location.");
						}
						target.add(((BasePage)getPage()).feedback);
						
					}
				};
				item.add(deleteLink);
			}

		};
		container.add(locations);

		// override the action button
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
