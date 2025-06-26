package com.hibernatestandalone.pantallas;

import com.hibernatestandalone.entity.Empleado;
import com.hibernatestandalone.entity.Gerente;
import com.hibernatestandalone.entity.Habitacion;
import com.hibernatestandalone.entity.Usuario;
import com.hibernatestandalone.services.EmpleadoService;
import com.hibernatestandalone.services.GerenteService;
import com.hibernatestandalone.services.HabitacionService;
import com.hibernatestandalone.services.UsuarioService;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;


public class PantallaGerente extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PantallaGerente.class.getName());
    private Long idEmpleadoActual;
    private Long idHabitacionActual;
    private final UsuarioService usuarioService = new UsuarioService();
    private final GerenteService gerenteService = new GerenteService();
    private final HabitacionService habitacionService = new HabitacionService();
    private TableRowSorter<DefaultTableModel> sorterEmpleados;
    private TableRowSorter<DefaultTableModel> sorterHabitaciones;
    
    


    public PantallaGerente(Gerente gerente) {
        initComponents();
        jPanelCargarEmpleado.setVisible(false);
        jPanelConsultarIngresos.setVisible(false);
        jPanelModificarHabitaciones.setVisible(false);
        jPanelModificarEmpleado.setVisible(false);
        cargarEmpleadosEnTabla();
        cargarHabitacionesEnTabla();
        
        lblBienvenido.setText("Bienvenido, " + gerente.getNombre() + " " + gerente.getApellido());
    }
    
 
    private void cargarEmpleadosEnTabla() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // La primera columna será booleana (checkbox)
                return columnIndex == 0 ? Boolean.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo la primera columna (checkbox) será editable
                return column == 0;
            }
        };

        JTableHeader header = tblEmpleados.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 22)); 
        header.setForeground(Color.WHITE);                  
        header.setBackground(new Color(232,130,0));       

        modelo.addColumn("");
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Email");
        modelo.addColumn("DNI");
        modelo.addColumn("Teléfono");

        List<Empleado> empleados = new EmpleadoService().getAll();

        for (Empleado e : empleados) {
            modelo.addRow(new Object[]{
                false, // valor inicial del checkbox
                e.getId_usuario(),
                e.getNombre(),
                e.getApellido(),
                e.getEmail(),
                e.getDni(),
                e.getTelefono()
            });
        }
        
        tblEmpleados.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 0) { // Columna del checkbox
                for (int i = 0; i < tblEmpleados.getRowCount(); i++) {
                    if (i != e.getFirstRow()) {
                        tblEmpleados.setValueAt(false, i, 0);
                    }
                }
            }
        });
        
        tblEmpleados.setModel(modelo);
        
        tblEmpleados.getColumnModel().getColumn(0).setPreferredWidth(40); 
        tblEmpleados.getColumnModel().getColumn(0).setMinWidth(40); 
        tblEmpleados.getColumnModel().getColumn(0).setMaxWidth(40); 
        tblEmpleados.getColumnModel().getColumn(0).setResizable(false); 
        
        tblEmpleados.getColumnModel().getColumn(1).setPreferredWidth(100); 
        tblEmpleados.getColumnModel().getColumn(1).setMinWidth(100); 
        tblEmpleados.getColumnModel().getColumn(1).setMaxWidth(100); 
        tblEmpleados.getColumnModel().getColumn(1).setResizable(false); 
        
        tblEmpleados.getColumnModel().getColumn(2).setPreferredWidth(150); 
        tblEmpleados.getColumnModel().getColumn(2).setMinWidth(150); 
        tblEmpleados.getColumnModel().getColumn(2).setMaxWidth(150); 
        tblEmpleados.getColumnModel().getColumn(2).setResizable(false); 
        
        tblEmpleados.getColumnModel().getColumn(3).setPreferredWidth(150); 
        tblEmpleados.getColumnModel().getColumn(3).setMinWidth(150); 
        tblEmpleados.getColumnModel().getColumn(3).setMaxWidth(150); 
        tblEmpleados.getColumnModel().getColumn(3).setResizable(false); 
        
        tblEmpleados.getColumnModel().getColumn(4).setPreferredWidth(350); 
        tblEmpleados.getColumnModel().getColumn(4).setMinWidth(350); 
        tblEmpleados.getColumnModel().getColumn(4).setMaxWidth(350); 
        tblEmpleados.getColumnModel().getColumn(4).setResizable(false); 
        
        tblEmpleados.getColumnModel().getColumn(5).setPreferredWidth(140); 
        tblEmpleados.getColumnModel().getColumn(5).setMinWidth(140); 
        tblEmpleados.getColumnModel().getColumn(5).setMaxWidth(140); 
        tblEmpleados.getColumnModel().getColumn(5).setResizable(false); 
        
        sorterEmpleados = new TableRowSorter<>(modelo);
        tblEmpleados.setRowSorter(sorterEmpleados);
        tblEmpleados.getTableHeader().setReorderingAllowed(false);
        
       
    }
    
    private void cargarHabitacionesEnTabla() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // La primera columna será booleana (checkbox)
                return columnIndex == 0 ? Boolean.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo la primera columna (checkbox) será editable
                return column == 0;
            }
        };

        JTableHeader header = tblHabitaciones.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 22)); // Cambia fuente y tamaño
        header.setForeground(Color.WHITE);                  // Color del texto
        header.setBackground(new Color(232,130,0));       // Color de fondo (azul)

        modelo.addColumn("");
        modelo.addColumn("Numero");
        modelo.addColumn("Piso");
        modelo.addColumn("Capacidad de personas");
        modelo.addColumn("Precio");
        modelo.addColumn("Descripcion");
        modelo.addColumn("ID");

        List<Habitacion> habitaciones = new HabitacionService().getAll();

        for (Habitacion h : habitaciones) {
            modelo.addRow(new Object[]{
                false, // valor inicial del checkbox
                h.getNumero(),
                h.getPiso(),
                h.getCapacidad_personas(),
                h.getPrecio_por_noche(),
                h.getDescripcion(),
                h.getId()
            });
        }
        
        tblHabitaciones.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 0) { // Columna del checkbox
                for (int i = 0; i < tblHabitaciones.getRowCount(); i++) {
                    if (i != e.getFirstRow()) {
                        tblHabitaciones.setValueAt(false, i, 0);
                    }
                }
            }
        });
        
        tblHabitaciones.setRowSorter(null); // Remover sorter temporalmente
        tblHabitaciones.setModel(modelo);
        
        tblHabitaciones.getColumnModel().getColumn(0).setPreferredWidth(40); 
        tblHabitaciones.getColumnModel().getColumn(0).setMinWidth(40); 
        tblHabitaciones.getColumnModel().getColumn(0).setMaxWidth(40); 
        tblHabitaciones.getColumnModel().getColumn(0).setResizable(false); 
        
        tblHabitaciones.getColumnModel().getColumn(1).setPreferredWidth(120);; 
        tblHabitaciones.getColumnModel().getColumn(1).setMinWidth(120);; 
        tblHabitaciones.getColumnModel().getColumn(1).setMaxWidth(120); 
        tblHabitaciones.getColumnModel().getColumn(1).setResizable(false); 
        
        tblHabitaciones.getColumnModel().getColumn(2).setPreferredWidth(80); 
        tblHabitaciones.getColumnModel().getColumn(2).setMinWidth(80); 
        tblHabitaciones.getColumnModel().getColumn(2).setMaxWidth(80); 
        tblHabitaciones.getColumnModel().getColumn(2).setResizable(false); 
        
        tblHabitaciones.getColumnModel().getColumn(3).setPreferredWidth(250); 
        tblHabitaciones.getColumnModel().getColumn(3).setMinWidth(250); 
        tblHabitaciones.getColumnModel().getColumn(3).setMaxWidth(250); 
        tblHabitaciones.getColumnModel().getColumn(3).setResizable(false); 
        
        tblHabitaciones.getColumnModel().getColumn(4).setPreferredWidth(120); 
        tblHabitaciones.getColumnModel().getColumn(4).setMinWidth(120); 
        tblHabitaciones.getColumnModel().getColumn(4).setMaxWidth(120); 
        tblHabitaciones.getColumnModel().getColumn(4).setResizable(false); 
        
        tblHabitaciones.getColumnModel().getColumn(6).setMinWidth(0);
        tblHabitaciones.getColumnModel().getColumn(6).setMaxWidth(0);
        tblHabitaciones.getColumnModel().getColumn(6).setWidth(0);
        tblHabitaciones.getColumnModel().getColumn(6).setPreferredWidth(0);
        tblHabitaciones.getColumnModel().getColumn(6).setResizable(false);
        
        sorterHabitaciones = new TableRowSorter<>(modelo);
        tblHabitaciones.setRowSorter(sorterHabitaciones);
        tblHabitaciones.getTableHeader().setReorderingAllowed(false);
        
        
      
        tblHabitaciones.revalidate();
        tblHabitaciones.repaint();
  }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnCargarEmpleado = new javax.swing.JButton();
        btnModificarEmpleado = new javax.swing.JButton();
        btnConsultarIngresos = new javax.swing.JButton();
        btnModificarHabitaciones = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnMiPerfil = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        lblBienvenido = new javax.swing.JLabel();
        jPanelContenido = new javax.swing.JPanel();
        jPanelCargarEmpleado = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblApellido = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        lblDni = new javax.swing.JLabel();
        lblNombre2 = new javax.swing.JLabel();
        lblContrasenia = new javax.swing.JLabel();
        lblTelefono = new javax.swing.JLabel();
        txtDni = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtContrasenia = new javax.swing.JPasswordField();
        btnConfirmar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jPanelConsultarIngresos = new javax.swing.JPanel();
        jPanelModificarHabitaciones = new javax.swing.JPanel();
        ScrollHabitaciones = new javax.swing.JScrollPane();
        tblHabitaciones = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtBuscarHabitacion = new javax.swing.JTextField();
        btnBuscarHabitacion = new javax.swing.JButton();
        btnModificarHabitacion1 = new javax.swing.JButton();
        jPanelModificarEmpleado = new javax.swing.JPanel();
        ScrollEmpleados = new javax.swing.JScrollPane();
        tblEmpleados = new javax.swing.JTable();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtBuscarEmpleado = new javax.swing.JTextField();
        btnBuscarEmpleado = new javax.swing.JButton();
        jSubPanelModificarEmpleado = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        lblApellidoMod = new javax.swing.JLabel();
        lblNombreMod1 = new javax.swing.JLabel();
        lblDniMod = new javax.swing.JLabel();
        lblTelefonoMod = new javax.swing.JLabel();
        lblEmailMod = new javax.swing.JLabel();
        txtEmailMod = new javax.swing.JTextField();
        txtNombreMod = new javax.swing.JTextField();
        txtApellidoMod = new javax.swing.JTextField();
        txtDniMod = new javax.swing.JTextField();
        txtTelefonoMod = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        btnCancelarMod = new javax.swing.JButton();
        btnConfirmarMod = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(232, 130, 0));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Menú ");

        jSeparator1.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        btnCargarEmpleado.setBackground(new java.awt.Color(252, 167, 85));
        btnCargarEmpleado.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCargarEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        btnCargarEmpleado.setText("CARGAR NUEVO EMPLEADO");
        btnCargarEmpleado.setBorder(null);
        btnCargarEmpleado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnCargarEmpleado.setFocusPainted(false);
        btnCargarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarEmpleadoActionPerformed(evt);
            }
        });

        btnModificarEmpleado.setBackground(new java.awt.Color(252, 167, 85));
        btnModificarEmpleado.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnModificarEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        btnModificarEmpleado.setText("MODIFICAR EMPLEADO");
        btnModificarEmpleado.setBorder(null);
        btnModificarEmpleado.setFocusPainted(false);
        btnModificarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarEmpleadoActionPerformed(evt);
            }
        });

        btnConsultarIngresos.setBackground(new java.awt.Color(252, 167, 85));
        btnConsultarIngresos.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnConsultarIngresos.setForeground(new java.awt.Color(255, 255, 255));
        btnConsultarIngresos.setText("CONSULTAR INGRESOS POR FECHA");
        btnConsultarIngresos.setBorder(null);
        btnConsultarIngresos.setFocusPainted(false);
        btnConsultarIngresos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarIngresosActionPerformed(evt);
            }
        });

        btnModificarHabitaciones.setBackground(new java.awt.Color(252, 167, 85));
        btnModificarHabitaciones.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnModificarHabitaciones.setForeground(new java.awt.Color(255, 255, 255));
        btnModificarHabitaciones.setText("MODIFICAR HABITACIONES");
        btnModificarHabitaciones.setBorder(null);
        btnModificarHabitaciones.setFocusPainted(false);
        btnModificarHabitaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarHabitacionesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnCargarEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnModificarEmpleado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnConsultarIngresos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(47, Short.MAX_VALUE))
            .addComponent(btnModificarHabitaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(btnCargarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnModificarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnConsultarIngresos, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnModificarHabitaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(446, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(204, 102, 0));
        jPanel3.setAlignmentX(0.0F);
        jPanel3.setAlignmentY(0.0F);

        jPanel5.setBackground(new java.awt.Color(204, 102, 0));
        jPanel5.setAlignmentX(0.0F);
        jPanel5.setAlignmentY(0.0F);

        btnMiPerfil.setBackground(new java.awt.Color(0, 83, 188));
        btnMiPerfil.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnMiPerfil.setForeground(new java.awt.Color(255, 255, 255));
        btnMiPerfil.setText("Mi perfil");
        btnMiPerfil.setBorder(null);
        btnMiPerfil.setFocusPainted(false);
        btnMiPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMiPerfilActionPerformed(evt);
            }
        });

        btnSalir.setBackground(new java.awt.Color(204, 0, 0));
        btnSalir.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setText("Salir");
        btnSalir.setBorder(null);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        lblBienvenido.setBackground(new java.awt.Color(204, 102, 0));
        lblBienvenido.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblBienvenido.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(lblBienvenido, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnMiPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnMiPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblBienvenido, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanelContenido.setBackground(new java.awt.Color(255, 255, 255));
        jPanelContenido.setForeground(new java.awt.Color(255, 255, 255));

        jPanelCargarEmpleado.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCargarEmpleado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel4.setBackground(new java.awt.Color(232, 130, 0));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CARGAR EMPLEADO");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        lblEmail.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblEmail.setText("EMAIL");

        txtNombre.setBackground(new java.awt.Color(255, 255, 255));
        txtNombre.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNombre.setForeground(new java.awt.Color(150, 150, 150));
        txtNombre.setText("Ingrese el nombre");
        txtNombre.setBorder(null);
        txtNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNombreFocusLost(evt);
            }
        });
        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        lblApellido.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblApellido.setText("APELLIDO");

        txtApellido.setBackground(new java.awt.Color(255, 255, 255));
        txtApellido.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtApellido.setForeground(new java.awt.Color(150, 150, 150));
        txtApellido.setText("Ingrese el apellido");
        txtApellido.setBorder(null);
        txtApellido.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtApellidoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtApellidoFocusLost(evt);
            }
        });
        txtApellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellidoActionPerformed(evt);
            }
        });

        lblDni.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblDni.setText("DNI");

        lblNombre2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblNombre2.setText("NOMBRE");

        lblContrasenia.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblContrasenia.setText("CONTRASEÑA");

        lblTelefono.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTelefono.setText("TELÉFONO");

        txtDni.setBackground(new java.awt.Color(255, 255, 255));
        txtDni.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtDni.setForeground(new java.awt.Color(150, 150, 150));
        txtDni.setText("Ingrese el DNI");
        txtDni.setBorder(null);
        txtDni.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDniFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDniFocusLost(evt);
            }
        });
        txtDni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDniActionPerformed(evt);
            }
        });

        txtEmail.setBackground(new java.awt.Color(255, 255, 255));
        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtEmail.setForeground(new java.awt.Color(150, 150, 150));
        txtEmail.setText("Ingrese el correo electrónico");
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

        txtTelefono.setBackground(new java.awt.Color(255, 255, 255));
        txtTelefono.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTelefono.setForeground(new java.awt.Color(150, 150, 150));
        txtTelefono.setText("Ingrese el número de teléfono");
        txtTelefono.setBorder(null);
        txtTelefono.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTelefonoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTelefonoFocusLost(evt);
            }
        });
        txtTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoActionPerformed(evt);
            }
        });

        txtContrasenia.setBackground(new java.awt.Color(255, 255, 255));
        txtContrasenia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtContrasenia.setForeground(new java.awt.Color(150, 150, 150));
        txtContrasenia.setText("Ingrese la contraseña");
        txtContrasenia.setBorder(null);
        txtContrasenia.setPreferredSize(new java.awt.Dimension(159, 16));
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

        btnConfirmar.setBackground(new java.awt.Color(0, 153, 51));
        btnConfirmar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnConfirmar.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmar.setText("CONFIRMAR");
        btnConfirmar.setBorder(null);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(204, 0, 0));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("CANCELAR");
        btnCancelar.setBorder(null);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCargarEmpleadoLayout = new javax.swing.GroupLayout(jPanelCargarEmpleado);
        jPanelCargarEmpleado.setLayout(jPanelCargarEmpleadoLayout);
        jPanelCargarEmpleadoLayout.setHorizontalGroup(
            jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelCargarEmpleadoLayout.createSequentialGroup()
                .addGroup(jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCargarEmpleadoLayout.createSequentialGroup()
                        .addGap(283, 283, 283)
                        .addGroup(jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelCargarEmpleadoLayout.createSequentialGroup()
                                .addGroup(jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblContrasenia)
                                    .addComponent(txtContrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(178, 178, 178)
                                .addGroup(jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblDni, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblApellido)
                                    .addComponent(lblTelefono)
                                    .addComponent(txtTelefono)
                                    .addComponent(jSeparator7)
                                    .addComponent(jSeparator5)
                                    .addComponent(txtDni)
                                    .addComponent(jSeparator3)
                                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(lblNombre2)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelCargarEmpleadoLayout.createSequentialGroup()
                        .addGap(301, 301, 301)
                        .addComponent(btnConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(106, 106, 106)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(386, Short.MAX_VALUE))
        );
        jPanelCargarEmpleadoLayout.setVerticalGroup(
            jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCargarEmpleadoLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addGroup(jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDni, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblContrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtContrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(111, 111, 111)
                .addGroup(jPanelCargarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanelConsultarIngresos.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanelConsultarIngresosLayout = new javax.swing.GroupLayout(jPanelConsultarIngresos);
        jPanelConsultarIngresos.setLayout(jPanelConsultarIngresosLayout);
        jPanelConsultarIngresosLayout.setHorizontalGroup(
            jPanelConsultarIngresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 788, Short.MAX_VALUE)
        );
        jPanelConsultarIngresosLayout.setVerticalGroup(
            jPanelConsultarIngresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 790, Short.MAX_VALUE)
        );

        jPanelModificarHabitaciones.setBackground(new java.awt.Color(255, 255, 255));

        ScrollHabitaciones.setBackground(new java.awt.Color(255, 255, 255));
        ScrollHabitaciones.setBorder(null);

        tblHabitaciones.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        tblHabitaciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Seleccionar", "Numero", "Piso", "Capacidad de personas", "Precio", "Descripcion"
            }
        ));
        tblHabitaciones.setGridColor(new java.awt.Color(0, 0, 0));
        tblHabitaciones.setRowHeight(35);
        tblHabitaciones.setSelectionBackground(new java.awt.Color(255, 255, 255));
        tblHabitaciones.setSelectionForeground(new java.awt.Color(0, 0, 0));
        ScrollHabitaciones.setViewportView(tblHabitaciones);

        jPanel8.setBackground(new java.awt.Color(232, 130, 0));
        jPanel8.setPreferredSize(new java.awt.Dimension(223, 84));

        jLabel4.setBackground(new java.awt.Color(232, 130, 0));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("MODIFICAR HABITACIONES");

        txtBuscarHabitacion.setBackground(new java.awt.Color(255, 255, 255));
        txtBuscarHabitacion.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtBuscarHabitacion.setText("Buscar habitacion por numero, piso, etc");
        txtBuscarHabitacion.setBorder(null);
        txtBuscarHabitacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarHabitacionFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBuscarHabitacionFocusLost(evt);
            }
        });
        txtBuscarHabitacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarHabitacionActionPerformed(evt);
            }
        });

        btnBuscarHabitacion.setBackground(new java.awt.Color(56, 121, 185));
        btnBuscarHabitacion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscarHabitacion.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarHabitacion.setText("BUSCAR");
        btnBuscarHabitacion.setFocusPainted(false);
        btnBuscarHabitacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarHabitacionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtBuscarHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnBuscarHabitacion)
                .addGap(15, 15, 15))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscarHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        btnModificarHabitacion1.setBackground(new java.awt.Color(0, 153, 51));
        btnModificarHabitacion1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnModificarHabitacion1.setForeground(new java.awt.Color(255, 255, 255));
        btnModificarHabitacion1.setText("MODIFICAR");
        btnModificarHabitacion1.setBorder(null);
        btnModificarHabitacion1.setFocusPainted(false);
        btnModificarHabitacion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarHabitacion1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelModificarHabitacionesLayout = new javax.swing.GroupLayout(jPanelModificarHabitaciones);
        jPanelModificarHabitaciones.setLayout(jPanelModificarHabitacionesLayout);
        jPanelModificarHabitacionesLayout.setHorizontalGroup(
            jPanelModificarHabitacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ScrollHabitaciones, javax.swing.GroupLayout.DEFAULT_SIZE, 1271, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 1271, Short.MAX_VALUE)
            .addGroup(jPanelModificarHabitacionesLayout.createSequentialGroup()
                .addGap(445, 445, 445)
                .addComponent(btnModificarHabitacion1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelModificarHabitacionesLayout.setVerticalGroup(
            jPanelModificarHabitacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelModificarHabitacionesLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(ScrollHabitaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 690, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModificarHabitacion1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        jPanelModificarEmpleado.setBackground(new java.awt.Color(255, 255, 255));
        jPanelModificarEmpleado.setPreferredSize(new java.awt.Dimension(1271, 790));

        ScrollEmpleados.setBackground(new java.awt.Color(255, 255, 255));
        ScrollEmpleados.setBorder(null);

        tblEmpleados.setBackground(new java.awt.Color(255, 255, 255));
        tblEmpleados.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        tblEmpleados.setForeground(new java.awt.Color(0, 0, 0));
        tblEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblEmpleados.setGridColor(new java.awt.Color(0, 0, 0));
        tblEmpleados.setRowHeight(35);
        tblEmpleados.setSelectionBackground(new java.awt.Color(255, 255, 255));
        tblEmpleados.setSelectionForeground(new java.awt.Color(0, 0, 0));
        ScrollEmpleados.setViewportView(tblEmpleados);

        btnModificar.setBackground(new java.awt.Color(0, 153, 51));
        btnModificar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnModificar.setForeground(new java.awt.Color(255, 255, 255));
        btnModificar.setText("MODIFICAR");
        btnModificar.setBorder(null);
        btnModificar.setFocusPainted(false);
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(204, 0, 0));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("ELIMINAR");
        btnEliminar.setBorder(null);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(232, 130, 0));
        jPanel6.setPreferredSize(new java.awt.Dimension(223, 84));

        jLabel3.setBackground(new java.awt.Color(232, 130, 0));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("MODIFICAR EMPLEADOS");

        txtBuscarEmpleado.setBackground(new java.awt.Color(255, 255, 255));
        txtBuscarEmpleado.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtBuscarEmpleado.setText("Buscar empleado por nombre, dni, etc");
        txtBuscarEmpleado.setBorder(null);
        txtBuscarEmpleado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarEmpleadoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBuscarEmpleadoFocusLost(evt);
            }
        });
        txtBuscarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarEmpleadoActionPerformed(evt);
            }
        });

        btnBuscarEmpleado.setBackground(new java.awt.Color(56, 121, 185));
        btnBuscarEmpleado.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscarEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarEmpleado.setText("BUSCAR");
        btnBuscarEmpleado.setFocusPainted(false);
        btnBuscarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarEmpleadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 592, Short.MAX_VALUE)
                .addComponent(txtBuscarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btnBuscarEmpleado)
                .addGap(22, 22, 22))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtBuscarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jSubPanelModificarEmpleado.setBackground(new java.awt.Color(255, 255, 255));
        jSubPanelModificarEmpleado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel7.setBackground(new java.awt.Color(232, 130, 0));
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 84, Short.MAX_VALUE)
        );

        lblApellidoMod.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblApellidoMod.setText("APELLIDO");

        lblNombreMod1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblNombreMod1.setText("NOMBRE");

        lblDniMod.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblDniMod.setText("DNI");

        lblTelefonoMod.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTelefonoMod.setText("TELÉFONO");

        lblEmailMod.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblEmailMod.setText("EMAIL");

        txtEmailMod.setBackground(new java.awt.Color(255, 255, 255));
        txtEmailMod.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtEmailMod.setText("Ingrese el email");
        txtEmailMod.setBorder(null);
        txtEmailMod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEmailModFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmailModFocusLost(evt);
            }
        });

        txtNombreMod.setBackground(new java.awt.Color(255, 255, 255));
        txtNombreMod.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNombreMod.setText("Ingrese el nombre");
        txtNombreMod.setBorder(null);
        txtNombreMod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreModFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNombreModFocusLost(evt);
            }
        });

        txtApellidoMod.setBackground(new java.awt.Color(255, 255, 255));
        txtApellidoMod.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtApellidoMod.setText("Ingrese el apellido");
        txtApellidoMod.setBorder(null);
        txtApellidoMod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtApellidoModFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtApellidoModFocusLost(evt);
            }
        });

        txtDniMod.setBackground(new java.awt.Color(255, 255, 255));
        txtDniMod.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtDniMod.setText("Ingrese el DNI");
        txtDniMod.setBorder(null);
        txtDniMod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDniModFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDniModFocusLost(evt);
            }
        });

        txtTelefonoMod.setBackground(new java.awt.Color(255, 255, 255));
        txtTelefonoMod.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTelefonoMod.setText("Ingrese el número de teléfono");
        txtTelefonoMod.setBorder(null);
        txtTelefonoMod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTelefonoModFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTelefonoModFocusLost(evt);
            }
        });

        btnCancelarMod.setBackground(new java.awt.Color(204, 0, 0));
        btnCancelarMod.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCancelarMod.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelarMod.setText("CANCELAR");
        btnCancelarMod.setBorder(null);
        btnCancelarMod.setFocusPainted(false);
        btnCancelarMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarModActionPerformed(evt);
            }
        });

        btnConfirmarMod.setBackground(new java.awt.Color(0, 153, 51));
        btnConfirmarMod.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnConfirmarMod.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmarMod.setText("CONFIRMAR");
        btnConfirmarMod.setBorder(null);
        btnConfirmarMod.setFocusPainted(false);
        btnConfirmarMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarModActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jSubPanelModificarEmpleadoLayout = new javax.swing.GroupLayout(jSubPanelModificarEmpleado);
        jSubPanelModificarEmpleado.setLayout(jSubPanelModificarEmpleadoLayout);
        jSubPanelModificarEmpleadoLayout.setHorizontalGroup(
            jSubPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jSubPanelModificarEmpleadoLayout.createSequentialGroup()
                .addGroup(jSubPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jSubPanelModificarEmpleadoLayout.createSequentialGroup()
                        .addGap(273, 273, 273)
                        .addComponent(btnConfirmarMod, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(145, 145, 145)
                        .addComponent(btnCancelarMod, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jSubPanelModificarEmpleadoLayout.createSequentialGroup()
                        .addGap(290, 290, 290)
                        .addGroup(jSubPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTelefonoMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jSubPanelModificarEmpleadoLayout.createSequentialGroup()
                                .addGroup(jSubPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblEmailMod, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtEmailMod, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNombreMod, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTelefonoMod, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(36, 36, 36)
                                .addGroup(jSubPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtApellidoMod, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblApellidoMod, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblDniMod, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDniMod, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNombreMod1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(453, Short.MAX_VALUE))
        );
        jSubPanelModificarEmpleadoLayout.setVerticalGroup(
            jSubPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSubPanelModificarEmpleadoLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addGroup(jSubPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreMod1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblApellidoMod, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jSubPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellidoMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jSubPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(jSubPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDniMod, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmailMod, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jSubPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmailMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDniMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jSubPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(lblTelefonoMod, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTelefonoMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 139, Short.MAX_VALUE)
                .addGroup(jSubPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelarMod, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirmarMod, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(175, 175, 175))
        );

        javax.swing.GroupLayout jPanelModificarEmpleadoLayout = new javax.swing.GroupLayout(jPanelModificarEmpleado);
        jPanelModificarEmpleado.setLayout(jPanelModificarEmpleadoLayout);
        jPanelModificarEmpleadoLayout.setHorizontalGroup(
            jPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ScrollEmpleados)
            .addGroup(jPanelModificarEmpleadoLayout.createSequentialGroup()
                .addGap(305, 305, 305)
                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(157, 157, 157)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 1271, Short.MAX_VALUE)
            .addGroup(jPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSubPanelModificarEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelModificarEmpleadoLayout.setVerticalGroup(
            jPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelModificarEmpleadoLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(ScrollEmpleados, javax.swing.GroupLayout.PREFERRED_SIZE, 690, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelModificarEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSubPanelModificarEmpleado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelContenidoLayout = new javax.swing.GroupLayout(jPanelContenido);
        jPanelContenido.setLayout(jPanelContenidoLayout);
        jPanelContenidoLayout.setHorizontalGroup(
            jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelContenidoLayout.createSequentialGroup()
                .addComponent(jPanelCargarEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
            .addGroup(jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanelConsultarIngresos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanelModificarHabitaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanelModificarEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelContenidoLayout.setVerticalGroup(
            jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelCargarEmpleado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanelConsultarIngresos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanelModificarHabitaciones, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanelModificarEmpleado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 862, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelContenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelContenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCargarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarEmpleadoActionPerformed
        Color color = new Color(204,102,0);
        Color colorOriginal = new Color(252,167,85);
       
        btnCargarEmpleado.setBackground(color);
        if (btnCargarEmpleado.getBackground().equals(color)) {
            
            btnModificarEmpleado.setBackground(colorOriginal);
            btnModificarHabitaciones.setBackground(colorOriginal);
            btnConsultarIngresos.setBackground(colorOriginal);
            
        } else {
            btnCargarEmpleado.setBackground(Color.RED);
        }

        txtContrasenia.setEchoChar((char) 0);
        jPanelCargarEmpleado.setVisible(true);
        jPanelConsultarIngresos.setVisible(false);
        jPanelModificarHabitaciones.setVisible(false);
        jPanelModificarEmpleado.setVisible(false);
        
    }//GEN-LAST:event_btnCargarEmpleadoActionPerformed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed

    }//GEN-LAST:event_txtNombreActionPerformed

    private void txtApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidoActionPerformed

    private void txtDniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDniActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDniActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoActionPerformed

    private void txtContraseniaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContraseniaFocusGained
        if (String.valueOf(txtContrasenia.getPassword()).equals("Ingrese la contraseña")) {
            txtContrasenia.setText("");
            txtContrasenia.setForeground(Color.BLACK);
            txtContrasenia.setEchoChar('•'); // Activar asteriscos
        }
    }//GEN-LAST:event_txtContraseniaFocusGained

    private void txtContraseniaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContraseniaFocusLost
        if (String.valueOf(txtContrasenia.getPassword()).isEmpty()) {
            txtContrasenia.setText("Ingrese la contraseña");
            txtContrasenia.setForeground(new java.awt.Color(150, 150, 150));
            txtContrasenia.setEchoChar((char) 0); // Mostrar texto visible
        }
    }//GEN-LAST:event_txtContraseniaFocusLost

    private void txtContraseniaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContraseniaActionPerformed

    }//GEN-LAST:event_txtContraseniaActionPerformed

    private void txtNombreFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreFocusGained
        if (txtNombre.getText().equals("Ingrese el nombre")) {
            txtNombre.setText("");
            txtNombre.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtNombreFocusGained

    private void txtNombreFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreFocusLost
        if (txtNombre.getText().isEmpty()) {
            txtNombre.setForeground(new java.awt.Color(150, 150, 150));
            txtNombre.setText("Ingrese el nombre");
        }
    }//GEN-LAST:event_txtNombreFocusLost

    private void txtApellidoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellidoFocusGained
        if (txtApellido.getText().equals("Ingrese el apellido")) {
            txtApellido.setText("");
            txtApellido.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtApellidoFocusGained

    private void txtApellidoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellidoFocusLost
        if (txtApellido.getText().isEmpty()) {
            txtApellido.setForeground(new java.awt.Color(150, 150, 150));
            txtApellido.setText("Ingrese el apellido");
        }
    }//GEN-LAST:event_txtApellidoFocusLost

    private void txtEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusGained
       if (txtEmail.getText().equals("Ingrese el correo electrónico")) {
            txtEmail.setText("");
            txtEmail.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtEmailFocusGained

    private void txtEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusLost
        if (txtEmail.getText().isEmpty()) {
            txtEmail.setForeground(new java.awt.Color(150, 150, 150));
            txtEmail.setText("Ingrese el correo electrónico");
        }
    }//GEN-LAST:event_txtEmailFocusLost

    private void txtDniFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDniFocusGained
        if (txtDni.getText().equals("Ingrese el DNI")) {
            txtDni.setText("");
            txtDni.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtDniFocusGained

    private void txtDniFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDniFocusLost
       if (txtDni.getText().isEmpty()) {
            txtDni.setForeground(new java.awt.Color(150, 150, 150));
            txtDni.setText("Ingrese el DNI");
        }
    }//GEN-LAST:event_txtDniFocusLost

    private void txtTelefonoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoFocusGained
       if (txtTelefono.getText().equals("Ingrese el número de teléfono")) {
            txtTelefono.setText("");
            txtTelefono.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtTelefonoFocusGained

    private void txtTelefonoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoFocusLost
        if (txtTelefono.getText().isEmpty()) {
            txtTelefono.setForeground(new java.awt.Color(150, 150, 150));
            txtTelefono.setText("Ingrese el número de teléfono");
        }
    }//GEN-LAST:event_txtTelefonoFocusLost

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String email = txtEmail.getText().trim();
        String contrasenia = new String(txtContrasenia.getPassword()).trim();
        String dni = txtDni.getText().trim();
        String telefono = txtTelefono.getText().trim();

        // Validación de campos vacíos
        if (
            nombre.isEmpty() || nombre.equals("Ingrese el nombre") ||
            apellido.isEmpty() || apellido.equals("Ingrese el apellido") ||
            email.isEmpty() || email.equals("Ingrese el correo electrónico") ||
            contrasenia.isEmpty() || contrasenia.equals("Ingrese la contraseña") ||
            dni.isEmpty() || dni.equals("Ingrese el DNI") ||
            telefono.isEmpty() || telefono.equals("Ingrese el número de teléfono")
        ) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben estar completos", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmación
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea cargar el nuevo empleado?", "Confirmar acción", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                 // Asegurate de tener esta clase
                Empleado empleado = gerenteService.cargarEmpleado(nombre, apellido, email, contrasenia, dni, telefono);
                cargarEmpleadosEnTabla();
                JOptionPane.showMessageDialog(this, "Empleado cargado exitosamente");

                txtNombre.setText("Ingrese el nombre");
                txtNombre.setForeground(new java.awt.Color(150, 150, 150));

                // Apellido
                txtApellido.setText("Ingrese el apellido");
                txtApellido.setForeground(new java.awt.Color(150, 150, 150));

                // Email
                txtEmail.setText("Ingrese el correo electrónico");
                txtEmail.setForeground(new java.awt.Color(150, 150, 150));

                // DNI
                txtDni.setText("Ingrese el DNI");
                txtDni.setForeground(new java.awt.Color(150, 150, 150));

                // Teléfono
                txtTelefono.setText("Ingrese el número de teléfono");
                txtTelefono.setForeground(new java.awt.Color(150, 150, 150));

                // Contraseña
                txtContrasenia.setText("Ingrese la contraseña");
                txtContrasenia.setForeground(new java.awt.Color(150, 150, 150));
                txtContrasenia.setEchoChar((char) 0); // Mostrar texto plano

            } catch (IllegalArgumentException e) {
                
                 JOptionPane.showMessageDialog(this, e.getMessage(),"Error", JOptionPane.WARNING_MESSAGE);
            } catch (Exception e) {
               JOptionPane.showMessageDialog(this, "Error al cargar empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
         // Nombre
        txtNombre.setText("Ingrese el nombre");
        txtNombre.setForeground(new java.awt.Color(150, 150, 150));

        // Apellido
        txtApellido.setText("Ingrese el apellido");
        txtApellido.setForeground(new java.awt.Color(150, 150, 150));

        // Email
        txtEmail.setText("Ingrese el correo electrónico");
        txtEmail.setForeground(new java.awt.Color(150, 150, 150));

        // DNI
        txtDni.setText("Ingrese el DNI");
        txtDni.setForeground(new java.awt.Color(150, 150, 150));

        // Teléfono
        txtTelefono.setText("Ingrese el número de teléfono");
        txtTelefono.setForeground(new java.awt.Color(150, 150, 150));

        // Contraseña
        txtContrasenia.setText("Ingrese la contraseña");
        txtContrasenia.setForeground(new java.awt.Color(150, 150, 150));
        txtContrasenia.setEchoChar((char) 0); // Mostrar texto plano
        
        
        
        jPanelCargarEmpleado.setVisible(false);
        jPanelConsultarIngresos.setVisible(false);
        jPanelModificarHabitaciones.setVisible(false);
        jPanelModificarEmpleado.setVisible(false);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnModificarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarEmpleadoActionPerformed
        Color color = new Color(204,102,0);
        Color colorOriginal = new Color(252,167,85);
        
        btnModificarEmpleado.setBackground(color);
        if (btnModificarEmpleado.getBackground().equals(color)) {
      
            btnCargarEmpleado.setBackground(colorOriginal);
            btnModificarHabitaciones.setBackground(colorOriginal);
            btnConsultarIngresos.setBackground(colorOriginal);
        } else {
            btnModificarEmpleado.setBackground(Color.RED);
        }
        
         // Restaurar visibilidad del estado inicial
        jSubPanelModificarEmpleado.setVisible(false); // Ocultar formulario de modificación
        btnModificar.setVisible(true);
        btnEliminar.setVisible(true);
        tblEmpleados.setVisible(true);
        ScrollEmpleados.setVisible(true);
        txtBuscarEmpleado.setVisible(true);
        btnBuscarEmpleado.setVisible(true);
        
        jSubPanelModificarEmpleado.setVisible(false);
        jPanelCargarEmpleado.setVisible(false);
        jPanelConsultarIngresos.setVisible(false);
        jPanelModificarHabitaciones.setVisible(false);
        jPanelModificarEmpleado.setVisible(true);
    }//GEN-LAST:event_btnModificarEmpleadoActionPerformed

    private void btnMiPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMiPerfilActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMiPerfilActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            // Cierra la ventana actual
            this.dispose();

            // Abre la ventana de inicio de sesión
            InicioSesion login = new InicioSesion();
            login.setVisible(true);
            login.setLocationRelativeTo(null); // Centrar
        }
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        int filaSeleccionada = -1;
        Long idSeleccionado = null;

        // Recorremos la tabla para ver si hay algún checkbox en true
        for (int i = 0; i < tblEmpleados.getRowCount(); i++) {
            Boolean seleccionado = (Boolean) tblEmpleados.getValueAt(i, 0); // columna del checkbox
            if (seleccionado != null && seleccionado) {
                filaSeleccionada = i;
                Object idValue = tblEmpleados.getValueAt(i, 1); // columna del ID
                if (idValue instanceof Integer integer) {
                    idSeleccionado = integer.longValue();
                } else if (idValue instanceof Long aLong) {
                    idSeleccionado = aLong;
                }
                break;
            }
        }

        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado primero.");
            return;
        }

        Usuario usuario = usuarioService.findById(idSeleccionado);
        if (usuario instanceof Empleado empleado) {
            idEmpleadoActual = idSeleccionado;

            // Mostrar el subpanel y cargar datos
            jSubPanelModificarEmpleado.setVisible(true);
            txtNombreMod.setText(empleado.getNombre());
            txtApellidoMod.setText(empleado.getApellido());
            txtEmailMod.setText(empleado.getEmail());
            txtDniMod.setText(empleado.getDni());
            txtTelefonoMod.setText(empleado.getTelefono());

            // Ocultar panel original
            txtBuscarEmpleado.setVisible(false);
            btnBuscarEmpleado.setVisible(false);
            btnModificar.setVisible(false);
            btnEliminar.setVisible(false);
            ScrollEmpleados.setVisible(false);
            tblEmpleados.setVisible(true);
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void txtNombreModFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreModFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreModFocusGained

    private void txtNombreModFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreModFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreModFocusLost

    private void txtApellidoModFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellidoModFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidoModFocusGained

    private void txtApellidoModFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellidoModFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidoModFocusLost

    private void txtEmailModFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailModFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailModFocusGained

    private void txtEmailModFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailModFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailModFocusLost

    private void txtDniModFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDniModFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDniModFocusGained

    private void txtDniModFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDniModFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDniModFocusLost

    private void txtTelefonoModFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoModFocusGained

    }//GEN-LAST:event_txtTelefonoModFocusGained

    private void txtTelefonoModFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoModFocusLost
        
    }//GEN-LAST:event_txtTelefonoModFocusLost

    private void btnConfirmarModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarModActionPerformed
        
        Long id = idEmpleadoActual; 
        String nombre = txtNombreMod.getText();
        String apellido = txtApellidoMod.getText();
        String email = txtEmailMod.getText();
        String dni = txtDniMod.getText();
        String telefono = txtTelefonoMod.getText();

        Empleado empleado = (Empleado) usuarioService.findById(id);
        
        // Actualizar campos
        empleado.setNombre(nombre);
        empleado.setApellido(apellido);
        empleado.setEmail(email);
        empleado.setDni(dni);
        empleado.setTelefono(telefono);
        
        // Validación de campos vacíos
        if (
            nombre.isEmpty() || 
            apellido.isEmpty() || 
            email.isEmpty() || 
            dni.isEmpty() || 
            telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben estar completos", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
       int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea modificar este empleado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
       if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                gerenteService.modificarEmpleado(empleado);
                cargarEmpleadosEnTabla();
                JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            }catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al modificar el empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
       
        btnBuscarEmpleado.setVisible(true);
        txtBuscarEmpleado.setVisible(true);
        jSubPanelModificarEmpleado.setVisible(false);
        btnModificar.setVisible(true);
        btnEliminar.setVisible(true);
        ScrollEmpleados.setVisible(true);

    }//GEN-LAST:event_btnConfirmarModActionPerformed

    private void btnCancelarModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarModActionPerformed
        // Restaurar visibilidad del estado inicial
        jSubPanelModificarEmpleado.setVisible(false); // Ocultar formulario de modificación
        btnModificar.setVisible(true);
        btnEliminar.setVisible(true);
        tblEmpleados.setVisible(true);
        ScrollEmpleados.setVisible(true);
        txtBuscarEmpleado.setVisible(true);
        btnBuscarEmpleado.setVisible(true);
        
        tblEmpleados.clearSelection();
        idEmpleadoActual = null;
        
        jSubPanelModificarEmpleado.setVisible(false);
        jPanelCargarEmpleado.setVisible(false);
        jPanelConsultarIngresos.setVisible(false);
        jPanelModificarHabitaciones.setVisible(false);
        jPanelModificarEmpleado.setVisible(true);
    }//GEN-LAST:event_btnCancelarModActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int filaSeleccionada = -1;
        Long idSeleccionado = null;

        // Recorremos la tabla para ver si hay algún checkbox en true
        for (int i = 0; i < tblEmpleados.getRowCount(); i++) {
            Boolean seleccionado = (Boolean) tblEmpleados.getValueAt(i, 0); // columna del checkbox
            if (seleccionado != null && seleccionado) {
                filaSeleccionada = i;
                Object idValue = tblEmpleados.getValueAt(i, 1); // columna del ID
                if (idValue instanceof Integer integer) {
                    idSeleccionado = integer.longValue();
                } else if (idValue instanceof Long aLong) {
                    idSeleccionado = aLong;
                }
                break;
            }
        }

        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado primero.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar este empleado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                gerenteService.removerEmpleado(idSeleccionado);
                cargarEmpleadosEnTabla(); // Refrescar tabla
                JOptionPane.showMessageDialog(this, "Empleado eliminado correctamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnBuscarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarEmpleadoActionPerformed
        String filtro = txtBuscarEmpleado.getText().trim().toLowerCase();
        
        if (filtro.isEmpty() || filtro.equalsIgnoreCase("Buscar empleado por nombre, dni, etc")) {
            sorterEmpleados.setRowFilter(null);  // Mostrar todo
        } else {
            sorterEmpleados.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(filtro), 2,3, 4, 5,6,7));
        }
    }//GEN-LAST:event_btnBuscarEmpleadoActionPerformed

    private void txtBuscarEmpleadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarEmpleadoFocusGained
        if (txtBuscarEmpleado.getText().equals("Buscar empleado por nombre, dni, etc")) {
            txtBuscarEmpleado.setText("");
            txtBuscarEmpleado.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtBuscarEmpleadoFocusGained

    private void txtBuscarEmpleadoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarEmpleadoFocusLost
        if (txtBuscarEmpleado.getText().isEmpty()) {
            txtBuscarEmpleado.setForeground(new java.awt.Color(150, 150, 150));
            txtBuscarEmpleado.setText("Buscar empleado por nombre, dni, etc");
        }
    }//GEN-LAST:event_txtBuscarEmpleadoFocusLost

    private void txtBuscarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarEmpleadoActionPerformed

    private void btnModificarHabitacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarHabitacionesActionPerformed
        Color color = new Color(204,102,0);
        Color colorOriginal = new Color(252,167,85);
       
        btnModificarHabitaciones.setBackground(color);
        if (btnModificarHabitaciones.getBackground().equals(color)) {
 
            btnModificarEmpleado.setBackground(colorOriginal);
            btnConsultarIngresos.setBackground(colorOriginal);
            btnCargarEmpleado.setBackground(colorOriginal);
            
        } else {
            btnCargarEmpleado.setBackground(Color.RED);
        }

        txtContrasenia.setEchoChar((char) 0);
        jPanelCargarEmpleado.setVisible(false);
        jPanelConsultarIngresos.setVisible(false);
        jPanelModificarHabitaciones.setVisible(true);
        jPanelModificarEmpleado.setVisible(false);
        
    }//GEN-LAST:event_btnModificarHabitacionesActionPerformed

    private void txtBuscarHabitacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarHabitacionFocusGained
        if (txtBuscarHabitacion.getText().equals("Buscar habitacion por numero, piso, etc")) {
            txtBuscarHabitacion.setText("");
            txtBuscarHabitacion.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtBuscarHabitacionFocusGained

    private void txtBuscarHabitacionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarHabitacionFocusLost
        if (txtBuscarHabitacion.getText().isEmpty()) {
            txtBuscarHabitacion.setForeground(new java.awt.Color(150, 150, 150));
            txtBuscarHabitacion.setText("Buscar habitacion por numero, piso, etc");
        }
    }//GEN-LAST:event_txtBuscarHabitacionFocusLost

    private void txtBuscarHabitacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarHabitacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarHabitacionActionPerformed

    private void btnBuscarHabitacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarHabitacionActionPerformed
        String filtro = txtBuscarHabitacion.getText().trim().toLowerCase();
        
        if (filtro.isEmpty() || filtro.equalsIgnoreCase("Buscar habitacion por numero, piso, etc")) {
            sorterHabitaciones.setRowFilter(null);  // Mostrar todo
        } else {
            sorterHabitaciones.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(filtro),1,2,3,4,5));
        }
    }//GEN-LAST:event_btnBuscarHabitacionActionPerformed

    private void btnConsultarIngresosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarIngresosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConsultarIngresosActionPerformed

    private void btnModificarHabitacion1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarHabitacion1ActionPerformed
        int filaSeleccionada = -1;
        Long idSeleccionado = null;

        // Recorrer la tabla de habitaciones y buscar cuál tiene el checkbox seleccionado
        for (int i = 0; i < tblHabitaciones.getRowCount(); i++) {
            Boolean seleccionado = (Boolean) tblHabitaciones.getValueAt(i, 0); // columna checkbox
            if (seleccionado != null && seleccionado) {
                filaSeleccionada = i;
                Object idValue = tblHabitaciones.getValueAt(i, 6); // columna del ID (ajustá el índice según donde tengas el id)
                if (idValue instanceof Integer integer) {
                    idSeleccionado = integer.longValue();
                } else if (idValue instanceof Long aLong) {
                    idSeleccionado = aLong;
                } else if (idValue instanceof String s) {
                    try {
                        idSeleccionado = Long.parseLong(s);
                    } catch (NumberFormatException e) {
                        idSeleccionado = null;
                    }
                }
                break;
            }
        }

        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una habitación primero.");
            return;
        }

        // Usar el id para buscar la habitación en la DB
        // Pedir confirmación 
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea modificar esta habitación?", "Confirmar modificación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
        try {
        
            // Cargar habitación de la base
            Habitacion habitacion = habitacionService.findById(idSeleccionado);
           

            // Pedir nuevo precio (o cualquier otro dato que quieras modificar)
            String nuevoPrecioStr = JOptionPane.showInputDialog(this, 
                "Ingrese el nuevo precio para la habitación número " + habitacion.getNumero() + ":");
            if (nuevoPrecioStr == null || nuevoPrecioStr.trim().isEmpty()) {
                return; // Canceló
            }
            
            double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
            habitacion.setPrecio_por_noche(nuevoPrecio);
            // Actualizar en BD
            habitacionService.actualizar(habitacion);

            // Refrescar tabla
            cargarHabitacionesEnTabla();

            JOptionPane.showMessageDialog(this, "Habitación modificada correctamente.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio inválido. Debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al modificar la habitación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnModificarHabitacion1ActionPerformed
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ScrollEmpleados;
    private javax.swing.JScrollPane ScrollHabitaciones;
    private javax.swing.JButton btnBuscarEmpleado;
    private javax.swing.JButton btnBuscarHabitacion;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelarMod;
    private javax.swing.JButton btnCargarEmpleado;
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JButton btnConfirmarMod;
    private javax.swing.JButton btnConsultarIngresos;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnMiPerfil;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnModificarEmpleado;
    private javax.swing.JButton btnModificarHabitacion1;
    private javax.swing.JButton btnModificarHabitaciones;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelCargarEmpleado;
    private javax.swing.JPanel jPanelConsultarIngresos;
    private javax.swing.JPanel jPanelContenido;
    private javax.swing.JPanel jPanelModificarEmpleado;
    private javax.swing.JPanel jPanelModificarHabitaciones;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPanel jSubPanelModificarEmpleado;
    private javax.swing.JLabel lblApellido;
    private javax.swing.JLabel lblApellidoMod;
    private javax.swing.JLabel lblBienvenido;
    private javax.swing.JLabel lblContrasenia;
    private javax.swing.JLabel lblDni;
    private javax.swing.JLabel lblDniMod;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEmailMod;
    private javax.swing.JLabel lblNombre2;
    private javax.swing.JLabel lblNombreMod1;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblTelefonoMod;
    private javax.swing.JTable tblEmpleados;
    private javax.swing.JTable tblHabitaciones;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtApellidoMod;
    private javax.swing.JTextField txtBuscarEmpleado;
    private javax.swing.JTextField txtBuscarHabitacion;
    private javax.swing.JPasswordField txtContrasenia;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtDniMod;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEmailMod;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNombreMod;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtTelefonoMod;
    // End of variables declaration//GEN-END:variables
}
