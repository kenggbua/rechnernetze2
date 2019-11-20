package blatt5.aufgabe2;

import java.io.*;
import java.net.Socket;

public class pop3 {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host,port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        readResponse();
    }

    public void disconnect() throws IOException {
        if (socket != null && socket.isConnected()) {
            socket.close();
            reader.close();
            writer.close();
            System.out.println("Disconnected.");
        } else {
            System.out.println("not connected.");
        }
    }

    public String readResponse() throws IOException {
        String response = reader.readLine();
        if (response.startsWith("-ERR")) {
            System.out.println(response);
        } else {
            System.out.println(response);
        }
        return response;
    }

    public void sendCommand(String command) throws IOException {
        writer.write(command + "\n");
        writer.flush();
        readResponse();
    }



    public static void main(String[] args) {
        pop3 client = new pop3();
        try {
            client.connect("firemail.de", 110);
            client.sendCommand("USER" + "hustlerino@firemail.de");
            client.sendCommand("PASS " + "nilsinils2");

            client.sendCommand("LIST");

            client.sendCommand("RETR 2");
            String response = client.readResponse();
            while (!response.isEmpty()) {
                response = client.readResponse();
            }


            client.sendCommand("qUiT");
            client.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
