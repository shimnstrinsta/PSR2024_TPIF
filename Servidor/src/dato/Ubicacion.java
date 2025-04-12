package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

public class Ubicacion {

    private final static Object bloqueo = new Object();

    public static String consultarUbicaciones() {
        String resultado = "0";

        synchronized (bloqueo) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Ubicaciones/Ubicaciones.txt"))) {
                String linea, contenido = "";

                while ((linea = br.readLine()) != null) {
                    contenido += linea + "@";
                }

                if (!contenido.isEmpty()) {
                    resultado = contenido.substring(0, contenido.length() - 1);
                }

            } catch (IOException e) {
            }
        }

        return resultado;
    }

    public static String registrarUbicacion(String ubicacion, double li_latitud, double ls_latitud, double li_longitud, double ls_longitud) {
        String resultado = "1";

        synchronized (bloqueo) {
            if (validarUbicacion(ubicacion)) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Ubicaciones/Ubicaciones.txt", true))) {
                    bw.write(ubicacion + "," + ls_latitud + "," + li_latitud + "," + ls_longitud + "," + li_longitud + "\n");
                } catch (IOException e) {
                }
                resultado = "2";

                try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Ubicaciones/Suscripciones/"+ubicacion+".txt"))) {
                    bw.write("");
                } catch (IOException e) {
                }

            }
        }

        return resultado;

    }

    private static boolean validarUbicacion(String ubicacion) {
        boolean resultado = true;
        LinkedList<Hashtable<String, String>> ubicaciones = new LinkedList<>();

        for (String ubicacion_string : consultarUbicaciones().split("@")) {
            String[] valores = ubicacion_string.split(",");

            Hashtable<String, String> ubicacionHS = new Hashtable<>();

            ubicacionHS.put("Nombre", valores[0]);
            ubicacionHS.put("LiLat", valores[1]);
            ubicacionHS.put("LsLat", valores[2]);
            ubicacionHS.put("LiLon", valores[3]);
            ubicacionHS.put("LsLon", valores[4]);

            ubicaciones.add(ubicacionHS);
        }

        int i = 0;

        while (resultado && i < ubicaciones.size()) {
            if (ubicaciones.get(i).get("Nombre").equals(ubicacion)) {
                resultado = false;
            }
            i++;
        }

        return resultado;
    }
}
