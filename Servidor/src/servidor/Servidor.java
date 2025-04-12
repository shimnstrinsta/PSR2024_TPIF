package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class Servidor {

    public static void main(String[] args) throws IOException{
        DatagramSocket socket = new DatagramSocket(5566);
                        
        byte[] mensaje = new byte[1024];
        
        while (true) {                    
            DatagramPacket paquete = new DatagramPacket(mensaje,mensaje.length);
            socket.receive(paquete);                        
            new Thread(new Peticion(paquete,socket)).start();
            
        }
        
        
    }

}
