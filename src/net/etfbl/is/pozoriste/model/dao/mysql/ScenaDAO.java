package net.etfbl.is.pozoriste.model.dao.mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.etfbl.is.pozoriste.model.dto.Scena;

/**
 *
 * @author Ognjen
 */
public class ScenaDAO {
    
    public static List<Scena> scene() {
        List<Scena> scene = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * " + "FROM  scene";
        
        try {
            connection = ConnectionPool.getInstance().checkOut();
            preparedStatement = connection.prepareStatement(query);
            
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
               Scena scena = new Scena(resultSet.getString("Naziv"),resultSet.getInt("BrojRedova"),resultSet.getInt("BrojKolona"));
               scena.setIdScene(new Integer(resultSet.getString("Identifikator")));
               scene.add(scena);
            }

        } catch (SQLException sql) {
            Logger.getLogger(ScenaDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(ScenaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(ScenaDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return scene;
    }


    public static boolean dodavanjeScene(String naziv,Integer brojRedova, Integer brojKolona) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeNoveScene(?,?,?)}");
            callableStatement.setString(1, naziv);
            callableStatement.setInt(2, brojRedova);
            callableStatement.setInt(3, brojKolona);
            
           return callableStatement.executeUpdate() > 0;
            
        } catch (SQLException sql) {
            Logger.getLogger(ScenaDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(ScenaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(ScenaDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return false;
    }
}
