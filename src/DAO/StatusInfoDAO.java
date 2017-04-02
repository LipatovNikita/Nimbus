package DAO;

import Entities.InformationSystem;
import Entities.StatusInfo;
import Entities.User;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatusInfoDAO extends DAO {
    private final String ALL_STATUS_BY_SYSTEM = "SELECT id_status, Status, Datetime, Published FROM statusinfo WHERE id_system = ?";
    private final String ALL_STATUS = "SELECT id_system, id_status, Status, Datetime, Published FROM statusinfo";
    private final String ALL_STATUS_FOR_DEV = "SELECT statusinfo.id_system, statusinfo.id_status, statusinfo.Status, statusinfo.Datetime, statusinfo.Published," +
            "informationsystem.id_supersystem, informationsystem.id_system FROM statusinfo, informationsystem " +
            "WHERE informationsystem.id_system = statusinfo.id_system and informationsystem.id_supersystem=?";
    private final String ADD_STATUS = "INSERT INTO statusinfo VALUES(id_status,?,now(),?,?)";
    private final String CURRENT_STATUS = "SELECT id_status, Status, Datetime, Published FROM statusinfo WHERE id_status = ?";
    private final String STATUS_EXIST = "SELECT DISTINCT Status FROM statusinfo;";
    private final String sqlInsert = "insert into statusinfo (id_status, Status, Datetime, Published, id_system) values (null, ?, ?, ?, ?)";
    private final String sqlSelectIds = "select id_system from informationsystem";
    private final String sqlSelectLastStatusById = "select Status from statusinfo where id_system = ? order by id_status desc limit 1";
    private final String sqlSelectLastSevenDaysById = "select id_status,Status, Datetime, Published from statusinfo where " +
            "(date(Datetime) > (now() - interval 7 day)) and id_system = ? and (Published <> 0)";
    private final String PUBLISH = "UPDATE statusinfo SET published = 1 WHERE id_status = ?";

    public void updatePublished(StatusInfo statusInfo) throws SQLException, NamingException {
        connection = toConnection();
        PreparedStatement prst = connection.prepareStatement(PUBLISH);
        prst.setInt(1, statusInfo.getIdstatus());
        prst.executeUpdate();
        connection.close();
    }

    //все статусы конкретной системы
    public List<StatusInfo> getAllStatusBySystem(InformationSystem system) {
        List<StatusInfo> statusInfoList = new ArrayList<StatusInfo>();
        try {
            connection = toConnection();
            PreparedStatement prst = connection.prepareStatement(ALL_STATUS_BY_SYSTEM);
            prst.setInt(1, system.getIdsystem());
            ResultSet rs = prst.executeQuery();
            while (rs.next()) {
                StatusInfo statusInfo = new StatusInfo();
                statusInfo.setIdstatus(rs.getInt("id_status"));
                statusInfo.setStatus(rs.getString("Status"));
                statusInfo.setPublished(rs.getBoolean("published"));
                statusInfo.setDate(rs.getTimestamp("Datetime"));
                statusInfoList.add(statusInfo);
            }
            connection.close();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        } finally {
            return statusInfoList;
        }
    }

    public List<StatusInfo> getAllStatusBySystemWeek(InformationSystem system) {
        List<StatusInfo> statusInfoList = new ArrayList<StatusInfo>();
        try {
            connection = toConnection();
            PreparedStatement prst = connection.prepareStatement(sqlSelectLastSevenDaysById);
            prst.setInt(1, system.getIdsystem());
            ResultSet rs = prst.executeQuery();
            while (rs.next()) {
                StatusInfo statusInfo = new StatusInfo();
                statusInfo.setIdstatus(rs.getInt("id_status"));
                statusInfo.setStatus(rs.getString("Status"));
                statusInfo.setPublished(rs.getBoolean("published"));
                statusInfo.setDate(rs.getTimestamp("Datetime"));
                statusInfoList.add(statusInfo);
            }
            connection.close();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        } finally {
            return statusInfoList;
        }
    }

    // все статусы всех систем
    public List<StatusInfo> getAllStatus() throws SQLException, NamingException {
        List<StatusInfo> statusInfoList = new ArrayList<StatusInfo>();
        try {
            connection = toConnection();
            PreparedStatement prst = connection.prepareStatement(ALL_STATUS);
            ResultSet rs = prst.executeQuery();
            while (rs.next()) {
                StatusInfo statusInfo = new StatusInfo();
                statusInfo.setIdsystem(rs.getInt("id_system"));
                statusInfo.setIdstatus(rs.getInt("id_status"));
                statusInfo.setStatus(rs.getString("Status"));
                statusInfo.setPublished(rs.getBoolean("published"));
                statusInfo.setDate(rs.getTimestamp("Datetime"));
                statusInfoList.add(statusInfo);
            }
            connection.close();
        } finally {
            return statusInfoList;
        }
    }

    // все статусы всех систем
    public List<StatusInfo> getAllStatusByDeveloper(User user) throws SQLException, NamingException {
        List<StatusInfo> statusInfoList = new ArrayList<StatusInfo>();

        connection = toConnection();
        PreparedStatement prst = connection.prepareStatement(ALL_STATUS_FOR_DEV);
        prst.setInt(1, user.getId_supersystem());
        ResultSet rs = prst.executeQuery();
        while (rs.next()) {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setIdsystem(rs.getInt("id_system"));
            statusInfo.setIdstatus(rs.getInt("id_status"));
            statusInfo.setStatus(rs.getString("Status"));
            statusInfo.setPublished(rs.getBoolean("published"));
            statusInfo.setDate(rs.getTimestamp("Datetime"));
            statusInfoList.add(statusInfo);
        }
        connection.close();
        return statusInfoList;
    }

    //добавить новый статус, возвращает сгенерированный id_status

    public int insert(StatusInfo statusInfo) {
        int key = 0;
        try {
            connection = toConnection();
            ResultSet rs = null;
            PreparedStatement prst = connection.prepareStatement(ADD_STATUS, new String[]{"id_status"});
            prst.setString(1, statusInfo.getStatus());

            prst.setBoolean(2, statusInfo.getPublished());

            prst.setInt(3, statusInfo.getIdsystem());
            prst.executeUpdate();
            rs = prst.getGeneratedKeys();
            if (rs.next()) {
                key = rs.getInt(1);
            }
            connection.close();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        } finally {
            return key;
        }
    }

    public StatusInfo getStatusBySystem(InformationSystem system) throws SQLException {
        StatusInfo statusInfo = new StatusInfo();
        try {
            connection = toConnection();
            PreparedStatement prst = connection.prepareStatement(CURRENT_STATUS);
            if (system.getIdstatus() != 0) {
                prst.setInt(1, system.getIdstatus());
                ResultSet rs = prst.executeQuery();
                rs.next();
                statusInfo.setIdstatus(rs.getInt("id_status"));
                statusInfo.setStatus(rs.getString("Status"));
                statusInfo.setPublished(rs.getBoolean("published"));
                statusInfo.setDate(rs.getTimestamp("Datetime"));
            }

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return statusInfo;
    }


    public List<String> getExistsStatus() {
        List<String> statusInfoList = new ArrayList<String>();
        try {
            connection = toConnection();
            PreparedStatement prst = connection.prepareStatement(STATUS_EXIST);
            ResultSet rs = prst.executeQuery();
            while (rs.next()) {
                statusInfoList.add(rs.getString("status"));
            }
            connection.close();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        } finally {
            return statusInfoList;
        }
    }

    public List<StatusInfo> selectLastDaysById(Integer id_system) throws SQLException, NamingException {
        connection = toConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectLastSevenDaysById);
        preparedStatement.setInt(1, id_system);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<StatusInfo> statusInfos = new ArrayList<>();
        while (resultSet.next()) {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setStatus(resultSet.getString("Status"));
            statusInfo.setDate(resultSet.getTimestamp("Datetime"));
            statusInfo.setPublished(resultSet.getBoolean("Published"));
            statusInfos.add(statusInfo);
        }
        connection.close();
        return statusInfos;
    }

    public List<Integer> selectIds() throws SQLException, NamingException {
        connection = toConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectIds);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Integer> ids = new ArrayList<>();
        while (resultSet.next()) {
            Integer id;
            id = resultSet.getInt("id_system");
            ids.add(id);
        }
        connection.close();
        return ids;
    }

    public String sqlSelectLastRowById(int id_system) throws SQLException, NamingException {
        connection = toConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectLastStatusById);
        preparedStatement.setInt(1, id_system);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String status = resultSet.getString("Status");
        connection.close();
        return status;
    }

    public String insertStatus(String status, Timestamp datetime, int published, int id_system) throws SQLException, NamingException {
        connection = toConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
            preparedStatement.setString(1, status);
            preparedStatement.setTimestamp(2, datetime);
            preparedStatement.setInt(3, published);
            preparedStatement.setInt(4, id_system);
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                return "Добавление успешно";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return "Добавление не успешно";
    }
}
