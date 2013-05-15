package com.talool.website.panel.image.upload.manipulator;

import java.io.IOException;

import org.apache.wicket.util.file.File;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.Info;
import org.im4java.core.InfoException;
import org.im4java.process.ProcessStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.website.Config;
import com.talool.website.panel.image.upload.FileUploadUtils;

public abstract class AbstractMagick implements IMagick
{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractMagick.class);
	private File inputFile;
	private File outputFile;
	
	protected static final int maxLogoWidth = 250;
	protected static final int maxLogoHeight = 75;
	
	private static final String baseUploadDir = Config.get().getUploadDir();
	private static final String tealGradientFileName = "tealGradient5.png";

	static
	{
		ProcessStarter.setGlobalSearchPath(Config.get().getImageMagickPath());
	}

	public File getInputFile()
	{
		return inputFile;
	}

	public void setInputFile(File inputFile)
	{
		this.inputFile = inputFile;
	}
	

	public File getOutputFile()
	{
		return outputFile;
	}

	public void setOutputFile(File outputFile)
	{
		this.outputFile = outputFile;
	}

	@Override
	public void process()
	{

		String inputFilePath = getInputFile().getAbsolutePath();
		String outputFilePath = getOutputFile().getAbsolutePath();

		ConvertCmd cmd = new ConvertCmd();
		try
		{
			WizardApprentice image = new WizardApprentice();
			if (image.isEPS)
			{
				//convert it to a PNG first
				inputFilePath = convertEPS();
			}
			
			// execute the operation
			cmd.run(getOperation(),inputFilePath,outputFilePath);
			
			//debug();
		}
		catch (IM4JavaException ime)
		{
			LOG.error("failed to process image: ", ime);
		}
		catch (Exception e)
		{
			LOG.error("failed to process image: ", e);
		}

	}

	protected String getTealGradientFilePath()
	{
		StringBuilder filepath = new StringBuilder(baseUploadDir);
		filepath.append(tealGradientFileName);
		String gradientPath = filepath.toString();

		/*
		 * Check out this IM KungFu!!!
		 */
		File gradient = new File(gradientPath);
		if (!gradient.exists())
		{
			// create this file if it doesn't exist
			ConvertCmd cmd = new ConvertCmd();
			IMOperation op = new IMOperation();
			op.size(10, 100);
			op.addImage("gradient:teal", gradientPath);
			try
			{
				// execute the operation
				cmd.run(op);
			}
			catch (Exception e)
			{
				LOG.error("failed to generate the teal gradient: ", e);
			}
		}

		return gradientPath;
	}
	
	private String convertEPS() throws IM4JavaException, InterruptedException, IOException
	{
		ConvertCmd cmd = new ConvertCmd();
		IMOperation op = new IMOperation();

		op.colorspace("RGB");
		op.flatten();
		op.addImage();
		op.addImage();
		
		// execute the operation
		String filename = FileUploadUtils.getPngFileName(getInputFile().getName());
		String inputFilePath = getInputFile().getAbsolutePath();
		StringBuilder sb = new StringBuilder(inputFilePath.substring(0,inputFilePath.lastIndexOf("/")));
		String tempFilePath = sb.append("/").append(filename).toString();
		cmd.run(op,inputFilePath,tempFilePath);
		
		return tempFilePath;	
	}

	private void debug()
	{
		StringBuilder sb = new StringBuilder("IMOperation: convert ");
		sb.append(getOperation().toString());
		LOG.debug(sb.toString());
	}
	
	protected class WizardApprentice {
		public boolean isRGB;
		public boolean isJPEG;
		public boolean isTooBig;
		public boolean hasAlpha;
		public boolean isEPS;
		
		public WizardApprentice()
		{
			try
			{
				Info info = new Info(getInputFile().getAbsolutePath());
				String colorspace = info.getProperty("Colorspace");
				String alpha = info.getProperty("Alpha");
				
				isTooBig = (info.getImageWidth() > maxLogoWidth || info.getImageHeight() > maxLogoHeight);
				isRGB = !colorspace.equalsIgnoreCase("cmyk");
				isJPEG = info.getImageFormat().contains("JPEG");
				isEPS = info.getImageFormat().contains("PostScript");
				hasAlpha = (alpha!=null);
				
				LOG.info(info.getImageFormat());
				//LOG.info(colorspace);
				//LOG.info(alpha);
			}
			catch(InfoException ie)
			{
				LOG.debug("failed to get info on image.", ie);
			}
		}
	}

}
