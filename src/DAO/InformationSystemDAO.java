package DAO;


import Entities.InformationSystem;
import Entities.StatusInfo;

import javax.naming.NamingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InformationSystemDAO extends DAO {

    private final String INSERT_SYSTEM = "INSERT INTO informationsystem VALUES (null,?,?,null,null,?)";
    private final String DELETE = "DELETE FROM informationsystem WHERE id_system = ?";
    private final String UPDATE = "UPDATE informationsystem SET id_supersystem = ?, name = ?, id_status = ? WHERE id_system=?";
    private final String SELECT_BY_ID = "SELECT id_supersystem, name, id_status, discription FROM informationsystem WHERE id_system = ?";
    private final String SELECT_BY_SUPERID = "SELECT id_supersystem, id_system, name, id_status, discription, path FROM informationsystem WHERE id_supersystem = ?";  //вернет лист подсистем
    private final String UPDATE_STATUS = "UPDATE informationsystem SET id_status = ? WHERE id_system=?";
    private final String ADD_PICTURE = "UPDATE informationsystem SET path=? WHERE id_system=?";
    private final String ALL_IS_FOR_ANON = "SELECT `id_system`, `id_supersystem`, `name`, `id_status`, `path`,`discription` FROM informationsystem WHERE id_supersystem=id_system";
    private final String ALL_IS = "SELECT `id_system`, `id_supersystem`, `name`, `id_status`, `path`,`discription` FROM informationsystem";
    private final String SELECT_IS_BY_ID = "SELECT `id_system`, `id_supersystem`, `name`, `id_status`, `path`,`discription` FROM informationsystem WHERE `id_system` = ?";
    private final String sqlSelectSuperSystems = "select id_system, id_supersystem, name, id_status from informationsystem where " +
            "id_system = id_supersystem";
    private final String sqlSelectSystemsForTable = "select `id_system`, `id_supersystem`,  `name`, `id_status` from informationsystem where " +
            "id_supersystem = ? and id_system<>id_supersystem";
    private final String sqlSelectSystems = "select `id_system`, `id_supersystem`,  `name`, `id_status`, `path`,`discription` from informationsystem where " +
            "id_supersystem = ?";
    private final String sqlSelectISByDeveloper = "select id_system, id_supersystem, name, id_status, path, discription from informationsystem " +
            "where id_system in (select id_supersystem from developer where Login = ?)";

    public InformationSystemDAO() {
    }

    public InformationSystem getSystemByDeveloper(String login) throws SQLException, NamingException {
        connection = toConnection();
        StatusInfoDAO statusInfoDAO = new StatusInfoDAO();
        InformationSystem informationSystem = new InformationSystem();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectISByDeveloper)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            informationSystem.setIdsystem(resultSet.getInt("id_system"));
            informationSystem.setIdSuperSystem(resultSet.getInt("id_supersystem"));
            informationSystem.setName(resultSet.getString("name"));
            informationSystem.setIdstatus(resultSet.getInt("id_status"));
            informationSystem.setPicture(resultSet.getString("path"));
            informationSystem.setDiscription(resultSet.getString("discription"));
            informationSystem.setStatusInfo(statusInfoDAO.getStatusBySystem(informationSystem));
        } finally {
            close();
        }
        return informationSystem;
    }

    public List<InformationSystem> selectSuperSystem() throws SQLException, NamingException {
        connection = toConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectSuperSystems);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<InformationSystem> informationSystems = new ArrayList<>();
        while (resultSet.next()) {
            InformationSystem informationSystem = new InformationSystem();
            informationSystem.setIdsystem(resultSet.getInt("id_system"));
            informationSystem.setIdSuperSystem(resultSet.getInt("id_supersystem"));
            informationSystem.setName(resultSet.getString("name"));
            informationSystem.setIdstatus(resultSet.getInt("id_status"));
            informationSystems.add(informationSystem);
        }
        close();
        return informationSystems;
    }

    public List<InformationSystem> selectSystem(Integer id) throws SQLException, NamingException {
        connection = toConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectSystems);
        preparedStatement.setObject(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<InformationSystem> systems = new ArrayList<>();
        StatusInfoDAO statusInfoDAO = new StatusInfoDAO();
        while (resultSet.next()) {
            InformationSystem system = new InformationSystem();
            system.setIdsystem(resultSet.getInt("id_system"));
            system.setIdSuperSystem(resultSet.getInt("id_supersystem"));
            system.setName(resultSet.getString("name"));
            system.setIdstatus(resultSet.getInt("id_status"));
            system.setPicture(resultSet.getString("path"));
            system.setDiscription(resultSet.getString("discription"));
            system.setStatusInfo(statusInfoDAO.getStatusBySystem(system));
            systems.add(system);
        }
        close();
        return systems;
    }
    public List<InformationSystem> selectSystemForTable(Integer id) throws SQLException, NamingException {
        connection = toConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectSystemsForTable);
        preparedStatement.setObject(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<InformationSystem> systems = new ArrayList<>();
        while (resultSet.next()) {
            InformationSystem system = new InformationSystem();
            system.setIdsystem(resultSet.getInt("id_system"));
            system.setIdSuperSystem(resultSet.getInt("id_supersystem"));
            system.setName(resultSet.getString("name"));
            system.setIdstatus(resultSet.getInt("id_status"));
            systems.add(system);
        }
        close();
        return systems;
    }

    public List<InformationSystem> getAll() throws SQLException, NamingException {
        List<InformationSystem> list = new ArrayList<InformationSystem>();
        try {
            connection = toConnection();
            PreparedStatement stm = connection.prepareStatement(ALL_IS);
            ResultSet rs = stm.executeQuery();
            StatusInfoDAO  statusInfoDAO = new StatusInfoDAO();
            while (rs.next()) {
                InformationSystem system = new InformationSystem();
                system.setIdsystem(rs.getInt("id_system"));
                system.setIdSuperSystem(rs.getInt("id_supersystem"));
                system.setName(rs.getString("name"));
                system.setIdstatus(rs.getInt("id_status"));
                system.setPicture(rs.getString("path"));
                system.setDiscription(rs.getString("discription"));
                system.setStatusInfo(statusInfoDAO.getStatusBySystem(system));
                list.add(system);
            }

        } finally {
            close();
        }
        return list;
    }

    public int insert(InformationSystem system) throws SQLException, NamingException {
        int key = 0;
        try {
            connection = toConnection();
            ResultSet rs = null;
            PreparedStatement prst = connection.prepareStatement(INSERT_SYSTEM, new String[]{"id_system"});
            prst.setInt(1, system.getIdSuperSystem());
            prst.setString(2, system.getName());
            prst.setString(3, system.getDiscription());
            prst.executeUpdate();
            rs = prst.getGeneratedKeys();
            if (rs.next()) {
                key = rs.getInt(1);
            }
        } finally {
            close();
        }
        return key;
    }

    public void delete(int idSystem) throws SQLException, NamingException {
        try {
            connection = toConnection();
            PreparedStatement prst = connection.prepareStatement(DELETE);
            prst.setInt(1, idSystem);
            prst.executeUpdate();
        } finally {
            close();
        }
    }

    public InformationSystem getById(InformationSystem system) {
        try {
            connection = toConnection();
            PreparedStatement prst = connection.prepareStatement(SELECT_BY_ID);
            prst.setInt(1, system.getIdsystem());
            ResultSet rs = prst.executeQuery();
            rs.next();
            system.setName(rs.getString("name"));
            system.setIdSuperSystem(rs.getInt("id_supersystem"));
            system.setIdstatus(rs.getInt("id_status"));
            system.setDiscription(rs.getString("discription"));
            close();
        }
        catch (SQLException | NamingException exception) {
            exception.printStackTrace();
        }
        return system;
    }

    //возвращает лист системы и ее подсистем
    public List<InformationSystem> getListSuperSystem(InformationSystem superSystem) throws SQLException, NamingException {
        List<InformationSystem> systemList = new ArrayList<>();
        try {
            connection = toConnection();
            PreparedStatement prst = connection.prepareStatement(SELECT_BY_SUPERID);
            prst.setInt(1, superSystem.getIdsystem());
            StatusInfoDAO statusInfoDAO = new StatusInfoDAO();
            ResultSet resultSet = prst.executeQuery();
            while (resultSet.next()) {
                InformationSystem system = new InformationSystem();
                system.setIdsystem(resultSet.getInt("id_system"));
                system.setIdSuperSystem(resultSet.getInt("id_supersystem"));
                system.setName(resultSet.getString("name"));
                system.setIdstatus(resultSet.getInt("id_status"));
                system.setPicture(resultSet.getString("path"));
                system.setDiscription(resultSet.getString("discription"));
                system.setStatusInfo(statusInfoDAO.getStatusBySystem(system));
                systemList.add(system);
            }
        } finally {
            close();
        }
        return systemList;
    }

    //на случай изменения айди, имени и статуса
    public void update(InformationSystem system) {
        try {
            connection = toConnection();
            PreparedStatement prst = connection.prepareStatement(UPDATE);
            prst.setInt(1, system.getIdSuperSystem());
            prst.setString(2, system.getName());
            prst.setInt(3, system.getIdstatus());
            prst.setInt(4, system.getIdsystem());
            prst.executeUpdate();
            connection.close();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }

    //изменение текущего статуса
    public void updateStatus(InformationSystem system, StatusInfo statusInfo) {

        try {
            connection = toConnection();
            PreparedStatement prst = connection.prepareStatement(UPDATE_STATUS);
            prst.setInt(1, statusInfo.getIdstatus());
            prst.setInt(2, system.getIdsystem());
            prst.executeUpdate();
            connection.close();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }

    public void addPicture(InformationSystem system) {
        try {
            connection = toConnection();
            PreparedStatement prst = connection.prepareStatement(ADD_PICTURE);
            prst.setString(1, system.getPicture());
            prst.setInt(2, system.getIdsystem());
            prst.executeUpdate();
            connection.close();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }

    public InformationSystem selectInformationSystemById(int idInformationSystem) throws SQLException, NamingException {
        connection = toConnection();
        InformationSystem informationSystem = new InformationSystem();
        StatusInfoDAO statusInfoDAO = new StatusInfoDAO();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_IS_BY_ID)) {
            statement.setInt(1, idInformationSystem);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                informationSystem.setIdsystem(resultSet.getInt("id_system"));
                informationSystem.setIdSuperSystem(resultSet.getInt("id_supersystem"));
                informationSystem.setName(resultSet.getString("name"));
                informationSystem.setIdstatus(resultSet.getInt("id_status"));
                informationSystem.setPicture(resultSet.getString("path"));
                informationSystem.setDiscription(resultSet.getString("discription"));
            }
            informationSystem.setStatusInfo(statusInfoDAO.getStatusBySystem(informationSystem));
        } finally {
            close();
        }
        return informationSystem;
    }

    public List<InformationSystem> selectAllInfSyst() throws SQLException, NamingException {
        List<InformationSystem> list = new ArrayList<>();
        StatusInfoDAO statusInfoDAO = new StatusInfoDAO();
        connection = toConnection();
        try (PreparedStatement stm = connection.prepareStatement(ALL_IS_FOR_ANON)) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                InformationSystem is = new InformationSystem();
                is.setIdsystem(rs.getInt("id_system"));
                is.setIdSuperSystem(rs.getInt("id_supersystem"));
                is.setName(rs.getString("name"));
                is.setIdstatus(rs.getInt("id_status"));
                is.setPicture(rs.getString("path"));
                is.setDiscription(rs.getString("discription"));
                is.setStatusInfo(statusInfoDAO.getStatusBySystem(is));
                list.add(is);
            }
        } finally {
            close();
        }
        return list;
    }
}
