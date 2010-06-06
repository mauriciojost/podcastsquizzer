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

    public static Image scale(Image src, int width, int height) {
        long start = System.currentTimeMillis();
        int scanline = src.getWidth();
        int srcw = src.getWidth();
        int srch = src.getHeight();
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
    }

}
