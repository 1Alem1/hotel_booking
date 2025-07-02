
package com.hibernatestandalone.HibernateStandalone;

import java.util.Date;
import java.util.List;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.PageSize;
import com.lowagie.text.Chunk;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

public class GenerarPdfGerente extends Documento implements GenerarDocumento {

    private List<Object[]> datos;
    private Date desde;
    private Date hasta;

    public GenerarPdfGerente(List<Object[]> datos, Date desde, Date hasta) {
        this.datos = datos;
        this.desde = desde;
        this.hasta = hasta;
    }
    
    @Override
    public void generarPdf(String rutaArchivo) {
        try {
            Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(doc, new FileOutputStream(rutaArchivo));
            doc.open();

            agregarEncabezado(doc, "Reporte de Ingresos por Habitación");
            doc.add(new Paragraph("Periodo: " + desde + " a " + hasta));
            doc.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.addCell("Habitación");
            table.addCell("Reservas");
            table.addCell("Total Ganado");

            double totalGeneral = 0;

            for (Object[] fila : datos) {
                String habitacion = (String) fila[0];
                Long cantidad = (Long) fila[1];
                Double total = (Double) fila[2];

                table.addCell(habitacion);
                table.addCell(String.valueOf(cantidad));
                table.addCell("$" + String.format("%.2f", total));
                totalGeneral += total;
            }

            doc.add(table);
            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("TOTAL GENERAL: $" + String.format("%.2f", totalGeneral)));
            agregarPie(doc);
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
