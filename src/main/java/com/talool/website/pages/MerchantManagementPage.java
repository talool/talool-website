package com.talool.website.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.MerchantLocationsPanelDemo;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantManagementPage extends BasePage
{
	private static final long serialVersionUID = -6214364791355264043L;

	public MerchantManagementPage()
	{
		super();
	}

	public MerchantManagementPage(PageParameters parameters)
	{
		super(parameters);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		// TODO Replace with model, no need to pass in name
		add(new Label("headerTitle", "Merchants > " + getPageParameters().get("name")));

		List<ITab> tabs = new ArrayList<ITab>();
		tabs.add(new AbstractTab(new Model<String>("Deals"))
		{

			private static final long serialVersionUID = -8759935169052512532L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new MerchantLocationsPanelDemo(panelId, getPageParameters());
			}
		});

		tabs.add(new AbstractTab(new Model<String>("Deal Offers"))
		{

			private static final long serialVersionUID = 6405610365875810783L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new MerchantLocationsPanelDemo(panelId, getPageParameters());
			}
		});

		tabs.add(new AbstractTab(new Model<String>("Locations"))
		{

			private static final long serialVersionUID = 5458420599727527535L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new MerchantLocationsPanelDemo(panelId, getPageParameters());
			}
		});

		tabs.add(new AbstractTab(new Model<String>("Accounts"))
		{

			private static final long serialVersionUID = 5853871222415506440L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new MerchantLocationsPanelDemo(panelId, getPageParameters());
			}
		});

		final AjaxTabbedPanel<ITab> tabbedPanel = new AjaxTabbedPanel<ITab>("tabs", tabs);
		tabbedPanel.setSelectedTab(2);
		add(tabbedPanel);

	}
}
