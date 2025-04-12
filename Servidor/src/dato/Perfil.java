package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

public class Perfil {

    private final static Object bloqueo = new Object();

    private final static Hashtable<String, Object> bloqueos = new Hashtable<>();

    private static final Object bloquear(String usuario) {
        synchronized (bloqueos) {
            if (!bloqueos.containsKey(usuario)) {
                bloqueos.put(usuario, new Object());
            }
            return bloqueos.get(usuario);
        }
    }

    public String buscarPerfil(String nombre, String contrasenia) {
        String resultado = "2";
        synchronized (bloqueo) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/Usuarios.txt"))) {

                String linea;
                while ((linea = br.readLine()) != null) {
                    String palabras[] = linea.split(",");
                    if (palabras[0].equals(nombre) && palabras[1].equals(contrasenia)) {
                        resultado = String.valueOf(Privilegio.rolUsuario(nombre));
                    }
                }
            } catch (IOException e) {
            }
        }

        return resultado;
    }

    private boolean buscarPerfil(String nombre) {
        boolean encontro = false;
        synchronized (bloqueo) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/Usuarios.txt"))) {

                String linea;
                while ((linea = br.readLine()) != null && !encontro) {
                    String palabras[] = linea.split(",");
                    if (palabras[0].equals(nombre)) {
                        encontro = true;
                    }
                }
            } catch (IOException e) {
            }
        }

        return encontro;
    }

    public String registrarPerfil(String nombre, String contrasenia) {
        String resultado;

        if (!buscarPerfil(nombre)) {

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/Usuarios.txt", true))) {
                bw.write(nombre + "," + contrasenia + "\n");
            } catch (IOException e) {
            }

            synchronized (bloquear(nombre)) {

                File carpeta = new File("./Datos/Usuarios/" + nombre);
                carpeta.mkdir();

                Suscripcion.crearArchivo(nombre);
                Notificacion.crearArchivo(nombre);
                Privilegio.crearPrivilegios(nombre);
                Configuracion.crearConfiguracion(nombre);
                resultado = "3";
            }
        } else {
            resultado = "2";
        }

        return resultado;
    }

    public String consultarPerfiles() {
        String perfiles = " ";

        synchronized (bloqueo) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/Usuarios.txt"))) {
                String linea, contenido = "";

                while ((linea = br.readLine()) != null) {
                    String usuario = linea.split(",")[0];
                    contenido += linea + "," + Privilegio.rolUsuario(usuario) + "," + Privilegio.tiempoEntreDenuncias(usuario) + "," + Privilegio.suscripcionesMaximas(usuario) + "@";
                }

                if (!contenido.isEmpty()) {
                    perfiles = contenido.substring(0, contenido.length() - 1);
                }

            } catch (IOException e) {
            }
        }

        return perfiles;
    }

    public static LinkedList<String> consultarPerfilesUsuarios() {
        LinkedList<String> usuarios = new LinkedList<>();

        synchronized (bloqueo) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/Usuarios.txt"))) {
                String linea;

                while ((linea = br.readLine()) != null) {
                    String usuario = linea.split(",")[0];     
                    if(Privilegio.rolUsuario(usuario) == 3){
                        usuarios.add(usuario);
                    }
                }

            } catch (IOException e) {
            }
        }

        return usuarios;
    }
    
}
