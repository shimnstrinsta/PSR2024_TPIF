package persona;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

public class Persona {

    protected String nombre;    
    protected static int FRAGMENTO = 1024;

    public String getNombre() {
        return nombre;
    }

    protected static void enviarServidor(String request) {

        try (DatagramSocket socket = new DatagramSocket(5567)) {
            InetAddress ip = InetAddress.getByName("localhost");

            byte[] mensaje = request.getBytes();
            DatagramPacket paquete = new DatagramPacket(mensaje, mensaje.length, ip, 5566);
            socket.send(paquete);

        } catch (IOException e) {            
        }
    }

    protected static String recibirServidor() {
        String resultado = "0";
        

        try (DatagramSocket socket = new DatagramSocket(5567)) {
            socket.setSoTimeout(2000);

            byte[] buffer = new byte[FRAGMENTO + 2];
            List<byte[]> fragmentosRecibidos = new ArrayList<>();
            boolean mensajeCompleto = false;

            while (!mensajeCompleto) {
                try {
                    DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                    socket.receive(paquete);

                    byte esUltimoFragmento = paquete.getData()[1];

                    byte[] fragmento = new byte[paquete.getLength() - 2];
                    System.arraycopy(paquete.getData(), 2, fragmento, 0, fragmento.length);

                    fragmentosRecibidos.add(fragmento);

                    if (esUltimoFragmento == 1) {
                        mensajeCompleto = true;
                    }
                } catch (SocketTimeoutException e) {                    
                    break;
                }
            }
            
            if (!fragmentosRecibidos.isEmpty()) {
                int tamañoTotal = fragmentosRecibidos.stream().mapToInt(f -> f.length).sum();
                byte[] mensajeCompletoArray = new byte[tamañoTotal];
                int offset = 0;

                for (byte[] fragmento : fragmentosRecibidos) {
                    System.arraycopy(fragmento, 0, mensajeCompletoArray, offset, fragmento.length);
                    offset += fragmento.length;
                }

                resultado = new String(mensajeCompletoArray);                
            }

        } catch (IOException e) {}

        return resultado;
    }

    // ------------------------- RF01 ------------------------- //
    public int iniciarSesion(String nombre, String contrasenia) {
        int resultado = 1;

        if (validarUsuario(nombre, contrasenia)) {
            enviarServidor("buscarPerfil@" + nombre + "@" + contrasenia);
            resultado = Integer.parseInt(recibirServidor());
            if (resultado == 3 || resultado == 4) {
                this.nombre = nombre;
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

    // ------------------------- RF02 ------------------------- //
    public static LinkedList<Hashtable<String, String>> seleccionarUbicaciones() {
        LinkedList<Hashtable<String, String>> ubicaciones = new LinkedList<>();

        enviarServidor("consultarUbicaciones");
        String resultado_servidor = recibirServidor();

        if (!resultado_servidor.equals("0")) {
            for (String ubicacion_string : resultado_servidor.split("@")) {
                String[] valores = ubicacion_string.split(",");

                Hashtable<String, String> ubicacion = new Hashtable<>();

                ubicacion.put("Nombre", valores[0]);
                ubicacion.put("LiLat", valores[1]);
                ubicacion.put("LsLat", valores[2]);
                ubicacion.put("LiLon", valores[3]);
                ubicacion.put("LsLon", valores[4]);

                ubicaciones.add(ubicacion);
            }
        }

        return ubicaciones;
    }

    // ------------------------- RF15 ------------------------- //
    public int crearCuenta(String nombre, String contrasenia) {
        int resultado = 0;

        if (validarUsuario(nombre, contrasenia)) {

            enviarServidor("registrarPerfil@" + nombre + "@" + contrasenia);
            resultado = Integer.parseInt(recibirServidor());
            if (resultado == 3 || resultado == 4) {
                this.nombre = nombre;
            }
        }

        return resultado;
    }

}
