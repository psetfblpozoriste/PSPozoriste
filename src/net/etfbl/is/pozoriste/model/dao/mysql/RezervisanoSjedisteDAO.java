/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dao.mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.etfbl.is.pozoriste.model.dto.RezervisanoSjediste;

/**
 *
 * @author Ognjen
 */
public class RezervisanoSjedisteDAO {
    
    
    public static List<RezervisanoSjediste> sjedista(Date termin,Integer idScene) {
        List<RezervisanoSjediste> sjedistaRezervisana = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * " + "FROM  rezervisano_sjediste" + "WHERE rezervisano_sjediste.termin="+termin.toString()+ "and rezervisano_sjediste.idScene="+idScene;
        
        try {
            connection = ConnectionPool.getInstance().checkOut();
            preparedStatement = connection.prepareStatement(query);
            
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
               RezervisanoSjediste rezervisanoSjediste = new RezervisanoSjediste(
                    resultSet.getInt(0),resultSet.getInt(1),resultSet.getInt(2),resultSet.getDate(4)); 
               sjedistaRezervisana.add(rezervisanoSjediste);
            }

        } catch (SQLException sql) {
            Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return sjedistaRezervisana;
    }
    
    
    
     public static boolean addRezervisanoSjediste(RezervisanoSjediste rezervisanoSjediste) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeRezervisanogSjedista(?,?,?,?)}");
            callableStatement.setInt(1, rezervisanoSjediste.getBrojSjedista());
            callableStatement.setDate(2, rezervisanoSjediste.getTermin());
            callableStatement.setInt(3, rezervisanoSjediste.getIdScene());
            callableStatement.setInt(4, rezervisanoSjediste.getIdRezervacije());

            int count = callableStatement.executeUpdate();
            if (count <= 0) {
                return false;
            }
        } catch (SQLException sql) {
            Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(RezervisanoSjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return false;
    }
}
