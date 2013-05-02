package com.talool.website.panel.deal.wizard;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.InlineFrame;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.template.PackageTextTemplate;

import com.talool.website.pages.UploadPage;

public class DealUpload extends WizardStep {

	private static final long serialVersionUID = 1L;
	
	private String url;
	
	public DealUpload()
    {
        super(new ResourceModel("title"), new ResourceModel("summary"));
        url = "no image uploaded";
    }
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		/*
		 * A testing label that will show that his all works
		 */
		final Label imageUrl = new Label("uploadedUrl", new PropertyModel<String>(this,"url"));
		imageUrl.setOutputMarkupId(true);
		addOrReplace(imageUrl);
		
		/*
		 * Add an iframe that keep the upload in a sandbox
		 */
		final InlineFrame iframe = new InlineFrame("uploaderIFrame", UploadPage.class);
		addOrReplace(iframe);
		
		/*
		 *  Enable messages to be posted from that sandbox
		 */
		iframe.add(new AbstractDefaultAjaxBehavior(){

			private static final long serialVersionUID = 1L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				url = RequestCycle.get().getRequest().getRequestParameters().getParameterValue("url").toString();
				target.add(imageUrl);
			}
			
			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);
				
				PackageTextTemplate ptt = new PackageTextTemplate( DealUpload.class, "DealUpload.js" );

				Map<String, Object> map = new HashMap<String, Object>();
				map.put( "callbackUrl", getCallbackUrl().toString() );
				
				response.render(JavaScriptHeaderItem.forScript(ptt.asString(map), "dealupload"));
			}
			
		});
		
	}

}
