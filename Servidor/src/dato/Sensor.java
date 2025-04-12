package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.Random;

public class Sensor {

    private final static Object bloqueo = new Object();

    private static void notificarCambio(String cambio, Object[] valores) {

        System.out.println("Notifico al sensor de cambio: " + cambio);

        for (Object valor : valores) {
            cambio += "," + valor;
        }

        try (DatagramSocket socket = new DatagramSocket(9900)) {
            InetAddress ip = InetAddress.getByName("localhost");

            byte[] mensaje = cambio.getBytes();
            DatagramPacket paquete = new DatagramPacket(mensaje, mensaje.length, ip, 6600);
            socket.send(paquete);

        } catch (IOException e) {
            System.out.println("Error al enviar " + e);
        }
    }

    public static String consultarSensores() {
        String sensores = " ";

        synchronized (bloqueo) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Sensores/Sensores.txt"))) {
                String linea, contenido = "";

                while ((linea = br.readLine()) != null) {
                    contenido += linea + "@";
                }

                if (!contenido.isEmpty()) {
                    sensores = contenido.substring(0, contenido.length() - 1);
                }

            } catch (IOException e) {
                System.out.println("Error "+e);
            }
        }

        return sensores;
    }

    public static String registrarSensor(String ubicacion, double latitud, double longitud, int intervalo, String estado_s) {
        String resultado = "0";
        int cantidad = 1;

        synchronized (bloqueo) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Sensores/Sensores.txt"))) {
                String linea;

                while ((linea = br.readLine()) != null) {
                    cantidad++;
                }
            } catch (IOException e) {
                System.out.println(e);
            }

            String ID = "#" + new Random().nextInt(1000) * cantidad;
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Sensores/Sensores.txt", true))) {
                bw.write(ID + "," + intervalo + "," + latitud + "," + longitud + "," + ubicacion + "," + estado_s + "\n");
                resultado = "1";
            } catch (IOException e) {
                System.out.println(e);
            }

            if (resultado.equals("1")) {
                String[] valores = {ID, String.valueOf(intervalo), String.valueOf(latitud), String.valueOf(longitud), ubicacion, estado_s};
                notificarCambio("1", valores);
            }
        }

        return resultado;
    }

    public static String eliminarSensor(String id) {
        String resultado = "0", linea, contenido = "";

        synchronized (bloqueo) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Sensores/Sensores.txt"))) {

                while ((linea = br.readLine()) != null) {

                    if (!linea.split(",")[0].equals(id)) {
                        contenido += linea + "\n";
                    }

                }

            } catch (IOException e) {
                System.out.println(e);
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Sensores/Sensores.txt"))) {
                bw.write(contenido);
                resultado = "1";
            } catch (IOException e) {
            }
            String[] valores = {id};
            notificarCambio("2", valores);

        }

        return resultado;
    }

    public static String modificarSensor(String id, String ubicacion, double latitud, double longitud, int intervalo, String estado) {
        String resultado = "0", linea, contenido = "";

        synchronized (bloqueo) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Sensores/Sensores.txt"))) {

                while ((linea = br.readLine()) != null) {

                    if (!linea.split(",")[0].equals(id)) {
                        contenido += linea + "\n";
                    } else {
                        contenido += id + "," + intervalo + "," + latitud + "," + longitud + "," + ubicacion + "," + estado + "\n";
                    }

                }

            } catch (IOException e) {
                System.out.println(e);
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Sensores/Sensores.txt"))) {
                bw.write(contenido);
                resultado = "1";
            } catch (IOException e) {
            }

            Object[] valores = {id, ubicacion, latitud, longitud, intervalo, estado};
            notificarCambio("3", valores);
        }

        return resultado;
    }

}
