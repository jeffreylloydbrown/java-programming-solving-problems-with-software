import edu.duke.*;
import java.io.*;

/**
 * Creates color-inverted images (aka "negatives") of pictures the user selects.
 * 
 * @author Jeff Brown
 * @version 1
 */
public class BatchInversions {

    public ImageResource makeInversion (ImageResource inImage) {
        //I made a blank image of the same size
        ImageResource outImage = new ImageResource(inImage.getWidth(), inImage.getHeight());
        //for each pixel in outImage
        for (Pixel pixel: outImage.pixels()) {
            //look at the corresponding pixel in inImage
            Pixel inPixel = inImage.getPixel(pixel.getX(), pixel.getY());
            //The inverted value is 255 - each rgb component of the pixel.
            pixel.setRed(255 - inPixel.getRed());
            pixel.setGreen(255 - inPixel.getGreen());
            pixel.setBlue(255 - inPixel.getBlue());
        }
        //outImage is your answer
        return outImage;
    }

    public void selectAndConvert () {
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            ImageResource inImage = new ImageResource(f);
            ImageResource inverted = makeInversion(inImage);
            String fname = inImage.getFileName();
            inverted.setFileName(f.getParentFile()+"/"+"inverted-"+fname);
            inverted.draw();
            inverted.save();
        }
    }

}  // BatchInversions
