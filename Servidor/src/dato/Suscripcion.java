package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Arrays;

public class Suscripcion {

    private final static Hashtable<String, Object> bloqueos = new Hashtable<>();

    private static final Object bloquearUsuario(String usuario) {
        synchronized (bloqueos) {
            if (!bloqueos.containsKey(usuario)) {
                bloqueos.put(usuario, new Object());
            }
            return bloqueos.get(usuario);
        }
    }

    private static final Object bloquearUbicacion(String ubicacion) {
        synchronized (bloqueos) {
            if (!bloqueos.containsKey(ubicacion)) {
                bloqueos.put(ubicacion, new Object());
            }
            return bloqueos.get(ubicacion);
        }
    }

    public static void crearArchivo(String usuario) {

        synchronized (bloquearUsuario(usuario)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Suscripciones.txt", true))) {
                bw.write("");
            } catch (IOException e) {
            }
        }

    }

    public static String consultarSuscripciones(String usuario) {

        String linea, contenido = "", resultado = "0";
        String[] ubicaciones = Ubicacion.consultarUbicaciones().split("@");
        LinkedList<String> suscripciones = new LinkedList<>();

        synchronized (bloquearUsuario(usuario)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + usuario + "/Suscripciones.txt"))) {

                while ((linea = br.readLine()) != null) {
                    suscripciones.add(linea);
                }

            } catch (IOException e) {
                System.out.println(e);
            }
        }

        for (String ubicaciond : ubicaciones) {
            String ubicacion = ubicaciond.split(",")[0];
            String suscrito = "0";

            if (suscripciones.contains(ubicacion)) {
                suscrito = "1";
            }

            contenido += ubicacion + "," + suscrito + "@";
        }

        if (!contenido.isEmpty()) {
            resultado = contenido.substring(0, contenido.length() - 1);
        }

        return resultado;

    }

    public static String registrarSuscripcion(String usuario, String ubicacion) {
        String resultado = "1", linea;
        int cantidad = 0;

        synchronized (bloquearUsuario(usuario)) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + usuario + "/Suscripciones.txt"))) {

                while ((linea = br.readLine()) != null) {
                    cantidad++;
                }

            } catch (IOException e) {
                System.out.println(e);
            }

            if (cantidad < Privilegio.suscripcionesMaximas(usuario)) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Suscripciones.txt", true))) {
                    bw.write(ubicacion + "\n");
                    resultado = "2";
                } catch (IOException e) {
                }
                registrarSuscripcionUbicacion(usuario, ubicacion);
            }
        }

        return resultado;
    }

    public static String registrarSuscripcion(String usuario) {
        String resultado = "1";

        synchronized (bloquearUsuario(usuario)) {

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Suscripciones.txt"))) {

                String[] ubicaciones = Ubicacion.consultarUbicaciones().split("@");
                for (String ubicacion : ubicaciones) {
                    bw.write(ubicacion.split(",")[0] + "\n");
                    registrarSuscripcionUbicacion(usuario, ubicacion.split(",")[0]);
                }
                resultado = "2";
            } catch (IOException e) {
            }
        }

        return resultado;
    }

    private static void registrarSuscripcionUbicacion(String usuario, String ubicacion) {
        synchronized (bloquearUbicacion(ubicacion)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Ubicaciones/Suscripciones/" + ubicacion + ".txt", true))) {
                bw.write(usuario + "\n");
            } catch (IOException e) {
            }
        }
    }

    private static void eliminarSuscripcionUbicacion(String usuario, String ubicacion) {
        String linea, contenido = "";
        synchronized (bloquearUbicacion(ubicacion)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Ubicaciones/Suscripciones/" + ubicacion + ".txt"))) {

                while ((linea = br.readLine()) != null) {

                    if (!linea.equals(usuario)) {                        
                        contenido += linea + "\n";
                    }

                }

            } catch (IOException e) {
                System.out.println(e);
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Ubicaciones/Suscripciones/" + ubicacion + ".txt"))) {
                bw.write(contenido);
            } catch (IOException e) {
            }
        }
    }

    public static String eliminarSuscripcion(String usuario, String ubicacion) {
        String resultado = "1", linea, contenido = "";
        int cantidad = 0;

        synchronized (bloquearUsuario(usuario)) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + usuario + "/Suscripciones.txt"))) {

                while ((linea = br.readLine()) != null) {

                    if (!linea.equals(ubicacion)) {
                        cantidad++;
                        contenido += linea + "\n";
                    }

                }

            } catch (IOException e) {
                System.out.println(e);
            }

            if (cantidad < Privilegio.suscripcionesMaximas(usuario)) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Suscripciones.txt"))) {
                    bw.write(contenido);
                    resultado = "2";
                } catch (IOException e) {
                }
                eliminarSuscripcionUbicacion(usuario, ubicacion.split(",")[0]);
            }
        }

        return resultado;
    }

    public static String eliminarSuscripcion(String usuario) {
        String resultado = "1";

        synchronized (bloquearUsuario(usuario)) {

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Suscripciones.txt"))) {
                bw.write("");
                resultado = "2";
            } catch (IOException e) {
            }

            for (String ubicacion : Ubicacion.consultarUbicaciones().split("@")) {
                eliminarSuscripcionUbicacion(usuario, ubicacion.split(",")[0]);
            }

        }

        return resultado;
    }

}
