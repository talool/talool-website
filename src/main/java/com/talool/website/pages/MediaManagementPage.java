package com.talool.website.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.MediaType;
import com.talool.core.Merchant;
import com.talool.core.MerchantMedia;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.image.selection.MediaPickerTab;
import com.talool.website.panel.image.selection.MediaTaggerPanel;
import com.talool.website.panel.image.selection.MediaUploadTab;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author dmccuen
 * 
 */
@SecuredPage
public class MediaManagementPage extends BaseManagementPage
{
	private static final long serialVersionUID = -6214364791355264043L;
	private UUID _merchantId;

	private MerchantMedia selectedMedia;
	private static final String TABS = "tabs";
	
	public MediaManagementPage(PageParameters parameters)
	{
		super(parameters);
		_merchantId = SessionUtils.getSession().getMerchantAccount().getMerchant().getId();
	}

	@Override
	public String getHeaderTitle()
	{
		return "Image Library ";
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		List<ITab> tabs = new ArrayList<ITab>();
		
		tabs.add(new AbstractTab(new Model<String>("Images"))
		{

			private static final long serialVersionUID = 5853871222415506440L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new MediaPickerTab(panelId, null, MediaType.MERCHANT_IMAGE, Model.of(selectedMedia), true){

					private static final long serialVersionUID = -8058739501703670519L;

					@Override
					public void onMediaSelectionComplete(AjaxRequestTarget target, MerchantMedia media) 
					{
						showMediaSummary(target, media);
					}
					
				};
			}
		});
		
		tabs.add(new AbstractTab(new Model<String>("Untagged Images"))
		{

			private static final long serialVersionUID = 5853871222415506440L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new MediaTaggerPanel(panelId);
			}
		});
		
		tabs.add(new AbstractTab(new Model<String>("Logos"))
		{

			private static final long serialVersionUID = 5853871222415506440L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new MediaPickerTab(panelId, _merchantId, MediaType.MERCHANT_LOGO, Model.of(selectedMedia)){

					private static final long serialVersionUID = -8058739501703670519L;

					@Override
					public void onMediaSelectionComplete(AjaxRequestTarget target, MerchantMedia media) 
					{
						showMediaSummary(target, media);
					}
					
				};
			}
		});
		
		tabs.add(new AbstractTab(new Model<String>("Book Backgrounds"))
		{

			private static final long serialVersionUID = 5853871222415506440L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new MediaPickerTab(panelId, _merchantId, MediaType.DEAL_OFFER_BACKGROUND_IMAGE, Model.of(selectedMedia)){

					private static final long serialVersionUID = -8058739501703670519L;

					@Override
					public void onMediaSelectionComplete(AjaxRequestTarget target, MerchantMedia media) 
					{
						showMediaSummary(target, media);
					}
					
				};
			}
		});
		
		tabs.add(new AbstractTab(new Model<String>("Book Logos"))
		{

			private static final long serialVersionUID = 5853871222415506440L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new MediaPickerTab(panelId, _merchantId, MediaType.DEAL_OFFER_MERCHANT_LOGO, Model.of(selectedMedia)){

					private static final long serialVersionUID = -8058739501703670519L;

					@Override
					public void onMediaSelectionComplete(AjaxRequestTarget target, MerchantMedia media) 
					{
						showMediaSummary(target, media);
					}
					
				};
			}
		});

		final AjaxTabbedPanel<ITab> tabbedPanel = new AjaxTabbedPanel<ITab>(TABS, tabs)
		{

			private static final long serialVersionUID = -9186300115065742114L;

			@Override
			protected void onAjaxUpdate(AjaxRequestTarget target)
			{
				super.onAjaxUpdate(target);
				getSession().getFeedbackMessages().clear();
				
				selectedMedia = null;
				MediaManagementPage page = (MediaManagementPage) this.getPage();
				target.add(page.getFeedback());
				target.add(page.getActionLink());
			}

		};
		tabbedPanel.setSelectedTab(0);
		add(tabbedPanel.setOutputMarkupId(true));

	}
	
	private void showMediaSummary(AjaxRequestTarget target, MerchantMedia media)
	{
		PageParameters parameters = new PageParameters();
		parameters.set("id", media.getId());
		throw new RestartResponseException(MediaSummaryPage.class, parameters);
	}
	
	@SuppressWarnings({ "unchecked" })
	private MediaType getMediaTypeForSelectedTab()
	{
		MediaType mt = MediaType.DEAL_IMAGE;
		AjaxTabbedPanel<ITab> tabbedPanel = (AjaxTabbedPanel<ITab>) get(TABS);
		if (tabbedPanel==null) return mt;
		
		switch (tabbedPanel.getSelectedTab()) 
		{
		case 1: 
			mt = MediaType.MERCHANT_LOGO;
			break;
		case 2:
			mt = MediaType.DEAL_OFFER_BACKGROUND_IMAGE;
			break;
		case 3:
			mt = MediaType.DEAL_OFFER_MERCHANT_LOGO;
			break;
		case 0:
		default:
			mt = MediaType.DEAL_IMAGE;
			break;
		}

		return mt;
	}
	

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		Merchant m = SessionUtils.getSession().getMerchantAccount().getMerchant();
		MediaUploadTab uploadPanel = new MediaUploadTab(contentId, m.getId(), getMediaTypeForSelectedTab())
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onMediaUploadComplete(AjaxRequestTarget target,
					MerchantMedia media) {
				// TODO it might be nice to update the content of the current image picker
				
			}
			
		};
		return uploadPanel;
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		return "Upload Media";
	}
}
