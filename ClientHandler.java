// E/17/005 M.I RISHARD

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler implements Runnable{
    
    InetAddress sourceAddress;
    int port;
    Monitor monitor;

    public ClientHandler(Monitor monitor) { //constructor
        this.monitor = monitor;
       
    }
     
    public void run(){

        try {
            Socket socket = new Socket(monitor.getIp(),monitor.getPort()); //Server create a TCP socket with the client based on the client IP and the Client ID
            System.out.println("Connected");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //input data in the stream is read and then  is stored in a buffer

            while (true) {
                String str = in.readLine(); //bufer is next read line by line

                System.out.println("Message=" + str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }
    
}