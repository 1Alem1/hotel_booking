package com.hotel_booking.hotel_booking;

import com.hotel_booking.entity.Nota;
import com.hotel_booking.entity.Usuario;
import com.hotel_booking.services.NotaService;
import com.hotel_booking.services.UsuarioService;
import java.util.List;

public class App {
    public static void main(String[] args) {
    	UsuarioService usuarioService = new UsuarioService();
    	NotaService notaService = new NotaService();
    	try{
            Usuario usuario = usuarioService.findById(20002);
            List<Nota> notas = usuario.getNotas();
            System.out.println(notas);
    	}catch(Exception e) {
            System.out.println("ERROR"); 
            throw e;
    	}
        
    }
} 