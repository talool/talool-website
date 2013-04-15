package com.talool.website.panel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

public class AdminModalWindow extends ModalWindow
{
	public Component getCurrentContent()
	{
		return currentContent;
	}

	public void setCurrentContent(Component currentContent)
	{
		this.currentContent = currentContent;
	}

	private Component currentContent;

	@Override
	public ModalWindow setContent(Component component)
	{
		currentContent = component;
		return super.setContent(component.setOutputMarkupId(true));
	}

	private static final long serialVersionUID = 1L;

	public AdminModalWindow(String id)
	{
		super(id);
		this.setAutoSize(true);
		this.setInitialWidth(840);
		this.setResizable(false);
		this.setCssClassName(ModalWindow.CSS_CLASS_GRAY);

		this.setTitle("Crud");

		this.setCookieName(null);

		this.setCloseButtonCallback(new ModalWindow.CloseButtonCallback()
		{

			private static final long serialVersionUID = 1421735013059613512L;

			public boolean onCloseButtonClicked(AjaxRequestTarget target)
			{
				// setResult("Modal window 2 - close button");
				return true;
			}
		});

		this.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 8961311909740932319L;

			public void onClose(AjaxRequestTarget target)
			{
				// target.addComponent(result);
			}
		});

	}

}
