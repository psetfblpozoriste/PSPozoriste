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
import net.etfbl.is.pozoriste.model.dto.Rezervacija;

/**
 *
 * @author Ognjen
 */
public class RezervacijaDAO {
    
    
    
    public static List<Rezervacija> rezervacije(Date termin,Integer idScene) {
        List<Rezervacija> rezervacije = new ArrayList<>();
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call pregledRezervacija(?,?)}");
            callableStatement.setDate(1, termin);
            callableStatement.setInt(2, idScene);
            
            resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
               Rezervacija rezervacijs = new Rezervacija(
                    resultSet.getInt("id"),resultSet.getString("ime"),resultSet.getDate("termin"),resultSet.getInt("idScene")); 
               rezervacije.add(rezervacijs);
            }

        } catch (SQLException sql) {
            Logger.getLogger(RezervacijaDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(RezervacijaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(RezervacijaDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return rezervacije;
    }
    
    
     public static Rezervacija addRezervacija(Rezervacija rezervacija) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        Rezervacija rezervacijaDodata = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeRezervacije(?,?,?,?,?)}");
            callableStatement.setInt(1, rezervacija.getId());
            callableStatement.setString(2, rezervacija.getIme());
            callableStatement.setDate(3, rezervacija.getTermin());
            callableStatement.setInt(4, rezervacija.getIdScene());
            callableStatement.registerOutParameter(5, Types.INTEGER);

            if (callableStatement.executeUpdate() == 0) {
                rezervacija.setId(callableStatement.getInt(5));
                rezervacijaDodata = rezervacija;
                return rezervacijaDodata;
            }
        } catch (SQLException sql) {
            Logger.getLogger(RezervacijaDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(RezervacijaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(RezervacijaDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return rezervacijaDodata;
    }
}
