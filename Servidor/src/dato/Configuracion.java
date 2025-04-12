package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

public class Configuracion {

    private final static Hashtable<String, Object> bloqueos = new Hashtable<>();

    private static final Object bloquearUsuario(String usuario) {
        synchronized (bloqueos) {
            if (!bloqueos.containsKey(usuario)) {
                bloqueos.put(usuario, new Object());
            }
            return bloqueos.get(usuario);
        }
    }

    public static String consultarConfiguracion(String usuario) {

        String linea, contenido = "";

        synchronized (bloquearUsuario(usuario)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + usuario + "/Configuracion.txt"))) {

                while ((linea = br.readLine()) != null) {
                    contenido += linea + "@";
                }

            } catch (IOException e) {
            }
        }

        return contenido;

    }

    public static String modificarConfiguracion(String usuario, int temperatura, int denuncias, int humedad,int alertas) {

        String resultado = "0";

        synchronized (bloquearUsuario(usuario)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Configuracion.txt"))) {
                bw.write("Cantidad de denuncias para generar alerta," + denuncias + "\n");
                bw.write("Temperatura Crítica para generar alerta," + temperatura + "\n");
                bw.write("Humedad crítica para generar alerta," + humedad + "\n");
                bw.write("Alertas," + alertas);
                resultado = "1";
            } catch (IOException e) {
            }

        }

        return resultado;

    }

    public static void crearConfiguracion(String usuario) {
        
        String[] parametros = Parametro.consultarParametros().split("@");
        synchronized (bloquearUsuario(usuario)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Configuracion.txt"))) {
                bw.write("Cantidad de denuncias para generar alerta," +  parametros[5].split(",")[1] + "\n");
                bw.write("Temperatura Crítica para generar alerta," +  parametros[3].split(",")[1] + "\n");
                bw.write("Humedad crítica para generar alerta," +  parametros[4].split(",")[1] + "\n");
                bw.write("Alertas,1");                
            } catch (IOException e) {
            }

        }
    }
}
