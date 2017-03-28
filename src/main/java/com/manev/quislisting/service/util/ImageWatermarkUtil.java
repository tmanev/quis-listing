package com.manev.quislisting.service.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageWatermarkUtil {

    /**
     * Embeds a textual watermark over a source image to produce
     * a watermarked one.
     *
     * @param text            The text to be embedded as watermark.
     * @param sourceImageFile The source image file.
     * @param destImageFile   The output image file.
     */
    static void addTextWatermark(String text, File sourceImageFile, File destImageFile) {
        try {
            BufferedImage sourceImage = ImageIO.read(sourceImageFile);
            Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();

            // initializes necessary graphic properties
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);
            g2d.setComposite(alphaChannel);
            g2d.setColor(Color.BLUE);
            g2d.setFont(new Font("Arial", Font.BOLD, 64));
            FontMetrics fontMetrics = g2d.getFontMetrics();
            Rectangle2D rect = fontMetrics.getStringBounds(text, g2d);

            // calculates the coordinate where the String is painted
            int centerX = (sourceImage.getWidth() - (int) rect.getWidth()) / 2;
            int centerY = sourceImage.getHeight() / 2;

            // paints the textual watermark
            g2d.drawString(text, centerX, centerY);

            ImageIO.write(sourceImage, "png", destImageFile);
            g2d.dispose();

            System.out.println("The tex watermark is added to the image.");

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    /**
     * Embeds an image watermark over a source image to produce
     * a watermarked one.
     *
     * @param watermarkImageFile The image file used as the watermark.
     * @param sourceImageFile    The source image file.
     * @param destImageFile      The output image file.
     */
    public static void addImageWatermark(File watermarkImageFile, File sourceImageFile, File destImageFile) {
        try {
            BufferedImage sourceImage = ImageIO.read(sourceImageFile);
            BufferedImage watermarkImage = ImageIO.read(watermarkImageFile);

            // initializes necessary graphic properties
            Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
            g2d.setComposite(alphaChannel);

            // calculates the coordinate where the image is painted
            int topLeftX = (sourceImage.getWidth() - watermarkImage.getWidth()) / 2;
            int topLeftY = (sourceImage.getHeight() - watermarkImage.getHeight()) / 2;

            // paints the image watermark
            g2d.drawImage(watermarkImage, topLeftX, topLeftY, null);

            ImageIO.write(sourceImage, "jpg", destImageFile);
            g2d.dispose();

            System.out.println("The image watermark is added to the image.");

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private static BufferedImage createOptimalImage(BufferedImage src) {
        return createOptimalImage(src, src.getWidth(), src.getHeight());
    }

    private static BufferedImage createOptimalImage(BufferedImage src,
                                                    int width, int height) throws IllegalArgumentException {
        if (width < 0 || height < 0)
            throw new IllegalArgumentException("width [" + width
                    + "] and height [" + height + "] must be >= 0");

        return new BufferedImage(
                width,
                height,
                (src.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB
                        : BufferedImage.TYPE_INT_ARGB));
    }

    public static BufferedImage addImageWatermark(InputStream watermarkImageFile, InputStream sourceImageFile) throws IOException {
        BufferedImage sourceImage = ImageIO.read(sourceImageFile);
        BufferedImage watermarkImage = ImageIO.read(watermarkImageFile);

        // initializes necessary graphic properties
        Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
        g2d.setComposite(alphaChannel);

        // calculates the coordinate where the image is painted
        int topLeftX = (sourceImage.getWidth() - watermarkImage.getWidth()) / 2;
        int topLeftY = (sourceImage.getHeight() - watermarkImage.getHeight()) / 2;

        // paints the image watermark
        g2d.drawImage(watermarkImage, topLeftX, topLeftY, null);

        g2d.dispose();

        System.out.println("The image watermark is added to the image.");

        return sourceImage;
    }

//    public static void main(String[] args) {
//        File sourceImageFile = new File("d:/seo-for-small-business.jpg");
//        File destImageFile = new File("d:/image_watermarked.png");
//        addTextWatermark("CodeJava", sourceImageFile, destImageFile);
//
//        destImageFile = new File("d:/image_watermarked2.png");
//        File watermarkImageFile = new File("d:/ql-logo-01-119x118.png");
//        addImageWatermark(watermarkImageFile, sourceImageFile, destImageFile);
//    }

}
