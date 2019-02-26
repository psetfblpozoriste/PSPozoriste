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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import net.etfbl.is.pozoriste.controller.PregledPredstavaController;
import net.etfbl.is.pozoriste.controller.PregledRadnikaController;
import net.etfbl.is.pozoriste.model.dto.GostujucaPredstava;
import net.etfbl.is.pozoriste.model.dto.Umjetnik;

/**
 *
 * @author Tanja
 */
public class GostujucaPredstavaDAO {

    public static List<GostujucaPredstava> gostujucePredstave() {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        ArrayList<GostujucaPredstava> gostujucePredstave = new ArrayList<>();

        try {
            connection = ConnectionPool.getInstance().checkOut();
            statement = connection.createStatement();
            rs = statement.executeQuery("select * from gostujuca_predstava");
            while (rs.next()) {
                GostujucaPredstava predstava = new GostujucaPredstava(rs.getInt("id"), rs.getString("naziv"), rs.getString("opis"), rs.getString("tip"), rs.getString("pisac"), rs.getString("reziser"), rs.getString("glumci"));
                gostujucePredstave.add(predstava);
            }

        } catch (SQLException ex) {
            Logger.getLogger(GostujucaPredstavaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GostujucaPredstavaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GostujucaPredstavaDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return gostujucePredstave;
    }
    
    public static void dodajGostujucuPredstavu(GostujucaPredstava gostujucaPredstava){
        Connection connection = null;
        CallableStatement callableStatement = null;
        String poruka;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodajGostujucuPredstavu(?,?,?,?,?,?,?,?)}");
            callableStatement.setString(1, gostujucaPredstava.getNaziv());
            callableStatement.setString(2, gostujucaPredstava.getOpis());
            callableStatement.setString(3, gostujucaPredstava.getTip());
            callableStatement.setString(4, gostujucaPredstava.getPisac());
            callableStatement.setString(5, gostujucaPredstava.getReziser());
            callableStatement.setString(6, gostujucaPredstava.getGlumci());
            callableStatement.registerOutParameter(7,Types.VARCHAR);
            callableStatement.registerOutParameter(8,Types.INTEGER);
            callableStatement.executeQuery();
            
            poruka=callableStatement.getString(7);
            gostujucaPredstava.setId(callableStatement.getInt(8));
            if(poruka!=null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(null);
                alert.setHeaderText(null);
                alert.setContentText(poruka);
                alert.showAndWait();
            }
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
    
    
    public static void azurirajGostujucuPredstavu(GostujucaPredstava predstava){
        Connection connection = null;
        CallableStatement callableStatement = null;
        String poruka;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call azuriranjeGostujucePredstave(?,?,?,?,?,?,?)}");
            callableStatement.setInt(1, predstava.getId());
            callableStatement.setString(2, predstava.getNaziv());
            callableStatement.setString(3, predstava.getOpis());
            callableStatement.setString(4, predstava.getTip());
            callableStatement.setString(5, predstava.getPisac());
            callableStatement.setString(6, predstava.getReziser());
            callableStatement.setString(7, predstava.getGlumci());
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
