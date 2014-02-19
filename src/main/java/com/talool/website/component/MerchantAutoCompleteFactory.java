package com.talool.website.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.wicketstuff.objectautocomplete.AutoCompletionChoicesProvider;
import org.wicketstuff.objectautocomplete.ObjectAutoCompleteBuilder;
import org.wicketstuff.objectautocomplete.ObjectAutoCompleteRenderer;
import org.wicketstuff.objectautocomplete.ObjectReadOnlyRenderer;

import com.talool.core.Merchant;
import com.talool.website.models.MerchantListModel;

public class MerchantAutoCompleteFactory implements Serializable
{

	private static final long serialVersionUID = 348887019963831116L;
	private static final int MAX_CHOICES_PER_SEARCH = 6;
	
	private transient ObjectAutoCompleteBuilder<Merchant,UUID> builder;
	
	public MerchantAutoCompleteFactory(final Merchant merchant) {
		builder = new ObjectAutoCompleteBuilder<Merchant,UUID>(new AutoCompletionChoicesProvider<Merchant>()
		{
			private static final long serialVersionUID = 1L;
			private MerchantListModel merchantModel = new MerchantListModel();

			public Iterator<Merchant> getChoices(String input)
			{
				// limit the search with a merchant id
				merchantModel.setMerchantId(merchant.getId());
				merchantModel.setIncludeCurrentMerchantId(true);
				
				List<Merchant> ret = new ArrayList<Merchant>();
				for (Merchant m : merchantModel.getObject())
				{
					if (m.getName().toLowerCase().contains(input.toLowerCase()))
					{
						ret.add(m);
					}
					if(ret.size() == MAX_CHOICES_PER_SEARCH) {
	    				break;
	    			}
				}
				return ret.iterator();
			}
		});
		
		// This helps us render the correct strings for auto-complete choices
		builder.autoCompleteRenderer(new ObjectAutoCompleteRenderer<Merchant>(){
			private static final long serialVersionUID = 1L;

			protected String getIdValue(Merchant m) {
				return m.getId().toString();
			}
			protected String getTextValue(Merchant m) {
				return m.getName();
			}
		});
		
		// This is the link text that opens the search field
		builder.searchLinkText("(change)");
		
		// Make sure we can show the current merchant by default
		builder.readOnlyRenderer(new ObjectReadOnlyRenderer<UUID>() {
			private static final long serialVersionUID = 1L;

			public Component getObjectRenderer(String id, IModel<UUID> pModel, IModel<String> pSearchTextModel) {
				Label label = new Label(id, pSearchTextModel);
				label.setDefaultModelObject(merchant.getName());
		        return label;
		    }
		});
	
	}
	
	public ObjectAutoCompleteBuilder<Merchant,UUID> builder()
	{
		return builder;
	}

}