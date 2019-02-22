/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dao.mysql;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.etfbl.is.pozoriste.controller.PregledRadnikaController;
import net.etfbl.is.pozoriste.model.dto.Biletar;
import net.etfbl.is.pozoriste.model.dto.Radnik;

/**
 *
 * @author djord
 */
public class BIletarDAO {

    public static void dodajBiletara(Biletar biletar) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeBiletara(?,?,?,?,?,?,?,?)}");
            callableStatement.setString(1, biletar.getIme());
            callableStatement.setString(2, biletar.getPrezime());
            callableStatement.setString(3, biletar.getJmb());
            callableStatement.setString(4, biletar.getOpisPosla());
            callableStatement.setString(5, biletar.getKontakt());
            callableStatement.setString(6, biletar.getKorisnickoIme());
            callableStatement.setString(7, hashSHA256(biletar.getHash()));
            callableStatement.setString(8, biletar.getTipKorisnika());

            callableStatement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(BIletarDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BIletarDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private static String hashSHA256(String value) {
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

    private static String bytesToHex(byte[] hash) {
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

    public static void ubaciUTabeluRadnik() {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        Biletar biletar;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            statement = connection.createStatement();
            rs = statement.executeQuery("select * from biltetari_info");
            while (rs.next()) {

                biletar = new Biletar(rs.getString("Ime")
                       ,rs.getString("Prezime"), rs.getString("OpisPosla"), rs.getString("JMB")
                       ,rs.getBoolean("StatusRadnika"), rs.getString("Kontakt"), rs.getString("KorisnickoIme")
                       ,rs.getString("HashLozinke"),rs.getString("TipKorisnika"));
                if (!PregledRadnikaController.radniciObservaleList.contains(biletar)) {
                    PregledRadnikaController.radniciObservaleList.add(biletar);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PregledRadnikaController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PregledRadnikaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
