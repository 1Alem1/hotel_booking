package com.hibernatestandalone.HibernateStandalone;

import com.hibernatestandalone.entity.Reserva;

public class ReservaThread extends Thread{
    private Reserva reserva;
    
    public ReservaThread(Reserva reserva){
        this.reserva = reserva;
    }
    
    @Override
    public void run(){
        
    }   
}
