package com.talool.website.panel.image.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.upload.FileItem;

import com.talool.core.MediaType;
import com.talool.website.panel.image.upload.manipulator.AbstractMagick;
import com.talool.website.panel.image.upload.manipulator.IMagickFactory;
import com.talool.website.panel.image.upload.manipulator.MagickFactory;


/**
 * A simple file manager that knows how to store, read and delete files
 * from the file system.
 */
public class FileManager
{
    private final Folder baseFolder;
    private final IMagickFactory magickFactory;

    public FileManager(final String baseFolder)
    {
        this.baseFolder = new Folder(baseFolder);
        this.magickFactory = new MagickFactory();
    }

    public int save(FileItem fileItem) throws IOException
    {
        File file = new File(baseFolder, fileItem.getName());
        FileOutputStream fileOS = new FileOutputStream(file, false);
        return IOUtils.copy(fileItem.getInputStream(), fileOS);
    }
    
    public int process(FileItem image, MediaType mediaType, UUID merchantId) throws IOException
    {
    	// stash the original file
    	FileUploadUtils.saveImage(image, merchantId, true);
    	
    	// process the file
    	AbstractMagick magick = (AbstractMagick)
    			this.magickFactory.getMagickForMediaType(mediaType);
    	magick.setImage(image);
    	magick.process();
    	image = magick.getImage();
    	
    	// store the updated file in the merchant's folder
    	return FileUploadUtils.saveImage(image, merchantId, false);

    }

    // NOTE: we're not using this, but it is broken when "process" is used
    public byte[] get(String fileName) throws IOException {
        File file = new File(baseFolder, fileName);
        return IOUtils.toByteArray(new FileInputStream(file));
    }

    // NOTE: we're not using this, but it is broken when "process" is used
    public boolean delete(String fileName)
    {
        File file = new File(baseFolder, fileName);
        return Files.remove(file);
    }
}
