package edu.cooper.ece465;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Process {

    private static BufferedImage original, equalized;

    public static BufferedImage computeProcess(BufferedImage im) {
        return imageprocessing(im);
    }

    private static void writeImage(String output) throws IOException {
        File file = new File(output + ".jpg");
        ImageIO.write(equalized, "jpg", file);
    }

    private static BufferedImage imageprocessing(BufferedImage original) {
        /**  YENI ADIM: HISTOGRAM OLUSTURMA
        *   Noktasal yogunluk donusumunun ilk adimi olan histogram olusturma
        *   isleminde ImageThread class ında pikselleri okunan gorsel burada
        *   histograma donusturulur.
        *
        * */

        int red;
        int green;
        int blue;
        int alpha;
        int newPixel = 0;

        ArrayList<int[]> histLUT = imageprocessingLUT(original);

        BufferedImage imagePro = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();

                red = histLUT.get(0)[red];
                green = histLUT.get(1)[green];
                blue = histLUT.get(2)[blue];

                newPixel = colorToRGB(alpha, red, green, blue);

                imagePro.setRGB(i, j, newPixel);

            }
        }

        return imagePro;
    }
    /**
     * histogram islemlerinin bitisi
     * */

    private static ArrayList<int[]> imageprocessingLUT(BufferedImage input) {
        /**  YENI ADIM: NORMALIZASYON
         *   Kumulatif histogrami olasilik dagilim fonksiyonu gibi kullanabilmemiz
         *   icin normalizasyona gerek duyulur. Bu islem icin gorselin sayisal
         *   deger araliklarini olmasini istedigimiz araliga gore duzenliyoruz.
         *   Bu adımın kontrast germe olarak dusunulebilir.
         *
         * */
        ArrayList<int[]> imageHist = imageCalculation(input);

        ArrayList<int[]> imageLUT = new ArrayList<int[]>();

        int[] r_image = new int[256];
        int[] g_image = new int[256];
        int[] b_image = new int[256];

        for (int i = 0; i < r_image.length; i++) r_image[i] = 0;
        for (int i = 0; i < g_image.length; i++) g_image[i] = 0;
        for (int i = 0; i < b_image.length; i++) b_image[i] = 0;

        long sumr = 0;
        long sumg = 0;
        long sumb = 0;

        float scale_factor = (float) (255.0 / (input.getWidth() * input.getHeight()));

        for (int i = 0; i < r_image.length; i++) {
            sumr += imageHist.get(0)[i];
            int valr = (int) (sumr * scale_factor);
            if (valr > 255) {
                r_image[i] = 255;
            } else r_image[i] = valr;

            sumg += imageHist.get(1)[i];
            int valg = (int) (sumg * scale_factor);
            if (valg > 255) {
                g_image[i] = 255;
            } else g_image[i] = valg;

            sumb += imageHist.get(2)[i];
            int valb = (int) (sumb * scale_factor);
            if (valb > 255) {
                b_image[i] = 255;
            } else b_image[i] = valb;
        }

        imageLUT.add(r_image);
        imageLUT.add(g_image);
        imageLUT.add(b_image);

        return imageLUT;

    }

    public static ArrayList<int[]> imageCalculation(BufferedImage input) {
        /**
         * Normalizasyon adiminda cagirilan fonksiyon
         * */
        int[] r_image = new int[256];
        int[] g_image = new int[256];
        int[] b_image = new int[256];

        for (int i = 0; i < r_image.length; i++) r_image[i] = 0;
        for (int i = 0; i < g_image.length; i++) g_image[i] = 0;
        for (int i = 0; i < b_image.length; i++) b_image[i] = 0;

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {

                int red = new Color(input.getRGB(i, j)).getRed();
                int green = new Color(input.getRGB(i, j)).getGreen();
                int blue = new Color(input.getRGB(i, j)).getBlue();

                // Increase the values of colors
                r_image[red]++;
                g_image[green]++;
                b_image[blue]++;

            }
        }

        ArrayList<int[]> hist = new ArrayList<int[]>();
        hist.add(r_image);
        hist.add(g_image);
        hist.add(b_image);

        return hist;

    }
    /**
    * Normalizasyon islemlerinin bitisi
    * */


    private static int colorToRGB(int alpha, int red, int green, int blue) {
    /**     YENI ADIM: YENI GORSEL OLUSTURMA
    *   colorToRGB fonksiyonunda ise onceki adimlarda elde edilendeğerleri birlestirerek
    *   yeni gorselimizi olusturuyoruz. Java'da ' << ' operatoru bit tabanlı kaydirma
    *   icin kullanilir ve  sagında ki ve solunda ki degerleri 2’lik sayı sistemine
    *   çevirir ve istenildigi kadarıyla saga veya sola kaydırılır.
    *
    * */
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;
        /**
         * yeni gorsel olusturmanin islemlerinin bitisi
         * */
    }

}
