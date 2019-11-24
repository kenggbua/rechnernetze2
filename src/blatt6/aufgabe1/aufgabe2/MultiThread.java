package blatt6.aufgabe1.aufgabe2;

import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Calendar;

public class MultiThread extends Thread {
    private Socket socket;
    private BufferedReader inFromClient;
    private DataOutputStream outToClient;
    private String documentRoot;

    public MultiThread(Socket socket, String documentRoot) {
        try {
            this.socket = socket;
            this.documentRoot = documentRoot;
            inFromClient = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            outToClient = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
             inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             outToClient = new DataOutputStream(socket.getOutputStream());
            String requestLine = inFromClient.readLine();
            if (requestLine != null) {
                String[] request = requestLine.split(" ");
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                System.out.println(ts + " " + requestLine + " " + socket.getInetAddress());
                File file;
                Calendar calendar = Calendar.getInstance();
                String newline = System.getProperty("line.separator");

                if (request[0].equalsIgnoreCase("GET") || request[0].equalsIgnoreCase("HEAD")) {
                    if (request[1].startsWith("/")) {
                        request[1] = request[1].substring(1);
                    }
                    try {
                        if (request[1].equalsIgnoreCase("")) {
                            file = new File(documentRoot + "/index.html");
                        } else {
                            file = new File(documentRoot + "/" + request[1]);
                        }
                        byte[] ba = new byte[(int) file.length()];
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                        bis.read(ba, 0, ba.length);
                        outToClient.writeBytes("HTTP/1.0 200 " + newline);
                        outToClient.writeBytes("Date: " + calendar.getTime() + newline);
                        outToClient.writeBytes("Server: RN_1.0" + newline);
                        outToClient.writeBytes("MIME-Version: 1.0" + newline);
                        outToClient.writeBytes("Last-Modified: " + calendar.getTime() + newline);
                        String filestr = file.getName();
                        String[] filestrarr = filestr.split("[.]");
                        switch (filestrarr[1]) {
                            case "gif":
                                outToClient.writeBytes("Content-Type: image/gif" + newline);
                                break;
                            case "png":
                                outToClient.writeBytes("Content-Type: image/png" + newline);
                                break;
                            case "html":
                                outToClient.writeBytes("Content-Type: text/html" + newline);
                                break;
                        }
                        outToClient.writeBytes("Content-Length: " + ba.length + newline);
                        if (request[0].equalsIgnoreCase("GET")) {
                            outToClient.writeBytes(newline);
                            outToClient.write(ba, 0, ba.length);
                        }
                        outToClient.flush();
                    } catch (FileNotFoundException fex) {
                        outToClient.writeBytes("HTTP/1.0 404 " + newline);
                        outToClient.flush();
                    }
                } else if (request[0].equalsIgnoreCase("POST")) {
                    outToClient.writeBytes("HTTP/1.0 200 " + newline);
                    outToClient.writeBytes("Date: " + calendar.getTime() + newline);
                    outToClient.writeBytes("Server: RN_1.0" + newline);
                    outToClient.writeBytes("MIME-Version: 1.0" + newline);
                    outToClient.writeBytes("Last-Modified: " + calendar.getTime() + newline);
                    outToClient.flush();
                    String line = inFromClient.readLine();
                    while (!line.isEmpty()) {
                        line = inFromClient.readLine();
                    }
                    char[] buf = new char[1000];
                    inFromClient.read(buf);
                    line = String.valueOf(buf);
                    String[] linestr = line.split("&");
                    String[] splitstr1 = linestr[0].split("=");
                    String[] splitstr2 = linestr[1].split("=");
                    String response = "<html><body><p>Received form variable with name ["+splitstr1[0]+"] and value [" +
                            splitstr1[1]+"]</p><p>Received form variable with name ["+splitstr2[0]+"] and value [" +
                            splitstr2[1]+"]</p></body></html>";
                    byte[] ba = response.getBytes();
                    outToClient.writeBytes("Content-Type: text/html" + newline);
                    outToClient.writeBytes("Content-Length: " + ba.length + newline);
                    outToClient.writeBytes(newline);
                    outToClient.write(ba, 0, ba.length);
                } else {
                    outToClient.writeBytes("HTTP/1.0 501 " + newline);
                    outToClient.flush();
                }
                socket.close();
            }
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
