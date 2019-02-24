/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.model.dao.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
}
