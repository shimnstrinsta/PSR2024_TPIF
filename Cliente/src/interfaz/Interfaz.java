package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import persona.Persona;
import javax.swing.table.TableColumn;
import java.util.LinkedList;
import java.util.Hashtable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.border.LineBorder;

public class Interfaz {

    protected JFrame ventana;

    protected final Font fnt_titulo = new Font("Arial", Font.BOLD, 20);
    protected final Font fnt_txt_normal = new Font("Arial", Font.BOLD, 13);
    protected final Font fnt_txt_pequeño = new Font("Arial", Font.BOLD, 11);

    protected final Color color_principal = Color.decode("#45a049");
    protected final Color color_secundario = Color.decode("#4CAF50");
    protected final Color color_fondo = Color.decode("#FFFFFF");

    protected Container contenedor_main;
    protected CardLayout cl;
    private Persona persona;

    // -------------- Componentes -------------- //
    protected JFrame crearVentana(int d1, int d2) {

        JFrame ventanaNueva = new JFrame("Sistema de sensores de incendio forestal");

        ventanaNueva.setSize(d1, d2);
        ventanaNueva.setLocationRelativeTo(null);
        ventanaNueva.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaNueva.setResizable(false);
        ventanaNueva.setBackground(color_fondo);
        ventanaNueva.setVisible(true);

        contenedor_main = ventanaNueva.getContentPane();
        contenedor_main.setLayout(new CardLayout());
        cl = (CardLayout) contenedor_main.getLayout();
        return ventanaNueva;
    }

    protected JButton boton(String texto, JPanel contenedor) {
        JButton btn = new JButton(texto);

        btn.setFocusPainted(false);

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setFont(fnt_txt_pequeño);

        btn.setBackground(color_principal);
        btn.setForeground(Color.white);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        contenedor.add(btn);
        return btn;
    }

