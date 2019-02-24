/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dao.mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.etfbl.is.pozoriste.model.dto.AdministrativniRadnik;

/**
 *
 * @author djord
 */
public class AdministratorDAO {

    public static void dodajAdministrativnogRadnika(AdministrativniRadnik admin) {
        System.out.println("DODAVANJE BILETARA: " + admin.getHash());
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
}
