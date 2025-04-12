package persona;

import java.util.Hashtable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import background.EscuchadorAlerta;
import java.util.LinkedList;

public class Usuario extends Persona {

    private final Thread escuchador;

    public Usuario(Persona persona) {
        this.nombre = persona.getNombre();
        escuchador = new Thread(new EscuchadorAlerta(this));
        escuchador.start();
    }

    public void cerrarSesion() {        
        escuchador.interrupt();

    }

    public Hashtable<String, Boolean> seleccionarSuscripciones() {
        Hashtable<String, Boolean> suscripciones = new Hashtable<>();

        enviarServidor("consultarSuscripciones@" + nombre);
        String resultado = recibirServidor();

        for (String suscripcion : resultado.split("@")) {

            String[] valores = suscripcion.split(",");

            boolean suscripto = false;

            if (valores[1].equals("1")) {
                suscripto = true;
            }

            suscripciones.put(valores[0], suscripto);
        }

        return suscripciones;
    }

    public int suscribirseUbicacion(String ubicacion) {
        enviarServidor("registrarSuscripcion@" + nombre + "@" + ubicacion);
        int resultado = Integer.parseInt(recibirServidor());
        return resultado;
    }

    public int suscribirseUbicacion() {
        enviarServidor("registrarSuscripcion@" + nombre);
        int resultado = Integer.parseInt(recibirServidor());
        return resultado;
    }

    public int desuscribirseUbicacion(String ubicacion) {
        enviarServidor("eliminarSuscripcion@" + nombre + "@" + ubicacion);
        int resultado = Integer.parseInt(recibirServidor());
        return resultado;
    }

    public int desuscribirseUbicacion() {
        enviarServidor("eliminarSuscripcion@" + nombre);
        int resultado = Integer.parseInt(recibirServidor());
        return resultado;
    }

    public int denunciarVecino(String ubicacion, double lat, double lon, String intencionalidad, String descripcion) {
        int resultado = 0;

        if (validarDenuncia(descripcion)) {
            enviarServidor("registrarDenuncia@" + nombre + "@" + ubicacion + "@" + lat + "@" + lon + "@" + intencionalidad + "@" + descripcion);
            resultado = Integer.parseInt(recibirServidor());
        }

        return resultado;
    }

    private static boolean validarDenuncia(String descripcion) {
        boolean resultado = false;

        if (descripcion.length() > 5 && descripcion.length() < 100) {
            resultado = true;
        }

        return resultado;
    }

    public Hashtable<String, String> seleccionarConfiguracion() {
        Hashtable<String, String> configuraciones = new Hashtable<>();

        enviarServidor("consultarConfiguracion@" + nombre);
        String resultado = recibirServidor();

        for (String configuracion : resultado.split("@")) {

            String[] valores = configuracion.split(",");
            configuraciones.put(valores[0], valores[1]);
        }
        

        return configuraciones;
    }

    public int cambiarConfiguracion(int temperatura, int denuncias, int humedad, int alertas) {
        enviarServidor("modificarConfiguracion@" + nombre + "@" + temperatura + "@" + denuncias + "@" + humedad + "@" + alertas);
        int resultado = Integer.parseInt(recibirServidor());

        return resultado;
    }

    public LinkedList<Hashtable<String, String>> seleccionarAlertas() {
        LinkedList<Hashtable<String, String>> alertas = new LinkedList<>();

        enviarServidor("consultarAlertas@" + nombre);
        String resultado = recibirServidor();
        
        if (!resultado.equals(" ")) {

            for (String alerta_s : resultado.split("@")) {

                Hashtable<String, String> alerta = new Hashtable<>();
                String[] valores = alerta_s.split("_");

                alerta.put("fecha", valores[0]);
                alerta.put("hora", valores[1]);
                alerta.put("ubicacion", valores[2]);
                alerta.put("latitud", valores[3]);
                alerta.put("longitud", valores[4]);
                alerta.put("temperatura", valores[5]);
                alerta.put("humedad", valores[6]);

                alertas.add(alerta);
            }
        }        

        return alertas;
    }

}