    protected JButton boton(String texto, JPanel contenedor, JTable tabla) {
        JButton btn = boton(texto, contenedor);

        btn.setEnabled(false);

        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btn.setEnabled(true);
            }
        });

        return btn;
    }

    protected JButton boton(String texto) {
        JButton btn = new JButton(texto);

        btn.setFocusPainted(false);

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setFont(fnt_txt_pequeño);

        btn.setBackground(color_principal);
        btn.setForeground(Color.white);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    protected JButton botonInvisible(String texto, JPanel contenedor) {
        JButton btn = new JButton(texto);

        btn.setBorderPainted(false);       // No pintar el borde
        btn.setContentAreaFilled(false);   // No rellenar el área del contenido
        btn.setFocusPainted(false);        // No pintar el borde de enfoque
        btn.setOpaque(false);

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setFont(fnt_txt_pequeño);

        btn.setBackground(Color.WHITE);
        btn.setForeground(color_principal);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        contenedor.add(btn);
        return btn;
    }

    protected JButton botonInvisible(String texto) {
        JButton btn = new JButton(texto);

        btn.setBorderPainted(false);       // No pintar el borde
        btn.setContentAreaFilled(false);   // No rellenar el área del contenido
        btn.setFocusPainted(false);        // No pintar el borde de enfoque
        btn.setOpaque(false);

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setFont(fnt_txt_pequeño);

        btn.setBackground(Color.WHITE);
        btn.setForeground(color_principal);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    protected JLabel titulo(String texto, JPanel contenedor) {
        JLabel titulo = new JLabel(texto);

        titulo.setFont(fnt_titulo);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenedor.add(titulo);

        titulo.setBorder(new EmptyBorder(20, 0, 20, 0));

        return titulo;
    }

    protected JLabel textoNormal(String texto) {
        JLabel txt = new JLabel(texto);

        txt.setFont(fnt_txt_normal);
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);

        txt.setBorder(new EmptyBorder(5, 0, 5, 0));

        return txt;
    }

    protected JLabel textoNormal(String texto, JPanel contenedor) {
        JLabel txt = new JLabel(texto);

        txt.setFont(fnt_txt_normal);
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenedor.add(txt);

        txt.setBorder(new EmptyBorder(5, 0, 5, 0));

        return txt;
    }

    protected JLabel textoNormal(String texto, int s1, int s2, JPanel contenedor) {
        JLabel txt = new JLabel(texto);

        txt.setFont(fnt_txt_normal);
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenedor.add(txt);

        txt.setBorder(new EmptyBorder(s1, 0, s2, 0));

        return txt;
    }

    protected JLabel textoNormal(String texto, int s1, int s2) {
        JLabel txt = new JLabel(texto);

        txt.setFont(fnt_txt_normal);
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);

        txt.setBorder(new EmptyBorder(s1, 0, s2, 0));

        return txt;
    }

    protected JPanel panel(String nombre, int dx, int dy, int b1, int b2, int b3, int b4) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBackground(color_fondo);

        panel.setMinimumSize(new Dimension(dx, dy));
        panel.setBorder(new EmptyBorder(b1, b2, b3, b4));
        ventana.add(panel);
        contenedor_main.add(panel, nombre);
        return panel;
    }

    protected JPanel panel(String nombre, int b1, int b2, int b3, int b4) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBackground(color_fondo);

        panel.setBorder(new EmptyBorder(b1, b2, b3, b4));
        ventana.add(panel);
        contenedor_main.add(panel, nombre);
        return panel;
    }

    protected JPanel panel(String nombre) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBackground(color_fondo);

        contenedor_main.add(panel, nombre);
        return panel;
    }

    protected JPanel panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBackground(color_fondo);

        return panel;
    }

    protected JPanel grilla(int filas, int columnas) {
        JPanel panel = new JPanel(new GridLayout(filas, columnas, 15, 15));

        panel.setBackground(color_fondo);

        return panel;
    }

    protected JTextField entradaTexto(int d1, int d2, JPanel contenedor) {
        JTextField ent = new JTextField();

        ent.setAlignmentX(Component.CENTER_ALIGNMENT);
        ent.setMaximumSize(new Dimension(d1, d2));
        ent.setFont(fnt_txt_normal);

        contenedor.add(Box.createVerticalStrut(5));
        contenedor.add(ent);
        contenedor.add(Box.createVerticalStrut(5));

        return ent;
    }

    protected JPasswordField entradaTextoContrasenia(int d1, int d2, JPanel contenedor) {
        JPasswordField ent = new JPasswordField();

        ent.setAlignmentX(Component.CENTER_ALIGNMENT);

        ent.setMaximumSize(new Dimension(d1, d2));
        ent.setFont(fnt_txt_normal);

        contenedor.add(Box.createVerticalStrut(5));
        contenedor.add(ent);
        contenedor.add(Box.createVerticalStrut(5));

        return ent;
    }

    protected JTextArea bancoTexto(int d1, int d2, JPanel contenedor) {
        JTextArea texto = new JTextArea();

        texto.setMaximumSize(new Dimension(d1, d2));
        texto.setFont(fnt_txt_normal);
        texto.setBorder(new CompoundBorder(new LineBorder(Color.black, 1), new EmptyBorder(10, 10, 10, 10)));
        texto.setAlignmentX(Component.CENTER_ALIGNMENT);
        texto.setLineWrap(true);

        contenedor.add(texto);

        return texto;
    }

    protected JTextArea bancoTexto(int d1, int d2) {
        JTextArea texto = new JTextArea();

        texto.setMaximumSize(new Dimension(d1, d2));
        texto.setFont(fnt_txt_normal);
        texto.setBorder(new CompoundBorder(new LineBorder(Color.black, 1), new EmptyBorder(10, 10, 10, 10)));
        texto.setAlignmentX(Component.CENTER_ALIGNMENT);
        texto.setLineWrap(true);
        texto.setEditable(false);

        return texto;
    }

    protected JMenuBar menuBarra(JFrame ventana) {
        JMenuBar menu = new JMenuBar();

        menu.setBackground(color_principal);
        ventana.setJMenuBar(menu);
        return menu;
    }

    protected JMenuItem menuItem(JMenuBar menu, String texto, int d1, int d2, String icon_n) {
        JMenuItem item = new JMenuItem(texto);
        ImageIcon icon = new ImageIcon("./icons/" + icon_n + ".png");

        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setForeground(color_fondo);
        item.setBackground(color_principal);
        item.setIcon(icon);
        item.setFont(fnt_txt_normal);
        item.setMaximumSize(new Dimension(d1, d2));

        menu.add(item);

        return item;
    }

    protected JMenuItem menuItem(JMenu menu, String texto) {
        JMenuItem item = new JMenuItem(texto);

        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setBackground(color_fondo);
        item.setFont(fnt_txt_normal);
        menu.add(item);

        return item;
    }

    protected JMenu menu(JMenuBar menuPrincipal, String texto, String icon_n) {
        JMenu menu = new JMenu(texto);
        ImageIcon icon = new ImageIcon("./icons/" + icon_n + ".png");

        menu.setBackground(color_principal);
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menu.setForeground(color_fondo);
        menu.setIcon(icon);
        menu.setFont(fnt_txt_normal);

        menuPrincipal.add(menu);

        return menu;
    }

    protected JComboBox comboBox(String[] opciones, JPanel contenedor) {
        JComboBox combo = new JComboBox(opciones);

        combo.setBackground(color_fondo);
        combo.setFont(fnt_txt_normal);

        combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contenedor.add(combo);
        return combo;

    }

    protected JSpinner incrementador(int minimo, int maximo, JPanel contenedor) {
        SpinnerNumberModel modelo = new SpinnerNumberModel(minimo, minimo, maximo, 1);

        JSpinner incrementador = new JSpinner(modelo);

        incrementador.setBackground(color_fondo);
        contenedor.add(incrementador);
        incrementador.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return incrementador;
    }
    
    protected JSpinner incrementador(double minimo, double maximo, JPanel contenedor) {
        SpinnerNumberModel modelo = new SpinnerNumberModel(minimo, minimo, maximo, 1);

        JSpinner incrementador = new JSpinner(modelo);

        incrementador.setBackground(color_fondo);
        contenedor.add(incrementador);
        incrementador.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return incrementador;
    }

    protected JSpinner incrementador(JPanel contenedor, JComboBox dependencia, String indice) {
        SpinnerNumberModel modelo = new SpinnerNumberModel();
        JSpinner incrementador = new JSpinner(modelo);

        DecimalFormat formato = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        formato.setGroupingUsed(false);
        formato.setMinimumFractionDigits(1);
        formato.setMaximumFractionDigits(7);

        incrementador.setEditor(new JSpinner.NumberEditor(incrementador, formato.toPattern()));
        incrementador.setEditor(new JSpinner.NumberEditor(incrementador, "00.000000"));

        incrementador.setBackground(color_fondo);
        contenedor.add(incrementador);
        cambiarIncrementador(incrementador,modelo,dependencia,indice,formato);
        
        dependencia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarIncrementador(incrementador,modelo,dependencia,indice,formato);
            }
        });

        incrementador.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return incrementador;
    }

    protected void cambiarIncrementador(JSpinner incrementador,SpinnerNumberModel modelo, JComboBox dependencia,String indice,DecimalFormat formato) {
        String seleccion = (String) dependencia.getSelectedItem();
        LinkedList<Hashtable<String, String>> datos = Persona.seleccionarUbicaciones();

        for (Hashtable<String, String> ubicacion : datos) {
            if (ubicacion.get("Nombre").equals(seleccion)) {
                try {

                    double l_inferior = Double.parseDouble(ubicacion.get("Li" + indice));
                    double l_superior = Double.parseDouble(ubicacion.get("Ls" + indice));

                    modelo.setMinimum(l_inferior);
                    modelo.setMaximum(l_superior);
                    modelo.setValue(l_inferior);  // Asignar valor minimo
                    modelo.setStepSize(0.0010);

                    incrementador.setEditor(new JSpinner.NumberEditor(incrementador, formato.toPattern()));

                } catch (NumberFormatException ex) {                    
                }
            }
        }
    }

    protected JTable tabla(String[] columnas, Object[][] datos, int[] tamanios, JPanel contenedor) {

        DefaultTableModel model = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Todas las celdas no editables
                return false;
            }
        };

        JTable table = new JTable(model);
        int max_tamanio = 0;

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        contenedor.add(scrollPane);

        for (int i = 0; i < tamanios.length; i++) {
            TableColumn columna = table.getColumnModel().getColumn(i);
            columna.setPreferredWidth(tamanios[i]);
            max_tamanio += tamanios[i];
        }

        scrollPane.setMaximumSize(new Dimension(max_tamanio, 400));
        contenedor.revalidate();
        contenedor.repaint();

        return table;
    }

    protected JDialog ventanaEmergente(String titulo, int d1, int d2) {
        JDialog dialog = new JDialog(ventana, titulo, true);

        dialog.setSize(d1, d2);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setBackground(color_fondo);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        return dialog;
    }

    protected JButton panelAceptarCancelar(String textoAceptar, JPanel contenedor, JDialog ventanaEmergente) {
        JPanel btns = new JPanel();
        JButton btn_cancelar = boton("Cancelar", btns);
        JButton btn_agregar = boton(textoAceptar, btns);
        btns.setBackground(color_fondo);

        contenedor.add(btns);
        btn_cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventanaEmergente.dispose();
            }
        });

        ventanaEmergente.pack();  // Ajusta el tamaño de la ventana        
        return btn_agregar;
    }

    protected JCheckBox checkBox(String texto, JPanel contenedor) {
        JCheckBox checkBox = new JCheckBox(texto);

        contenedor.add(checkBox);
        checkBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkBox.setFont(fnt_txt_normal);
        checkBox.setBackground(color_fondo);
        checkBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return checkBox;
    }

    protected JCheckBox checkBox(String texto) {
        JCheckBox checkBox = new JCheckBox(texto);

        checkBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkBox.setFont(fnt_txt_normal);
        checkBox.setBackground(color_fondo);
        checkBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return checkBox;
    }

    // ---------------------------------------- //
    public void iniciar() {
        ventana = crearVentana(400, 500);

        iniciarSesion();
        crearCuenta();

        cl.show(contenedor_main, "iniciarSesion");
        persona = new Persona();
    }

    private void iniciarSesion() {

        JPanel panel = Interfaz.this.panel("iniciarSesion", 10, 20, 10, 20);

        titulo("Iniciar sesión", panel);
        Interfaz.this.textoNormal("Nombre", panel);
        JTextField ent_nombre = entradaTexto(200, 25, panel);
        Interfaz.this.textoNormal("Contraseña", panel);
        JPasswordField ent_contrasenia = entradaTextoContrasenia(200, 25, panel);
        JButton btn_iniciar_sesion = boton("Iniciar sesión", panel);
        textoNormal("¿No tiene cuenta?", 30, 0, panel);
        JButton btn_crear_cuenta = botonInvisible("Crear cuenta", panel);

        btn_crear_cuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(contenedor_main, "crearCuenta");
            }
        });

        btn_iniciar_sesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = ent_nombre.getText();
                String contrasenia = ent_contrasenia.getText();

                switch (persona.iniciarSesion(usuario, contrasenia)) {
                    case 0:
                        JOptionPane.showMessageDialog(null, "No es posible conectarse con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Los datos no cumplen con el formato", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "No hay ningún perfil que coincida con esas credenciales", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 3:
                        new InterfazUsuario(persona);
                        ventana.dispose();
                        break;
                    case 4:
                        new InterfazAdmin(persona);
                        ventana.dispose();
                        break;

                }

            }
        });
    }

    private void crearCuenta() {

        JPanel panel = Interfaz.this.panel("crearCuenta", 10, 20, 10, 20);

        titulo("Crear cuenta", panel);
        Interfaz.this.textoNormal("Nombre", panel);
        JTextField ent_nombre = entradaTexto(200, 25, panel);
        Interfaz.this.textoNormal("Contraseña", panel);
        JPasswordField ent_contrasenia = entradaTextoContrasenia(200, 25, panel);
        JButton btn_crear_cuenta = boton("Crear cuenta", panel);
        textoNormal("¿Ya tiene cuenta?", 30, 0, panel);
        JButton btn_iniciar_sesion = botonInvisible("Iniciar sesión", panel);

        btn_crear_cuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = ent_nombre.getText();
                String contrasenia = ent_contrasenia.getText();

                switch (persona.crearCuenta(usuario, contrasenia)) {
                    case 0:
                        JOptionPane.showMessageDialog(null, "No es posible conectarse con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Los datos no cumplen con el formato", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "Ya hay un perfil con ese nombre de usuario", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 3:
                        new InterfazUsuario(persona);
                        ventana.dispose();
                        break;

                }
            }
        });

        btn_iniciar_sesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(contenedor_main, "iniciarSesion");
            }
        });
    }

}
