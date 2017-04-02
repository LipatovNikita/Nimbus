package DAO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class DAO {
    DataSource ds;
    Connection connection;

    public DAO() {
    }

    public Connection toConnection() throws SQLException, NamingException {
        InitialContext ctx;
        ctx = new InitialContext();
        ds = (DataSource) ctx.lookup("java:comp/env/jdbc/nimbus");
        connection = ds.getConnection();
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null)
            connection.close();
    }
}

