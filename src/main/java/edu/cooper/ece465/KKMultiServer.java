package edu.cooper.ece465;

import java.net.*;
import java.io.*;

public class KKMultiServer {
    public static void main(String[] args) throws IOException {

    if (args.length != 1) {
        System.err.println("Usage: java KKMultiServer <port number>");
        System.exit(1);
    }

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                System.out.println("New Connection!");
                Socket socketdata = serverSocket.accept();
                System.out.println("Socket data received!");
                new ImageThread(socketdata).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
