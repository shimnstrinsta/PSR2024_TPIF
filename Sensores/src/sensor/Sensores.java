
package sensor;

public class Sensores {

    public static void main(String[] args) {
        GestorSensor gestor = new GestorSensor();
        gestor.iniciarSensores();
        
        new Thread(new Controlador(gestor)).start();                
    }
    
}
