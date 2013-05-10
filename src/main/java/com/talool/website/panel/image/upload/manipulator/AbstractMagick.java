package com.talool.website.panel.image.upload.manipulator;

import org.apache.wicket.util.file.File;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.website.Config;

public abstract class AbstractMagick implements IMagick
{

	private static final Logger LOG = LoggerFactory.getLogger(AbstractMagick.class);
	private File inputFile;
	private File outputFile;

	private static final String baseUploadDir = Config.get().getUploadDir();
	private static final String tealGradientFileName = "tealGradient3.png";

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
			// execute the operation
			cmd.run(getOperation(), inputFilePath, outputFilePath);

			// debug();
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
		filepath.append("/").append(tealGradientFileName);
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

	private void debug()
	{
		StringBuilder sb = new StringBuilder("IMOperation: ");
		sb.append(getOperation().toString());
		LOG.debug(sb.toString());
	}

}
