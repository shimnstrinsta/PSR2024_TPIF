package interfaz;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import persona.Persona;
import persona.Administrador;
import java.util.LinkedList;
import java.util.Hashtable;
import persona.Usuario;
import background.Actualizador;
import java.lang.Math;

public class InterfazAdmin extends Interfaz {

    private final Administrador admin;

    public String ventanaActual;

    public InterfazAdmin(Persona persona) {
        ventana = crearVentana(1000, 900);
        Persona.seleccionarUbicaciones();

        admin = new Administrador(persona);

        JMenuBar menuPrincipal = menuBarra(ventana);

        JMenuItem item_home = menuItem(menuPrincipal, "Principal", 90, 90, "home");

        JMenu men_sensores = menu(menuPrincipal, "Sensores", "sensor");
        JMenuItem item_consultar_sensores = menuItem(men_sensores, "Consultar");
        JMenuItem item_agregar_sensores = menuItem(men_sensores, "Agregar");
        JMenuItem item_user = menuItem(menuPrincipal, "Usuarios", 90, 90, "user");
        JMenuItem item_ubicacion = menuItem(menuPrincipal, "Ubicaciones", 110, 90, "location");
        JMenuItem item_params = menuItem(menuPrincipal, "Parámetros", 670, 90, "param");

        JMenu men_usuario = menu(menuPrincipal, admin.getNombre(), "admin");
        JMenuItem item_cerrar_sesion = menuItem(men_usuario, "Cerrar sesión");

        Thread actualizador = new Thread(new Actualizador(InterfazAdmin.this));

        item_agregar_sensores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarSensor();
            }
        });

        item_cerrar_sesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION) == 0) {
                    ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    ventana.dispose();
                    new Interfaz().iniciar();
                    ventanaActual = "";
                }
            }
        });

        item_consultar_sensores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarSensores();
            }
        });

        item_home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelControl();
            }
        });

        item_params.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarParametros();
            }
        });

        item_user.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarUsuarios();
            }
        });

        item_ubicacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarUbicacion();
            }
        });

        panelControl();
        actualizador.start();
    }

    public void panelControl() {

        ventanaActual = "control";
        JPanel contenedor = panel("control");

        JPanel contenedor_denuncias = new JPanel(new BorderLayout());

        LinkedList<Hashtable<String, String>> denuncias = Administrador.seleccionarDenuncias();

        if (!denuncias.isEmpty()) {
            titulo("Denuncias", contenedor);

            JPanel lista_denuncias = panel();
            JTextArea descripcion = bancoTexto(400, 200);

            for (Hashtable<String, String> denuncia : denuncias) {
                JPanel pnl_denuncia = new JPanel(new BorderLayout());
                String mensaje = denuncia.get("fecha") + "       " + denuncia.get("horario") + "       " + denuncia.get("ubicacion");

                JButton btn = boton("+");

                JLabel info = textoNormal(mensaje);
                info.setBorder(new EmptyBorder(0, 5, 0, 5));
                info.setHorizontalAlignment(SwingConstants.LEFT);

                pnl_denuncia.add(info, BorderLayout.WEST);
                pnl_denuncia.add(btn, BorderLayout.EAST);

                lista_denuncias.add(pnl_denuncia);
                pnl_denuncia.setBorder(new CompoundBorder(new LineBorder(color_fondo, 1), new EmptyBorder(5, 5, 5, 15)));
                pnl_denuncia.setMaximumSize(new Dimension(400, 40));

                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        btn.setText("-");
                        String mensaje = "Usuario: " + denuncia.get("usuario") + "\nCoordenadas: " + denuncia.get("latitud") + "," + denuncia.get("longitud") + "\nIntencionalidad:" + denuncia.get("intencionalidad") + "\n\n" + denuncia.get("descripcion");

                        descripcion.setText(mensaje);
                    }
                });

            }

            JScrollPane denuncias_indices = new JScrollPane(lista_denuncias);
            denuncias_indices.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            contenedor_denuncias.add(denuncias_indices, BorderLayout.WEST);
            contenedor_denuncias.add(descripcion, BorderLayout.CENTER);

            contenedor_denuncias.setMaximumSize(new Dimension(900, 300));
            contenedor.add(contenedor_denuncias);
        } else {
            titulo("No hay denuncias", contenedor);
        }

        JPanel contenedor_reportes = panel();
        LinkedList<Hashtable<String, String>> reportes = Administrador.seleccionarReportes();

        if (!reportes.isEmpty()) {
            titulo("Reportes", contenedor_reportes);

            String[] columnas = {"Sensor", "Ubicación", "Latitud", "Longitud", "Temperatura", "Humedad", "Humo"};

            Object[][] datos = new Object[reportes.size()][columnas.length];

            int i = 0;
            for (Hashtable<String, String> reporte : reportes) {
                datos[i][0] = reporte.get("sensor");
                datos[i][1] = reporte.get("ubicacion");
                datos[i][2] = reporte.get("latitud");
                datos[i][3] = reporte.get("longitud");
                datos[i][4] = reporte.get("temperatura");
                datos[i][5] = reporte.get("humedad");
                if (reporte.get("humo").equals("1")) {
                    datos[i][6] = "Presente";
                } else {
                    datos[i][6] = "Ausente";
                }

                i++;
            }

            int[] tamanios = {80, 110, 100, 100, 100, 80, 100};

            JTable tabla = tabla(columnas, datos, tamanios, contenedor_reportes);
            contenedor_reportes.add(Box.createVerticalStrut(5));
            JButton btn_sensor = boton("Gestionar", contenedor_reportes);
            contenedor_reportes.add(Box.createVerticalStrut(5));

            btn_sensor.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    consultarSensores();
                }
            });
        } else {
            titulo("No hay reportes", contenedor_reportes);
        }

        contenedor.add(contenedor_reportes);

        cl.show(contenedor_main, "control");
    }

    private void consultarSensores() {

        JPanel contenedor = panel("sensores");

        titulo("Sensores", contenedor);
        JPanel btns = new JPanel(new FlowLayout());
        btns.setBackground(color_fondo);

        String[] columnas = {"ID", "Ubicación", "Latitud", "Longitud", "Intervalo (s)", "Estado"};
        int[] tamanios = {80, 150, 80, 80, 90, 100};

        LinkedList<Hashtable<String, String>> sensores = admin.seleccionarSensores();

        Object[][] datos = new Object[sensores.size()][columnas.length];

        int i = 0;
        for (Hashtable<String, String> sensor : sensores) {
            datos[i][0] = sensor.get("ID");
            datos[i][4] = sensor.get("intervalo");
            datos[i][2] = sensor.get("latitud");
            datos[i][3] = sensor.get("longitud");
            datos[i][1] = sensor.get("ubicacion");
            if (sensor.get("estado").equals("1")) {
                datos[i][5] = "Encendido";
            } else {
                datos[i][5] = "Apagado";
            }

            i++;
        }

        if (!sensores.isEmpty()) {
            JTable tabla = tabla(columnas, datos, tamanios, contenedor);

            contenedor.add(btns);
            JButton btn_agregar = boton("Agregar", btns);
            JButton btn_modificar = boton("Modificar", btns, tabla);
            JButton btn_eliminar = boton("Eliminiar", btns, tabla);

            btn_agregar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    agregarSensor();
                }
            });

            btn_eliminar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String id = (String) tabla.getValueAt(tabla.getSelectedRow(), 0);
                    switch (admin.borrarSensor(id)) {
                        case 0:
                            JOptionPane.showMessageDialog(null, "No se pudo conectar con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        case 1:
                            consultarSensores();
                            cl.show(contenedor_main, "sensores");
                            break;
                    }
                }
            });

            btn_modificar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String id = (String) tabla.getValueAt(tabla.getSelectedRow(), 0);
                    String ubicacion = (String) tabla.getValueAt(tabla.getSelectedRow(), 1);
                    double latitud = Double.parseDouble((String) tabla.getValueAt(tabla.getSelectedRow(), 2));
                    double longitud = Double.parseDouble((String) tabla.getValueAt(tabla.getSelectedRow(), 3));
                    int intervalo = Integer.parseInt((String) tabla.getValueAt(tabla.getSelectedRow(), 4));
                    String estado = (String) tabla.getValueAt(tabla.getSelectedRow(), 5);

                    modificarSensor(id, ubicacion, latitud, longitud, intervalo, estado);
                }
            });

        } else {
            titulo("No hay sensores registrados", contenedor);
        }
        cl.show(contenedor_main, "sensores");
        ventanaActual = "sensores";
    }

    private void agregarSensor() {
        JDialog ventanaEmergente = ventanaEmergente("Agregar sensor", 400, 400);

        JPanel contenedor = new JPanel();
        JPanel grilla = grilla(5, 2);
        // ----------- //
        textoNormal("Ubicación: ", grilla);
        LinkedList<Hashtable<String, String>> datos_ubicaciones = Usuario.seleccionarUbicaciones();
        String[] ubicaciones = new String[datos_ubicaciones.size()];

        for (int i = 0; i < ubicaciones.length; i++) {
            ubicaciones[i] = datos_ubicaciones.get(i).get("Nombre");
        }
        JComboBox ent_ubicacion = comboBox(ubicaciones, grilla);
        // ----------- //
        textoNormal("Latitud: ", grilla);
        JSpinner ent_lat = incrementador(grilla, ent_ubicacion, "Lat");
        // ----------- //
        textoNormal("Longitud: ", grilla);
        JSpinner ent_lon = incrementador(grilla, ent_ubicacion, "Lon");
        // ----------- //
        textoNormal("Intervalo (s): ", grilla);
        JSpinner ent_intervalo = incrementador(1, 3600, grilla);

        // ----------- //
        textoNormal("Estado: ", grilla);
        String[] estados = {"Encendido", "Apagado"};
        JComboBox ent_estado = comboBox(estados, grilla);
        // ----------- //
        grilla.setMaximumSize(new Dimension(350, 100));
        grilla.setBorder(new EmptyBorder(20, 20, 20, 20));

        contenedor.setBackground(color_fondo);
        ventanaEmergente.add(grilla);
        ventanaEmergente.add(contenedor);

        JButton btn_agregar = panelAceptarCancelar("Agregar", contenedor, ventanaEmergente);

        btn_agregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ubicacion = (String) ent_ubicacion.getSelectedItem();
                double latitud = (double) ent_lat.getValue();
                double longitud = (double) ent_lon.getValue();
                int intervalo = (int) ent_intervalo.getValue();
                String estado = (String) ent_estado.getSelectedItem();

                switch (admin.ingresarSensor(ubicacion, latitud, longitud, intervalo, estado)) {
                    case 0:
                        JOptionPane.showMessageDialog(null, "No se pudo conectar con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Sensor agregado", "Agregar sensor", JOptionPane.INFORMATION_MESSAGE);
                        ventanaEmergente.dispose();
                        break;
                }

            }
        });

        ventanaEmergente.setVisible(true);
    }

    private void modificarSensor(String id, String ubicacion, double latitud, double longitud, int intervalo, String estado) {
        JDialog ventanaEmergente = ventanaEmergente("Sensor " + id, 400, 400);

        JPanel contenedor = new JPanel();
        JPanel grilla = grilla(5, 2);
        // ----------- //
        textoNormal("Ubicación: ", grilla);
        LinkedList<Hashtable<String, String>> datos_ubicaciones = Usuario.seleccionarUbicaciones();
        String[] ubicaciones = new String[datos_ubicaciones.size()];

        for (int i = 0; i < ubicaciones.length; i++) {
            ubicaciones[i] = datos_ubicaciones.get(i).get("Nombre");
        }
        JComboBox ent_ubicacion = comboBox(ubicaciones, grilla);
        ent_ubicacion.setSelectedItem(ubicacion);
        // ----------- //
        textoNormal("Latitud: ", grilla);
        JSpinner ent_lat = incrementador(grilla, ent_ubicacion, "Lat");
        ent_lat.setValue(latitud);
        // ----------- //
        textoNormal("Longitud: ", grilla);
        JSpinner ent_lon = incrementador(grilla, ent_ubicacion, "Lon");
        ent_lon.setValue(longitud);
        // ----------- //
        textoNormal("Intervalo (s): ", grilla);
        JSpinner ent_intervalo = incrementador(1, 3600, grilla);
        ent_intervalo.setValue(intervalo);
        // ----------- //
        textoNormal("Estado: ", grilla);
        String[] estados = {"Encendido", "Apagado"};
        JComboBox ent_estado = comboBox(estados, grilla);
        ent_estado.setSelectedItem(estado);
        // ----------- //
        grilla.setMaximumSize(new Dimension(350, 100));
        grilla.setBorder(new EmptyBorder(20, 20, 20, 20));

        contenedor.setBackground(color_fondo);
        ventanaEmergente.add(grilla);
        ventanaEmergente.add(contenedor);

        JButton btn_modificar = panelAceptarCancelar("Confirmar", contenedor, ventanaEmergente);

        btn_modificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ubicacion = (String) ent_ubicacion.getSelectedItem();
                double latitud = (double) ent_lat.getValue();
                double longitud = (double) ent_lon.getValue();
                int intervalo = (int) ent_intervalo.getValue();
                String estado = (String) ent_estado.getSelectedItem();

                switch (admin.modificarSensor(id, ubicacion, latitud, longitud, intervalo, estado)) {
                    case 0:
                        JOptionPane.showMessageDialog(null, "No se pudo conectar con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 1:
                        ventanaEmergente.dispose();
                        consultarSensores();
                        cl.show(contenedor_main, "sensores");
                        break;
                }

            }
        });

        ventanaEmergente.setVisible(true);
    }

    private void consultarParametros() {
        JPanel contenedor = panel("parametros");

        LinkedList<Hashtable<String, String>> parametros = Administrador.seleccionarParametros();        
        if (!parametros.isEmpty()) {
            titulo("Parámetros", contenedor);
            String[] columnas = {"Parámetro", "Valor"};
            int[] tamanios = {300, 60};
            Object[][] datos = new Object[parametros.size()][columnas.length];
            int i = 0;

            for (Hashtable<String, String> parametro : parametros) {
                datos[i][0] = parametro.get("parametro");
                datos[i][1] = parametro.get("valor");
                i++;
            }

            JTable tabla = tabla(columnas, datos, tamanios, contenedor);

            JButton btn_modificar = boton("Modificar", contenedor, tabla);

            btn_modificar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String parametro = (String) tabla.getValueAt(tabla.getSelectedRow(), 0);
                    String valor = (String) tabla.getValueAt(tabla.getSelectedRow(), 1);

                    configurarParametro(parametro, valor);
                }
            });
        } else {
            titulo("No se encontraron parámetros", contenedor);
            JOptionPane.showMessageDialog(null, "No se pudo encontrar los parámetros", "Error", JOptionPane.ERROR_MESSAGE);
        }

        cl.show(contenedor_main, "parametros");
        ventanaActual = "parametro";
    }

    private void configurarParametro(String parametro, String valor) {
        JDialog ventanaEmergente = ventanaEmergente("Parámetro", 400, 400);

        JPanel contenedor = new JPanel();
        JPanel grilla = grilla(1, 2);

        textoNormal(parametro, grilla);
        JSpinner entrada = incrementador(1, 3600, grilla);

        grilla.setBorder(new EmptyBorder(20, 20, 20, 20));
        entrada.setValue(Integer.parseInt(valor));

        ventanaEmergente.add(grilla);
        ventanaEmergente.add(contenedor);

        contenedor.setBackground(color_fondo);
        JButton btn_modificar = panelAceptarCancelar("Modificar", contenedor, ventanaEmergente);

        btn_modificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int valor = (int) entrada.getValue();
                switch (admin.configurarParametro(parametro, valor)) {
                    case 0:
                        JOptionPane.showMessageDialog(null, "No se pudo conectar con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 1:
                        ventanaEmergente.dispose();
                        consultarParametros();
                        break;
                }

            }
        });

        ventanaEmergente.setVisible(true);
    }

    private void consultarUsuarios() {
        JPanel contenedor = panel("usuarios");

        titulo("Usuarios", contenedor);

        String[] columnas = {"Nombre", "Contraseña", "Rol"};

        LinkedList<Hashtable<String, String>> usuarios = admin.seleccionarUsuarios();
        

        Object[][] datos = new Object[usuarios.size()][5];

        int i = 0;
        for (Hashtable<String, String> usuario : usuarios) {

            datos[i][0] = usuario.get("nombre");
            datos[i][1] = usuario.get("contrasenia");

            if (usuario.get("rol").equals("3")) {
                datos[i][2] = "Usuario";
            } else {
                datos[i][2] = "Administrador";
            }

            datos[i][3] = usuario.get("p_tiempo");
            datos[i][4] = usuario.get("p_suscripciones");
            i++;
        }

        int[] tamanios = {110, 120, 90};

        JTable tabla = tabla(columnas, datos, tamanios, contenedor);

        contenedor.add(Box.createVerticalStrut(10));
        JButton btn_privilegios = boton("Modificar privilegios", contenedor, tabla);

        btn_privilegios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = (String) tabla.getValueAt(tabla.getSelectedRow(), 0);

                for (Hashtable<String, String> usuario : usuarios) {
                    if (usuario.get("nombre").equals(nombre)) {
                        configurarPrivilegios(nombre, usuario.get("p_tiempo"), usuario.get("p_suscripciones"), usuario.get("rol"));
                    }
                }

            }
        });

        cl.show(contenedor_main, "usuarios");
        ventanaActual = "usuarios";
    }

    private void configurarPrivilegios(String nombre, String tiempo, String suscripciones, String rol) {

        JDialog ventanaEmergente = ventanaEmergente("Privilegios de " + nombre, 400, 400);

        JPanel contenedor = new JPanel();
        JPanel grilla = grilla(3, 2);

        textoNormal("Tiempo entre denuncias", grilla);
        JSpinner ent_tiempo = incrementador(1, 3600, grilla);
        ent_tiempo.setValue(Integer.parseInt(tiempo));

        textoNormal("Suscripciones máximas", grilla);
        JSpinner ent_suscripciones = incrementador(0, 3600, grilla);
        ent_suscripciones.setValue(Integer.parseInt(suscripciones));

        textoNormal("Administrador", grilla);
        JCheckBox ent_rol = checkBox("", grilla);
        if (rol.equals("4")) {
            ent_rol.setSelected(true);
        }

        grilla.setBorder(new EmptyBorder(20, 20, 20, 20));

        ventanaEmergente.add(grilla);
        ventanaEmergente.add(contenedor);

        contenedor.setBackground(color_fondo);

        JButton btn_modificar = panelAceptarCancelar("Cambiar", contenedor, ventanaEmergente);

        btn_modificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int v_rol = 3, v_tiempo, v_suscripciones;

                if (ent_rol.isSelected()) {
                    v_rol = 4;
                }
                v_tiempo = (int) ent_tiempo.getValue();
                v_suscripciones = (int) ent_suscripciones.getValue();

                switch (admin.modificarPrivilegios(nombre, v_rol, v_tiempo, v_suscripciones)) {
                    case 0:
                        JOptionPane.showMessageDialog(null, "No se pudo conectar con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 1:
                        ventanaEmergente.dispose();
                        consultarUsuarios();
                        break;
                }

            }
        });

        ventanaEmergente.setVisible(true);

    }

    private void consultarUbicacion() {
        JPanel contenedor = panel("ubicaciones");
        titulo("Focos de incendios", contenedor);

        LinkedList<Hashtable<String, String>> ubicaciones = Persona.seleccionarUbicaciones();
        

        for (Hashtable<String, String> ubicacion : ubicaciones) {

            JPanel panel = new JPanel(new BorderLayout());
            panel.setMaximumSize(new Dimension(600, 50));
            panel.setBorder(new CompoundBorder(new LineBorder(Color.black, 1), new EmptyBorder(10, 10, 10, 10)));

            panel.add(textoNormal(ubicacion.get("Nombre"), 10, 10), BorderLayout.WEST);

            double l1 = (Math.abs(Double.parseDouble(ubicacion.get("LsLon"))) - Math.abs(Double.parseDouble(ubicacion.get("LiLon"))));
            double l2 = (Math.abs(Double.parseDouble(ubicacion.get("LsLat"))) - Math.abs(Double.parseDouble(ubicacion.get("LiLat"))));

            double tamanio = (111 * l1) * (l2 * 111);
            panel.add(textoNormal(String.format("%.2f", tamanio) + " km2", 10, 10), BorderLayout.EAST);

            contenedor.add(Box.createVerticalStrut(5));
            contenedor.add(panel);
        }

        contenedor.add(Box.createVerticalStrut(15));
        JButton btn_agregar = boton("Agregar", contenedor);

        btn_agregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarUbicacion();
            }
        });

        cl.show(contenedor_main, "ubicaciones");
        ventanaActual = "ubicaciones";
    }

    private void agregarUbicacion() {
        JDialog ventanaEmergente = ventanaEmergente("Agregar ubicacion", 400, 400);

        JPanel contenedor = new JPanel();
        JPanel grilla = grilla(6, 2);
        // ----------- //
        textoNormal("Nombre: ", grilla);
        JTextField ent_ubicacion = entradaTexto(100, 20, grilla);
        // ----------- //
        textoNormal("Límite inferior de latitud: ", grilla);
        JSpinner ent_li_lat = incrementador(-34.95536818635516, -29.372191659594527, grilla);
        // ----------- //
        textoNormal("Límite superior de latitud: ", grilla);
        JSpinner ent_ls_lat = incrementador(-34.95536818635516, -29.372191659594527, grilla);
        // ----------- //
        textoNormal("Límite inferior de longitud: ", grilla);
        JSpinner ent_li_lon = incrementador(-65.99211661457356, -61.79405352391993, grilla);
        // ----------- //
        textoNormal("Límite superior de longitud: ", grilla);
        JSpinner ent_ls_lon = incrementador(-65.99211661457356, -61.79405352391993, grilla);
        // ----------- //
        grilla.setMaximumSize(new Dimension(400, 200));
        grilla.setBorder(new EmptyBorder(20, 20, 20, 20));

        contenedor.setBackground(color_fondo);
        ventanaEmergente.setBackground(color_fondo);
        ventanaEmergente.add(grilla);
        ventanaEmergente.add(contenedor);

        JButton btn_agregar = panelAceptarCancelar("Agregar", contenedor, ventanaEmergente);

        btn_agregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double li_latitud = (double) ent_li_lat.getValue();
                double ls_latitud = (double) ent_ls_lat.getValue();

                double li_longitud = (double) ent_li_lon.getValue();
                double ls_longitud = (double) ent_ls_lon.getValue();
                String ubicacion = ent_ubicacion.getText();

                switch (admin.ingresarUbicacion(ubicacion, li_latitud, ls_latitud, li_longitud, ls_longitud)) {
                    case 0:
                        JOptionPane.showMessageDialog(null, "Los valores mínimos no pueden ser mayores que los máximos", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Ya existe esa ubicación", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 2:
                        ventanaEmergente.dispose();
                        consultarUbicacion();
                        break;                    

                }

            }
        });

        ventanaEmergente.setVisible(true);
    }

}
