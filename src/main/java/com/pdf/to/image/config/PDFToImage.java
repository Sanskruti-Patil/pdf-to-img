package com.pdf.to.image.config;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * This class is to convert given pdf file into an image
 * 
 * @author sans.kruti
 *
 */
public class PDFToImage {
	public static void main(String[] args) {
		final String sourceDir = "F:\\sample.pdf"; // Pdf files are read from this folder
		final String destinationDir = "F:\\Converted_PdfFiles_to_Image/"; // converted images from pdf document are saved
		final String crpImg = "F:\\Converted_PdfFiles_to_Image\\sample_2.png"; // Image to be cropped
		final String crpFile = "F:\\Converted_PdfFiles_to_Image\\sample_cropped.png"; //Cropped image

		convertToImage(sourceDir, destinationDir, crpImg, crpFile);

	}

	public static void convertToImage(final String sourceDir, final String destinationDir, final String crpImg, final String crpFile) {
		try {
			final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
			final File sourceFile = new File(sourceDir);
			final File destinationFile = new File(destinationDir);
			if (!destinationFile.exists()) {
				destinationFile.mkdir();
				logger.info("Folder Created: " + destinationFile.getAbsolutePath());
			}
			if (sourceFile.exists()) {
				logger.info("Images copied to Folder Location: " + destinationFile.getAbsolutePath());
				final PDDocument document = PDDocument.load(sourceFile);
				final PDFRenderer pdfRenderer = new PDFRenderer(document);

				final int numberOfPages = document.getNumberOfPages();
				logger.info("Total files to be converting: " + numberOfPages);

				final String fileName = sourceFile.getName().replace(".pdf", "");
				final String fileExtension = "png";

				final int dpi = 300;
				{
					File outPutFile;
					BufferedImage bImage;
					for (int i = 0; i < numberOfPages; ++i) {
						outPutFile = new File(destinationDir + fileName + "_" + (i + 1) + "." + fileExtension);
						bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
						ImageIO.write(bImage, fileExtension, outPutFile);
					}
				}
				final BufferedImage image = ImageIO.read(new File(crpImg));

				// crop image
				final BufferedImage croppedImg = image.getSubimage(300, 1800, 1000, 500);

				final File croppedFile = new File(crpFile);

				ImageIO.write(croppedImg, "png", croppedFile);

				document.close();
				logger.info("Converted Images are saved at: " + destinationFile.getAbsolutePath());
			} else {
				logger.info(sourceFile.getName() + " File not exists");
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}