package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

public class Reporte {

    private final static Object bloqueo = new Object();

    public void registrarReporte(String ID, String ubicacion, double lat, double lon, String temperatura, String humedad, String humo) {

        synchronized (bloqueo) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Sensores/Reportes.txt", true))) {
                bw.write(ID + "," + ubicacion + "," + lat + "," + lon + "," + temperatura + "," + humedad + "," + humo + "\n");
            } catch (IOException e) {
            }
        }

    }

    public static String consultarReportes() {
        String reportes = " ", linea, contenido = "";
        Hashtable<String, String> reportesSensores = new Hashtable<>();

        synchronized (bloqueo) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Sensores/Reportes.txt"))) {
                while ((linea = br.readLine()) != null) {
                    reportesSensores.put(linea.split(",")[0], linea); // Filtrador 1 reporte por sensor (el mas reciente)
                }

            } catch (IOException e) {
            }
        }


        for (String reporte : reportesSensores.keySet()) {
            contenido += reportesSensores.get(reporte) + "@";            
        }
        
        if (!contenido.isEmpty()) {
            reportes = contenido.substring(0, contenido.length() - 1);
        }

        return reportes;
    }

    public static Hashtable<String, Hashtable<String, String>> consultarReportes(String ubicacion) {
        Hashtable<String, Hashtable<String, String>> reportes = new Hashtable<>();

        synchronized (bloqueo) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Sensores/Reportes.txt"))) {
                String linea;

                while ((linea = br.readLine()) != null) {
                    String[] valores = linea.split(",");

                    if (valores[1].equals(ubicacion)) {
                        Hashtable<String, String> info = new Hashtable<>();

                        info.put("latitud", valores[2]);
                        info.put("longitud", valores[3]);
                        info.put("temperatura", valores[4]);
                        info.put("humedad", valores[5]);
                        info.put("humo", valores[6]);

                        reportes.put(valores[0], info);

                    }

                }

            } catch (IOException e) {
            }
        }

        return reportes;
    }
}
