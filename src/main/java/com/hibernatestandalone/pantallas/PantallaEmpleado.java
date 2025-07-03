package com.hibernatestandalone.pantallas;

import com.hibernatestandalone.services.EmailService;
import com.hibernatestandalone.HibernateStandalone.EstadoReserva;
import com.hibernatestandalone.HibernateStandalone.GenerarDocumento;
import com.hibernatestandalone.HibernateStandalone.GenerarPdfEmpleado;
import com.hibernatestandalone.entity.Empleado;
import com.hibernatestandalone.entity.Factura;
import com.hibernatestandalone.entity.Habitacion;
import com.hibernatestandalone.entity.Huesped;
import com.hibernatestandalone.entity.Reserva;
import com.hibernatestandalone.services.EmpleadoService;
import com.hibernatestandalone.services.FacturaService;
import com.hibernatestandalone.services.HabitacionService;
import com.hibernatestandalone.services.ReservaService;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

public class PantallaEmpleado extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PantallaEmpleado.class.getName());
    private Long idHabitacionActual;
    private TableRowSorter<DefaultTableModel> sorterHabitacionesOcupadas;
    private TableRowSorter<DefaultTableModel> sorterHabitacionesDisponibles;
    private final HabitacionService habitacionService = new HabitacionService();
    private final ReservaService reservaService = new ReservaService();
    private final EmpleadoService empleadoService = new EmpleadoService();
    private final FacturaService facturaService = new FacturaService();

    public PantallaEmpleado(Empleado empleado) {
        initComponents();
        jPanelContenido.setVisible(false);

        tblHabitacionDisponible.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblHabitacionDisponible.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int col = tblHabitacionDisponible.columnAtPoint(e.getPoint());
                int viewRow = tblHabitacionDisponible.rowAtPoint(e.getPoint());
                if (viewRow >= 0 && col == 0) {
                    if (tblHabitacionDisponible.isEditing()) {
                        tblHabitacionDisponible.getCellEditor().stopCellEditing();
                    }
                    DefaultTableModel modelo = (DefaultTableModel) tblHabitacionDisponible.getModel();
                    int modelRow = tblHabitacionDisponible.convertRowIndexToModel(viewRow);
                    // Verificación de rango por si acaso
                    if (modelRow < 0 || modelRow >= modelo.getRowCount()) {
                        return;
                    }

                    // Limpiar todos
                    for (int i = 0; i < modelo.getRowCount(); i++) {
                        modelo.setValueAt(false, i, 0);
                    }
                    // Marcar solo el seleccionado
                    modelo.setValueAt(true, modelRow, 0);
                }
            }
        });

        tblHabitacionOcupada.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int col = tblHabitacionOcupada.columnAtPoint(e.getPoint());
                int viewRow = tblHabitacionOcupada.rowAtPoint(e.getPoint());
                if (viewRow >= 0 && col == 0) {
                    if (tblHabitacionOcupada.isEditing()) {
                        tblHabitacionOcupada.getCellEditor().stopCellEditing();
                    }

                    DefaultTableModel modelo = (DefaultTableModel) tblHabitacionOcupada.getModel();
                    int modelRow = tblHabitacionOcupada.convertRowIndexToModel(viewRow);

                    for (int i = 0; i < modelo.getRowCount(); i++) {
                        modelo.setValueAt(false, i, 0);
                    }
                    modelo.setValueAt(true, modelRow, 0);
                }
            }
        });
        cargarReservasEnTabla();

        lblBienvenido.setText("Bienvenido, " + empleado.getNombre() + " " + empleado.getApellido());

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

        // Nuevas columnas
        modelo.addColumn(""); // Checkbox
        modelo.addColumn("Número");
        modelo.addColumn("Piso");
        modelo.addColumn("Fecha inicio");
        modelo.addColumn("Fecha fin");
        modelo.addColumn("Check-in");
        modelo.addColumn("Check-out");
        modelo.addColumn("Huésped");
        modelo.addColumn("Total");

        // Traer reservas con estado CONFIRMADA o CHECKIN
        List<Reserva> reservas = reservaService.getReservasActivas();
        for (Reserva r : reservas) {
            Habitacion h = r.getHabitacion();
            Huesped huesped = r.getHuesped();
            if (h != null && huesped != null) {
                Date fInicio = r.getFechaInicio();
                Date fFin = r.getFechaFin();
                Date chkIn = r.getCheckIn();
                Date chkOut = r.getCheckOut();

                long dias = (fFin.getTime() - fInicio.getTime()) / (1000L * 60 * 60 * 24);
                if (dias == 0) {
                    dias = 1;
                }
                double total = dias * h.getPrecio_por_noche();

                String huéspedInfo = huesped.getDni() + " / " + huesped.getEmail();

                modelo.addRow(new Object[]{
                    false,
                    h.getNumero(),
                    h.getPiso(),
                    fInicio,
                    fFin,
                    chkIn,
                    chkOut,
                    huéspedInfo,
                    total
                });
            }
        }

        tblHabitacionOcupada.setModel(modelo);
        aplicarFormatoTablaOcupadas(); 
        
        sorterHabitacionesOcupadas = new TableRowSorter<>(modelo);
        sorterHabitacionesOcupadas.setRowFilter(null);
        tblHabitacionOcupada.setRowSorter(sorterHabitacionesOcupadas);
        tblHabitacionOcupada.getTableHeader().setReorderingAllowed(false);
        tblHabitacionOcupada.revalidate();
        tblHabitacionOcupada.repaint();
    }

    private void aplicarFormatoTablaDisponibles() {
        JTableHeader header = tblHabitacionDisponible.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(232, 130, 0));
        tblHabitacionDisponible.getTableHeader().setReorderingAllowed(false);

        tblHabitacionDisponible.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblHabitacionDisponible.getColumnModel().getColumn(0).setMinWidth(40);
        tblHabitacionDisponible.getColumnModel().getColumn(0).setMaxWidth(40);
        tblHabitacionDisponible.getColumnModel().getColumn(0).setResizable(false);

        tblHabitacionDisponible.getColumnModel().getColumn(1).setPreferredWidth(120);;
        tblHabitacionDisponible.getColumnModel().getColumn(1).setMinWidth(120);;
        tblHabitacionDisponible.getColumnModel().getColumn(1).setMaxWidth(120);
        tblHabitacionDisponible.getColumnModel().getColumn(1).setResizable(false);

        tblHabitacionDisponible.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblHabitacionDisponible.getColumnModel().getColumn(2).setMinWidth(80);
        tblHabitacionDisponible.getColumnModel().getColumn(2).setMaxWidth(80);
        tblHabitacionDisponible.getColumnModel().getColumn(2).setResizable(false);

        tblHabitacionDisponible.getColumnModel().getColumn(3).setPreferredWidth(250);
        tblHabitacionDisponible.getColumnModel().getColumn(3).setMinWidth(250);
        tblHabitacionDisponible.getColumnModel().getColumn(3).setMaxWidth(250);
        tblHabitacionDisponible.getColumnModel().getColumn(3).setResizable(false);

        tblHabitacionDisponible.getColumnModel().getColumn(4).setPreferredWidth(120);
        tblHabitacionDisponible.getColumnModel().getColumn(4).setMinWidth(120);
        tblHabitacionDisponible.getColumnModel().getColumn(4).setMaxWidth(120);
        tblHabitacionDisponible.getColumnModel().getColumn(4).setResizable(false);

        tblHabitacionDisponible.getColumnModel().getColumn(6).setMinWidth(0);
        tblHabitacionDisponible.getColumnModel().getColumn(6).setMaxWidth(0);
        tblHabitacionDisponible.getColumnModel().getColumn(6).setWidth(0);
        tblHabitacionDisponible.getColumnModel().getColumn(6).setPreferredWidth(0);
        tblHabitacionDisponible.getColumnModel().getColumn(6).setResizable(false);
    }

    private void aplicarFormatoTablaOcupadas() {
        JTableHeader header = tblHabitacionOcupada.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(232, 130, 0));
        tblHabitacionOcupada.getTableHeader().setReorderingAllowed(false);

        tblHabitacionOcupada.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblHabitacionOcupada.getColumnModel().getColumn(0).setMinWidth(40);
        tblHabitacionOcupada.getColumnModel().getColumn(0).setMaxWidth(40);
        tblHabitacionOcupada.getColumnModel().getColumn(0).setResizable(false);

        tblHabitacionOcupada.getColumnModel().getColumn(1).setPreferredWidth(120);;
        tblHabitacionOcupada.getColumnModel().getColumn(1).setMinWidth(120);;
        tblHabitacionOcupada.getColumnModel().getColumn(1).setMaxWidth(120);
        tblHabitacionOcupada.getColumnModel().getColumn(1).setResizable(false);

        tblHabitacionOcupada.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblHabitacionOcupada.getColumnModel().getColumn(2).setMinWidth(80);
        tblHabitacionOcupada.getColumnModel().getColumn(2).setMaxWidth(80);
        tblHabitacionOcupada.getColumnModel().getColumn(2).setResizable(false);

        tblHabitacionOcupada.getColumnModel().getColumn(3).setPreferredWidth(200);
        tblHabitacionOcupada.getColumnModel().getColumn(3).setMinWidth(200);
        tblHabitacionOcupada.getColumnModel().getColumn(3).setMaxWidth(200);
        tblHabitacionOcupada.getColumnModel().getColumn(3).setResizable(false);

        tblHabitacionOcupada.getColumnModel().getColumn(4).setPreferredWidth(200);
        tblHabitacionOcupada.getColumnModel().getColumn(4).setMinWidth(200);
        tblHabitacionOcupada.getColumnModel().getColumn(4).setMaxWidth(200);
        tblHabitacionOcupada.getColumnModel().getColumn(4).setResizable(false);

        tblHabitacionOcupada.getColumnModel().getColumn(5).setPreferredWidth(200);
        tblHabitacionOcupada.getColumnModel().getColumn(5).setMinWidth(200);
        tblHabitacionOcupada.getColumnModel().getColumn(5).setMaxWidth(200);
        tblHabitacionOcupada.getColumnModel().getColumn(5).setResizable(false);

        tblHabitacionOcupada.getColumnModel().getColumn(6).setMinWidth(200);
        tblHabitacionOcupada.getColumnModel().getColumn(6).setMaxWidth(200);
        tblHabitacionOcupada.getColumnModel().getColumn(6).setWidth(200);
        tblHabitacionOcupada.getColumnModel().getColumn(6).setPreferredWidth(200);
        tblHabitacionOcupada.getColumnModel().getColumn(6).setResizable(false);
    }

    
    private void cargarHabitacionesEnTabla(Date fechaInicio, Date fechaFin) {
        List<Habitacion> habitaciones = habitacionService.buscarDisponibles(fechaInicio, fechaFin);
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"", "Número", "Piso", "Capacidad", "Precio", "Descripción", "ID"},
                0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        modelo.setRowCount(0);
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

        tblHabitacionDisponible.setRowSorter(null); // Remover sorter temporalmente
        tblHabitacionDisponible.setModel(modelo);

        // reaplicar formato
        aplicarFormatoTablaDisponibles();

        // actualizar sorter
        sorterHabitacionesDisponibles = new TableRowSorter<>(modelo);
        tblHabitacionDisponible.setRowSorter(sorterHabitacionesDisponibles);
        tblHabitacionDisponible.getTableHeader().setReorderingAllowed(false);

        tblHabitacionDisponible.revalidate();
        tblHabitacionDisponible.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanelMenu = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        btnReservar = new javax.swing.JButton();
        btnListaReservas = new javax.swing.JButton();
        jPanelDecoracion = new javax.swing.JPanel();
        jSalir = new javax.swing.JButton();
        lblBienvenido = new javax.swing.JLabel();
        jPanelContenido = new javax.swing.JPanel();
        jPanelReservar = new javax.swing.JPanel();
        jScrollHabitacionDisponible = new javax.swing.JScrollPane();
        tblHabitacionDisponible = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnBuscarHabitacion = new javax.swing.JButton();
        jFechaInicio = new com.toedter.calendar.JDateChooser();
        jFechafinal = new com.toedter.calendar.JDateChooser();
        check_out = new javax.swing.JLabel();
        Check_in = new javax.swing.JLabel();
        btnReservarHabitacion = new javax.swing.JButton();
        jPanelCargarHuesped = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        txtNombreHuesped = new javax.swing.JTextField();
        txtDniHuesped = new javax.swing.JTextField();
        btnCancelarHuesped = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JSeparator();
        txtTelefonoHuesped = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        txtEmailHuesped = new javax.swing.JTextField();
        lblEmailHuesped = new javax.swing.JLabel();
        lblMetodoDePago = new javax.swing.JLabel();
        lblNombreHuesped = new javax.swing.JLabel();
        lblDniHuesped = new javax.swing.JLabel();
        lblTelefonoHuesped = new javax.swing.JLabel();
        lblApellidoHuesped = new javax.swing.JLabel();
        txtApellidoHuesped = new javax.swing.JTextField();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        btnConfirmarHuesped = new javax.swing.JButton();
        txtMetodoDePago = new javax.swing.JTextField();
        jSeparator13 = new javax.swing.JSeparator();
        jPanelListarReservas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHabitacionOcupada = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtBuscarReserva = new javax.swing.JTextField();
        btnBuscarReserva = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnCheckin = new javax.swing.JButton();
        btnCheckout = new javax.swing.JButton();
        jPanelModificar = new javax.swing.JPanel();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

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
                .addGap(118, 118, 118)
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
                .addGap(18, 18, 18)
                .addComponent(btnReservar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnListaReservas, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelDecoracion.setBackground(new java.awt.Color(204, 102, 0));

        jSalir.setBackground(new java.awt.Color(204, 0, 0));
        jSalir.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jSalir.setForeground(new java.awt.Color(255, 255, 255));
        jSalir.setText("SALIR");
        jSalir.setBorder(null);
        jSalir.setFocusPainted(false);
        jSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSalirActionPerformed(evt);
            }
        });

        lblBienvenido.setBackground(new java.awt.Color(232, 130, 0));
        lblBienvenido.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblBienvenido.setForeground(new java.awt.Color(255, 255, 255));
        lblBienvenido.setText("Bienvenido");

        javax.swing.GroupLayout jPanelDecoracionLayout = new javax.swing.GroupLayout(jPanelDecoracion);
        jPanelDecoracion.setLayout(jPanelDecoracionLayout);
        jPanelDecoracionLayout.setHorizontalGroup(
            jPanelDecoracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDecoracionLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblBienvenido, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jPanelDecoracionLayout.setVerticalGroup(
            jPanelDecoracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDecoracionLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanelDecoracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBienvenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                "Seleccionar", "Numero", "Piso", "Capacidad", "Precio", "Descripcion"
            }
        ));
        tblHabitacionDisponible.setGridColor(new java.awt.Color(0, 0, 0));
        tblHabitacionDisponible.setRowHeight(35);
        tblHabitacionDisponible.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jScrollHabitacionDisponible.setViewportView(tblHabitacionDisponible);

        jPanel2.setBackground(new java.awt.Color(232, 130, 0));

        jLabel2.setBackground(new java.awt.Color(232, 130, 0));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("RESERVAR");

        btnBuscarHabitacion.setBackground(new java.awt.Color(56, 121, 185));
        btnBuscarHabitacion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscarHabitacion.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarHabitacion.setText("BUSCAR");
        btnBuscarHabitacion.setBorder(null);
        btnBuscarHabitacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarHabitacionActionPerformed(evt);
            }
        });

        jFechaInicio.setBackground(new java.awt.Color(56, 121, 185));

        jFechafinal.setBackground(new java.awt.Color(56, 121, 185));

        check_out.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        check_out.setForeground(new java.awt.Color(255, 255, 255));
        check_out.setText("FEFCHA FIN");

        Check_in.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Check_in.setForeground(new java.awt.Color(255, 255, 255));
        Check_in.setText("FECHA INICIO");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 455, Short.MAX_VALUE)
                .addComponent(Check_in)
                .addGap(18, 18, 18)
                .addComponent(jFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(check_out)
                .addGap(18, 18, 18)
                .addComponent(jFechafinal, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(btnBuscarHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(btnBuscarHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Check_in))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(check_out, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jFechafinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(9, 9, 9)))
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

        jPanelCargarHuesped.setBackground(new java.awt.Color(255, 255, 255));

        jPanel5.setBackground(new java.awt.Color(232, 130, 0));

        jLabel4.setBackground(new java.awt.Color(232, 130, 0));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("CARGAR HUESPED");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1217, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(106, Short.MAX_VALUE))
        );

        txtNombreHuesped.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNombreHuesped.setText("Ingrese el nombre");
        txtNombreHuesped.setBorder(null);
        txtNombreHuesped.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreHuespedFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNombreHuespedFocusLost(evt);
            }
        });

        txtDniHuesped.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtDniHuesped.setText("Ingrese el DNI");
        txtDniHuesped.setBorder(null);
        txtDniHuesped.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDniHuespedFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDniHuespedFocusLost(evt);
            }
        });

        btnCancelarHuesped.setBackground(new java.awt.Color(204, 0, 0));
        btnCancelarHuesped.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCancelarHuesped.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelarHuesped.setText("CANCELAR");
        btnCancelarHuesped.setBorder(null);
        btnCancelarHuesped.setFocusPainted(false);
        btnCancelarHuesped.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarHuespedActionPerformed(evt);
            }
        });

        txtTelefonoHuesped.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTelefonoHuesped.setText("Ingrese el número de teléfono");
        txtTelefonoHuesped.setBorder(null);
        txtTelefonoHuesped.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTelefonoHuespedFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTelefonoHuespedFocusLost(evt);
            }
        });

        txtEmailHuesped.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtEmailHuesped.setText("Ingrese el email");
        txtEmailHuesped.setBorder(null);
        txtEmailHuesped.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEmailHuespedFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmailHuespedFocusLost(evt);
            }
        });

        lblEmailHuesped.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblEmailHuesped.setText("EMAIL");

        lblMetodoDePago.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblMetodoDePago.setText("METODO DE PAGO");

        lblNombreHuesped.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblNombreHuesped.setText("NOMBRE");

        lblDniHuesped.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblDniHuesped.setText("DNI");

        lblTelefonoHuesped.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTelefonoHuesped.setText("TELÉFONO");

        lblApellidoHuesped.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblApellidoHuesped.setText("APELLIDO");

        txtApellidoHuesped.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtApellidoHuesped.setText("Ingrese el apellido");
        txtApellidoHuesped.setBorder(null);
        txtApellidoHuesped.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtApellidoHuespedFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtApellidoHuespedFocusLost(evt);
            }
        });
        txtApellidoHuesped.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellidoHuespedActionPerformed(evt);
            }
        });

        btnConfirmarHuesped.setBackground(new java.awt.Color(0, 153, 51));
        btnConfirmarHuesped.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnConfirmarHuesped.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmarHuesped.setText("CONFIRMAR");
        btnConfirmarHuesped.setBorder(null);
        btnConfirmarHuesped.setFocusPainted(false);
        btnConfirmarHuesped.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarHuespedActionPerformed(evt);
            }
        });

        txtMetodoDePago.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtMetodoDePago.setText("Ingrese el metodo de pago");
        txtMetodoDePago.setBorder(null);
        txtMetodoDePago.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMetodoDePagoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMetodoDePagoFocusLost(evt);
            }
        });
        txtMetodoDePago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMetodoDePagoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCargarHuespedLayout = new javax.swing.GroupLayout(jPanelCargarHuesped);
        jPanelCargarHuesped.setLayout(jPanelCargarHuespedLayout);
        jPanelCargarHuespedLayout.setHorizontalGroup(
            jPanelCargarHuespedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelCargarHuespedLayout.createSequentialGroup()
                .addGroup(jPanelCargarHuespedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCargarHuespedLayout.createSequentialGroup()
                        .addGap(321, 321, 321)
                        .addGroup(jPanelCargarHuespedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelCargarHuespedLayout.createSequentialGroup()
                                .addGroup(jPanelCargarHuespedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNombreHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNombreHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtEmailHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblEmailHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTelefonoHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTelefonoHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanelCargarHuespedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelCargarHuespedLayout.createSequentialGroup()
                                        .addGap(99, 99, 99)
                                        .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanelCargarHuespedLayout.createSequentialGroup()
                                        .addGap(87, 87, 87)
                                        .addGroup(jPanelCargarHuespedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblMetodoDePago, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtMetodoDePago, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblDniHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtDniHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtApellidoHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblApellidoHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                    .addGroup(jPanelCargarHuespedLayout.createSequentialGroup()
                        .addGap(339, 339, 339)
                        .addComponent(btnConfirmarHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(131, 131, 131)
                        .addComponent(btnCancelarHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(298, Short.MAX_VALUE))
        );
        jPanelCargarHuespedLayout.setVerticalGroup(
            jPanelCargarHuespedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCargarHuespedLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addGroup(jPanelCargarHuespedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblApellidoHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCargarHuespedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellidoHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCargarHuespedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanelCargarHuespedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelCargarHuespedLayout.createSequentialGroup()
                        .addGroup(jPanelCargarHuespedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCargarHuespedLayout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(lblEmailHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtEmailHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addGroup(jPanelCargarHuespedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblTelefonoHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMetodoDePago, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(4, 4, 4)
                                .addGroup(jPanelCargarHuespedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTelefonoHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMetodoDePago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanelCargarHuespedLayout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(lblDniHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDniHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelCargarHuespedLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(117, 117, 117)
                .addGroup(jPanelCargarHuespedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConfirmarHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelarHuesped, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 231, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelReservarLayout = new javax.swing.GroupLayout(jPanelReservar);
        jPanelReservar.setLayout(jPanelReservarLayout);
        jPanelReservarLayout.setHorizontalGroup(
            jPanelReservarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollHabitacionDisponible, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanelReservarLayout.createSequentialGroup()
                .addGap(547, 547, 547)
                .addComponent(btnReservarHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelReservarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanelCargarHuesped, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelReservarLayout.setVerticalGroup(
            jPanelReservarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelReservarLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollHabitacionDisponible, javax.swing.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnReservarHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
            .addGroup(jPanelReservarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanelCargarHuesped, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                "Seleccionar", "Numero", "Piso", "Fecha Inicio", "Fecha Fin", "Total"
            }
        ));
        tblHabitacionOcupada.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblHabitacionOcupada.setGridColor(new java.awt.Color(0, 0, 0));
        tblHabitacionOcupada.setPreferredSize(new java.awt.Dimension(450, 140));
        tblHabitacionOcupada.setRowHeight(35);
        tblHabitacionOcupada.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(tblHabitacionOcupada);

        jPanel3.setBackground(new java.awt.Color(232, 130, 0));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("LISTA DE RESERVAS");

        txtBuscarReserva.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtBuscarReserva.setText("Buscar reserva por DNI o email");
        txtBuscarReserva.setBorder(null);
        txtBuscarReserva.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarReservaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBuscarReservaFocusLost(evt);
            }
        });
        txtBuscarReserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarReservaActionPerformed(evt);
            }
        });

        btnBuscarReserva.setBackground(new java.awt.Color(56, 121, 185));
        btnBuscarReserva.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscarReserva.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarReserva.setText("BUSCAR");
        btnBuscarReserva.setFocusPainted(false);
        btnBuscarReserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarReservaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtBuscarReserva, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscarReserva)
                .addGap(25, 25, 25))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(btnBuscarReserva, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscarReserva, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        btnModificar.setBackground(new java.awt.Color(0, 153, 255));
        btnModificar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnModificar.setForeground(new java.awt.Color(255, 255, 255));
        btnModificar.setText("MODIFICAR");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(204, 0, 0));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("CANCELAR");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnCheckin.setBackground(new java.awt.Color(0, 153, 51));
        btnCheckin.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCheckin.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckin.setText("CHECK-IN");
        btnCheckin.setToolTipText("");
        btnCheckin.setBorder(null);
        btnCheckin.setFocusPainted(false);
        btnCheckin.setMaximumSize(new java.awt.Dimension(101, 25));
        btnCheckin.setMinimumSize(new java.awt.Dimension(101, 25));
        btnCheckin.setPreferredSize(new java.awt.Dimension(101, 25));
        btnCheckin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckinActionPerformed(evt);
            }
        });

        btnCheckout.setBackground(new java.awt.Color(102, 102, 102));
        btnCheckout.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCheckout.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckout.setText("CHECK-OUT");
        btnCheckout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelListarReservasLayout = new javax.swing.GroupLayout(jPanelListarReservas);
        jPanelListarReservas.setLayout(jPanelListarReservasLayout);
        jPanelListarReservasLayout.setHorizontalGroup(
            jPanelListarReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelListarReservasLayout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(389, 389, 389)
                .addComponent(btnCheckin, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(btnCheckout, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(764, Short.MAX_VALUE))
        );
        jPanelListarReservasLayout.setVerticalGroup(
            jPanelListarReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelListarReservasLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 719, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelListarReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelListarReservasLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanelListarReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelListarReservasLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelListarReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCheckin, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCheckout, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29))))
        );

        jPanelModificar.setPreferredSize(new java.awt.Dimension(600, 400));

        javax.swing.GroupLayout jPanelModificarLayout = new javax.swing.GroupLayout(jPanelModificar);
        jPanelModificar.setLayout(jPanelModificarLayout);
        jPanelModificarLayout.setHorizontalGroup(
            jPanelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1479, Short.MAX_VALUE)
        );
        jPanelModificarLayout.setVerticalGroup(
            jPanelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 933, Short.MAX_VALUE)
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
            .addGroup(jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelContenidoLayout.createSequentialGroup()
                    .addGap(689, 689, 689)
                    .addComponent(jPanelModificar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanelContenidoLayout.setVerticalGroup(
            jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelListarReservas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelContenidoLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jPanelReservar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
            .addGroup(jPanelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelContenidoLayout.createSequentialGroup()
                    .addGap(434, 434, 434)
                    .addComponent(jPanelModificar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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

        jScrollPane2.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1784, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnListaReservasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListaReservasActionPerformed
        jPanelContenido.setVisible(true);
        Color color = new Color(204, 102, 0);
        Color colorOriginal = new Color(252, 167, 85);
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
        jPanelContenido.setVisible(true);
        Color color = new Color(204, 102, 0);
        Color colorOriginal = new Color(252, 167, 85);
        btnReservar.setBackground(color);
        if (btnReservar.getBackground().equals(color)) {
            btnListaReservas.setBackground(colorOriginal);
        } else {
            btnReservar.setBackground(Color.RED);
        }

        // Mostrar el panel de reserva y ocultar el de listado
        jPanelReservar.setVisible(true);
        jPanelListarReservas.setVisible(false);

        // === SETEAR LAS FECHAS ===
        Calendar calendarioInicio = Calendar.getInstance();
        calendarioInicio.add(Calendar.DATE, 1); // mañana
        jFechaInicio.setDate(calendarioInicio.getTime());

        Calendar calendarioFin = (Calendar) calendarioInicio.clone();
        calendarioFin.add(Calendar.DATE, 1); // 1 días después del inicio
        jFechafinal.setDate(calendarioFin.getTime());

        btnBuscarHabitacionActionPerformed(null);
    }//GEN-LAST:event_btnReservarActionPerformed

    private void jSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSalirActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            // Cierra la ventana actual
            this.dispose();

            // Abre la ventana de inicio de sesión
            InicioSesion login = new InicioSesion();
            login.setVisible(true);
            login.setLocationRelativeTo(null); // Centrar
        }
    }//GEN-LAST:event_jSalirActionPerformed

    private void btnReservarHabitacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservarHabitacionActionPerformed
        // forzar confirmación de edición
        if (tblHabitacionDisponible.isEditing()) {
            tblHabitacionDisponible.getCellEditor().stopCellEditing();
        }
        int fila = -1;
        Long idSeleccionado = null;
        for (int i = 0; i < tblHabitacionDisponible.getRowCount(); i++) {
            Boolean sel = (Boolean) tblHabitacionDisponible.getValueAt(i, 0);
            if (Boolean.TRUE.equals(sel)) {
                fila = i;
                Object idVal = tblHabitacionDisponible.getValueAt(i, 6);
                idSeleccionado = (idVal instanceof Number n) ? n.longValue() : null;
                break;
            }
        }
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una habitación primero.");
            return;
        }
        idHabitacionActual = idSeleccionado;

        jPanelCargarHuesped.setVisible(true);

        jScrollHabitacionDisponible.setVisible(false);
        btnReservarHabitacion.setVisible(false);
    }//GEN-LAST:event_btnReservarHabitacionActionPerformed

    private void btnBuscarHabitacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarHabitacionActionPerformed
        Date fechaInicio = jFechaInicio.getDate();
        Date fechaFin = jFechafinal.getDate();
        Date hoy = new Date();

        if (fechaInicio == null || fechaFin == null) {
            JOptionPane.showMessageDialog(this, "Debés seleccionar ambas fechas.");
            return;
        }

        if (!fechaFin.after(fechaInicio)) {
            JOptionPane.showMessageDialog(this, "La fecha de egreso debe ser posterior a la de ingreso.");
            return;
        }

        if (fechaInicio.before(hoy)) {
            JOptionPane.showMessageDialog(this, "No se puede buscar disponibilidad para fechas pasadas.");
            return;
        }

        cargarHabitacionesEnTabla(fechaInicio, fechaFin);

    }//GEN-LAST:event_btnBuscarHabitacionActionPerformed

    private void txtNombreHuespedFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreHuespedFocusGained
        if (txtNombreHuesped.getText().equals("Ingrese el nombre")) {
            txtNombreHuesped.setText("");
            txtNombreHuesped.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtNombreHuespedFocusGained

    private void txtNombreHuespedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreHuespedFocusLost
        if (txtNombreHuesped.getText().isEmpty()) {
            txtNombreHuesped.setForeground(new java.awt.Color(150, 150, 150));
            txtNombreHuesped.setText("Ingrese el nombre");
        }
    }//GEN-LAST:event_txtNombreHuespedFocusLost

    private void txtDniHuespedFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDniHuespedFocusGained
        if (txtDniHuesped.getText().equals("Ingrese el DNI")) {
            txtDniHuesped.setText("");
            txtDniHuesped.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtDniHuespedFocusGained

    private void txtDniHuespedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDniHuespedFocusLost
        if (txtDniHuesped.getText().isEmpty()) {
            txtDniHuesped.setForeground(new java.awt.Color(150, 150, 150));
            txtDniHuesped.setText("Ingrese el DNI");
        }
    }//GEN-LAST:event_txtDniHuespedFocusLost

    private void btnCancelarHuespedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarHuespedActionPerformed
        // Ocultar el panel de carga de huésped
        jPanelCargarHuesped.setVisible(false);
        btnReservarHabitacion.setVisible(true);
        jScrollHabitacionDisponible.setVisible(true);
        tblHabitacionDisponible.setVisible(true);

        tblHabitacionDisponible.clearSelection();

        // Limpiar ID actual
        idHabitacionActual = null;;
    }//GEN-LAST:event_btnCancelarHuespedActionPerformed

    private void txtTelefonoHuespedFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoHuespedFocusGained
        if (txtTelefonoHuesped.getText().equals("Ingrese el número de teléfono")) {
            txtTelefonoHuesped.setText("");
            txtTelefonoHuesped.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtTelefonoHuespedFocusGained

    private void txtTelefonoHuespedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoHuespedFocusLost
        if (txtTelefonoHuesped.getText().isEmpty()) {
            txtTelefonoHuesped.setForeground(new java.awt.Color(150, 150, 150));
            txtTelefonoHuesped.setText("Ingrese el número de teléfono");
        }
    }//GEN-LAST:event_txtTelefonoHuespedFocusLost

    private void txtEmailHuespedFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailHuespedFocusGained
        if (txtEmailHuesped.getText().equals("Ingrese el email")) {
            txtEmailHuesped.setText("");
            txtEmailHuesped.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtEmailHuespedFocusGained

    private void txtEmailHuespedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailHuespedFocusLost
        if (txtEmailHuesped.getText().isEmpty()) {
            txtEmailHuesped.setForeground(new java.awt.Color(150, 150, 150));
            txtEmailHuesped.setText("Ingrese el email");
        }
    }//GEN-LAST:event_txtEmailHuespedFocusLost

    private void txtApellidoHuespedFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellidoHuespedFocusGained
        if (txtApellidoHuesped.getText().equals("Ingrese el apellido")) {
            txtApellidoHuesped.setText("");
            txtApellidoHuesped.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtApellidoHuespedFocusGained

    private void txtApellidoHuespedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellidoHuespedFocusLost
        if (txtApellidoHuesped.getText().isEmpty()) {
            txtApellidoHuesped.setForeground(new java.awt.Color(150, 150, 150));
            txtApellidoHuesped.setText("Ingrese el apellido");
        }
    }//GEN-LAST:event_txtApellidoHuespedFocusLost

    private void btnConfirmarHuespedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarHuespedActionPerformed
        String nombre = txtNombreHuesped.getText().trim();
        String apellido = txtApellidoHuesped.getText().trim();
        String email = txtEmailHuesped.getText().trim();
        String dni = txtDniHuesped.getText().trim();
        String telefono = txtTelefonoHuesped.getText().trim();

        Date fechaInicio = jFechaInicio.getDate();
        Date fechaFin = jFechafinal.getDate();

        // Validación de campos obligatorios
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || dni.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos del huésped deben estar completos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (fechaInicio == null || fechaFin == null || !fechaFin.after(fechaInicio)) {
            JOptionPane.showMessageDialog(this, "Seleccioná un rango de fechas válido.", "Fechas inválidas", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (idHabitacionActual == null) {
            JOptionPane.showMessageDialog(this, "No hay una habitación seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Deseás confirmar la reserva?", "Confirmar reserva", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            // Buscar o crear huésped

            Huesped huesped = empleadoService.cargarHuesped(nombre, apellido, email, dni, telefono);

            // Buscar habitación
            Habitacion habitacion = habitacionService.findById(idHabitacionActual);
            if (habitacion == null) {
                JOptionPane.showMessageDialog(this, "La habitación ya no está disponible.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Reserva reserva = reservaService.cargarReserva(fechaInicio, fechaFin, habitacion, huesped);

            Factura factura = facturaService.crearYGuardarFactura(reserva, txtMetodoDePago.getText().trim());

            JOptionPane.showMessageDialog(this, "Reserva confirmada exitosamente.");

            new Thread(() -> {
                try {
                    EmailService emailService = new EmailService();
                    emailService.enviarCorreoReserva(huesped, reserva, factura);
                } catch (Exception ex) {
                    System.err.println("Error al enviar el correo: " + ex.getMessage());
                }
            }).start();

            // Opcional: recargar tabla de habitaciones
            cargarHabitacionesEnTabla(fechaInicio, fechaFin);
            cargarReservasEnTabla();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        jPanelCargarHuesped.setVisible(false);
        btnReservarHabitacion.setVisible(true);
        jScrollHabitacionDisponible.setVisible(true);
        tblHabitacionDisponible.setVisible(true);
    }//GEN-LAST:event_btnConfirmarHuespedActionPerformed

    private void txtMetodoDePagoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMetodoDePagoFocusGained
        if (txtMetodoDePago.getText().equals("Ingrese el metodo de pago")) {
            txtMetodoDePago.setText("");
            txtMetodoDePago.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtMetodoDePagoFocusGained

    private void txtMetodoDePagoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMetodoDePagoFocusLost
        if (txtMetodoDePago.getText().isEmpty()) {
            txtMetodoDePago.setForeground(new java.awt.Color(150, 150, 150));
            txtMetodoDePago.setText("Ingrese el metodo de pago");
        }
    }//GEN-LAST:event_txtMetodoDePagoFocusLost

    private void txtMetodoDePagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMetodoDePagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMetodoDePagoActionPerformed

    private void txtApellidoHuespedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellidoHuespedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidoHuespedActionPerformed

    private void txtBuscarReservaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarReservaFocusGained
        if (txtBuscarReserva.getText().equals("Buscar reserva por DNI o email")) {
            txtBuscarReserva.setText("");
            txtBuscarReserva.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtBuscarReservaFocusGained

    private void txtBuscarReservaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarReservaFocusLost
        if (txtBuscarReserva.getText().isEmpty()) {
            txtBuscarReserva.setForeground(new java.awt.Color(150, 150, 150));
            txtBuscarReserva.setText("Buscar reserva por DNI o email");
        }
    }//GEN-LAST:event_txtBuscarReservaFocusLost

    private void txtBuscarReservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarReservaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarReservaActionPerformed

    private void btnBuscarReservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarReservaActionPerformed
        String filtro = txtBuscarReserva.getText().trim();
        if (filtro.isEmpty() || filtro.equalsIgnoreCase("Buscar reserva por DNI o email")) {
            sorterHabitacionesOcupadas.setRowFilter(null);  // Mostrar todo
        } else {
            // Buscamos en las columnas 1 a 7 (donde la 7 es Huésped: DNI/email)
            sorterHabitacionesOcupadas.setRowFilter(
                    RowFilter.regexFilter("(?i)" + Pattern.quote(filtro), 7)
            );
        }
    }//GEN-LAST:event_btnBuscarReservaActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        int filaSeleccionada = tblHabitacionOcupada.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una reserva.");
            return;
        }

        int filaModelo = tblHabitacionOcupada.convertRowIndexToModel(filaSeleccionada);
        List<Reserva> reservas = reservaService.getReservasActivas();
        Reserva reserva = reservas.get(filaModelo);

        EditarReserva form = new EditarReserva(reserva, this); // ← le pasás la reserva seleccionada
        form.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                cargarReservasEnTabla(); // recargar la tabla al cerrar
            }
        });
        form.setVisible(true);
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnCheckinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckinActionPerformed
        // Obtenemos la lista original de reservas activas (confirmadas o checkin)
        List<Reserva> reservas = reservaService.getReservasActivas();

        DefaultTableModel model = (DefaultTableModel) tblHabitacionOcupada.getModel();
        List<Integer> filasSeleccionadas = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean marcado = (Boolean) model.getValueAt(i, 0);
            if (marcado != null && marcado) {
                filasSeleccionadas.add(i);
            }
        }

        if (filasSeleccionadas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar al menos una reserva para hacer check-in.");
            return;
        }

        boolean alMenosUnoProcesado = false;
        Date hoy = new Date();

        for (int filaVista : filasSeleccionadas) {
            int filaModelo = tblHabitacionOcupada.convertRowIndexToModel(filaVista);
            Reserva reserva = reservas.get(filaModelo);

            if (reserva.getFechaInicio().after(hoy)) {
                JOptionPane.showMessageDialog(null, "No se puede hacer el check-in antes de la fecha de inicio: " + reserva.getFechaInicio());
                continue;
            }

            try {
                // Actualizamos el estado a CHECK_IN y la fecha checkIn
                reserva.setEstado(EstadoReserva.CHECK_IN);
                reserva.setCheckIn(hoy);
                reservaService.actualizarReserva(reserva);
                alMenosUnoProcesado = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al actualizar reserva: " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (alMenosUnoProcesado) {
            cargarReservasEnTabla(); // Actualizar la tabla para mostrar los cambios
            JOptionPane.showMessageDialog(null, "Check-in realizado correctamente.");
        }
    }//GEN-LAST:event_btnCheckinActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        List<Reserva> reservas = reservaService.getReservasActivas(); // Solo CONFIRMADA o CHECK_IN
        DefaultTableModel model = (DefaultTableModel) tblHabitacionOcupada.getModel();
        List<Integer> filasSeleccionadas = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean marcado = (Boolean) model.getValueAt(i, 0);
            if (marcado != null && marcado) {
                filasSeleccionadas.add(i);
            }
        }

        if (filasSeleccionadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos una reserva para cancelar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea cancelar la reserva?", "Confirmar cancelación", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean alMenosUnaCancelada = false;

        for (int fila : filasSeleccionadas) {
            int filaModelo = tblHabitacionOcupada.convertRowIndexToModel(fila);
            Reserva reserva = reservas.get(filaModelo);

            // ✅ Validar estado
            EstadoReserva estadoActual = reserva.getEstado();
            if (estadoActual == EstadoReserva.CHECK_IN || estadoActual == EstadoReserva.CHECK_OUT || estadoActual == EstadoReserva.CANCELADA) {
                JOptionPane.showMessageDialog(this,
                        "No se puede cancelar una reserva en estado " + estadoActual + ".");
                continue;
            }

            try {
                reserva.setEstado(EstadoReserva.CANCELADA);
                reservaService.actualizarReserva(reserva);
                alMenosUnaCancelada = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cancelar la reserva: " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (alMenosUnaCancelada) {
            cargarReservasEnTabla(); // Refresca
            JOptionPane.showMessageDialog(this, "Reserva cancelada correctamente.");
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCheckoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckoutActionPerformed
        List<Reserva> reservas = reservaService.getReservasActivas(); // Trae CONFIRMADA y CHECK_IN
        DefaultTableModel model = (DefaultTableModel) tblHabitacionOcupada.getModel();
        List<Integer> filasSeleccionadas = new ArrayList<>();

        // Buscar reservas marcadas
        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean marcado = (Boolean) model.getValueAt(i, 0);
            if (marcado != null && marcado) {
                filasSeleccionadas.add(i);
            }
        }

        if (filasSeleccionadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos una reserva para hacer check-out.");
            return;
        }

        boolean alMenosUnoProcesado = false;

        for (int fila : filasSeleccionadas) {
            int filaModelo = tblHabitacionOcupada.convertRowIndexToModel(fila);
            Reserva reserva = reservas.get(filaModelo);

            // Verificar que el estado actual sea CHECK_IN
            if (reserva.getEstado() != EstadoReserva.CHECK_IN) {
                JOptionPane.showMessageDialog(this,
                        "La reserva seleccionada no está en estado CHECK_IN. No se puede hacer check-out.");
                continue;
            }

            try {
                reservaService.hacerCheckOut(reserva.getId_reserva());

                reserva = reservaService.getReservaPorId(reserva.getId_reserva());

                alMenosUnoProcesado = true;

                Huesped huesped = reserva.getHuesped();
                String userHome = System.getProperty("user.home");
                String rutaFactura = userHome + File.separator + "Downloads" + File.separator + "factura_reserva_" + reserva.getId_reserva() + ".pdf";
                Factura factura = facturaService.getFacturaPorReserva(reserva.getId_reserva());

                if (factura == null) {
                    JOptionPane.showMessageDialog(this, "No se encontró la factura para la reserva.");
                    return;
                }

                GenerarDocumento generador = new GenerarPdfEmpleado(reserva, huesped, factura);
                generador.generarPdf(rutaFactura);
                JOptionPane.showMessageDialog(this, "Factura generada correctamente en:\n" + rutaFactura);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al hacer check-out: " + e.getMessage());
                e.printStackTrace();
            }
        }
        if (alMenosUnoProcesado) {
            cargarReservasEnTabla();
            JOptionPane.showMessageDialog(this, "Check-out realizado correctamente.");
        }
    }//GEN-LAST:event_btnCheckoutActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Check_in;
    private javax.swing.JButton btnBuscarHabitacion;
    private javax.swing.JButton btnBuscarReserva;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelarHuesped;
    private javax.swing.JButton btnCheckin;
    private javax.swing.JButton btnCheckout;
    private javax.swing.JButton btnConfirmarHuesped;
    private javax.swing.JButton btnListaReservas;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnReservar;
    private javax.swing.JButton btnReservarHabitacion;
    private javax.swing.JLabel check_out;
    private com.toedter.calendar.JDateChooser jFechaInicio;
    private com.toedter.calendar.JDateChooser jFechafinal;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelCargarHuesped;
    private javax.swing.JPanel jPanelContenido;
    private javax.swing.JPanel jPanelDecoracion;
    private javax.swing.JPanel jPanelListarReservas;
    private javax.swing.JPanel jPanelMenu;
    private javax.swing.JPanel jPanelModificar;
    private javax.swing.JPanel jPanelReservar;
    private javax.swing.JButton jSalir;
    private javax.swing.JScrollPane jScrollHabitacionDisponible;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel lblApellidoHuesped;
    private javax.swing.JLabel lblBienvenido;
    private javax.swing.JLabel lblDniHuesped;
    private javax.swing.JLabel lblEmailHuesped;
    private javax.swing.JLabel lblMetodoDePago;
    private javax.swing.JLabel lblNombreHuesped;
    private javax.swing.JLabel lblTelefonoHuesped;
    private javax.swing.JTable tblHabitacionDisponible;
    private javax.swing.JTable tblHabitacionOcupada;
    private javax.swing.JTextField txtApellidoHuesped;
    private javax.swing.JTextField txtBuscarReserva;
    private javax.swing.JTextField txtDniHuesped;
    private javax.swing.JTextField txtEmailHuesped;
    private javax.swing.JTextField txtMetodoDePago;
    private javax.swing.JTextField txtNombreHuesped;
    private javax.swing.JTextField txtTelefonoHuesped;
    // End of variables declaration//GEN-END:variables
}
