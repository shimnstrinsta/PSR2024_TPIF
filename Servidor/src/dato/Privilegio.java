package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class Privilegio {

    private final static Hashtable<String, Object> bloqueos = new Hashtable<>();

    private static final Object bloquearUsuario(String usuario) {
        synchronized (bloqueos) {
            if (!bloqueos.containsKey(usuario)) {
                bloqueos.put(usuario, new Object());
            }
            return bloqueos.get(usuario);
        }
    }

    public static int suscripcionesMaximas(String usuario) {
        int suscripciones = 0;
        synchronized (bloquearUsuario(usuario)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + usuario + "/Privilegios.txt"))) {
                br.readLine();
                suscripciones = Integer.parseInt(br.readLine().split(",")[1]);
            } catch (IOException e) {
            }
        }

        return suscripciones;
    }

    public static int tiempoEntreDenuncias(String usuario) {
        int tiempo = 0;
        synchronized (bloquearUsuario(usuario)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + usuario + "/Privilegios.txt"))) {
                tiempo = Integer.parseInt(br.readLine().split(",")[1]);
            } catch (IOException e) {
            }
        }

        return tiempo;
    }

    public static int rolUsuario(String usuario) {
        int rol = 0;
        synchronized (bloquearUsuario(usuario)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + usuario + "/Privilegios.txt"))) {
                br.readLine();
                br.readLine();
                rol = Integer.parseInt(br.readLine().split(",")[1]);
            } catch (IOException e) {
            }
        }

        return rol;
    }

    public static String configurarPrivilegios(String usuario, int rol, int tiempo, int suscripciones) {
        String resultado = "0";        
        
        synchronized (bloquearUsuario(usuario)) {

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Privilegios.txt"))) {
                bw.write("Tiempo entre denuncias," + tiempo + "\n");
                bw.write("Suscripciones máximas," + suscripciones + "\n");
                bw.write("Rol," + rol);
                resultado = "1";
            } catch (IOException e) {
            }
        }

        return resultado;
    }

    public static void crearPrivilegios(String usuario) {

        String[] parametros = Parametro.consultarParametros().split("@");
        
        
        synchronized (bloquearUsuario(usuario)) {

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Privilegios.txt"))) {                
                bw.write("Tiempo entre denuncias," + parametros[1].split(",")[1] + "\n");
                bw.write("Suscripciones máximas," + parametros[2].split(",")[1] + "\n");
                bw.write("Rol,3");
            } catch (IOException e) {
            }
        }
    }

}
