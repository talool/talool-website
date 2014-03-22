package com.talool.website.component;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.wicket.jquery.ui.kendo.combobox.ComboBox;
import com.talool.core.PropertyEntity;
import com.talool.core.service.ServiceException;
import com.talool.domain.Properties;
import com.talool.service.ServiceFactory;
import com.talool.website.util.SessionUtils;

abstract public class PropertyComboBox extends Panel
{

	private static final long serialVersionUID = 4630168463550416538L;
	private static final Logger LOG = LoggerFactory.getLogger(PropertyComboBox.class);

	private String propertyKey;
	private String propertyValue;
	private Class<? extends PropertyEntity> entity;

	public PropertyComboBox(String id, IModel<Properties> model, Class<? extends PropertyEntity> pEntity)
	{
		super(id, model);
		this.entity = pEntity;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		Form form = new Form("form");
		add(form);

		final IModel<List<String>> availablePropertiesModel = getUniquePropertyKeysModel();
		final ComboBox<String> combobox =
				new ComboBox<String>("combobox", new PropertyModel<String>(this, "propertyKey"), availablePropertiesModel.getObject());
		form.add(combobox.setRequired(true));

		form.add(new TextField<String>("pvalue", new PropertyModel<String>(this, "propertyValue")));

		form.add(new AjaxSubmitLink("button")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				Properties props = getProperties();

				if (propertyValue == null || StringUtils.isEmpty(propertyValue))
				{
					props.remove(propertyKey);
					SessionUtils.infoMessage(String.format("Successfully removed key '%s'", propertyKey));
				}
				else
				{
					props.createOrReplace(propertyKey, propertyValue);
					SessionUtils.infoMessage(String.format("Successfully added key '%s' with value '%s'", propertyKey, propertyValue));
				}

				onPropertySave(props, target);
			}
		});
	}

	private IModel<List<String>> getUniquePropertyKeysModel()
	{
		return new LoadableDetachableModel<List<String>>()
		{
			private static final long serialVersionUID = -2584692449199671293L;

			@Override
			protected List<String> load()
			{
				List<String> uniqueKeys = null;
				try
				{
					uniqueKeys = ServiceFactory.get().getTaloolService().getUniqueProperyKeys(entity);
				}
				catch (ServiceException e)
				{
					LOG.error("failed to get property keys.", e);
				}
				return uniqueKeys;
			}

		};
	}

	private Properties getProperties()
	{
		final Properties props = (Properties) getDefaultModelObject();
		return props;
	}

	abstract public void onPropertySave(final Properties props, final AjaxRequestTarget target);

}
