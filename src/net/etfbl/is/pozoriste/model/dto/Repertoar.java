/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dto;

import java.sql.Date;
import java.util.LinkedList;

/**
 *
 * @author Darko
 */
public class Repertoar {
    
    private Date mjesecIGodina;
    
    private Integer id;
    
    private LinkedList<Igranje> igranja;
    
    public Repertoar(Integer id,Date mjesecIGodina){
        this.mjesecIGodina=mjesecIGodina;
        this.id = id;
    }
    public void setMjesecIGodina(Date mjesecIGodina){
        this.mjesecIGodina=mjesecIGodina;
    }
    
    public Date getMjesecIGodina(){
        return mjesecIGodina;
    }

    public LinkedList<Igranje> getIgranja() {
        return igranja;
    }

    public void setIgranja(LinkedList<Igranje> igranja) {
        this.igranja = igranja;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    
    
    
}
