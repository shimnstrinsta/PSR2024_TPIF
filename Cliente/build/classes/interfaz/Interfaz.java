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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import persona.Persona;

public class Interfaz {

    protected JFrame ventana;

    protected final Font fnt_titulo = new Font("Arial", Font.BOLD, 20);
    protected final Font fnt_txt_normal = new Font("Arial", Font.BOLD, 13);
    protected final Font fnt_txt_pequeño = new Font("Arial", Font.BOLD, 11);

    protected final Color color_principal = Color.decode("#45a049");
    protected final Color color_secundario = Color.decode("#4CAF50");
    protected final Color color_fondo = Color.WHITE;

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
        ventanaNueva.setBackground(Color.WHITE);
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

    protected JLabel titulo(String texto, JPanel contenedor) {
        JLabel titulo = new JLabel(texto);

        titulo.setFont(fnt_titulo);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenedor.add(titulo);

        titulo.setBorder(new EmptyBorder(20, 0, 20, 0));

        return titulo;
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

    // ---------------------------------------- //
    public void iniciar() {
        ventana = crearVentana(400,500);

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

        ent_nombre.setText("david");
        ent_contrasenia.setText("123456789");

        //ent_contrasenia.setMaximumSize(new Dimension(200, 25));
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

        //ent_contrasenia.setMaximumSize(new Dimension(200, 25));
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
