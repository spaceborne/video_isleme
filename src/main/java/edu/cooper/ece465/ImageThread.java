package edu.cooper.ece465;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ImageThread extends Thread {
    private Socket socket = null;

    public ImageThread(Socket socket) {
        super("ImageThread");
        this.socket = socket;
    }

    public void run() {
        try {
            /*  ADIM 1: GORUNTUNUN HISTOGRAMINI OLUSTURMA
            *
            *   Bu adimda kullanilan gorselin her bir pikseli okunur.
            *
            *
            *
            * */




            System.out.println("Processing Image");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            int nbrToRead = in.readInt();
            byte[] byteArray = new byte[nbrToRead];
            int nbrRd = 0;
            int nbrLeftToRead = nbrToRead;
            while (nbrLeftToRead > 0) {
                int rd = in.read(byteArray, nbrRd, nbrLeftToRead);
                if (rd < 0)
                    break;
                nbrRd += rd; /** okuma islemi */
                nbrLeftToRead -= rd;
            }
            //Converting the image
            ByteArrayInputStream byteArrayI = new ByteArrayInputStream(byteArray);
            BufferedImage image = ImageIO.read(byteArrayI);

            System.out.println("Image Read");
            BufferedImage result = Process.computeProcess(image);
            System.out.println("Image Processed");
            ImageIO.write(result, "JPG", out);
            System.out.println("Image Sent");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
