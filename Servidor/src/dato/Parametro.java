package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Parametro {

    private final static Object bloqueo = new Object();

    public String tiempoActualizacionPanelControl() {
        String tiempo = "0";

        synchronized (bloqueo) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Parametros.txt"))) {
                tiempo = br.readLine().split(",")[1];
            } catch (IOException e) {
            }
        }

        return tiempo;
    }

    public static String consultarParametros() {
        String parametros = " ";

        synchronized (bloqueo) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Parametros.txt"))) {
                String linea, contenido = "";

                while ((linea = br.readLine()) != null) {
                    contenido += linea + "@";
                }

                if (!contenido.isEmpty()) {
                    parametros = contenido.substring(0, contenido.length() - 1);
                }

            } catch (IOException e) {
            }
        }

        return parametros;
    }

    public String cambiarParametro(String parametro,int valor) {
        String resultado = "0", contenido = "";

        synchronized (bloqueo) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Parametros.txt"))) {
                String linea;

                while ((linea = br.readLine()) != null) {
                    String[] valores = linea.split(",");
                    
                    if(!valores[0].equals(parametro)){
                        contenido += linea + "\n";
                    }
                    else{
                        contenido += parametro + "," + valor + "\n";
                    }                                        
                }

            } catch (IOException e) {
            }
            
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Parametros.txt"))) {
                bw.write(contenido);
                resultado = "1";
            } catch (IOException e) {
            }
            
        }
        
        return resultado;
    }


}
