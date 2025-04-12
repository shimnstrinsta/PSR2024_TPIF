package sensor;


import java.util.Hashtable;
import java.util.Random;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Sensor extends Enviador implements Runnable {

    private final String ID;
    private int intervalo;
    private final Hashtable<String, Double> posicion = new Hashtable<>();
    private String ubicacion;
    private boolean estado;
    private final int puerto;

    public Sensor(String ID, int intervalo, double latitud, double longitud, String ubicacion, boolean estado, int puerto) {        
        this.ID = ID;
        this.intervalo = intervalo;
        this.posicion.put("Latitud", latitud);
        this.posicion.put("Longitud", longitud);
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.puerto = 10000 + puerto;
    }

    public String getID() {
        return ID;
    }

    public void setIntervalo(int intervalo) {
        this.intervalo = intervalo;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void setUbicacion(String ubicacion){
        this.ubicacion = ubicacion;
    }
    
    public void setLatitud(double latitud){
        posicion.put("latitud",latitud);
    }
    
    public void setLongitud(double longitud){
        posicion.put("longitud",longitud);
    }
        
    private String obtenerDatos() {
        String datos;
        Random random = new Random();

        double t = random.nextDouble() * (60);
        BigDecimal bd = new BigDecimal(t);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        double temperatura = bd.doubleValue();

        double h = random.nextDouble() * 100;
        BigDecimal bgh = new BigDecimal(h);
        bgh = bgh.setScale(2, RoundingMode.HALF_UP);
        double humedad = bgh.doubleValue();

        int humo = random.nextInt(2);

        datos = temperatura + "@" + humedad + "@" + humo;


        return datos;
    }    

    @Override
    public void run() {
        while (estado) {
            enviarServidor("registrarReporte@" + ID + "@" + ubicacion + "@" + posicion.get("Latitud") + "@" + posicion.get("Longitud") + "@" + obtenerDatos(),puerto);            
            try {                
                Thread.sleep(intervalo * 1000);
            } catch (InterruptedException e) {
            }
        }
    }

}
