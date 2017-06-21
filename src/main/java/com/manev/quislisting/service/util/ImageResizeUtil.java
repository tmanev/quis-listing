package com.manev.quislisting.service.util;

import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

public class ImageResizeUtil {

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

        // Scale in respect to width or height?
        Scalr.Mode scaleMode;

        // find out which side is the shortest
        int maxSize;
        if (originHeight > originWidth) {
            // scale to width
            scaleMode = Scalr.Mode.FIT_TO_WIDTH;
            maxSize = resultWidth;
        } else {
            scaleMode = Scalr.Mode.FIT_TO_HEIGHT;
            maxSize = resultHeight;
        }

        // Scale the image to given size
        BufferedImage outputImage = Scalr.resize(inputImage, Scalr.Method.AUTOMATIC, scaleMode, maxSize);

        // okay, now let us check that both sides are fitting to our result scaling
        if (scaleMode.equals(Scalr.Mode.FIT_TO_WIDTH) && outputImage.getHeight() > resultHeight) {
            // the height is too large, resize again
            outputImage = Scalr.resize(outputImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, resultHeight);
        } else if (scaleMode.equals(Scalr.Mode.FIT_TO_HEIGHT) && outputImage.getWidth() > resultWidth) {
            // the width is too large, resize again
            outputImage = Scalr.resize(outputImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, resultWidth);
        }

        // flush both images
        inputImage.flush();
        outputImage.flush();

        // return the final image
        return outputImage;
    }

}
