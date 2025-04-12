package dato;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

public class Denuncia {

    private final static Object bloqueo = new Object();

    public String registrarDenuncia(String usuario, String ubicacion, double lat, double lon, String intencionalidad, String descripcion) {
        String resultado = "2", linea;
        boolean aux = true;

        LocalDate ultimaFecha = null;
        LocalTime ultimaHora = null;

        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalTime horaActual = LocalTime.now();
        LocalDate fechaActual = LocalDate.now();
        String usuarioBuscar;

        synchronized (bloqueo) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Ubicaciones/Denuncias.txt"))) {

                while ((linea = br.readLine()) != null) {

                    String[] valores = linea.split("/");
                    usuarioBuscar = valores[0];
                    if (usuarioBuscar.equals(usuario)) {
                        ultimaFecha = LocalDate.parse(valores[2], formatoFecha);
                        ultimaHora = LocalTime.parse(valores[3], formatoHora);                        
                    }

                }

            } catch (IOException e) {
            }
        }

        if (ultimaFecha != null) {

            if (ultimaFecha.equals(fechaActual)) {
                aux = false;
                if (Privilegio.tiempoEntreDenuncias(usuario) <= Duration.between(ultimaHora, horaActual).toMinutes()) {
                    aux = true;
                }
            }
        }

        if (aux) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Ubicaciones/Denuncias.txt", true))) {
                String fecha = fechaActual.format(formatoFecha);
                String hora = horaActual.format(formatoHora);

                bw.write(usuario + "/" + ubicacion + "/" + fecha + "/" + hora + "/" + lat + "/" + lon + "/" + intencionalidad + "/" + descripcion + "\n");
            } catch (IOException e) {
            }
            resultado = "3";
            Alerta.verificarRiesgo(ubicacion);
        }

        return resultado;
    }

    public static String consultarDenuncias(){
        String denuncias = " ";
        
        synchronized (bloqueo) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Ubicaciones/Denuncias.txt"))) {
                String linea, contenido = "";

                while ((linea = br.readLine()) != null) {
                    contenido += linea + "@";
                }

                if (!contenido.isEmpty()) {
                    denuncias = contenido.substring(0, contenido.length() - 1);
                }

            } catch (IOException e) {
            }
        }
        
        return denuncias;
    }
    
    public static int consultarCantidadDenuncias(String ubicacion){
        int cantidad = 0;
        
        synchronized (bloqueo) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Ubicaciones/Denuncias.txt"))) {
                String linea;

                while ((linea = br.readLine()) != null) {
                    if(linea.split("/")[1].equals(ubicacion)){
                        cantidad ++;
                    }
                }


            } catch (IOException e) {
            }
        }
        
        return cantidad;
    }
    
}
