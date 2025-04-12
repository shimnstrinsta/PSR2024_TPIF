package background;

import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.JOptionPane;
import persona.Usuario;

public class EscuchadorAlerta extends Thread {

    private final Usuario usuario;
    private final int PUERTO = 8080;

    public EscuchadorAlerta(Usuario usuario) {
        this.usuario = usuario;
    }

    private void enviarAlerta(String usuarios, String ubicacion) {
        
        if (!usuarios.equals("")) {
            for (String info_user : usuarios.split("@")) {

                String nombre = info_user.split("=")[0], aviso = info_user.split("=")[1];

                if (this.usuario.getNombre().equals(nombre) && aviso.equals("1")) {
                    JOptionPane.showMessageDialog(null, "Se ha detectado un incendio en " + ubicacion, "Alerta", JOptionPane.WARNING_MESSAGE);
                }

            }
        }

    }

    public void escucharAlerta() {
        try {
            byte[] mensaje = new byte[1024];
            MulticastSocket escucha = new MulticastSocket(PUERTO);
            escucha.joinGroup(InetAddress.getByName("230.0.0.1"));

            DatagramPacket paquete = new DatagramPacket(mensaje, mensaje.length);
            escucha.receive(paquete);

            String alerta = new String(paquete.getData(), 0, paquete.getLength());

            enviarAlerta(alerta.split(",")[0], alerta.split(",")[1]);

        } catch (IOException e) {            
        }
    }

    @Override
    public void run() {

        while (true) {            
            escucharAlerta();
        }

    }
}
