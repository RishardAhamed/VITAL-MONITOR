// E/17/005 M.I RISHARD
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

  
public class Gateway 
{
    static Vector<ClientHandler> ar = new Vector<>(); //Vector list to store the threads
    static List <String> list=new ArrayList<String>(); //List to store the MonitorIDs

    static int i = 0;
    public static void main(String[] args) throws IOException
    {
        InetAddress sourceAddress;
        int port;
        int BROADCAST_PORT = 6000;

   
            DatagramSocket ds = new DatagramSocket(BROADCAST_PORT) ;   //initiate a UDP socket to receive the broadcasted packets from the client monitors
            byte[] receive = new byte[65535];                          //Byte array to store the packet data
  
            DatagramPacket DpReceive = null;
          
                
            try {
                
           while (true)
            {
            

                System.out.println("Waiting for a Vital Monitor...");
                
                DpReceive = new DatagramPacket(receive, receive.length);         // create a DatgramPacket to receive the data.
  
                
                ds.receive(DpReceive);                                         //  revieve the data in byte buffer.
                Monitor monitor = convertToMonitorObject(DpReceive.getData()); //data from the packet is converted to monitor object
                port =monitor.getPort();                                       //Local port of the client stored
                sourceAddress = monitor.getIp();                               //Ip Address of the Client is  stored
                System.out.println(sourceAddress);
                System.out.println(port);

                
                if (!list.contains(monitor.getMonitorID())){                 //To prevent duplicate thread creation
                //System.out.println(list);
                ClientHandler mtch = new ClientHandler(monitor);
 
                
                Thread t = new Thread(mtch);                               // Create a new Thread with this object.
                 
                System.out.println("Adding this client to active client list");
     
                
                ar.add(mtch);                                             // add this client to active clients list

                list.add(monitor.getMonitorID());                         //add the monitor ID to the monitor Ids list
                
                //System.out.println(list);
                t.start();                                                // start the thread.
                
                // increment i for new client.
                // i is used for naming only, and can be replaced
                // by any naming scheme
             
                receive = new byte[65535];
                i++;
                }
                
                
                
            }
           } catch (SocketException e) {
            e.printStackTrace();
          }
            finally {
            ds.close();
                System.out.println("Connection closed due to unauthorized entry.\n");
        }
       
        
    
        
          


    }
  
    // A utility method to convert the byte array data into a string representation.

    public static StringBuilder data(byte[] a)      //not used in the code
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }

    
    private static Monitor convertToMonitorObject(byte[] data) { //method to convert  byte array to an object
        Monitor monitor = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream ois = null;
            ois = new ObjectInputStream(bis);
            
            monitor = (Monitor) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return monitor;
    }


    
    
}

