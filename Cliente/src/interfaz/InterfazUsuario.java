package interfaz;

import persona.Persona;
import persona.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Hashtable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class InterfazUsuario extends Interfaz {

    private final Usuario usuario;

    public InterfazUsuario(Persona persona) {
        ventana = crearVentana(1000, 900);
        usuario = new Usuario(persona);

        JMenuBar menuPrincipal = menuBarra(ventana);

        JMenuItem item_suscripciones = menuItem(menuPrincipal, "Suscripciones", 130, 90, "sub");
        JMenuItem item_denuncias = menuItem(menuPrincipal, "Denuncias", 110, 90, "den");
        JMenuItem item_avisos = menuItem(menuPrincipal, "Alertas", 650, 90, "alert");

        JMenu men_usuario = menu(menuPrincipal, usuario.getNombre(), "user");
        JMenuItem item_configuraciones = menuItem(men_usuario, "Preferencias");
        JMenuItem item_cerrar_sesion = menuItem(men_usuario, "Cerrar sesión");

        item_suscripciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarSuscripciones();
                cl.show(contenedor_main, "suscripciones");

            }
        });

        item_denuncias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                denunciarVecino();
                cl.show(contenedor_main, "denunciar");

            }
        });

        item_configuraciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarPreferencias();
                cl.show(contenedor_main, "configuraciones");
            }
        });

        item_avisos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarAlertas();
                cl.show(contenedor_main, "alertas");
            }
        });

        item_cerrar_sesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION) == 0) {
                    usuario.cerrarSesion();
                    ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    ventana.dispose();
                    new Interfaz().iniciar();
                }
            }
        });

        consultarAlertas();
        cl.show(contenedor_main, "alertas");
    }

    private void consultarSuscripciones() {
        JPanel contenedor = panel("suscripciones");

        Hashtable<String, Boolean> suscripciones = usuario.seleccionarSuscripciones();
        int ubicacionesSuscriptas = 0;

        titulo("Suscripciones", contenedor);

        for (String ubicacion : suscripciones.keySet()) {

            JPanel panel = new JPanel(new BorderLayout());
            panel.setMaximumSize(new Dimension(600, 50));
            panel.setBorder(new CompoundBorder(new LineBorder(Color.black, 1), new EmptyBorder(10, 10, 10, 10)));

            panel.add(textoNormal(ubicacion, 10, 10), BorderLayout.WEST);

            JButton btn = boton("Suscrito      ");
            String i = "check";
            if (!suscripciones.get(ubicacion)) {
                i = "plus";
                btn.setText("Suscribirse");

                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        switch (usuario.suscribirseUbicacion(ubicacion)) {
                            case 0:
                                JOptionPane.showMessageDialog(null, "No es posible conectarse con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                                break;
                            case 1:
                                JOptionPane.showMessageDialog(null, "Has superado el límite de suscripciones", "Error", JOptionPane.ERROR_MESSAGE);
                                break;
                        }
                        consultarSuscripciones();
                        cl.show(contenedor_main, "suscripciones");

                    }
                });
            } else {
                ubicacionesSuscriptas++;

                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        switch (usuario.desuscribirseUbicacion(ubicacion)) {
                            case 0:
                                JOptionPane.showMessageDialog(null, "No es posible conectarse con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                                break;
                            case 1:
                                JOptionPane.showMessageDialog(null, "Has superado el límite de suscripciones", "Error", JOptionPane.ERROR_MESSAGE);
                                break;
                        }
                        consultarSuscripciones();
                        cl.show(contenedor_main, "suscripciones");

                    }
                });
            }

            ImageIcon icon = new ImageIcon("./icons/" + i + ".png");
            btn.setIcon(icon);

            panel.add(btn, BorderLayout.EAST);

            contenedor.add(Box.createVerticalStrut(5));
            contenedor.add(panel);

        }

        contenedor.add(Box.createVerticalStrut(20));
        JCheckBox todas_suscripciones = checkBox("Suscribirse a todas las ubicaciones", contenedor);

        if (Usuario.seleccionarUbicaciones().size() == ubicacionesSuscriptas) {
            todas_suscripciones.setSelected(true);
        }

        todas_suscripciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (todas_suscripciones.isSelected()) {

                    if (JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere suscribirse a todas las ubicaciones?", "Suscripción", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                        switch (usuario.suscribirseUbicacion()) {
                            case 0:
                                JOptionPane.showMessageDialog(null, "No es posible conectarse con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                                break;
                            case 1:
                                JOptionPane.showMessageDialog(null, "Has superado el límite de suscripciones", "Error", JOptionPane.ERROR_MESSAGE);
                                break;
                        }
                    }

                } else {

                    if (JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere desuscribirse a todas las ubicaciones?", "Suscripción", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                        switch (usuario.desuscribirseUbicacion()) {
                            case 0:
                                JOptionPane.showMessageDialog(null, "No es posible conectarse con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                                break;
                            case 1:
                                JOptionPane.showMessageDialog(null, "Has superado el límite de suscripciones", "Error", JOptionPane.ERROR_MESSAGE);
                                break;
                        }
                    }
                }

                consultarSuscripciones();
                cl.show(contenedor_main, "suscripciones");
            }
        });

    }

    private void consultarAlertas() {
        JPanel contenedor = panel("alertas");
        titulo("Alertas", contenedor);

        LinkedList<Hashtable<String, String>> alertas = usuario.seleccionarAlertas();
        JPanel contenedor_denuncias = new JPanel(new BorderLayout());
        JLabel ayuda = textoNormal("");
        JButton btn_preferencias = botonInvisible("");
        btn_preferencias.setEnabled(false);
        btn_preferencias.setFont(fnt_txt_normal);

        if (alertas.isEmpty()) {
            titulo("Sin alertas de incendios", contenedor);
        } else {
            JPanel lista_denuncias = panel();
            JTextArea descripcion = bancoTexto(400, 200);

            for (Hashtable<String, String> alerta : alertas) {
                JPanel pnl_denuncia = new JPanel(new BorderLayout());
                String mensaje = alerta.get("fecha") + "       " + alerta.get("hora") + "       " + alerta.get("ubicacion");

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
                        ImageIcon icon = new ImageIcon("./icons/help.png");
                        btn.setText("-");
                        descripcion.setText("Se detectó una alerta de incendio en " + alerta.get("ubicacion") + " en el día " + alerta.get("fecha") + " a las " + alerta.get("hora") + " en las coordenadas: " + alerta.get("latitud") + "," + alerta.get("longitud") + ".\n\nDatos leidos \n" + "Temperatura: " + alerta.get("temperatura") + "\nHumedad: " + alerta.get("humedad"));
                        ayuda.setText("Si crees que los datos leidos son bajos para generar alertas, puedes configurarlos en ");
                        btn_preferencias.setText("Preferencias");
                        btn_preferencias.setEnabled(true);
                        ayuda.setIcon(icon);
                    }
                });

            }

            JScrollPane denuncias_indices = new JScrollPane(lista_denuncias);
            denuncias_indices.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            contenedor_denuncias.add(denuncias_indices, BorderLayout.WEST);
            contenedor_denuncias.add(descripcion, BorderLayout.CENTER);

            contenedor_denuncias.setMaximumSize(new Dimension(900, 300));
            contenedor.add(contenedor_denuncias);
            contenedor.add(ayuda);
            contenedor.add(btn_preferencias);
            

            btn_preferencias.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    consultarPreferencias();
                    cl.show(contenedor_main, "configuraciones");
                }
            });

            ayuda.setBorder(new EmptyBorder(30, 0, 0, 0));
        }

    }

    private void consultarPreferencias() {
        JPanel contenedor = panel("configuraciones");

        Hashtable<String, String> configuraciones = usuario.seleccionarConfiguracion();
        titulo("Preferencias", contenedor);

        JPanel grilla = grilla(configuraciones.size() * 2, 2);
        JButton btn_guardar = boton("Guardar");

        JSpinner[] entradas = new JSpinner[configuraciones.size() - 1];

        btn_guardar.setEnabled(false);
        int i = 0;
        final JCheckBox ent_alertas = checkBox("");;

        for (String configuracion : configuraciones.keySet()) {
            grilla.add(Box.createVerticalStrut(5));
            grilla.add(Box.createVerticalStrut(5));
            textoNormal(configuracion, grilla);

            if (!configuracion.equals("Alertas")) {

                JSpinner entrada = incrementador(1, 300, grilla);
                entrada.setValue(Integer.parseInt(configuraciones.get(configuracion)));

                entrada.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        btn_guardar.setEnabled(true);
                    }

                });

                entradas[i] = entrada;
                i++;
            } else {
                grilla.add(ent_alertas);
                if (configuraciones.get("Alertas").equals("1")) {
                    ent_alertas.setSelected(true);
                }

                ent_alertas.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        btn_guardar.setEnabled(true);
                    }

                });
            }

        }

        grilla.setMaximumSize(new Dimension(600, 300));

        contenedor.add(grilla);
        contenedor.add(Box.createVerticalStrut(25));
        contenedor.add(btn_guardar);

        btn_guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int temperatura = (int) entradas[0].getValue();
                int denuncias = (int) entradas[1].getValue();
                int humedad = (int) entradas[2].getValue();
                int alertas = 0;

                if (ent_alertas.isSelected()) {
                    alertas = 1;
                }

                switch (usuario.cambiarConfiguracion(temperatura, denuncias, humedad, alertas)) {
                    case 0:
                        JOptionPane.showMessageDialog(null, "No se pudo establecer conexión con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Configuración guardada", "Configuraciones", JOptionPane.INFORMATION_MESSAGE);
                        consultarPreferencias();
                        cl.show(contenedor_main, "configuraciones");
                        break;
                }
            }
        });

    }

    private void denunciarVecino() {
        JPanel contenedor = panel("denunciar");
        JPanel grilla = new JPanel(new GridLayout(5, 2, 30, 30));

        titulo("Denunciar", contenedor);

        textoNormal("Ubicación", grilla);

        LinkedList<Hashtable<String, String>> datos_ubicaciones = Usuario.seleccionarUbicaciones();
        String[] ubicaciones = new String[datos_ubicaciones.size()];

        for (int i = 0; i < ubicaciones.length; i++) {
            ubicaciones[i] = datos_ubicaciones.get(i).get("Nombre");
        }

        JComboBox ent_ubicacion = comboBox(ubicaciones, grilla);

        textoNormal("Longitud", grilla);
        textoNormal("Latitud", grilla);

        JSpinner ent_lat = incrementador(grilla, ent_ubicacion, "Lat");
        JSpinner ent_lon = incrementador(grilla, ent_ubicacion, "Lon");

        textoNormal("Intencionalidad", grilla);
        String[] val_intencionalidad = {"Intencional", "No intencional", "Desconocida"};
        JComboBox ent_intencionalidad = comboBox(val_intencionalidad, grilla);

        textoNormal("Descripción", grilla);

        contenedor.add(grilla);
        JTextArea ent_descripcion = bancoTexto(500, 80, contenedor);

        grilla.setMaximumSize(new Dimension(600, 300));
        grilla.setBorder(new EmptyBorder(20, 0, 20, 0));
        grilla.setAlignmentX(Component.CENTER_ALIGNMENT);
        grilla.setBackground(color_fondo);

        contenedor.add(Box.createVerticalStrut(25));
        JButton btn_denunciar = boton("Denunciar", contenedor);

        btn_denunciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ubicacion = (String) ent_ubicacion.getSelectedItem();
                double latitud = ((Number) ent_lat.getValue()).doubleValue();
                double longitud = ((Number) ent_lon.getValue()).doubleValue();
                String intencionalidad = (String) ent_intencionalidad.getSelectedItem();
                String descripcion = (String) ent_descripcion.getText();

                switch (usuario.denunciarVecino(ubicacion, latitud, longitud, intencionalidad, descripcion)) {
                    case 0:
                        JOptionPane.showMessageDialog(null, "El largo de la descripción no es correcto", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "No se pudo establecer conexión con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "No superas el tiempo límite de intervalo entre denuncias", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 3:
                        JOptionPane.showMessageDialog(null, "Denuncia registrada con éxito", "Denuncia", JOptionPane.INFORMATION_MESSAGE);
                        ent_descripcion.setText("");
                        break;
                }
            }
        });

    }

}
