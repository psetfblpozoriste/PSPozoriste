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
import net.etfbl.is.pozoriste.model.dto.Sjediste;

/**
 *
 * @author Ognjen
 */
public class SjedisteDAO {

    public static List<Sjediste> sjedista(Integer idScene) {
        List<Sjediste> sjedista = new ArrayList<>();
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;

        try {
           connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call pregledSjedistaZaScenu(?)}");
            callableStatement.setInt(1, idScene);

            resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                Sjediste sjediste = new Sjediste(resultSet.getInt("brojSjedista"));
                sjediste.setIdentifikatorSale(new Integer(resultSet.getString("idScene")));
                sjedista.add(sjediste);
            }

        } catch (SQLException sql) {
            Logger.getLogger(SjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(SjedisteDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(SjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return sjedista;
    }

    public static boolean dodavanjeSjedista(Integer idScene,Integer brojSjedista) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeSjedista(?,?)}");
            callableStatement.setInt(1, idScene);
            callableStatement.setInt(2, brojSjedista);

            return callableStatement.executeUpdate() > 0;

        } catch (SQLException sql) {
            Logger.getLogger(SjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(SjedisteDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(SjedisteDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return false;
    }
}
