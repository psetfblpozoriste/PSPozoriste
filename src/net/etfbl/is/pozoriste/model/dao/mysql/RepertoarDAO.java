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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.etfbl.is.pozoriste.model.dto.Repertoar;

/**
 *
 * @author Ognjen
 */
public class RepertoarDAO {

    public static void dodajRepertoar(Repertoar repertoar) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call dodavanjeRepertoara(?,?)}");
            callableStatement.setDate(1, repertoar.getMjesecIGodina());
            callableStatement.registerOutParameter(2, Types.INTEGER);

            callableStatement.executeQuery();
            repertoar.setId(callableStatement.getInt(2));
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

    public static Repertoar getRepertoar(final int id) {
        return repertoars().stream().filter(e -> e.getId() == id).findFirst().get();
    }

    public static List<Repertoar> repertoars() {
        List<Repertoar> repertoars = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT * " + "FROM  repertoari_info";

        try {
            connection = ConnectionPool.getInstance().checkOut();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Repertoar repertoar = new Repertoar(resultSet.getInt("id"), resultSet.getDate("mjesecIGodina"));
                repertoar.setIgranja(IgranjeDAO.getIgranja(repertoar.getId()));
                repertoars.add(repertoar);
            }

        } catch (SQLException sql) {
            Logger.getLogger(RepertoarDAO.class.getName()).log(Level.SEVERE, null, sql);
        } catch (Exception e) {
            Logger.getLogger(RepertoarDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException sql) {
                    Logger.getLogger(RepertoarDAO.class.getName()).log(Level.SEVERE, null, sql);
                }
            }
        }
        return repertoars;
    }

    public static void izmjeniRepertoar(Repertoar repertoar) {
        System.out.println("IZMJENA Repertoara : : : " + repertoar);

        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call azuriranjeRepertoara(?,?)}");

            callableStatement.setInt(1, repertoar.getId());
            callableStatement.setDate(2, repertoar.getMjesecIGodina());

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

}
