/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package canvaspackage;

import javax.microedition.lcdui.Image;

/**
 *
 * @author Mauricio
 */
public class ImageServices {

    public static Image nothing(Image src, int width, int height){
        return src;
    }
    
    public static Image scale(Image src, int width, int height) {
        long start = System.currentTimeMillis();
        int scanline = src.getWidth();
        int srcw = src.getWidth();
        int srch = src.getHeight();
        try{
            int buf[] = new int[srcw * srch];

            src.getRGB(buf, 0, scanline, 0, 0, srcw, srch);

            int buf2[] = new int[width*height];

            for (int y=0;y<height;y++){
                int c1 = y*width;
                int c2 = (y*srch/height)*scanline;

                for (int x=0;x<width;x++){
                    buf2[c1 + x] = buf[c2 + x*srcw/width];
                }
            }

            Image img = Image.createRGBImage(buf2, width, height, true);
            long end = System.currentTimeMillis();
            System.out.println("Scaled "+src.getWidth()+"x"+src.getHeight()+" in "+((end-start)/1000)+" seconds");
            return img;
        }catch(Exception e){
            return src;
        }
    }

    public static Image scaleImage(Image original, int newWidth, int newHeight)
    {        
        int[] rawInput = new int[original.getHeight() * original.getWidth()];
        original.getRGB(rawInput, 0, original.getWidth(), 0, 0, original.getWidth(), original.getHeight());
        
        int[] rawOutput = new int[newWidth*newHeight];        

        // YD compensates for the x loop by subtracting the width back out
        int YD = (original.getHeight() / newHeight) * original.getWidth() - original.getWidth(); 
        int YR = original.getHeight() % newHeight;
        int XD = original.getWidth() / newWidth;
        int XR = original.getWidth() % newWidth;        
        int outOffset= 0;
        int inOffset=  0;
        
        for (int y= newHeight, YE= 0; y > 0; y--) {            
            for (int x= newWidth, XE= 0; x > 0; x--) {
                rawOutput[outOffset++]= rawInput[inOffset];
                inOffset+=XD;
                XE+=XR;
                if (XE >= newWidth) {
                    XE-= newWidth;
                    inOffset++;
                }
            }            
            inOffset+= YD;
            YE+= YR;
            if (YE >= newHeight) {
                YE -= newHeight;     
                inOffset+=original.getWidth();
            }
        }               
        return Image.createRGBImage(rawOutput, newWidth, newHeight, false);        
    }
    
    public static Image resampleImage(Image orgImage, int newWidth, int newHeight) {

		int orgWidth = orgImage.getWidth();
		int orgHeight = orgImage.getHeight();
		int orgLength = orgWidth * orgHeight;
		int orgMax = orgLength - 1;

		int[] rawInput = new int[orgLength];
		orgImage.getRGB(rawInput, 0, orgWidth, 0, 0, orgWidth, orgHeight);

		int newLength = newWidth * newHeight;

		int[] rawOutput = new int[newLength];

		int yd = (orgHeight / newHeight - 1) * orgWidth;
		int yr = orgHeight % newHeight;
		int xd = orgWidth / newWidth;
		int xr = orgWidth % newWidth;
		int outOffset = 0;
		int inOffset = 0;

		// Whole pile of non array variables for the loop.
		int pixelA, pixelB, pixelC, pixelD;
		int xo, yo;
		int weightA, weightB, weightC, weightD;
		int redA, redB, redC, redD;
		int greenA, greenB, greenC, greenD;
		int blueA, blueB, blueC, blueD;
		int red, green, blue;

		for (int y = newHeight, ye = 0; y > 0; y--) {
			for (int x = newWidth, xe = 0; x > 0; x--) {

				// Set source pixels.
				pixelA = inOffset;
				pixelB = pixelA + 1;
				pixelC = pixelA + orgWidth;
				pixelD = pixelC + 1;

				// Get pixel values from array for speed, avoiding overflow.
				pixelA = rawInput[pixelA];
				pixelB = pixelB > orgMax ? pixelA : rawInput[pixelB];
				pixelC = pixelC > orgMax ? pixelA : rawInput[pixelC];
				pixelD = pixelD > orgMax ? pixelB : rawInput[pixelD];

				// Calculate pixel weights from error values xe & ye.
				xo = (xe << 8) / newWidth;
				yo = (ye << 8) / newHeight;
				weightD = xo * yo;
				weightC = (yo << 8) - weightD;
				weightB = (xo << 8) - weightD;
				weightA = 0x10000 - weightB - weightC - weightD;

				// Isolate colour channels.
				redA = pixelA >> 16;
				redB = pixelB >> 16;
				redC = pixelC >> 16;
				redD = pixelD >> 16;
				greenA = pixelA & 0x00FF00;
				greenB = pixelB & 0x00FF00;
				greenC = pixelC & 0x00FF00;
				greenD = pixelD & 0x00FF00;
				blueA = pixelA & 0x0000FF;
				blueB = pixelB & 0x0000FF;
				blueC = pixelC & 0x0000FF;
				blueD = pixelD & 0x0000FF;

				// Calculate new pixels colour and mask.
				red = 0x00FF0000 & (redA * weightA + redB * weightB + redC * weightC + redD * weightD);
				green = 0xFF000000 & (greenA * weightA + greenB * weightB + greenC * weightC + greenD * weightD);
				blue = 0x00FF0000 & (blueA * weightA + blueB * weightB + blueC * weightC + blueD * weightD);

				// Store pixel in output buffer and increment offset.
				rawOutput[outOffset++] = red + (((green | blue) >> 16));

				// Increment input by x delta.
				inOffset += xd;

				// Correct if we have a roll over error.
				xe += xr;
				if (xe >= newWidth) {
					xe -= newWidth;
					inOffset++;
				}
			}

			// Increment input by y delta.
			inOffset += yd;

			// Correct if we have a roll over error.
			ye += yr;
			if (ye >= newHeight) {
				ye -= newHeight;
				inOffset += orgWidth;
			}
		}
		return Image.createRGBImage(rawOutput, newWidth, newHeight, false);
	}
}
