package com.manev.quislisting.service.util;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

public class ImageResizeUtil {

    private static Logger log = LoggerFactory.getLogger(ImageResizeUtil.class);

    private ImageResizeUtil() {
        // closing the public constructor
    }

    /**
     * Resizes an image to a specific size and adds black lines in respect to
     * the ratio. The resizing will take care of the original ratio.
     *
     * @param inputImage   - Original input image. Will not be changed.
     * @param resultWidth  - The out coming width
     * @param resultHeight - The out coming height
     * @return - The out coming image
     */
    public static BufferedImage resizeImage(BufferedImage inputImage, int resultWidth, int resultHeight) {
        // first get the width and the height of the image
        int originWidth = inputImage.getWidth();
        int originHeight = inputImage.getHeight();

        // let us check if we have to scale the image
        if (originWidth <= resultWidth && originHeight <= resultHeight) {
            // we don't have to scale the image, just return the origin
            return inputImage;
        }

        // find out which side is the shortest
        int maxSize = 0;
        Scalr.Mode scaleMode = Scalr.Mode.AUTOMATIC;
        if (originHeight > originWidth) {
            // scale to width
            scaleMode = Scalr.Mode.FIT_TO_WIDTH;
            maxSize = resultWidth;
        } else if (originWidth >= originHeight) {
            // check ratio
            double originRatio = (double) originWidth/originHeight;
            double resultRatio = (double) resultWidth/resultHeight;
            if (originRatio < resultRatio) {
                scaleMode = Scalr.Mode.FIT_TO_WIDTH;
                maxSize = resultWidth;
            } else {
                scaleMode = Scalr.Mode.FIT_TO_HEIGHT;
                maxSize = resultHeight;
            }
        }

        // Scale the image to given size
        BufferedImage outputImage = Scalr.resize(inputImage, Scalr.Method.AUTOMATIC,  scaleMode, maxSize);

        try{
            // Crop the image in the center
            outputImage = Scalr.crop(outputImage,
                    Math.abs(outputImage.getWidth() - resultWidth) / 2,
                    Math.abs(outputImage.getHeight() - resultHeight) / 2,
                    resultWidth, resultHeight);
        } catch (IllegalArgumentException ex) {
            log.error("Could not crop image.", ex);
            throw ex;
        }

        // flush both images
        inputImage.flush();
        outputImage.flush();

        // return the final image
        return outputImage;
    }

}
