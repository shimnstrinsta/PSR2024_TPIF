package persona;

import java.util.Hashtable;
import java.util.LinkedList;

public class Administrador extends Persona {

    public Administrador(Persona persona) {
        this.nombre = persona.getNombre();
    }

    public LinkedList<Hashtable<String, String>> seleccionarSensores() {
        LinkedList<Hashtable<String, String>> sensores = new LinkedList<>();

        enviarServidor("consultarSensores");
        String sensores_s = recibirServidor();

        if (!sensores_s.equals(" ")) {
            for (String sensor_s : sensores_s.split("@")) {

                Hashtable<String, String> sensor = new Hashtable<>();
                String[] valores = sensor_s.split(",");

                sensor.put("ID", valores[0]);
                sensor.put("intervalo", valores[1]);
                sensor.put("latitud", valores[2]);
                sensor.put("longitud", valores[3]);
                sensor.put("ubicacion", valores[4]);
                sensor.put("estado", valores[5]);

                sensores.add(sensor);

            }
        }

        return sensores;
    }

    public int ingresarSensor(String ubicacion, double latitud, double longitud, int intervalo, String estado_s) {
        int resultado;
        String estado = "0";

        if (estado_s.equals("Encendido")) {
            estado = "1";
        }

        enviarServidor("registrarSensor@" + ubicacion + "@" + latitud + "@" + longitud + "@" + intervalo + "@" + estado);
        resultado = Integer.parseInt(recibirServidor());

        return resultado;
    }

    public int borrarSensor(String id) {
        enviarServidor("eliminarSensor@" + id);
        int resultado = Integer.parseInt(recibirServidor());
        return resultado;
    }

    public int modificarSensor(String id, String ubicacion, double latitud, double longitud, int intervalo, String estado_s) {
        String estado = "0";
        if (estado_s.equals("Encendido")) {
            estado = "1";
        }

        enviarServidor("modificarSensor@" + id + "@" + ubicacion + "@" + latitud + "@" + longitud + "@" + intervalo + "@" + estado);
        int resultado = Integer.parseInt(recibirServidor());
        return resultado;
    }

    public static int tiempoActualizacion() {
        enviarServidor("tiempoActualizacionPanelControl");
        int tiempo = Integer.parseInt(recibirServidor());
        return tiempo;
    }

    public static LinkedList<Hashtable<String, String>> seleccionarReportes() {
        LinkedList<Hashtable<String, String>> reportes = new LinkedList<>();

        enviarServidor("consultarReportes");
        String reportes_s = recibirServidor();

        if (!reportes_s.equals(" ")) {
            for (String sensor_s : reportes_s.split("@")) {

                Hashtable<String, String> reporte = new Hashtable<>();
                String[] valores = sensor_s.split(",");

                reporte.put("sensor", valores[0]);
                reporte.put("ubicacion", valores[1]);
                reporte.put("latitud", valores[2]);
                reporte.put("longitud", valores[3]);
                reporte.put("temperatura", valores[4]);
                reporte.put("humedad", valores[5]);
                reporte.put("humo", valores[6]);

                reportes.add(reporte);

            }
        }

        return reportes;
    }

    public static LinkedList<Hashtable<String, String>> seleccionarDenuncias() {
        LinkedList<Hashtable<String, String>> denuncias = new LinkedList<>();

        enviarServidor("consultarDenuncias");
        String denuncias_s = recibirServidor();

        if (!denuncias_s.equals(" ")) {
            for (String sensor_s : denuncias_s.split("@")) {

                Hashtable<String, String> reporte = new Hashtable<>();
                String[] valores = sensor_s.split("/");

                reporte.put("usuario", valores[0]);
                reporte.put("ubicacion", valores[1]);
                reporte.put("fecha", valores[2]);
                reporte.put("horario", valores[3]);
                reporte.put("latitud", valores[4]);
                reporte.put("longitud", valores[5]);
                reporte.put("intencionalidad", valores[6]);
                reporte.put("descripcion", valores[7]);

                denuncias.add(reporte);

            }
        }

        return denuncias;
    }

    public static LinkedList<Hashtable<String, String>> seleccionarParametros() {
        LinkedList<Hashtable<String, String>> parametros = new LinkedList<>();

        enviarServidor("consultarParametros");
        String prametros_s = recibirServidor();

        if (!prametros_s.equals(" ")) {
            for (String prametro_s : prametros_s.split("@")) {

                Hashtable<String, String> parametro = new Hashtable<>();
                String[] valores = prametro_s.split(",");

                parametro.put("parametro", valores[0]);
                parametro.put("valor", valores[1]);

                parametros.add(parametro);

            }
        }

        return parametros;
    }

    public int configurarParametro(String parametro, int valor) {

        enviarServidor("cambiarParametro@" + parametro + "@" + valor);
        int resultado = Integer.parseInt(recibirServidor());

        return resultado;
    }

    public LinkedList<Hashtable<String, String>> seleccionarUsuarios() {
        LinkedList<Hashtable<String, String>> usuarios = new LinkedList<>();

        enviarServidor("consultarPerfiles");
        String usuarios_s = recibirServidor();

        if (!usuarios_s.equals(" ")) {
            for (String usuario_s : usuarios_s.split("@")) {

                Hashtable<String, String> usuario = new Hashtable<>();
                String[] valores = usuario_s.split(",");

                usuario.put("nombre", valores[0]);
                usuario.put("contrasenia", valores[1]);
                usuario.put("rol", valores[2]);
                usuario.put("p_tiempo", valores[3]);
                usuario.put("p_suscripciones", valores[4]);

                usuarios.add(usuario);

            }
        }

        return usuarios;
    }

    public int modificarPrivilegios(String usuario, int rol, int tiempo, int suscripciones) {
        enviarServidor("configurarPrivilegios@" + usuario + "@" + rol + "@" + tiempo + "@" + suscripciones);
        int resultado = Integer.parseInt(recibirServidor());
        return resultado;
    }

    public int ingresarUbicacion(String ubicacion, double li_latitud, double ls_latitud, double li_longitud, double ls_longitud) {
        int resultado = 0;
        
        if (li_latitud < ls_latitud && li_longitud < ls_longitud) {            
            enviarServidor("registrarUbicacion@" + ubicacion + "@" + li_latitud + "@" + ls_latitud + "@" + li_longitud + "@" + ls_longitud);
            resultado = Integer.parseInt(recibirServidor());
        }

        return resultado;
    }
}
