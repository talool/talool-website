package com.talool.website.component;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.file.Files;

import com.talool.website.Config;

/**
 * 
 * @author clintz
 * 
 */
public class FileUploadForm extends Form<Void>
{
	private static final long serialVersionUID = 1L;
	private FileUploadField fileUploadField;
	private FileUpload fileUpload;
	private static final Logger logger = Logger.getLogger(FileUploadForm.class);
	private FeedbackPanel uploadFeedbackPanel;

	public FileUploadForm(final String name, final FeedbackPanel feedbackPanel)
	{
		super(name);
		;
		// set this form to multipart mode (always needed for uploads!)
		setMultiPart(true);
		add(fileUploadField = new FileUploadField("fileInput", new PropertyModel<List<FileUpload>>(
				this, "fileUpload")));

		// fileUploadField.add(new LogoValidator()).setRequired(true);

		add(new AjaxButton("submit", this)
		{

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				super.onError(target, form);
				uploadFeedbackPanel.modelChanged();
				target.add(uploadFeedbackPanel);
			}

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> arg1)
			{
				final FileUpload upload = fileUploadField.getFileUpload();
				saveLogoName(upload);

				// campaign.setLogoName(finalFileName);
				// adManagementService.saveCampaign(campaign);
				info("Successfully updated logo and saved to campaign.");

				// final Component c = getPage().get("cLogoContainer");
				// if (c != null)
				// target.add(c);

				target.prependJavaScript(("doPostActions();"));
				target.prependJavaScript(("doPreActions();"));
				// target.add(container);
			}
		});
	}

	protected void saveLogoName(final FileUpload uploadedFile)
	{
		if (uploadedFile != null)
		{
			final File newFile = new File(Config.get().getUploadDir() + uploadedFile.getClientFileName());

			if (newFile.exists())
			{
				newFile.delete();
			}

			try
			{
				newFile.createNewFile();
				uploadedFile.writeTo(newFile);

				info("saved file: " + uploadedFile.getClientFileName());
			}
			catch (Exception e)
			{
				throw new IllegalStateException("Error");
			}
		}
	}

	protected String getFinalLogoFileName(final FileUpload upload)
	{
		return upload.getClientFileName();
	}

	private void checkFileExists(File newFile)
	{
		if (newFile.exists())
		{
			// Try to delete the file
			if (!Files.remove(newFile))
			{
				throw new IllegalStateException("Unable to overwrite " + newFile.getAbsolutePath());
			}
		}
	}

}
