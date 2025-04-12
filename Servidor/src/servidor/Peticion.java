package servidor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class Peticion extends Thread {

    private final String request;    
    private final DatagramSocket socket;
    private final int puertoDestino;
    private static final int FRAGMENTO = 1024;

    public Peticion(DatagramPacket paquete, DatagramSocket socket) {
                
        request = new String(paquete.getData(), 0, paquete.getLength());        
        puertoDestino = paquete.getPort();
        this.socket = socket;        
    }

    private static String buscarClase(String metodo) {
        String clase = "";
        try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Metodos.txt"))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                String palabras[] = linea.split(",");
                if (palabras[0].equals(metodo)) {
                    clase = palabras[1];
                }

            }
        } catch (IOException e) {
        }
        return clase;
    }

    private void enviarRespuesta(String respuesta) {        
        try {
            byte[] datos = respuesta.getBytes();
            InetAddress direccionCliente = InetAddress.getByName("localhost");
            int totalFragmentos = (int) Math.ceil((double) datos.length / FRAGMENTO);
            
            for (int i = 0; i < totalFragmentos; i++) {
                int inicio = i * FRAGMENTO;
                int fin = Math.min(inicio + FRAGMENTO, datos.length);
                byte[] fragmento = Arrays.copyOfRange(datos, inicio, fin);
                
                byte[] encabezado = new byte[]{(byte) i, (byte) (i == totalFragmentos - 1 ? 1 : 0)};
                byte[] mensajeConEncabezado = new byte[encabezado.length + fragmento.length];

                System.arraycopy(encabezado, 0, mensajeConEncabezado, 0, encabezado.length);
                System.arraycopy(fragmento, 0, mensajeConEncabezado, encabezado.length, fragmento.length);
                
                DatagramPacket paqueteRespuesta = new DatagramPacket(mensajeConEncabezado, mensajeConEncabezado.length, direccionCliente, puertoDestino);
                socket.send(paqueteRespuesta);
            }
        }
        catch(IOException e){}
 
    }

    private String procesarPedido(String request) {
        String respuesta = "0";
        try {

            String[] parts = request.split("@");
            String methodName = parts[0];
            String className = buscarClase(methodName);

            String[] methodArgs = new String[parts.length - 1];
            System.arraycopy(parts, 1, methodArgs, 0, parts.length - 1);

            
            Class<?> clazz = Class.forName("dato." + className);

            Method[] methods = clazz.getDeclaredMethods();
            Method method = null;

            for (Method m : methods) {
                if (m.getName().equals(methodName) && m.getParameterCount() == methodArgs.length) {
                    method = m;
                    break;
                }
            }

            if (method == null) {
                throw new NoSuchMethodException("No se encontró un método con el nombre especificado y el número de parámetros en la clase.");
            }

            Object[] convertedArgs = new Object[methodArgs.length];

            Class<?>[] parameterTypes = method.getParameterTypes();

            for (int i = 0; i < methodArgs.length; i++) {
                if (parameterTypes[i] == int.class) {
                    convertedArgs[i] = Integer.parseInt(methodArgs[i]);
                } else if (parameterTypes[i] == String.class) {
                    convertedArgs[i] = methodArgs[i];
                } else if (parameterTypes[i] == boolean.class) {
                    convertedArgs[i] = Boolean.valueOf(methodArgs[i]);                
                } else if (parameterTypes[i] == double.class) {
                    convertedArgs[i] = Double.parseDouble(methodArgs[i]);
                }
            }

            Object instance = clazz.getConstructor().newInstance();
            Object result = method.invoke(instance, convertedArgs);
            respuesta = (String) result;

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println(e.getCause());
        }

        return respuesta;
    }

    @Override
    public void run() {
        
        String respuesta = procesarPedido(request);
        if(respuesta != null) enviarRespuesta(respuesta);
    }

}
