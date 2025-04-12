package sensor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Controlador extends Thread {

    private final GestorSensor sensores;
    private final int PUERTO = 6600;

    public Controlador(GestorSensor sensores) {
        this.sensores = sensores;
    }

    private void avisarCambio(String cambio) {
        String[] valores = cambio.split(",");
        String accion = valores[0];

        switch (Integer.parseInt(accion)) {
            case 1:

                String ID = valores[1];
                int intervalo = Integer.parseInt(valores[2]);
                double latitud = Double.parseDouble(valores[3]);
                double longitud = Double.parseDouble(valores[4]);
                String ubicacion = valores[5];
                boolean estado = false;

                if (valores[6].equals("1")) {
                    estado = true;
                }

                sensores.agregarSensor(ID, intervalo, latitud, longitud, ubicacion, estado);
                break;
            case 2:

                sensores.cambiarEstado(valores[1]);
                break;
            case 3:

                intervalo = Integer.parseInt(valores[5]);
                latitud = Double.parseDouble(valores[3]);
                longitud = Double.parseDouble(valores[4]);
                ubicacion = valores[2];
                estado = false;

                if (valores[6].equals("1")) {
                    estado = true;
                }

                sensores.cambiarSensor(valores[1], ubicacion, intervalo, latitud, longitud, estado);
                break;
        }
    }

    private String escucharCambio() {
        String cambio = "";
        try (DatagramSocket socket = new DatagramSocket(PUERTO)) {
            byte[] buffer = new byte[1024];
            DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
            socket.receive(paquete);
            cambio = new String(paquete.getData(), 0, paquete.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cambio;
    }

    @Override
    public void run() {
        while (true) {
            String cambio = escucharCambio();
            avisarCambio(cambio);
        }
    }

}
