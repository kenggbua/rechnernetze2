package blatt6.aufgabe1.aufgabe1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThread extends Thread {
    private ServerSocket welcomeSocket;
    private Socket socket;
    private BufferedReader reader;
    private DataOutputStream writer;
    private String documentRoot;


    public SingleThread() {
        try {
            welcomeSocket = new ServerSocket(80);
            documentRoot = "/Users/kevinwiltschnig/Desktop/bak/ipam-master/rechnernetze/out/production/rechnernetze/blatt6/aufgabe1/aufgabe1/documentRoot";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                socket = welcomeSocket.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new DataOutputStream(socket.getOutputStream());

                String line = reader.readLine();
                String[] request = line.split(" ");
                System.out.println(request[1]);

                if(request[0].equalsIgnoreCase("GET")) {
                    File file = new File(documentRoot + request[1]);
                    if(file.isDirectory()) {
                        file = new File(documentRoot  +  request[1] + "index.html");
                    }

                    if(file.exists()){
                        byte[] bytes = new byte[(int)file.length()];
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                        bis.read(bytes, 0, bytes.length);
                        writer.write(bytes, 0, bytes.length);
                        writer.flush();
                    }
                }
                socket.close();
            }
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
