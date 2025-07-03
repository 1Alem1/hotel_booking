package com.hibernatestandalone.HibernateStandalone;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.util.Date;

public class Documento {

    protected void agregarEncabezado(Document document, String titulo) throws Exception {
        java.net.URL logoUrl = getClass().getClassLoader().getResource("Logo.png");
        if (logoUrl == null) {
            throw new RuntimeException("No se encontró la imagen Logo.png en el classpath");
        }
        Image logo = Image.getInstance(logoUrl);
        logo.scaleAbsolute(60f, 60f);

        Paragraph tituloDoc = new Paragraph(titulo.toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        tituloDoc.setAlignment(Element.ALIGN_CENTER);

        PdfPTable header = new PdfPTable(2);
        header.setWidthPercentage(100);
        header.setWidths(new float[]{1, 4});
        header.addCell(celdaSinBorde(logo));
        header.addCell(celdaSinBorde(tituloDoc));
        document.add(header);
        document.add(new Paragraph("Fecha de generación: " + new Date()));
        document.add(Chunk.NEWLINE);
    }

    protected void agregarPie(Document document) throws DocumentException {
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Hotel El Pajarito S.A. - Documento generado automáticamente."));
    }

    protected PdfPCell celdaSinBorde(Element e) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(e);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
}
