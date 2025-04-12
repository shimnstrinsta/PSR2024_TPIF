package sensor;

import java.util.LinkedList;
import java.util.Hashtable;

public class GestorSensor extends Enviador {

    private final LinkedList<Sensor> sensores = new LinkedList<>();
    private final LinkedList<String> sensoresActivos = new LinkedList<>();

    private LinkedList<Hashtable<String, String>> consultarSensoresLista() {
        LinkedList<Hashtable<String, String>> sensores = new LinkedList<>();

        enviarServidor("consultarSensores", 6666);
        String datos_sensores = recibirServidor(6666);


        if (!datos_sensores.equals("0")) {
            for (String sensor_s : datos_sensores.split("@")) {

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

    public void iniciarSensores() {
        for (Hashtable<String, String> datoSensor : consultarSensoresLista()) {
            String ID = datoSensor.get("ID");
            int intervalo = Integer.parseInt(datoSensor.get("intervalo"));
            double latitud = Double.parseDouble(datoSensor.get("latitud"));
            double longitud = Double.parseDouble(datoSensor.get("longitud"));
            String ubicacion = datoSensor.get("ubicacion");
            boolean estado = false;

            if (datoSensor.get("estado").equals("1")) {
                estado = true;
            }

            Sensor sensor = new Sensor(ID, intervalo, latitud, longitud, ubicacion, estado, sensores.size() + 1);
            sensores.add(sensor);

            if (estado) {
                new Thread(sensor).start();
                synchronized(sensoresActivos){
                    sensoresActivos.add(sensor.getID());                
                }                
            }

        }

    }

    public void cambiarEstado(String IDSensor) {
        for (Sensor sensor : sensores) {
            if (IDSensor.equals(sensor.getID())) {
                synchronized (sensor) {
                    sensor.setEstado(false);
                }


                synchronized (sensoresActivos) {
                    sensoresActivos.remove(sensor.getID());
                }

            }

        }
    }

    public void agregarSensor(String ID, int intervalo, double latitud, double longitud, String ubicacion, boolean estado) {
        synchronized (sensores) {
            Sensor sensor = new Sensor(ID, intervalo, latitud, longitud, ubicacion, estado, sensores.size());
            sensores.add(sensor);
            if (estado) {
                new Thread(sensor).start();
                synchronized(sensoresActivos){
                    sensoresActivos.add(sensor.getID());
                }
                
            }
        }

    }

    public void cambiarSensor(String ID, String ubicacion, int intervalo, double latitud, double longitud, boolean estado) {
        for (Sensor sensor : sensores) {
            if (ID.equals(sensor.getID())) {
                sensor.setEstado(estado);
                sensor.setUbicacion(ubicacion);
                sensor.setLatitud(latitud);
                sensor.setIntervalo(intervalo);
                sensor.setLongitud(longitud);

                synchronized (sensoresActivos) {
                    if (estado && !sensoresActivos.contains(sensor.getID())) {
                        new Thread(sensor).start();
                        sensoresActivos.add(sensor.getID());
                    } 
                    else if (!estado && sensoresActivos.contains(sensor.getID())) {
                        sensoresActivos.remove(sensor.getID());
                    }
                }
            }
        }
    }
}
