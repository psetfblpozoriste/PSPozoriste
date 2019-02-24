/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dao.mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.etfbl.is.pozoriste.controller.PregledRadnikaController;
import net.etfbl.is.pozoriste.model.dto.AdministrativniRadnik;

/**
 *
 * @author djord
 */
public class AdministratorDAO {

    public static void dodajAdministrativnogRadnika(AdministrativniRadnik admin) {
        System.out.println("DODAVANJE ADMINA: " + admin.getHash());
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeAdministrativnogRadnika(?,?,?,?,?,?,?,?)}");
            callableStatement.setString(1, admin.getIme());
            callableStatement.setString(2, admin.getPrezime());
            callableStatement.setString(3, admin.getJmb());
            callableStatement.setString(4, admin.getKontakt());
            callableStatement.setString(5, admin.getKorisnickoIme());
            callableStatement.setString(6, admin.getHash());
            callableStatement.setString(7, admin.getTipRadnika());
            callableStatement.registerOutParameter(8, Types.INTEGER);

            callableStatement.executeQuery();

            admin.setIdRadnika(callableStatement.getInt(8));
            System.out.println("ADMIN ID: " + admin.getIdRadnika());
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

    public static void ubaciUTabeluRadnik() {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        AdministrativniRadnik admin;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            statement = connection.createStatement();
            rs = statement.executeQuery("select * from admini_info");
            while (rs.next()) {

                admin = new AdministrativniRadnik(rs.getString("Ime"), rs.getString("Prezime")/*, rs.getString("OpisPosla")*/, rs.getString("JMB"), rs.getBoolean("StatusRadnika"), rs.getString("Kontakt"), rs.getString("KorisnickoIme"), rs.getString("HashLozinke"), rs.getString("TipKorisnika"));
                admin.setIdRadnika(rs.getInt("Id"));
                if (!PregledRadnikaController.radniciObservableList.contains(admin)) {
                    PregledRadnikaController.radniciObservableList.add(admin);
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
