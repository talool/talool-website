package com.talool.website.panel.dealoffer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService.PropertySupportedEntity;
import com.talool.domain.Properties;
import com.talool.service.ServiceFactory;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.util.SessionUtils;

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

	private static final String NULL_DISPLAY = "---select existing key--";

	private static final ChoiceRenderer<String> choiceRenderer = new ChoiceRenderer<String>(
			"toString", "toString");

	private String selectedKey = null;
	private String addPropertyValue;
	private String newPropKey;
	private String newPropValue;

	private TextField<String> addPropertyValueTextField;

	private class KeyValue implements Serializable
	{
		private String key;
		private String value;

		public KeyValue(String key, String value)
		{
			this.key = key;
			this.value = value;
		}

		private static final long serialVersionUID = 7882876501860467190L;

	}

	private final PropertySupportedEntity propertySupportEntity;

	private class NewPropertyDropDown extends DropDownChoice<String>
	{
		private static final long serialVersionUID = 3554167399057308945L;

		@Override
		protected String getNullKeyDisplayValue()
		{
			return NULL_DISPLAY;
		}

		@Override
		public boolean isNullValid()
		{
			return false;
		}

		public NewPropertyDropDown(String id, IModel<String> model, List<? extends String> choices)
		{
			super(id, model, choices, choiceRenderer);

		}

		@Override
		protected boolean isDisabled(String object, int index, String selected)
		{
			return isKeyInProperties(object);
		}

	};

	public static interface PropertiesSaveCallback
	{
		public void save(final Properties properties);
	}

	public List<KeyValue> getKeyValues()
	{
		List<KeyValue> keyVals = new ArrayList<KeyValue>();
		Properties props = getProperties();

		for (Entry<String, String> entry : props.getAllProperties().entrySet())
		{
			keyVals.add(new KeyValue(entry.getKey(), entry.getValue()));
		}
		return keyVals;

	}

	public Properties getProperties()
	{
		final Properties props = (Properties) getDefaultModelObject();
		return props;
	}

	public IModel<List<String>> getUniquePropertyKeysModel()
	{
		return new LoadableDetachableModel<List<String>>()
		{
			private static final long serialVersionUID = -2584692449199671293L;

			@Override
			protected List<String> load()
			{
				List<String> keys = getUniquePropertyKeys();
				return keys;
			}

		};
	}

	public boolean isKeyInProperties(final String key)
	{
		boolean isIn = getProperties().getAllProperties().containsKey(key);
		return isIn;
	}

	public PropertiesPanel(String id, IModel<Properties> model, PropertySupportedEntity propSupportEntity)
	{
		super(id, model);
		this.propertySupportEntity = propSupportEntity;
	}

	public List<String> getContainedKeys()
	{
		List<String> uniqueKeys = new ArrayList<String>();
		uniqueKeys.addAll(getProperties().getAllProperties().keySet());
		return uniqueKeys;
	}

	public List<String> getUniquePropertyKeys()
	{
		List<String> uniqueKeys = null;
		try
		{
			uniqueKeys = ServiceFactory.get().getTaloolService().getUniqueProperyKeys(propertySupportEntity);
		}
		catch (ServiceException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uniqueKeys;
	}

	@Override
	@SuppressWarnings("serial")
	protected void onInitialize()
	{
		super.onInitialize();

		final NewPropertyDropDown keys =
				new NewPropertyDropDown("keys", new PropertyModel<String>(this, "selectedKey"), getUniquePropertyKeys());

		keys.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.add(addPropertyValueTextField);
				addPropertyValueTextField.setEnabled(selectedKey != null && !selectedKey.equals(NULL_DISPLAY));
			}
		});

		final WebMarkupContainer propertiesContainer = new WebMarkupContainer("propertiesContainer");
		add(propertiesContainer.setOutputMarkupId(true));

		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		// setMarkupId to ensure it keeps the id how we want it
		propertiesContainer.add(feedback.setOutputMarkupId(true).setMarkupId("feedback"));

		Form<Void> savePropForm = new Form<Void>("savePropForm");

		savePropForm.add(new TextField<String>("newPropKey", new PropertyModel<String>(this, "newPropKey")));
		savePropForm.add(new TextField<String>("newPropValue", new PropertyModel<String>(this, "newPropValue")));

		savePropForm.add(new AjaxSubmitLink("saveNewPropSubmitLink")
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{

				try
				{
					getProperties().createOrReplace(newPropKey, newPropValue);
					saveEntityProperties(getProperties());
					SessionUtils.infoMessage(String.format("Successfully added key '%s' with value '%s'", newPropKey, newPropValue));
					newPropKey = null;
					newPropValue = null;
					keys.setChoices(getUniquePropertyKeysModel());
				}
				catch (ServiceException e)
				{
					LOG.error("Probelm saving entity props: " + e.getLocalizedMessage(), e);
					SessionUtils.errorMessage("Probelm saving entity properties");
				}

				target.add(propertiesContainer);

			}

		});

		savePropForm.add(keys.setOutputMarkupPlaceholderTag(true).setOutputMarkupId(true));
		// savePropForm.add(new TextField<String>("addPropertyKey", new
		// PropertyModel<String>(this, "addPropertyKey")));
		addPropertyValueTextField = new TextField<String>("addPropertyValue", new PropertyModel<String>(this, "addPropertyValue"));
		savePropForm.add(addPropertyValueTextField.setEnabled(false).setOutputMarkupId(true));
		savePropForm.add(new AjaxSubmitLink("savePropSubmitLink")
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{

				try
				{
					getProperties().createOrReplace(selectedKey, addPropertyValue);
					saveEntityProperties(getProperties());
					SessionUtils.infoMessage(String.format("Successfully added key '%s' with value '%s'", selectedKey, addPropertyValue));
					addPropertyValue = null;
					selectedKey = null;
					keys.setChoices(getUniquePropertyKeysModel());
				}
				catch (ServiceException e)
				{
					LOG.error("Probelm saving entity props: " + e.getLocalizedMessage(), e);
					SessionUtils.errorMessage("Probelm saving entity properties");
				}
				target.add(propertiesContainer);

			}

		});

		propertiesContainer.add(savePropForm);

		final ListView<KeyValue> props = new ListView<KeyValue>("properties", new PropertyModel<List<KeyValue>>(this, "keyValues"))
		{

			@Override
			protected void populateItem(ListItem<KeyValue> item)
			{
				Form<Void> editExistingForm = new Form<Void>("editExistingForm");
				item.add(editExistingForm);

				final KeyValue keyVal = (KeyValue) item.getModelObject();

				System.out.println("Rendering " + keyVal.key + "," + keyVal.value);

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				// a trick so disabled field doesnt write null into model
				final TextField<String> keyField = new TextField<String>("key", Model.of(keyVal.key));

				final TextField<KeyValue> valField = new TextField<KeyValue>("value", new PropertyModel<KeyValue>(keyVal, "value"));
				editExistingForm.add(keyField);
				editExistingForm.add(valField);

				editExistingForm.add(new AjaxSubmitLink("save")
				{

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form)
					{

						try
						{
							System.out.println(keyVal.key + ", " + keyVal.value);
							getProperties().createOrReplace(keyField.getInput(), valField.getInput());
							saveEntityProperties(getProperties());
							keys.setChoices(getUniquePropertyKeysModel());
						}
						catch (ServiceException e)
						{
							LOG.error("Probelm saving entity props: " + e.getLocalizedMessage(), e);
							SessionUtils.errorMessage("Probelm saving entity properties");
						}

						target.add(propertiesContainer);

					}

				});

				editExistingForm.add(new AjaxSubmitLink("remove")
				{

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form)
					{

						try
						{
							getProperties().remove(keyVal.key);
							saveEntityProperties(getProperties());
							SessionUtils.infoMessage("Success in removing key '" + keyVal.key + "'");
							keys.setChoices(getUniquePropertyKeysModel());
						}
						catch (ServiceException e)
						{
							LOG.error("Probelm saving entity props: " + e.getLocalizedMessage(), e);
							SessionUtils.errorMessage("Probelm saving entity properties");
						}

						target.add(propertiesContainer);

					}

				});

			}

		};

		WebMarkupContainer propsMessage = new WebMarkupContainer("noPropsMessage")
		{
			@Override
			public boolean isVisible()
			{
				return !CollectionUtils.isNotEmpty(props.getList());
			}
		};

		propertiesContainer.add(propsMessage);

		WebMarkupContainer managePropsContainer = new WebMarkupContainer("managePropsContainer")
		{

			@Override
			public boolean isVisible()
			{
				return CollectionUtils.isNotEmpty(props.getList());
			}

		};

		propertiesContainer.add(managePropsContainer.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

		managePropsContainer.add(props);

	}

	public abstract void saveEntityProperties(final Properties props) throws ServiceException;

}
