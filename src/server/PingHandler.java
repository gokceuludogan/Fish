/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import database.Database;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.SQLException;

import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gokce
 */
class PingHandler extends Thread{
    private ObjectOutputStream out;
    private BufferedInputStream in;

   
    public PingHandler(Database db) throws SQLException, IOException {
           start(db);
    }
    
    public void start(Database db) throws SQLException, UnknownHostException, IOException {
        Socket clientSocket;
        while (true) {
            try {
                // Wait for 10 second.
                Thread.sleep(10000);
            }
            catch (InterruptedException ex) { 
            }
            if(db.getSharedClients.execute()){
                ResultSet resultSet = db.getSharedClients.executeQuery();
                if(resultSet != null){
                    while (resultSet.next()) {
                        String clientName = resultSet.getString("USERNAME");
                        String IP = resultSet.getString("IP");
                        String port = resultSet.getString("PORT");
                        InetAddress addr = InetAddress.getByName(IP.substring(1));
                        
                        try {
                          
                             clientSocket = new Socket(addr,8888);
                             out = new ObjectOutputStream(clientSocket.getOutputStream());
                             in = new BufferedInputStream(clientSocket.getInputStream());
                             System.out.println("Connected to filedistributor");
                             
                             
                        } catch (IOException ex) {
                            Logger.getLogger(PingHandler.class.getName()).log(Level.SEVERE, null, ex);
                            System.out.println("not available anymore "+clientName); 
                             db.removeOnlineClient.setString(1, clientName);
                             db.removeOnlineClient.setString(2, IP);
                              db.removeOnlineClient.setString(3, port);
                             db.removeOnlineClient.executeUpdate();
                             db.removeAllFilesByClient.setString(1, clientName);
                             db.removeAllFilesByClient.executeUpdate();
                         }
            
                    }
                }
            }
        }   
    }
    
}
