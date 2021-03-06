/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dao.mysql;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import net.etfbl.is.pozoriste.controller.PregledRadnikaController;
import net.etfbl.is.pozoriste.model.dto.Angazman;
import net.etfbl.is.pozoriste.model.dto.Biletar;
import net.etfbl.is.pozoriste.model.dto.Radnik;
import net.etfbl.is.pozoriste.model.dto.Umjetnik;

/**
 *
 * @author djord
 */
public class UmjetnikDAO {

    public static void dodajUmjetnika(Umjetnik umjetnik) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeUmjetnika(?,?,?,?,?,?)}");
            callableStatement.setString(1, umjetnik.getIme());
            callableStatement.setString(2, umjetnik.getPrezime());
            callableStatement.setString(3, umjetnik.getJmb());
            callableStatement.setString(4, umjetnik.getKontakt());
            callableStatement.setString(5, umjetnik.getBiografija());
            callableStatement.registerOutParameter(6, Types.INTEGER);

            callableStatement.executeQuery();

            umjetnik.setIdRadnika(callableStatement.getInt(6));
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

        System.out.println("BLA BLA BLA BLA");
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        Umjetnik umjetnik;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            statement = connection.createStatement();
            rs = statement.executeQuery("select * from umjetnici_info");
            while (rs.next()) {

                umjetnik = new Umjetnik(rs.getString("Ime"), rs.getString("Prezime")/*, rs.getString("OpisPosla")*/, rs.getString("JMB"), rs.getBoolean("StatusRadnika"), rs.getString("Kontakt"), rs.getString("Biografija"));
                umjetnik.setIdRadnika(rs.getInt("Id"));
                if (!PregledRadnikaController.radniciObservableList.contains(umjetnik)) {
                    PregledRadnikaController.radniciObservableList.add(umjetnik);
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

    public static void izmjeniUmjetnika(Umjetnik umjetnik) {

        System.out.println("IZMJENA UMJETNIKA");
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call azuriranjeUmjetnika(?,?,?,?,?,?)}");

            callableStatement.setString(1, umjetnik.getIme());
            callableStatement.setString(2, umjetnik.getPrezime());
            callableStatement.setString(3, umjetnik.getJmb());
            callableStatement.setInt(4, umjetnik.getIdRadnika());
            callableStatement.setBoolean(5, umjetnik.isStatusRadnika());
            callableStatement.setString(6, umjetnik.getBiografija());

            callableStatement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(UmjetnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UmjetnikDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static List<Umjetnik> umjetnici() {
        List<Umjetnik> umjetnici = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT * " + "FROM  vratiUmjetnike";

        try {
            connection = ConnectionPool.getInstance().checkOut();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Umjetnik umjetnik = new Umjetnik(resultSet.getString("ime"), resultSet.getString("prezime"), resultSet.getString("jmb"), resultSet.getBoolean("statusRadnika"), resultSet.getString("kontakt"), resultSet.getString("biografija"));
                umjetnik.setIdRadnika(resultSet.getInt("idRadnik"));
                umjetnici.add(umjetnik);
            }
        } catch (SQLException sql) {
            Logger.getLogger(PredstavaDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(PredstavaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(PredstavaDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return umjetnici;
    }
}
