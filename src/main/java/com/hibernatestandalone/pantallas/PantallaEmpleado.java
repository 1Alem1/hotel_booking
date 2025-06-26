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

public class PantallaEmpleado extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PantallaEmpleado.class.getName());

    private TableRowSorter<DefaultTableModel> sorterHabitaciones;
    
    public PantallaEmpleado() {
        initComponents();
        cargarHabitacionesEnTabla();
        cargarReservasEnTabla();
    }

    private void cargarReservasEnTabla() {
    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 0 ? Boolean.class : Object.class;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0;
        }
    };

    JTableHeader header = tblHabitacionOcupada.getTableHeader();
    header.setFont(new Font("Segoe UI", Font.BOLD, 22));
    header.setForeground(Color.WHITE);
    header.setBackground(new Color(232, 130, 0));

    modelo.addColumn("");
    modelo.addColumn("Numero");
    modelo.addColumn("Piso");
    modelo.addColumn("Capacidad de personas");
    modelo.addColumn("Precio");
    modelo.addColumn("Descripcion");
    modelo.addColumn("ID");

    List<Habitacion> habitaciones = new HabitacionService().getAll();
    System.out.println("Habitaciones en tabla ocupadas: " + habitaciones.size());

    for (Habitacion h : habitaciones) {
        modelo.addRow(new Object[]{
            false,
            h.getNumero(),
            h.getPiso(),
            h.getCapacidad_personas(),
            h.getPrecio_por_noche(),
            h.getDescripcion(),
            h.getId()
        });
    }

    tblHabitacionOcupada.setModel(modelo); // set antes del listener
    tblHabitacionOcupada.getModel().addTableModelListener(e -> {
        if (e.getColumn() == 0) {
            for (int i = 0; i < tblHabitacionOcupada.getRowCount(); i++) {
                if (i != e.getFirstRow()) {
                    tblHabitacionOcupada.setValueAt(false, i, 0);
                }
            }
        }
    });

    // Config columnas (omitido aquí por brevedad)

    sorterHabitaciones = new TableRowSorter<>(modelo);
    sorterHabitaciones.setRowFilter(null); // 🔥 sin filtros
    tblHabitacionOcupada.setRowSorter(sorterHabitaciones);
    System.out.println("Filas visibles en tabla ocupadas: " + tblHabitacionOcupada.getRowCount());

    tblHabitacionOcupada.getTableHeader().setReorderingAllowed(false);
    tblHabitacionOcupada.revalidate();
    tblHabitacionOcupada.repaint();
}

    //Hay qeu cambiarle el sorter porque carga tablas de menos
