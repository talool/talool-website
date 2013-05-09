package com.talool.website.panel.image.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.upload.FileItem;

import com.talool.website.Config;

public class FileUploadUtils {

	private static final String baseUrl = Config.get().getStaticLogoBaseUrl();
	private static final String baseUploadDir = Config.get().getUploadDir();
	
	public static String getImageUrl(FileItem image, UUID merchantId) {
		StringBuilder sb = new StringBuilder(baseUrl);
    	if (merchantId != null)
    	{
    		sb.append(getMerchantFolderName(merchantId))
    			.append("/");
    	}
    	sb.append(image.getName());
        return sb.toString();
	}
	
	public static Folder getImageDir(UUID merchantId, boolean original) throws IOException {
		StringBuilder folderPath = new StringBuilder(baseUploadDir);
    	folderPath.append("/")
    		.append(getMerchantFolderName(merchantId));
    	
    	if (original)
    	{
    		folderPath.append("/original");
    	}
    	
    	Folder merchantFolder = new Folder(folderPath.toString());
    	merchantFolder.ensureExists();
    	
    	return merchantFolder;
	}
	
	/*
	 * TODO consider renaming file if there is a name collision
	 */
	public static int saveImage(FileItem image, UUID merchantId, boolean original) throws IOException 
	{
		Folder folder = getImageDir(merchantId, original);
    	File imageFile = new File(folder, image.getName());
    	FileOutputStream fileOS = new FileOutputStream(imageFile, false);
    	return IOUtils.copy(image.getInputStream(), fileOS);
	}
	
	/*
	 * TODO hash the UUID if we don't like it in the url 
	 * and the file system
	 */
	private static String getMerchantFolderName(UUID merchantId)
	{
		return merchantId.toString();
	}
	
}
