/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dto;

import java.sql.Date;

/**
 *
 * @author Darko
 */
public class Repertoar {
    
    private Date mjesecIGodina;
    
    public Repertoar(Date mjesecIGodina){
        this.mjesecIGodina=mjesecIGodina;
    }
    public void setMjesecIGodina(Date mjesecIGodina){
        this.mjesecIGodina=mjesecIGodina;
    }
    
    public Date getMjesecIGodina(){
        return mjesecIGodina;
    }
    
}
