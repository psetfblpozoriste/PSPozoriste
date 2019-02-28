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
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import net.etfbl.is.pozoriste.model.dto.Angazman;
import net.etfbl.is.pozoriste.model.dto.Predstava;

/**
 *
 * @author Mina
 */
public class AngazmanDAO {
    
    public static List<Angazman> angazmani(Predstava predstava){
        List<Angazman> angazmani = new ArrayList<>();
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call pregledAngazmana(?)}");
            callableStatement.setInt(1, predstava.getId());
            resultSet=callableStatement.executeQuery();
            
            while (resultSet.next()) {
                Angazman angazman = new Angazman(resultSet.getString("ime"), resultSet.getString("prezime"), resultSet.getString("naziv"),resultSet.getDate("datumOd"),resultSet.getDate("datumDo"));
                angazmani.add(angazman);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AngazmanDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AngazmanDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return angazmani;
    }
    
    public static void dodajAngazman(Integer idPredstave,Integer idUmjetnika,Integer idVrstaAngazmana,Date datumOd){
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeAngazmana(?,?,?,?)}");
            callableStatement.setInt(1, idPredstave);
            callableStatement.setInt(2, idUmjetnika);
            callableStatement.setInt(3, idVrstaAngazmana);
            callableStatement.setDate(4, datumOd);
            callableStatement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(AngazmanDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AngazmanDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public static void azurirajAngazman(Integer idPredstave,Integer idUmjetnika,Integer idVrstaAngazmana,Date datumOd,Date datumDo){
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call azuriranjeAngazmana(?,?,?,?,?)}");
            callableStatement.setInt(1, idVrstaAngazmana);
            callableStatement.setInt(2, idPredstave);
            callableStatement.setInt(3, idUmjetnika);
            callableStatement.setDate(4, datumOd);
            callableStatement.setDate(5, datumDo);
            callableStatement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(AngazmanDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AngazmanDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
