package com.talool.website.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

public class MerchantModalWindow extends ModalWindow
{

	private static final long serialVersionUID = 1L;

	public MerchantModalWindow(String id)
	{
		super(id);
		this.setInitialWidth(840);
		this.setInitialHeight(400);
		this.setAutoSize(true);
		this.setResizable(false);
		this.setCssClassName(ModalWindow.CSS_CLASS_GRAY);
		
		this.setTitle("Create Merchant");

		this.setCookieName("merch-modal");

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
