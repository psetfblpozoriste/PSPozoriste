/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;

/**
 *
 * @author Ognjen
 */
public class Karta {
    
    
    public Integer id;
    
    public Integer brojReda;
    
    public Integer brojSjedista;
    
    public float iznos = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.getInstance().get(Calendar.WEDNESDAY) ? 2.0f : 3.0f;  
    
    public Date termin;
    
    public Integer idScene;
    
    
    public Karta(Integer id,Integer brojReda,Integer brojSjedista,Date termin,Integer idScene){
        this.id = id;
        this.brojReda = brojReda;
        this.brojSjedista = brojSjedista;
        this.termin = termin;
        this.idScene = idScene;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBrojReda() {
        return brojReda;
    }

    public void setBrojReda(Integer brojReda) {
        this.brojReda = brojReda;
    }

    public Integer getBrojSjedista() {
        return brojSjedista;
    }

    public void setBrojSjedista(Integer brojSjedista) {
        this.brojSjedista = brojSjedista;
    }

    public float getIznos() {
        return iznos;
    }

    public void setIznos(Float iznos) {
        this.iznos = iznos;
    }

    public Date getTermin() {
        return termin;
    }

    public void setTermin(Date termin) {
        this.termin = termin;
    }

    public Integer getIdScene() {
        return idScene;
    }

    public void setIdScene(Integer idScene) {
        this.idScene = idScene;
    }
    
    
    
}
