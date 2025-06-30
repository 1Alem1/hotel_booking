
package com.hibernatestandalone.HibernateStandalone;

   

import com.hibernatestandalone.entity.Factura;
import com.hibernatestandalone.entity.Huesped;
import com.hibernatestandalone.entity.Reserva;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {

    private final String remitente = "facundotestaperea@gmail.com"; // Cambiá por tu email
    private final String clave = "dmju lamw ervr ejec"; // Usá clave de aplicación en Gmail

    public void enviarCorreoReserva(Huesped huesped, Reserva reserva, Factura factura) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        Message mensaje = new MimeMessage(session);
        mensaje.setFrom(new InternetAddress(remitente));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(huesped.getEmail()));
        mensaje.setSubject("Confirmación de Reserva – Hotel");

        String cuerpo = "Hola " + huesped.getNombre() + " " + huesped.getApellido() + ",\n\n"
                + "Tu reserva ha sido confirmada con éxito.\n\n"
                + "📅 Fecha de entrada: " + reserva.getFechaInicio() + "\n"
                + "📅 Fecha de salida: " + reserva.getFechaFin() + "\n"
                + "🛏 Habitación: " + reserva.getHabitacion().getNumero() + "\n"
                + "💰 Precio total: $" + factura.getTotal() + "\n\n"
                + "Gracias por elegirnos.\n"
                + "Hotel San remo.";

        mensaje.setText(cuerpo);
        Transport.send(mensaje);
    }
}

