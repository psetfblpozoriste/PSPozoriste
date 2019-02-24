package net.etfbl.is.pozoriste.model.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.etfbl.is.pozoriste.model.dto.Predstava;

public class PredstavaDAO {

    public static List<Predstava> predstave() {
        List<Predstava> predstave = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT * " + "FROM  predstava";

        try {
            connection = ConnectionPool.getInstance().checkOut();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Predstava predstava = new Predstava(resultSet.getString("naziv"), resultSet.getString("opis"), resultSet.getString("tip"));
                predstava.setId(resultSet.getInt("id"));
                predstave.add(predstava);
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
        return predstave;
    }

}