private void cargarHabitacionesEnTabla() {
    HabitacionService habitacionService = new HabitacionService();
    List<Habitacion> habitaciones = habitacionService.getAll();

    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 0 ? Boolean.class : Object.class;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0;
        }
    };

    JTableHeader header = tblHabitacionDisponible.getTableHeader();
    header.setFont(new Font("Segoe UI", Font.BOLD, 22));
    header.setForeground(Color.WHITE);
    header.setBackground(new Color(232, 130, 0));

    modelo.addColumn("");
    modelo.addColumn("Numero");
    modelo.addColumn("Piso");
    modelo.addColumn("Capacidad de personas");
    modelo.addColumn("Precio");
    modelo.addColumn("Descripcion");
    modelo.addColumn("ID");

    // 🔄 Setear modelo y limpiarlo ANTES de llenarlo
    tblHabitacionDisponible.setModel(modelo);
    modelo.setRowCount(0); // Por si NetBeans ya tenía valores precargados

    for (Habitacion h : habitaciones) {
        modelo.addRow(new Object[]{
            false,
            h.getNumero(),
            h.getPiso(),
            h.getCapacidad_personas(),
            h.getPrecio_por_noche(),
            h.getDescripcion(),
            h.getId()
        });
    }

    tblHabitacionDisponible.getModel().addTableModelListener(e -> {
        if (e.getColumn() == 0) {
            for (int i = 0; i < tblHabitacionDisponible.getRowCount(); i++) {
                if (i != e.getFirstRow()) {
                    tblHabitacionDisponible.setValueAt(false, i, 0);
                }
            }
        }
    });

    // 🧱 Configuración de columnas (si usás esto)
    tblHabitacionDisponible.getColumnModel().getColumn(0).setPreferredWidth(40); 
    tblHabitacionDisponible.getColumnModel().getColumn(0).setMinWidth(40); 
    tblHabitacionDisponible.getColumnModel().getColumn(0).setMaxWidth(40); 
    tblHabitacionDisponible.getColumnModel().getColumn(0).setResizable(false); 
    
    tblHabitacionDisponible.getColumnModel().getColumn(1).setPreferredWidth(120); 
    tblHabitacionDisponible.getColumnModel().getColumn(2).setPreferredWidth(80); 
    tblHabitacionDisponible.getColumnModel().getColumn(3).setPreferredWidth(250); 
    tblHabitacionDisponible.getColumnModel().getColumn(4).setPreferredWidth(120); 

    // Ocultar columna ID
    tblHabitacionDisponible.getColumnModel().getColumn(6).setMinWidth(0);
    tblHabitacionDisponible.getColumnModel().getColumn(6).setMaxWidth(0);
    tblHabitacionDisponible.getColumnModel().getColumn(6).setWidth(0);
    tblHabitacionDisponible.getColumnModel().getColumn(6).setPreferredWidth(0);
    tblHabitacionDisponible.getColumnModel().getColumn(6).setResizable(false);

    // 🆕 NUEVO sorter exclusivo para esta tabla
    TableRowSorter<DefaultTableModel> sorterDisponible = new TableRowSorter<>(modelo);
    sorterDisponible.setRowFilter(null);
    tblHabitacionDisponible.setRowSorter(sorterDisponible);

    System.out.println("Filas visibles en tabla disponibles: " + tblHabitacionDisponible.getRowCount());

    tblHabitacionDisponible.getTableHeader().setReorderingAllowed(false);
    tblHabitacionDisponible.revalidate();
    tblHabitacionDisponible.repaint();
}

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelMenu = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        btnReservar = new javax.swing.JButton();
        btnListaReservas = new javax.swing.JButton();
        jPanelDecoracion = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanelContenido = new javax.swing.JPanel();
        jPanelReservar = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHabitacionDisponible = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnBuscarHabitacion = new javax.swing.JButton();
        txtBuscarHabitacion = new javax.swing.JTextField();
        btnReservarHabitacion = new javax.swing.JButton();
        jPanelListarReservas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHabitacionOcupada = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelMenu.setBackground(new java.awt.Color(232, 130, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Menú");

        jSeparator2.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));

        btnReservar.setBackground(new java.awt.Color(252, 167, 85));
        btnReservar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnReservar.setForeground(new java.awt.Color(255, 255, 255));
        btnReservar.setText("RESERVAR");
        btnReservar.setBorder(null);
        btnReservar.setFocusPainted(false);
        btnReservar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReservarActionPerformed(evt);
            }
        });

        btnListaReservas.setBackground(new java.awt.Color(252, 167, 85));
        btnListaReservas.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnListaReservas.setForeground(new java.awt.Color(255, 255, 255));
        btnListaReservas.setText("LISTAR RESERVAS");
        btnListaReservas.setBorder(null);
        btnListaReservas.setFocusPainted(false);
        btnListaReservas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListaReservasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelMenuLayout = new javax.swing.GroupLayout(jPanelMenu);
        jPanelMenu.setLayout(jPanelMenuLayout);
        jPanelMenuLayout.setHorizontalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMenuLayout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(btnListaReservas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnReservar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelMenuLayout.setVerticalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98)
                .addComponent(btnReservar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnListaReservas, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelDecoracion.setBackground(new java.awt.Color(204, 102, 0));

        jButton1.setBackground(new java.awt.Color(204, 0, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("SALIR");
        jButton1.setBorder(null);
        jButton1.setFocusPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelDecoracionLayout = new javax.swing.GroupLayout(jPanelDecoracion);
        jPanelDecoracion.setLayout(jPanelDecoracionLayout);
        jPanelDecoracionLayout.setHorizontalGroup(
            jPanelDecoracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDecoracionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jPanelDecoracionLayout.setVerticalGroup(
            jPanelDecoracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDecoracionLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        tblHabitacionDisponible.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        tblHabitacionDisponible.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Seleccionar", "Numero", "Piso", "Capacidad de Personas", "Precio", "Descripcion"
            }
        ));
        tblHabitacionDisponible.setGridColor(new java.awt.Color(0, 0, 0));
        tblHabitacionDisponible.setRowHeight(35);
        tblHabitacionDisponible.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(tblHabitacionDisponible);

        jPanel2.setBackground(new java.awt.Color(232, 130, 0));

        jLabel2.setBackground(new java.awt.Color(232, 130, 0));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("RESERVAR");

        btnBuscarHabitacion.setBackground(new java.awt.Color(56, 121, 185));
        btnBuscarHabitacion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscarHabitacion.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarHabitacion.setText("BUSCAR");
        btnBuscarHabitacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarHabitacionActionPerformed(evt);
            }
        });

        txtBuscarHabitacion.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtBuscarHabitacion.setText("Buscar habitacion por numero, piso, etc");
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtBuscarHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btnBuscarHabitacion)
                .addGap(14, 14, 14))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(btnBuscarHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscarHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        btnReservarHabitacion.setBackground(new java.awt.Color(0, 153, 51));
        btnReservarHabitacion.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnReservarHabitacion.setForeground(new java.awt.Color(255, 255, 255));
        btnReservarHabitacion.setText("RESERVAR");
        btnReservarHabitacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReservarHabitacionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelReservarLayout = new javax.swing.GroupLayout(jPanelReservar);
        jPanelReservar.setLayout(jPanelReservarLayout);
        jPanelReservarLayout.setHorizontalGroup(
            jPanelReservarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1265, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelReservarLayout.createSequentialGroup()
                .addGap(440, 440, 440)
                .addComponent(btnReservarHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelReservarLayout.setVerticalGroup(
            jPanelReservarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelReservarLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 751, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReservarHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        tblHabitacionOcupada.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        tblHabitacionOcupada.setModel(new javax.swing.table.DefaultTableModel(
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
        tblHabitacionOcupada.setGridColor(new java.awt.Color(0, 0, 0));
        tblHabitacionOcupada.setPreferredSize(new java.awt.Dimension(450, 140));
        tblHabitacionOcupada.setRowHeight(35);
        tblHabitacionOcupada.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(tblHabitacionOcupada);

        jPanel3.setBackground(new java.awt.Color(232, 130, 0));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("LISTA DE RESERVAS");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel3)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelListarReservasLayout = new javax.swing.GroupLayout(jPanelListarReservas);
        jPanelListarReservas.setLayout(jPanelListarReservasLayout);
        jPanelListarReservasLayout.setHorizontalGroup(
            jPanelListarReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1265, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelListarReservasLayout.setVerticalGroup(
            jPanelListarReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelListarReservasLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 719, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78))
        );

        javax.swing.GroupLayout jPanelContenidoLayout = new javax.swing.GroupLayout(jPanelContenido);
        jPanelContenido.setLayout(jPanelContenidoLayout);
        jPanelContenidoLayout.setHorizontalGroup(
            jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelContenidoLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanelListarReservas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelContenidoLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jPanelReservar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
        jPanelContenidoLayout.setVerticalGroup(
            jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelListarReservas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelContenidoLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jPanelReservar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelContenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelDecoracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanelDecoracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelContenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void btnListaReservasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListaReservasActionPerformed
          Color color = new Color(204,102,0);
          Color colorOriginal = new Color(252,167,85);
          btnListaReservas.setBackground(color);
        if (btnListaReservas.getBackground().equals(color)) {
            btnReservar.setBackground(colorOriginal);
        } else {
            btnListaReservas.setBackground(Color.RED);
            
        }
        jPanelReservar.setVisible(false);
        jPanelListarReservas.setVisible(true);
    }//GEN-LAST:event_btnListaReservasActionPerformed

    private void btnReservarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservarActionPerformed
          Color color = new Color(204,102,0);
          Color colorOriginal = new Color(252,167,85);
          btnReservar.setBackground(color);
        if (btnReservar.getBackground().equals(color)) {
            btnListaReservas.setBackground(colorOriginal);
        } else {
            btnReservar.setBackground(Color.RED);
        }
        jPanelReservar.setVisible(true);
        jPanelListarReservas.setVisible(false);
    }//GEN-LAST:event_btnReservarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            // Cierra la ventana actual
            this.dispose();

            // Abre la ventana de inicio de sesión
            InicioSesion login = new InicioSesion();
            login.setVisible(true);
            login.setLocationRelativeTo(null); // Centrar
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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

    private void txtBuscarHabitacionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarHabitacionFocusLost
    if (txtBuscarHabitacion.getText().isEmpty()) {
            txtBuscarHabitacion.setForeground(new java.awt.Color(150, 150, 150));
            txtBuscarHabitacion.setText("Buscar habitacion por numero, piso, etc");
        }   
    }//GEN-LAST:event_txtBuscarHabitacionFocusLost

    private void txtBuscarHabitacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarHabitacionFocusGained
    if (txtBuscarHabitacion.getText().equals("Buscar habitacion por numero, piso, etc")) {
            txtBuscarHabitacion.setText("");
            txtBuscarHabitacion.setForeground(Color.BLACK);
    }
    }//GEN-LAST:event_txtBuscarHabitacionFocusGained

    private void btnReservarHabitacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservarHabitacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnReservarHabitacionActionPerformed

  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarHabitacion;
    private javax.swing.JButton btnListaReservas;
    private javax.swing.JButton btnReservar;
    private javax.swing.JButton btnReservarHabitacion;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelContenido;
    private javax.swing.JPanel jPanelDecoracion;
    private javax.swing.JPanel jPanelListarReservas;
    private javax.swing.JPanel jPanelMenu;
    private javax.swing.JPanel jPanelReservar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable tblHabitacionDisponible;
    private javax.swing.JTable tblHabitacionOcupada;
    private javax.swing.JTextField txtBuscarHabitacion;
    // End of variables declaration//GEN-END:variables
}
