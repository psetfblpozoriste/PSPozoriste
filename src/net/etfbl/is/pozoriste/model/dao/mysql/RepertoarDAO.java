/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dao.mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.etfbl.is.pozoriste.model.dto.Biletar;
import net.etfbl.is.pozoriste.model.dto.Repertoar;

/**
 *
 * @author Darko
 */
public class RepertoarDAO {
    
    public static void dodajRepertoar(Repertoar repertoar) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeRepertoar(?)}");
            callableStatement.setDate(0, repertoar.getMjesecIGodina());
            

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
    
    
    

    
}
