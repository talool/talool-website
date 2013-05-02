package com.talool.website.panel.image.upload;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * A panel that combines all the parts of the file uploader
 */
public class FileUploadPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public FileUploadPanel(String id) {
        super(id);

        // The buttons toolbar. Mandatory
        FileUploadBar fileUpload = new FileUploadBar("fileUpload");
        add(fileUpload);

        // The gallery that can be used to view the uploaded files
        // Optional
        FileUploadGallery preview = new FileUploadGallery("preview");
        add(preview);

        // The template used by jquery.fileupload-ui.js to show the files
        // scheduled for upload (i.e. the added files).
        // Optional
        FileUploadTemplate uploadTemplate = new FileUploadTemplate("uploadTemplate");
        add(uploadTemplate);

        // The template used by jquery.fileupload-ui.js to show the uploaded files
        // Optional
        FileDownloadTemplate downloadTemplate = new FileDownloadTemplate("downloadTemplate");
        add(downloadTemplate);
        
    }
	

}
