package com.hibernatestandalone.pantallas;

import com.hibernatestandalone.entity.Empleado;
import com.hibernatestandalone.entity.Gerente;
import com.hibernatestandalone.entity.Usuario;
import com.hibernatestandalone.services.UsuarioService;
import java.awt.Color;
import javax.swing.JOptionPane;

public class InicioSesion extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(InicioSesion.class.getName());

    public InicioSesion() {
        initComponents();
        this.setFocusable(true);
        this.requestFocusInWindow();
        lblCargando.setVisible(false);
        txtContrasenia.setEchoChar((char) 0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblIniciarSesion = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        txtContrasenia = new javax.swing.JPasswordField();
        btnIniciarSesion = new javax.swing.JButton();
        lblCargando = new javax.swing.JLabel();
        lblFondoSesion = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        lblContrasenia = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        lblIniciarSesion.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblIniciarSesion.setText("INICIAR SESIÓN");

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtEmail.setForeground(new java.awt.Color(150, 150, 150));
        txtEmail.setText("Ingrese su correo electrónico");
        txtEmail.setBorder(null);
        txtEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEmailFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmailFocusLost(evt);
            }
        });
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        txtContrasenia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtContrasenia.setForeground(new java.awt.Color(150, 150, 150));
        txtContrasenia.setText("Ingrese su contraseña");
        txtContrasenia.setBorder(null);
        txtContrasenia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtContraseniaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtContraseniaFocusLost(evt);
            }
        });
        txtContrasenia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContraseniaActionPerformed(evt);
            }
        });

        btnIniciarSesion.setBackground(new java.awt.Color(0, 102, 204));
        btnIniciarSesion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnIniciarSesion.setForeground(new java.awt.Color(255, 255, 255));
        btnIniciarSesion.setText("ENTRAR");
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarSesionActionPerformed(evt);
            }
        });

        lblCargando.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCargando.setForeground(new java.awt.Color(153, 153, 153));
        lblCargando.setText("Iniciando sesión...");

        lblFondoSesion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FondoSesion.jpg"))); // NOI18N
        lblFondoSesion.setText("jLabel2");

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Logo.png"))); // NOI18N

        lblContrasenia.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblContrasenia.setText("CONTRASEÑA");

        lblEmail.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblEmail.setText("CORREO ELECTRÓNICO");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                                .addGap(67, 67, 67))
                            .addComponent(lblIniciarSesion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtEmail)
                                .addGap(15, 15, 15))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblContrasenia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(145, 145, 145))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtContrasenia)
                                .addGap(69, 69, 69))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblCargando, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(113, 113, 113))
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnIniciarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(136, 136, 136))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(lblLogo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(lblFondoSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 716, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblLogo)
                .addGap(54, 54, 54)
                .addComponent(lblIniciarSesion)
                .addGap(28, 28, 28)
                .addComponent(lblEmail)
                .addGap(18, 18, 18)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(lblContrasenia)
                .addGap(18, 18, 18)
                .addComponent(txtContrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(btnIniciarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCargando)
                .addContainerGap(104, Short.MAX_VALUE))
            .addComponent(lblFondoSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIniciarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarSesionActionPerformed
        lblCargando.setVisible(true); // Mostrar mensaje de carga

        // Deshabilitar botón para evitar clicks múltiples
        btnIniciarSesion.setEnabled(false);

        // Crear un SwingWorker para hacer la tarea en background
        new javax.swing.SwingWorker<Usuario, Void>() {
            @Override
            protected Usuario doInBackground() throws Exception {
                String email = txtEmail.getText();
                String contrasenia = String.valueOf(txtContrasenia.getPassword());

                UsuarioService servicio = new UsuarioService();
                return servicio.iniciarSesion(email, contrasenia);
            }

            @Override
            protected void done() {
                try {
                    Usuario usuario = get(); // obtener resultado

                    lblCargando.setVisible(false); // Ocultar mensaje de carga
                    btnIniciarSesion.setEnabled(true);

                    if (usuario != null) {
                        if (usuario instanceof Gerente) {
                            PantallaGerente pantalla = new PantallaGerente((Gerente) usuario);
                            pantalla.setVisible(true);
                            pantalla.setLocationRelativeTo(null);
                            dispose();
                        } else if (usuario instanceof Empleado) {
                            PantallaEmpleado pantalla = new PantallaEmpleado((Empleado) usuario);
                            pantalla.setVisible(true);
                            pantalla.setLocationRelativeTo(null);
                            dispose();
                        }
                    } else {
                        JOptionPane.showMessageDialog(InicioSesion.this, "Credenciales incorrectas");
                    }

                } catch (Exception ex) {
                    lblCargando.setVisible(false);
                    btnIniciarSesion.setEnabled(true);
                    JOptionPane.showMessageDialog(InicioSesion.this, "Error: " + ex.getMessage());
                }
            }
        }.execute();
    }//GEN-LAST:event_btnIniciarSesionActionPerformed

    private void txtContraseniaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContraseniaActionPerformed

    }//GEN-LAST:event_txtContraseniaActionPerformed

    private void txtContraseniaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContraseniaFocusLost
        if (String.valueOf(txtContrasenia.getPassword()).isEmpty()) {
            txtContrasenia.setText("Ingrese su contraseña");
            txtContrasenia.setForeground(new java.awt.Color(150, 150, 150));
            txtContrasenia.setEchoChar((char) 0); // Mostrar texto visible
        }
    }//GEN-LAST:event_txtContraseniaFocusLost

    private void txtContraseniaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContraseniaFocusGained
        if (String.valueOf(txtContrasenia.getPassword()).equals("Ingrese su contraseña")) {
            txtContrasenia.setText("");
            txtContrasenia.setForeground(Color.BLACK);
            txtContrasenia.setEchoChar('•'); // Activar asteriscos
        }
    }//GEN-LAST:event_txtContraseniaFocusGained

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusLost
        if (txtEmail.getText().isEmpty()) {
            txtEmail.setForeground(new java.awt.Color(150, 150, 150));
            txtEmail.setText("Ingrese su correo electrónico");
        }
    }//GEN-LAST:event_txtEmailFocusLost

    private void txtEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusGained
        if (txtEmail.getText().equals("Ingrese su correo electrónico")) {
            txtEmail.setText("");
            txtEmail.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtEmailFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIniciarSesion;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel lblCargando;
    private javax.swing.JLabel lblContrasenia;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFondoSesion;
    private javax.swing.JLabel lblIniciarSesion;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JPasswordField txtContrasenia;
    private javax.swing.JTextField txtEmail;
    // End of variables declaration//GEN-END:variables
}
