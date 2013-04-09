package com.talool.website.panel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Use CSS for example:
 * 
 * .feedbackPanel { border: 1px solid #099; background-color: #eff; color: #099;
 * }
 * 
 * div.error ul.feedbackPanel { background-color: #fee; border: 1px solid #900;
 * color: #900; }
 * 
 * div.info ul.feedbackPanel { border: 1px solid #090; background-color: #efe;
 * color: #090; }
 * 
 * @author clintz
 * 
 */
public class NiceFeedbackPanel extends FeedbackPanel
{
	private static final long serialVersionUID = 4648228266685526319L;

	public NiceFeedbackPanel(String id, IFeedbackMessageFilter filter)
	{
		super(id, filter);
	}

	public NiceFeedbackPanel(String id)
	{
		super(id);

	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		if (anyMessage(FeedbackMessage.ERROR))
		{
			add(new AttributeModifier("class", "error"));
		}
		else if (anyMessage(FeedbackMessage.INFO))
		{
			add(new AttributeModifier("class", "info"));
		}

		// else use default
	}

}
