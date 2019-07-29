/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc.samples;

import ca.weblite.objc.Client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author shannah
 */
public class BringToFrontSample extends JFrame {
    ServerSocket sock;
    
    
    BringToFrontSample() {
        initUI();
    }
    private void initUI() {
        setMinimumSize(new Dimension(640, 480));
        setLayout(new BorderLayout());
        add(new JLabel("Test Bring to Front"), BorderLayout.CENTER);
        
    }
    
    private void listen() throws IOException {
        sock = new ServerSocket(0);
        System.out.println("Listening on port "+sock.getLocalPort());
        while (true) {
            Socket s = sock.accept();
            System.out.println("Connection received");
            Client c = Client.getInstance();
            c.sendProxy("NSApplication", "sharedApplication").send("activateIgnoringOtherApps:", Boolean.TRUE);
            s.close();
            
        }
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        EventQueue.invokeLater(()->{
            BringToFrontSample sample = new BringToFrontSample();
            sample.pack();
            sample.setVisible(true);
            new Thread(()->{
                try {
                    sample.listen();
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }).start();
        });
        
    }
    
}
