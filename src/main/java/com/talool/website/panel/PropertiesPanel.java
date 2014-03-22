package com.talool.website.panel;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.PropertyEntity;
import com.talool.core.service.ServiceException;
import com.talool.domain.Properties;
import com.talool.utils.KeyValue;
import com.talool.website.component.PropertyComboBox;

/**
 * 
 * An abstract panel that manages properties and doesn't care what entity
 * contains the properties.
 * 
 * @author clintz
 */

public abstract class PropertiesPanel extends Panel
{
	private static final Logger LOG = LoggerFactory.getLogger(PropertiesPanel.class);
	private static final long serialVersionUID = 2119205500872981012L;

	private Class<? extends PropertyEntity> entity;
	private List<KeyValue> keyValues;

	public PropertiesPanel(String id, IModel<Properties> model, Class<? extends PropertyEntity> propSupportEntity)
	{
		super(id, model);
		this.entity = propSupportEntity;
		this.keyValues = KeyValue.getKeyValues(model.getObject());
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	protected void onInitialize()
	{
		super.onInitialize();

		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));

		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		// setMarkupId to ensure it keeps the id how we want it
		container.add(feedback.setOutputMarkupId(true).setMarkupId("feedback"));

		IModel<Properties> props = (IModel<Properties>) getDefaultModel();
		final PropertyComboBox comboBox = new PropertyComboBox("comboBox", props, entity)
		{

			private static final long serialVersionUID = 7609398573563991376L;

			@Override
			public void onPropertySave(Properties props,
					AjaxRequestTarget target)
			{
				try
				{
					LOG.info(props.dumpProperties());

					saveEntityProperties(props, target);

					keyValues = KeyValue.getKeyValues(props);
					target.add(container);
				}
				catch (ServiceException e)
				{
					LOG.error("failed to merge merchant after saving properties.", e);
				}

			}

		};
		container.add(comboBox);

		final ListView<KeyValue> propteryList = new ListView<KeyValue>("propertyRptr", new PropertyModel<List<KeyValue>>(this, "keyValues"))
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<KeyValue> item)
			{
				KeyValue prop = item.getModelObject();
				item.add(new Label("pKey", prop.key));
				item.add(new Label("pVal", prop.value));
			}

		};
		container.add(propteryList);

	}

	public abstract void saveEntityProperties(final Properties props, AjaxRequestTarget target) throws ServiceException;

}
