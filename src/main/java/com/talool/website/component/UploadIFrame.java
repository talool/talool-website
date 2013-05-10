package com.talool.website.component;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.link.InlineFrame;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.template.PackageTextTemplate;

import com.talool.website.pages.UploadPage;

abstract public class UploadIFrame extends InlineFrame {

	private static final long serialVersionUID = -4183138880833396304L;

	public UploadIFrame(String id, PageParameters params) {
		super(id, UploadPage.class, params);
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		/*
		 * Enable messages to be posted from that sandbox
		 */
		add(new AbstractDefaultAjaxBehavior()
		{

			private static final long serialVersionUID = 1L;
			private PackageTextTemplate ptt;

			@Override
			protected void respond(AjaxRequestTarget target)
			{
				IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
				String url = params.getParameterValue("url").toString();

				onUploadComplete(target,url);
			}

			@Override
			public void renderHead(Component component, IHeaderResponse response)
			{
				super.renderHead(component, response);

				ptt = new PackageTextTemplate(UploadIFrame.class, "UploadIFrame.js");

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("callbackUrl", getCallbackUrl().toString());

				response.render(JavaScriptHeaderItem.forScript(ptt.asString(map), "UploadIFrame"));
			}

		});
	}
	
	abstract public void onUploadComplete(AjaxRequestTarget target, String url);

}
