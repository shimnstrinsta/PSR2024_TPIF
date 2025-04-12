package background;

import interfaz.InterfazAdmin;
import persona.Administrador;

public class Actualizador extends Thread {
        
    private final InterfazAdmin interfaz;

    public Actualizador(InterfazAdmin interfaz) {                
        this.interfaz = interfaz;
    }

    @Override
    public void run() {

        while (true) {            
            int tiempo = Administrador.tiempoActualizacion();            
            try {
                Thread.sleep(tiempo*1000);
                if(interfaz.ventanaActual.equals("control")) interfaz.panelControl();                
            } catch (InterruptedException e) {
            }
        }

    }

}
