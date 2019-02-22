/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.etfbl.is.pozoriste.model.dao.mysql.RadnikDAO;

/**
 *
 * @author djord
 */
public class Biletar extends RadnikKojiKoristiSistem {

    private String hash = "";

    private String korisnickoIme = "";

    private String tipKorisnika = "Biletar";
    
    
    public Biletar(){
        
    }

    public Biletar(String ime, String prezime, String opisPosla, String jmb, boolean statusRadnika, String kontakt,
            String korisnickoIme, String hashLozinke, String tipKorisnika) {
        super(ime, prezime, opisPosla, jmb, statusRadnika, kontakt);
        this.korisnickoIme = korisnickoIme;
        this.hash = hashLozinke;
        this.tipKorisnika = tipKorisnika;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public String getTipKorisnika() {
        return tipKorisnika;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public void setTipKorisnika(String tipKorisnika) {
        this.tipKorisnika = tipKorisnika;
    }

    private String hashSHA256(String value) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RadnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] encodedhash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        String hash = bytesToHex(encodedhash);
        return hash;
    }

    private String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    

}
