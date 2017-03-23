package file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import sun.awt.X11.XConstants;

/**
 * Description of FileDistributor class:<br> This class is used to send files
 * from a given location (shared_folder) over a
 * given serverSocket. The FileDistributor starts by waiting incoming requests
 * to serve files. Incoming requests include file names that exist in the
 * shared_folder. Then FileDistributor reads the requested file from local disk
 * and sends it over the socket to the remote client.
 *
 * @author Gökçe Uludoğan
 *
 * 
 */
public class FileDistributor extends Thread {

    private ServerSocket serverSocket;
    private BufferedInputStream in;
    private ObjectOutputStream out;
    private Socket clientSocket;
    private String shared_folder;
    String fileName = "";

    /**
     * @param serverSocket The socket over which the FileDistributor will listen
     * to requests and sent requested files.
     * @param shared_folder The location in which the requested files are and
     * will be retrieved from.
     * @throws java.io.IOException
     */
    public FileDistributor(ServerSocket serverSocket, String shared_folder) throws IOException {
        this.serverSocket = serverSocket;
        this.shared_folder = shared_folder;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //New client appears:
                clientSocket = serverSocket.accept();
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new BufferedInputStream(clientSocket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(FileDistributor.class.getName()).log(Level.SEVERE, null, ex);
            }
          
            
           
            try {
                int len = (int)in.read(); 
                
                byte[] msg = new byte[len];

                in.read(msg, 0, len);
                //System.out.println("file "+msg);
                fileName = new String(msg).replaceAll("/s", "");

                //Create a new file and directory if that does not exist:
                //String f=fileName.replace("PDF","pdf");
                System.out.println(fileName);

                //File myFile = new File(System.getProperty("user.dir")+"/SharedFolder/"+fileName);
                File myFile =new File(System.getProperty("user.dir")+"/SharedFolder/"+fileName);
                //System.out.println(new File(System.getProperty("user.dir")+"/SharedFolder/file1.txt").exists());
                //System.out.println(System.getProperty("user.dir"));
                if(myFile.exists()){
                     System.out.println("Accepted connection from address: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                

                    //Initialize variables:
                    long fileSize = myFile.length();
                    long completed = 0;
                    int step = 100000;

                    //Start a fileInputStream to read bytes from the requested file.
                    FileInputStream fis = new FileInputStream(myFile);
                    int size = (int)myFile.length();
                    // Start sending the requested file.
                    System.out.println("Start sending file: " + fileName);
                    byte[] buffer;
                    buffer = new byte[size];
                    //while (completed <= fileSize) {

                    fis.read(buffer);
                    out.write(size);
                    out.flush();
                    out.write(buffer);
                        //completed += step;
                    //}
                    out.flush();
                    System.out.println("Sending file " + fileName + " completed successfully!");
                }else{
                    //System.out.println("here");
                    //System.out.println("Break");
                }
    //}
            } catch (IOException ex) {
                Logger.getLogger(FileDistributor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}