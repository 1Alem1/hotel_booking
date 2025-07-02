package com.hibernatestandalone.HibernateStandalone;

import com.hibernatestandalone.entity.Factura;
import com.hibernatestandalone.entity.Huesped;
import com.hibernatestandalone.entity.Reserva;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

public class GenerarPdfEmpleado extends Documento implements GenerarDocumento {

     private Reserva reserva;
    private Huesped huesped;
    private final Factura factura;

    public GenerarPdfEmpleado(Reserva reserva, Huesped huesped, Factura factura) {
        this.reserva = reserva;
        this.huesped = huesped;
        this.factura = factura;
    }
    @Override
    public void generarPdf(String rutaArchivo) {
          try {
            Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(doc, new FileOutputStream(rutaArchivo));
            doc.open();

            // Agregar encabezado con logo y título
            agregarEncabezado(doc, "Factura de Reserva");

            // Tabla con los datos del cliente y la reserva
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font normal = new Font(Font.HELVETICA, 12);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            table.addCell(new Phrase("Cliente:", bold));
            table.addCell(new Phrase(huesped.getNombre() + " " + huesped.getApellido(), normal));

            table.addCell(new Phrase("DNI:", bold));
            table.addCell(new Phrase(huesped.getDni(), normal));

            table.addCell(new Phrase("Habitación:", bold));
            table.addCell(new Phrase(reserva.getHabitacion().getNumero(), normal));

            table.addCell(new Phrase("Fecha Ingreso:", bold));
            table.addCell(new Phrase(sdf.format(reserva.getCheckIn()), normal));

            table.addCell(new Phrase("Fecha Salida:", bold));
            table.addCell(new Phrase(sdf.format(reserva.getCheckOut()), normal));

            doc.add(new Paragraph("Forma de pago: " + factura.getMedioDePago()));
            
            doc.add(new Paragraph("Total a pagar: $" + factura.getTotal()));

                       
            doc.add(table);

            // Pie del documento
            agregarPie(doc);

            doc.close();
            System.out.println("Factura generada: " + rutaArchivo);

        } catch (Exception e) {
            e.printStackTrace();
        }
}
}