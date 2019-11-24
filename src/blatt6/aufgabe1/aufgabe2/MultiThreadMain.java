package blatt6.aufgabe1.aufgabe2;

import blatt6.aufgabe1.aufgabe2.MultiThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadMain {
    private static ServerSocket welcomeSocket;
    private static Socket connectionSocket;

    public static void main(String[] args) {
        try {
            welcomeSocket = new ServerSocket(80);

            while (true) {
                //Wait, on welcoming socket for contact by client
                connectionSocket = welcomeSocket.accept();
                System.out.println("-------------");

                //pro Anfrage eigenen Thread starten
                MultiThread thread;
                thread = new MultiThread(connectionSocket, "/Users/kevinwiltschnig/Desktop/bak/ipam-master/rechnernetze/out/production/rechnernetze/blatt6/aufgabe1/aufgabe1/documentRoot");
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
