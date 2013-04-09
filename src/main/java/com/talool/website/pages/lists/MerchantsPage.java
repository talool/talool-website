package com.talool.website.pages.lists;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Merchant;
import com.talool.website.models.MerchantListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminMenuPanel;
import com.talool.website.panel.MerchantPanel;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantsPage extends BasePage
{
	private static final long serialVersionUID = 9023714664854633955L;

	public MerchantsPage()
	{
		super();
	}

	public MerchantsPage(PageParameters parameters)
	{
		super(parameters);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final ModalWindow merchantModal;
		add(merchantModal = new ModalWindow("modal"));
		merchantModal.setInitialWidth(840);
		merchantModal.setInitialHeight(800);
		merchantModal.setResizable(false);
		merchantModal.setCssClassName(ModalWindow.CSS_CLASS_GRAY);
		merchantModal.setTitle("Create/Edit Merchant");
		merchantModal.setContent(new MerchantPanel(merchantModal.getContentId()));

		merchantModal.setCookieName("m-modal");

		merchantModal.setCloseButtonCallback(new ModalWindow.CloseButtonCallback()
		{

			private static final long serialVersionUID = 1421735013059613512L;

			public boolean onCloseButtonClicked(AjaxRequestTarget target)
			{
				// setResult("Modal window 2 - close button");
				return true;
			}
		});

		merchantModal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 8961311909740932319L;

			public void onClose(AjaxRequestTarget target)
			{
				// target.addComponent(result);
			}
		});

		add(new AjaxLink<Void>("merchantLink")
		{
			private static final long serialVersionUID = 8539856864609166L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				merchantModal.show(target);
			}
		});

		add(new AdminMenuPanel("adminMenuPanel").setRenderBodyOnly(true));

		final ListView<Merchant> mechants = new ListView<Merchant>("merchRptr", new MerchantListModel())
		{

			private static final long serialVersionUID = 8844000843574646422L;

			@Override
			protected void populateItem(ListItem<Merchant> item)
			{
				Merchant merchant = item.getModelObject();

				item.setModel(new CompoundPropertyModel<Merchant>(merchant));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd"));
				}
				else
				{
					item.add(new AttributeModifier("class", "even"));
				}

				item.add(new Label("name"));
				item.add(new Label("primaryLocation.address.niceCityState"));
				item.add(new Label("accounts", merchant.getNumberOfMerchantAccounts()));
			}

		};

		add(mechants);

	}
}
