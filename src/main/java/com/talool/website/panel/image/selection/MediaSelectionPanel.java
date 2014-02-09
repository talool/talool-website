package com.talool.website.panel.image.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.talool.core.MediaType;
import com.talool.core.MerchantMedia;
import com.talool.website.models.MerchantMediaListModel;

public abstract class MediaSelectionPanel extends Panel {


	private static final long serialVersionUID = 1L;
	private UUID merchantId;
	private MediaType mediaType;
	private IModel<MerchantMedia> merchantMediaModel;
	private AjaxTabbedPanel<ITab> tabbedPanel;
	
	public MediaSelectionPanel(String id, UUID mid, MediaType mt, IModel<MerchantMedia> model) {
		super(id);
		merchantMediaModel = model;
		mediaType = mt;
		merchantId = mid;
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		tabbedPanel = new AjaxTabbedPanel<ITab>("tabs", getTabs());
		add(tabbedPanel);
		
		// selecting the correct tab
		if (!getMyMedia().isEmpty() && !isCurrentMediaMine())
		{
			tabbedPanel.setSelectedTab(1);
		}
		else
		{
			tabbedPanel.setSelectedTab(0);
		}
		

	}
	
	private List<ITab> getTabs()
	{

		List<ITab> tabs = new ArrayList<ITab>();
		
		if (!getMyMedia().isEmpty())
		{
			tabs.add(new AbstractTab(new Model<String>("My Media"))
			{
	
				private static final long serialVersionUID = -6020689518505770059L;
	
				@Override
				public Panel getPanel(String panelId)
				{
					return new MediaPickerTab(panelId, merchantId, mediaType, merchantMediaModel)
					{
	
						private static final long serialVersionUID = 1L;
	
						@Override
						public void onMediaSelectionComplete(AjaxRequestTarget target,
								MerchantMedia media) {
							merchantMediaModel.setObject(media);
							onMediaPicked(target,media);
						}
						
					};
					
				}
			});
		}
		
		if (mediaType.equals(MediaType.DEAL_IMAGE) || 
			mediaType.equals(MediaType.MERCHANT_IMAGE))
		{
			tabs.add(new AbstractTab(new Model<String>("Stock Media"))
			{
	
				private static final long serialVersionUID = -6020689518505770059L;
	
				@Override
				public Panel getPanel(String panelId)
				{
					return new MediaPickerTab(panelId, null, mediaType, merchantMediaModel)
					{
	
						private static final long serialVersionUID = 1L;
	
						@Override
						public void onMediaSelectionComplete(AjaxRequestTarget target,
								MerchantMedia media) {
							merchantMediaModel.setObject(media);
							onMediaPicked(target,media);
						}
						
					};
					
				}
			});
		}
		
		tabs.add(new AbstractTab(new Model<String>("Upload"))
		{

			private static final long serialVersionUID = -6020689518505770059L;

			@Override
			public Panel getPanel(String panelId)
			{

				return new MediaUploadTab(panelId, merchantId, mediaType)
				{

					private static final long serialVersionUID = 1L;

					@Override
					public void onMediaUploadComplete(AjaxRequestTarget target,
							MerchantMedia media) {
						merchantMediaModel.setObject(media);
						onMediaPicked(target,media);
						
						List<ITab> tabs = getTabs();
						tabbedPanel.getTabs().removeAll(tabbedPanel.getTabs());
						tabbedPanel.getTabs().addAll(tabs);
						tabbedPanel.setSelectedTab(0);
						target.add(tabbedPanel);
					}
					
				};
				
			}
		});
		
		return tabs;
	}
	
	private List<MerchantMedia> getMyMedia()
	{
		MerchantMediaListModel mediaListModel = new MerchantMediaListModel();
		mediaListModel.setMerchantId(merchantId);
		mediaListModel.setMediaType(mediaType);
		return mediaListModel.getObject();
	}
	
	private boolean isCurrentMediaMine()
	{
		MerchantMedia currentMedia = merchantMediaModel.getObject();
		return (currentMedia==null)?false:currentMedia.getMerchantId().equals(merchantId);
	}
	
	// This is the hook for updating a preview
	abstract public void onMediaPicked(AjaxRequestTarget target, MerchantMedia media);

}
