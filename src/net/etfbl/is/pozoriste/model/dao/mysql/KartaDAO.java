/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dao.mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import net.etfbl.is.pozoriste.model.dto.Karta;
import net.etfbl.is.pozoriste.model.dto.Predstava;

/**
 *
 * @author Ognjen
 */
public class KartaDAO {

    public static List<Karta> karte() {
        List<Karta> karte = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT * " + "FROM  karta";

        try {
            connection = ConnectionPool.getInstance().checkOut();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Karta karta = new Karta(resultSet.getInt("id"), resultSet.getInt("brojReda"), resultSet.getInt("brojSjedista"), resultSet.getDate("termin"), resultSet.getInt("idScene"));
                karte.add(karta);
            }
        } catch (SQLException sql) {
            Logger.getLogger(KartaDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(KartaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(KartaDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return karte;
    }

    public static boolean dodajKartu(Karta karta) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeKarte(?,?,?,?,?)}");
            callableStatement.setInt(1, karta.getBrojReda());
            callableStatement.setInt(2, karta.getBrojSjedista());
            callableStatement.setFloat(3, karta.getIznos());
            callableStatement.setDate(4, karta.getTermin());
            callableStatement.setInt(5, karta.getIdScene());
            
            callableStatement.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(KartaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(KartaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(KartaDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }

}
