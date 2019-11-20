package blatt5.aufgabe2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Pop3Server {
    private static ServerSocket serverSocket;
    private static Socket socket;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(110);
            while (true) {
                PopServerThread thread;
                socket = serverSocket.accept();
                thread = new PopServerThread(socket);
                thread.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
