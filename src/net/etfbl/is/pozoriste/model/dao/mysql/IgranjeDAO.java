/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dao.mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.etfbl.is.pozoriste.model.dto.Igranje;

/**
 *
 * @author Ognjen
 */
public class IgranjeDAO {

    public static LinkedList<Igranje> getIgranja(int idRepertoara) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        LinkedList<Igranje> igranja = new LinkedList<>();
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call pregledIgranjaZaOdredjeniRepertoar(?)}");
            callableStatement.setInt(1, idRepertoara);
            resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                Date termin = resultSet.getDate("termin");
                Integer idS = resultSet.getInt("idScene");
                Integer idGost = resultSet.getInt("idGostujucePredstave");
                Integer idP = resultSet.getInt("idPredstave");
                int idR = resultSet.getInt("idRepertoara");
                igranja.add(new Igranje(termin, idS, idP,idGost, idR));
                System.out.println(igranja.get(0).getIdScene());
                //bjekat2 != null ? objekat2 : null ,objekat3 != null ? (Int)objekat3 : null 
            }
        } catch (SQLException sql) {
            Logger.getLogger(IgranjeDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(IgranjeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(IgranjeDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return igranja;
    }

}
