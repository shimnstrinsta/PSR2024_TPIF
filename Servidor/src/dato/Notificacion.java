package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.LinkedList;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.io.IOException;
import java.net.InetAddress;

public class Notificacion {

    private final static Hashtable<String, Object> bloqueos = new Hashtable<>();

    private static final Object bloquear(String usuario) {
        synchronized (bloqueos) {
            if (!bloqueos.containsKey(usuario)) {
                bloqueos.put(usuario, new Object());
            }
            return bloqueos.get(usuario);
        }
    }

    public static void crearArchivo(String usuario) {

        synchronized (bloquear(usuario)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Notificaciones.txt", true))) {
                bw.write("");
            } catch (IOException e) {
            }
        }

    }

    public static void registrarNotificacionAlerta(LinkedList<String> usuariosReportar, Hashtable<String, Hashtable<String, String>> reportes, String ubicacion) {
        String usuarios_s = "";

        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        for (String usuario : usuariosReportar) {
            synchronized (bloquear(usuario)) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Notificaciones.txt", true))) {

                    for (String reporte : reportes.keySet()) {
                        bw.write(fecha + "_" + hora + "_" + ubicacion + "_" + reportes.get(reporte).get("latitud") + "_" + reportes.get(reporte).get("longitud") + "_" + reportes.get(reporte).get("temperatura") + "_" + reportes.get(reporte).get("humedad") + "\n");
                    }

                } catch (IOException e) {
                }
            }
            String aviso = Configuracion.consultarConfiguracion(usuario).split("@")[3].split(",")[1];
            usuarios_s += usuario + "=" + aviso + "@";
        }


        // Notificacion
        String mensaje_s = usuarios_s + "," + ubicacion;
        try {
            MulticastSocket enviador = new MulticastSocket();
            byte[] mensaje = new byte[1024];

            mensaje = mensaje_s.getBytes();

            enviador.send(new DatagramPacket(mensaje, mensaje.length, InetAddress.getByName("230.0.0.1"), 8080));
        } catch (IOException e) {
        }


    }

    public static String consultarAlertas(String usuario) {
        String alertas = " ";

        synchronized (bloquear(usuario)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/"+usuario+"/Notificaciones.txt"))) {
                String linea, contenido = "";

                while ((linea = br.readLine()) != null) {
                    contenido += linea + "@";
                }

                if (!contenido.isEmpty()) {
                    alertas = contenido.substring(0, contenido.length() - 1);
                }

            } catch (IOException e) {
            }
        }

        return alertas;
    }
}
