/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dto;

import java.util.Objects;

/**
 *
 * @author djord
 */
public class Radnik {

    private String ime;
    private String prezime;
    private String opisPosla;
    private String jmb;
    private boolean statusRadnika;
    private String kontakt;
    protected String tipRadnika = "";
    
    public Radnik(){}

    public Radnik(String ime, String prezime, String opisPosla, String jmb, boolean statusRadnika, String kontak) {
        this.ime = ime;
        this.prezime = prezime;
        this.opisPosla = opisPosla;
        this.jmb = jmb;
        this.statusRadnika = statusRadnika;
        this.kontakt = kontak;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getOpisPosla() {
        return opisPosla;
    }

    public String getJmb() {
        return jmb;
    }

    public boolean isStatusRadnika() {
        return statusRadnika;
    }

    public String getKontakt() {
        return kontakt;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public void setOpisPosla(String opisPosla) {
        this.opisPosla = opisPosla;
    }

    public void setJmb(String jmb) {
        this.jmb = jmb;
    }

    public void setStatusRadnika(boolean statusRadnika) {
        this.statusRadnika = statusRadnika;
    }

    public void setKontak(String kontak) {
        this.kontakt = kontak;
    }

    @Override
    public String toString() {
        return "Radnik{" + "ime=" + ime + ", prezime=" + prezime + ", opisPosla=" + opisPosla + ", jmb=" + jmb + ", statusRadnika=" + statusRadnika + ", kontak=" + kontakt + '}';
    }

    public String getTipRadnika() {
        return tipRadnika;
    }

    public void setTipRadnika(String tipRadnika) {
        this.tipRadnika = tipRadnika;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.jmb);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Radnik other = (Radnik) obj;
        if (!Objects.equals(this.ime, other.ime)) {
            return false;
        }
        if (!Objects.equals(this.prezime, other.prezime)) {
            return false;
        }
        if (!Objects.equals(this.opisPosla, other.opisPosla)) {
            return false;
        }
        if (!Objects.equals(this.jmb, other.jmb)) {
            return false;
        }
        if (this.statusRadnika != other.statusRadnika) {
            return false;
        }
        if (!Objects.equals(this.kontakt, other.kontakt)) {
            return false;
        }
        return true;
    }
    
    

}
