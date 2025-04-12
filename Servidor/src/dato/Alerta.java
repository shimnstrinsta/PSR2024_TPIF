package dato;

import java.util.LinkedList;
import java.util.Hashtable;

public class Alerta {

    private static boolean verificarAlerta(double temperatura, double humedad, int humo, int cantidadDenuncias, String[] configuracion) {
        boolean resultado = false;

        double temperaturaLimite = Double.parseDouble(configuracion[0].split(",")[1]);
        double humedadLimite = Double.parseDouble(configuracion[1].split(",")[1]);
        int denunciasLimite = Integer.parseInt(configuracion[2].split(",")[1]);

        
        if (humo == 1 && temperatura >= temperaturaLimite && humedad >= humedadLimite && cantidadDenuncias >= denunciasLimite) {
            resultado = true;
        }

        return resultado;
    }

    public static void verificarRiesgo(String ubicacion) {
        LinkedList<String> usuariosReportar = new LinkedList<>();
        Hashtable<String, Hashtable<String, String>> reportes = Reporte.consultarReportes(ubicacion);
        int cantidad = Denuncia.consultarCantidadDenuncias(ubicacion);
        LinkedList<String> usuarios = Perfil.consultarPerfilesUsuarios();

        for (String usuario : usuarios) {

            LinkedList<String> suscripciones = new LinkedList<>();
            for(String ubicacionSuscripcion : Suscripcion.consultarSuscripciones(usuario).split("@")){
                if(ubicacionSuscripcion.split(",")[1].equals("1"))
                    suscripciones.add(ubicacionSuscripcion.split(",")[0]);
            }
            
            
            String[] configuracion = Configuracion.consultarConfiguracion(usuario).split("@");
            
            
            if (suscripciones.contains(ubicacion)) {
                for (String reporte : reportes.keySet()) {

                    double temperatura = Double.parseDouble(reportes.get(reporte).get("temperatura"));
                    double humedad = Double.parseDouble(reportes.get(reporte).get("humedad"));
                    int humo = Integer.parseInt(reportes.get(reporte).get("humo"));

                    if (verificarAlerta(temperatura, humedad, humo, cantidad, configuracion) && !usuariosReportar.contains(usuario)) {
                        usuariosReportar.add(usuario);
                        
                    }
                }
            }

        }
        
        Notificacion.registrarNotificacionAlerta(usuariosReportar, reportes,ubicacion);
        
    }

}
