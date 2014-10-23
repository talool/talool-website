package com.talool.website.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import com.talool.core.DealOffer;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.panel.message.MessageJobPojo.MessageJobType;

public class MessageJobSelect extends DropDownChoice<MessageJobType>
{
	private static final long serialVersionUID = 6639221585443609156L;

	private static MessageJobType[] options = new MessageJobType[] { 
		MessageJobType.MerchantGiftJob,	
		MessageJobType.DealOfferPurchaseJob
	};

	private static List<MessageJobType> mjOptions = null;
	private static ChoiceRenderer<MessageJobType> cr =  null;
	static
	{
		mjOptions = Arrays.asList(options);
		cr = new ChoiceRenderer<MessageJobType>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(MessageJobType mjt) {
				return mjt.getName();
			}
			
		};
	}

	public MessageJobSelect(String id)
	{
		super(id, mjOptions, cr);
	}

	public MessageJobSelect(String id, IModel<MessageJobType> model)
	{
		super(id, model, mjOptions, cr);
	}
	
	public MessageJobSelect(String id, IModel<MessageJobType> model, UUID merchantId)
	{
		super(id, model, mjOptions, cr);
		List<DealOffer> books = new ArrayList<DealOffer>();
		try
		{
			books = ServiceFactory.get().getTaloolService().getDealOffersByMerchantId(merchantId);
		}
		catch(ServiceException e)
		{}
		
		// don't show the book option if the merchant doesn't have any books
		if (books.isEmpty())
		{
			this.setChoices(Arrays.asList(new MessageJobType[] { 
					MessageJobType.MerchantGiftJob
				}));
			
		}
		
	}

}
