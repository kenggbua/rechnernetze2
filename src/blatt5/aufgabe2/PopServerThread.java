package blatt5.aufgabe2;

import java.io.*;
import java.net.Socket;

public class PopServerThread extends Thread {
    private Socket socket;
    private SampleDataBase database;
    private BufferedReader reader;
    private BufferedWriter writer;
    private enum States {INIT, USEROK, AUTHOK}
    private States state;

    public PopServerThread(Socket socket) throws IOException {
        this.socket = socket;
        database = new SampleDataBase();
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        state = States.INIT;
    }

    private void sendResponse(String response) throws IOException {
        if (socket.isConnected()) {
            writer.write(response + "\n");
            writer.flush();
        } else {
            System.out.println("Error: not connected.");
        }
    }

    public void run() {
        String clientRequest;
        try {
            sendResponse("+OK sample POP3-Server");
            do {
                clientRequest = reader.readLine();
                sendResponse(processCommand(clientRequest));
            } while (!clientRequest.equalsIgnoreCase("QUIT"));
            socket.close();
            writer.close();
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String processCommand(String clientRequest) {
        String[] parts = clientRequest.split(" ");
        switch (parts[0]) {
            case "USER":
                if (state.equals(States.INIT)) {
                    state = States.USEROK;
                    return "+OK please enter password";
                }
                return "-ERR Invalid command";

            case "PASS":
                if (state == States.USEROK) {
                    state = States.AUTHOK;
                    return "+OK mailbox locked and ready";
                }
                return "-ERR Invalid command";

            case "STAT":
                if (state.equals(States.AUTHOK)) {
                    int sizeOfMessages = 0;
                    for (int i = 0; i < SampleDataBase.messages.size(); i++) {
                        sizeOfMessages += SampleDataBase.messages.get(i).getBytes().length;
                    }
                    return "+OK " + SampleDataBase.messages.size() + " " + sizeOfMessages;
                }
                return "-ERR Invalid command";

            case "LIST":
                if(state.equals(States.AUTHOK)){
                    String str = "";
                    for (int i = 0; i < SampleDataBase.messages.size(); i++) {
                        str = str + (i+1) + " " + SampleDataBase.messages.get(i).getBytes().length + "\n";
                    }
                    return "+OK " + SampleDataBase.messages.size() + " messages: \n" + str + ".";
                }

                return "-ERR Invalid command";


            case "RETR":

                if(state.equals(States.AUTHOK)){
                    String str2 = "";
                    try {
                        str2 = SampleDataBase.messages.get(Integer.parseInt(parts[1]) - 1);
                        return "+OK " + SampleDataBase.messages.get(Integer.parseInt(parts[1]) - 1).length() + " Octets\n"
                                + str2 + ".";
                    } catch (Exception ex) {
                        return "-ERR message not found";
                    }
                }
                return "-ERR Invalid command";



            case "DELE":

                if(state.equals(States.AUTHOK)){
                    try {
                        SampleDataBase.messages.remove(Integer.parseInt(parts[1]) -1 );
                        return "+OK message marked for delete";
                    } catch (Exception ex) {
                        return "-ERR message not found";
                    }
                }

                return "-ERR Invalid command";


            case "QUIT":
                return "+OK bye";

            default:
                return "-ERR unknown command";

        }
    }
}
