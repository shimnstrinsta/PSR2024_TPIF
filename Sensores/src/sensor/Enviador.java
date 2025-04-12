package sensor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Enviador {
    
    protected static int FRAGMENTO = 1024;
    
    protected void enviarServidor(String request, int puerto) {

        try (DatagramSocket socket = new DatagramSocket(puerto)) {
            InetAddress ip = InetAddress.getByName("localhost");

            byte[] mensaje = request.getBytes();
            DatagramPacket paquete = new DatagramPacket(mensaje, mensaje.length, ip, 5566);
            socket.send(paquete);

        } catch (IOException e) {
        }
    }

    protected String recibirServidor(int puerto) {
        String resultado = "0";

        try (DatagramSocket socket = new DatagramSocket(puerto)) {
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

        } catch (IOException e) {
        }

        return resultado;
    }
}
