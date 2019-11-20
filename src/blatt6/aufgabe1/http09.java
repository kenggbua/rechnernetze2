package blatt6.aufgabe1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class http09{


    private static ServerSocket serverSocket;
    private static int port;
    private static String address;
    private static boolean running = true;
    Thread runningThread;

    public http09(String[] test) {
        address = test[0];
        port = Integer.parseInt(test[1]);
    }

    public void test() {

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        while (running) {
            Socket socket;
            try {
                socket = serverSocket.accept();
                processRequest(socket);
            } catch (IOException ex) {
                if (!running) {
                    return;
                }
                ex.printStackTrace();
            }
        }
    }

    private static void processRequest(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
        String[] request = reader.readLine().split(" ");
        File file;

        if (request[0].equalsIgnoreCase("GET")) {
            if (request[1].startsWith("/")) {
                request[1] = request[1].substring(1);
            }
            if (request[1].equalsIgnoreCase("")) {
                file = new File(address+"/index.html");
            } else {
                file = new File(address+"/"+request[1]);
            }
            byte[] ba = new byte[(int)file.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.read(ba, 0, ba.length);
            writer.write(ba, 0, ba.length);
            writer.flush();
            socket.close();
        }
    }

    public synchronized void stop() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String[] a = {"/Users/kevinwiltschnig/Desktop/bak/ipam-master/rechnernetze/out/production/rechnernetze/blatt6/aufgabe1/documentRoot", "8080"};
        http09 testi = new http09(a);

        testi.test();


    }

}
