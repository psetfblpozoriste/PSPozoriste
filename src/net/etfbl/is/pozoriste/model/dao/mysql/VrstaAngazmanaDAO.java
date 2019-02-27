
package net.etfbl.is.pozoriste.model.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.etfbl.is.pozoriste.model.dto.VrstaAngazmana;

public class VrstaAngazmanaDAO {
    public static List<VrstaAngazmana> vrsteAngazmana() {
        List<VrstaAngazmana> angazmani = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT * " + "FROM  pregledVrstaAngazmana";

        try {
            connection = ConnectionPool.getInstance().checkOut();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                VrstaAngazmana vrsta = new VrstaAngazmana(resultSet.getInt("id"), resultSet.getString("naziv"));
                angazmani.add(vrsta);
            }
        } catch (SQLException sql) {
            Logger.getLogger(VrstaAngazmanaDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(VrstaAngazmanaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(VrstaAngazmanaDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return angazmani;
    }
}
