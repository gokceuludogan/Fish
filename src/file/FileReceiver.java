package file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Description of FileReceiver class:<br> This class is used to receive files
 * to a given location (downloaded_folder) over a
 * given Socket. 
 *
 * @author Gökçe Uludoğan
 *
 * 
 */
public class FileReceiver extends Thread {

    /**
     * 
     */
    private String IP, fileName;
    private int port, size;
    /*
     * @param IP The IP Address from where the file will be retrieved.
     * @param port The port from where the file will be retrieved.
     * @param fileName The name of the file with the extension.
     * @param size The size of the file in Bytes.
     */

    /**
     *
     * @param IP
     * @param port
     * @param fileName
     * @param size
     */
    public FileReceiver(String IP, int port, String fileName, int size) {
        this.IP = IP;
        this.port = port;
        this.size = size;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        //Connect to Server
        ObjectInputStream in = null;
        BufferedOutputStream out = null;

        Socket clientSocket = null;
        try {
            // Try to connect to the other client.
            System.out.println("Connecting...");
            InetAddress addr = InetAddress.getByName("localhost");
            clientSocket = new Socket(addr, port);
            System.out.println("Connected to " + IP + ":" + port);
            try {
                //Initialize Communication Buffers
                //System.out.println("here");
                System.out.println(fileName);
                in = new ObjectInputStream(clientSocket.getInputStream());
                out = new BufferedOutputStream(clientSocket.getOutputStream());
                
                // Initiating values:
                long start = System.currentTimeMillis();
                
                int bytesRead = 0, counter = 0;

                //Send the name of the file it will download:
                byte[] toServer = fileName.getBytes();
                System.out.println(toServer);
                out.write(toServer.length);
                out.flush();
                out.write(toServer, 0, toServer.length);
                out.flush();

                //Initialise a fileOutputStream to write the received bytes to the local disk.
                //System.out.println("/home/gokce/NetBeansProjects/Fish/DownloadedFolder/" + fileName);
                FileOutputStream fos = new FileOutputStream(new File("/home/gokce/NetBeansProjects/Fish/DownloadedFolder/" + fileName));
                int size=in.read();
                byte[] buffer = new byte[size];
                //Read the file from the BufferedInputStream and write to the local file.

                bytesRead=in.read(buffer);
                fos.write(buffer,0,bytesRead);
                fos.flush();
                long end = System.currentTimeMillis();
                System.out.println("Total Bytes read: " + size);
                System.out.println("File was successfully downloaded in: " + (end - start) + " Milisecconds.");
                out.close();
                fos.close();
                clientSocket.close();
                System.out.println("closed");
            } catch (IOException e) {
                System.err.println(e.toString());
                System.exit(1);
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + IP + ":" + port);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + IP + ":" + port);
        }
    }
}