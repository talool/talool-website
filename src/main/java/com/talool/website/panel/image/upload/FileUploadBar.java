package com.talool.website.panel.image.upload;

import java.util.UUID;

import org.apache.wicket.markup.html.panel.Panel;

import com.talool.core.MediaType;

/**
 * This bar contributes the toolbar with "Add files", "Start upload",
 * "Cancel upload" and "Delete all" buttons.
 */
public class FileUploadBar extends Panel {

	private static final long serialVersionUID = 1L;

	public FileUploadBar(String id, UUID merchantId, MediaType mediaType, String callbackUrl) {
        super(id);

        add(new FileUploadBehavior(merchantId, mediaType, callbackUrl));
    }
}
