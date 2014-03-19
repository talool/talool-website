package com.talool.website.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.core.MediaType;
import com.talool.core.MerchantLocation;
import com.talool.core.MerchantMedia;
import com.talool.core.service.ServiceException;
import com.talool.website.component.StaticImage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.image.selection.MediaPickerTab;
import com.talool.website.util.SecuredPage;

/**
 * 
 * @author dmccuen
 * 
 */
@SecuredPage
public class MediaSummaryPage extends BasePage
{
	private static final long serialVersionUID = 9023714664854633955L;
	private static final Logger LOG = LoggerFactory.getLogger(MediaSummaryPage.class);

	private MerchantMedia _media;
	private int usageCount;
	private List<MerchantLocation> locations = new ArrayList<MerchantLocation>();
	private List<Deal> deals = new ArrayList<Deal>();
	private List<DealOffer> offers = new ArrayList<DealOffer>();
	
	private AjaxLink<Void> delete;
	private AjaxLink<Void> replace;
	
	public MediaSummaryPage(PageParameters parameters)
	{
		super(parameters);
		UUID mediaId = UUID.fromString(parameters.get("id").toString());
		
		try
		{
			_media = taloolService.getMerchantMediaById(mediaId);
			updateModels();
		}
		catch (ServiceException se)
		{
			LOG.error("Failed to get media by id ", se);
		}
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));
		
		container.add(new StaticImage("image", true, _media.getMediaUrl()));
		container.add(new Label("usageCount",new PropertyModel<Integer>(this,"usageCount")));
		
		final BasePage page = (BasePage) this.getPage();
		final AdminModalWindow modal = page.getModal();
		
		replace = new AjaxLink<Void>("replaceLink")
		{
			private static final long serialVersionUID = 268692101349122303L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				
				MerchantMedia rMedia = null;
				MediaPickerTab mp = new MediaPickerTab(modal.getContentId(), _media.getMerchantId(), 
						_media.getMediaType(), Model.of(rMedia))
				{

					private static final long serialVersionUID = -8058739501703670519L;

					@Override
					public void onMediaSelectionComplete(AjaxRequestTarget target, MerchantMedia replacementMedia) 
					{
						try
						{
							taloolService.replaceMerchantMedia(_media.getId(), replacementMedia.getId(), _media.getMediaType());
							info("Media replaced.");
							updateModels();
							replace.setEnabled(usageCount > 0);
							delete.setEnabled(usageCount == 0);
							target.add(container);
						}
						catch (ServiceException se)
						{
							error("Failied to replace media");
							LOG.error("failed to replace media",se);
						}
						modal.close(target);
						target.add(page.feedback);
					}
					
				};
				modal.setContent(mp);
				modal.setTitle("Pick Replacement Media");
				modal.setInitialWidth(600);
				modal.setInitialHeight(300);
				modal.setAutoSize(false);
				modal.show(target);
			}
		};
		replace.setOutputMarkupId(true);
		container.add(replace.setEnabled(usageCount > 0));
		
		delete = new AjaxLink<Void>("deleteLink")
		{
			private static final long serialVersionUID = 268692101349122303L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();

				try
				{
					taloolService.deleteMerchantMedia(_media.getId());
				}
				catch (ServiceException se)
				{
					LOG.error("failed to delete media", se);
				}
				
				// go back to the library
				throw new RestartResponseException(MediaManagementPage.class, new PageParameters());
			}
		};
		delete.setOutputMarkupId(true);
		container.add(delete.setEnabled(usageCount == 0));
	}
	
	private void updateModels()
	{
		try
		{
			MediaType mediaType = _media.getMediaType();
			if (mediaType.equals(MediaType.DEAL_IMAGE) || mediaType.equals(MediaType.MERCHANT_IMAGE))
			{
				locations = taloolService.getMerchantLocationsUsingMedia(_media.getId(), mediaType);
				deals = taloolService.getDealsUsingMedia(_media.getId());
			}
			else if (mediaType.equals(MediaType.DEAL_OFFER_LOGO) || mediaType.equals(MediaType.MERCHANT_LOGO))
			{
				locations = taloolService.getMerchantLocationsUsingMedia(_media.getId(), mediaType);
				offers = taloolService.getDealOffersUsingMedia(_media.getId(), mediaType);
			}
			else if (mediaType.equals(MediaType.DEAL_OFFER_BACKGROUND_IMAGE) || mediaType.equals(MediaType.DEAL_OFFER_MERCHANT_LOGO))
			{
				offers = taloolService.getDealOffersUsingMedia(_media.getId(), mediaType);
			}
			usageCount = locations.size() + deals.size() + offers.size();
		}
		catch (ServiceException se)
		{
			LOG.error("Failed to get media usage metrics ", se);
		}
	}

	@Override
	protected void setHeaders(WebResponse response)
	{
		super.setHeaders(response);
	}

	@Override
	public String getHeaderTitle()
	{
		return "Media Summary";
	}

	@Override
	public boolean hasActionLink() {
		return false;
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		// intentionally null
		return null;
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		return "";
	}
}
