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
import javafx.scene.control.Alert;
import net.etfbl.is.pozoriste.model.dto.Kreiranje;
import net.etfbl.is.pozoriste.model.dto.Predstava;

/**
 *
 * @author Mina
 */
public class KreiranjeDAO {
    public static void dodajKreiranje(Kreiranje kreiranje) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        String poruka;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeKreiranja(?,?,?,?)}");
            callableStatement.setObject(1, kreiranje.getIdPredstave());
            callableStatement.setObject(2, kreiranje.getIdRepertoara());
            callableStatement.setObject(3, kreiranje.getIdGostujucePredstave());
            callableStatement.setInt(4, kreiranje.getIdRadnik());
            
            callableStatement.executeQuery();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(KreiranjeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(KreiranjeDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
