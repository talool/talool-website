package com.talool.website.panel.merchant;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.FactoryManager;
import com.talool.core.service.TaloolService;


public class MerchantPreviewUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
	
	private static final long serialVersionUID = 6447053015807823998L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantPreviewUpdatingBehavior.class);
	
	public static enum MerchantComponent {IMAGE};
	private MerchantPreview preview;
	private MerchantComponent component;
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public MerchantPreviewUpdatingBehavior(MerchantPreview preview, MerchantComponent component, String event) {
		super(event);
		this.preview = preview;
		this.component = component;
	}
	
	@Override
	protected void onUpdate(AjaxRequestTarget target) {
		switch (component) {
		case IMAGE:
			preview.merchantImageUrl = getFormComponent().getValue();
			target.add(preview.image);
			break;
		}
		
		
	}

}
