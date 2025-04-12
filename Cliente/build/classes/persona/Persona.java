package persona;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

public class Persona {

    protected String usuario;
            
    protected static int FRAGMENTO = 1024;

    protected static void enviarServidor(String request) {

        try (DatagramSocket socket = new DatagramSocket(5567)) {
            InetAddress ip = InetAddress.getByName("localhost");

            byte[] mensaje = request.getBytes();
            DatagramPacket paquete = new DatagramPacket(mensaje, mensaje.length, ip, 5566);
            socket.send(paquete);

        } catch (IOException e) {
            System.out.println("Error al enviar " + e);
        }
    }

    protected static String recibirServidor() {
        String resultado = "";

        try (DatagramSocket socket = new DatagramSocket(5567)) {
            byte[] buffer = new byte[FRAGMENTO + 2];

            List<byte[]> fragmentosRecibidos = new ArrayList<>();
            boolean mensajeCompleto = false;

            System.out.println("Cliente UDP esperando fragmentos del mensaje...");

            while (!mensajeCompleto) {
                DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                socket.receive(paquete);

                byte numeroSecuencia = paquete.getData()[0];
                byte esUltimoFragmento = paquete.getData()[1];

                byte[] fragmento = new byte[paquete.getLength() - 2];
                System.arraycopy(paquete.getData(), 2, fragmento, 0, fragmento.length);

                fragmentosRecibidos.add(fragmento);

                System.out.println("Recibido fragmento #" + numeroSecuencia);

                if (esUltimoFragmento == 1) {
                    mensajeCompleto = true;
                }
            }

            int tamañoTotal = fragmentosRecibidos.stream().mapToInt(f -> f.length).sum();
            byte[] mensajeCompletoArray = new byte[tamañoTotal];
            int offset = 0;

            for (byte[] fragmento : fragmentosRecibidos) {
                System.arraycopy(fragmento, 0, mensajeCompletoArray, offset, fragmento.length);
                offset += fragmento.length;
            }

            resultado = new String(mensajeCompletoArray);
            System.out.println("Mensaje completo reensamblado: " + resultado);
        } catch (IOException e) {
            System.out.println("Error al recibir " + e);
        }

        return resultado;
    }

    // ------------------------- RF01 ------------------------- //
    public int iniciarSesion(String nombre, String contrasenia) {
        int resultado = 1;

        if (validarUsuario(nombre, contrasenia)) {
            enviarServidor("buscarPerfil@" + nombre + "@" + contrasenia);
            resultado = Integer.parseInt(recibirServidor());
            if(resultado == 3 || resultado == 4){
                usuario = nombre;
            }
        }

        return resultado;
    }

    private static boolean validarUsuario(String nombre, String contrasenia) {
        boolean resultado = false;

        if ((nombre.length() >= 4 && nombre.length() <= 18) && (contrasenia.length() > 8 && contrasenia.length() <= 20)) {
            resultado = true;
        }

        return resultado;
    }

    // ------------------------- RF15 ------------------------- //
    
    public int crearCuenta(String nombre, String contrasenia) {
        int resultado = 0;

        if (validarUsuario(nombre, contrasenia)) {
            
            enviarServidor("registrarPerfil@" + nombre + "@" + contrasenia);
            resultado = Integer.parseInt(recibirServidor());
            if(resultado == 3 || resultado == 4){
                this.usuario = nombre;            
            }
        }

        return resultado;
    }

}
