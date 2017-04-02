package DAO;

import Entities.ChangeLog;

import javax.naming.NamingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChangeLogDAO extends DAO {

    private final String ALL_CHANGE = "SELECT id_log, id_author, id_infosystem, Changes, Datetime FROM changelog";
    private final String ADD_CHANGE = "INSERT INTO changelog VALUES(id_log,?,?,?,now())";

    public List<ChangeLog> getAll() {
        List<ChangeLog> changeLogList = new ArrayList<ChangeLog>();
        try {
            connection = toConnection();
            PreparedStatement prst = connection.prepareStatement(ALL_CHANGE);
            ResultSet rs = prst.executeQuery();
            while (rs.next()) {
                ChangeLog changeLog = new ChangeLog();
                changeLog.setIdlog(rs.getInt("id_log"));
                changeLog.setIdauthor(rs.getInt("id_author"));
                changeLog.setIdsystem(rs.getInt("id_infosystem"));
                changeLog.setChanges(rs.getString("Changes"));
                changeLog.setDatetime(rs.getTimestamp("Datetime"));
                changeLogList.add(changeLog);
            }
        connection.close();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        } finally {
            return changeLogList;
        }
    }

    public void addChangeLog(ChangeLog changeLog) throws SQLException, NamingException {

            connection = toConnection();
            PreparedStatement prst = connection.prepareStatement(ADD_CHANGE);
            prst.setInt(1, changeLog.getIdauthor());
            prst.setInt(2, changeLog.getIdsystem());
            prst.setString(3, changeLog.getChanges());
            prst.executeUpdate();
            connection.close();

    }
}