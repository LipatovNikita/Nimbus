package DAO;

import Entities.VirtualServer;

import javax.naming.NamingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VirtualServerDAO extends DAO {
    private final String INSERT_VIRTUAL_SERVER = "INSERT INTO virtualserver (id_virtualserver, id_physicalServer, Name, IP, Mask, Port) VALUES (NULL,?,?,?,?,?)";
    private final String UPDATE_VIRTUAL_SERVER = "UPDATE virtualserver SET id_physicalServer = ?, Name = ?, IP = ?, Mask = ?, Port = ? WHERE id_virtualserver = ?";
    private final String SELECT_VIRTUAL_SERVERS = "SELECT id_virtualserver, id_physicalServer, Name, IP, Mask, Port FROM virtualserver";
    private final String SELECT_VIRTUAL_SERVERS_FOR_PHYS_SERV = "SELECT id_virtualserver, id_physicalServer, Name, IP, Mask, Port FROM virtualserver WHERE id_physicalServer = ?";
    private final String SELECT_VIRTUAL_SERVERS_BY_ID = "SELECT id_virtualserver, id_physicalServer, Name, IP, Mask, Port FROM virtualserver WHERE id_virtualserver = ?";
    private final String DELETE_VIRTUAL_SERVERS = "DELETE FROM virtualserver WHERE id_virtualserver = ?";


    public VirtualServerDAO() {
        super();
    }

    public List<VirtualServer> selectVirtualServers() throws SQLException, NamingException {
        List<VirtualServer> virtualServers = new ArrayList<>();
        ComponentDAO componentDAO = new ComponentDAO();
        connection = toConnection();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_VIRTUAL_SERVERS);

            while (resultSet.next()) {
                VirtualServer virtServer = new VirtualServer(
                        resultSet.getInt("id_virtualserver"),
                        resultSet.getInt("id_physicalServer"),
                        resultSet.getString("Name"),
                        resultSet.getString("IP"),
                        resultSet.getString("Mask"),
                        resultSet.getInt("Port")
                );
                virtServer.setComponents(componentDAO.selectComponentsForVirServ(virtServer.getId()));
                virtualServers.add(virtServer);
            }

        } finally {
            close();
        }
        return virtualServers;
    }


    public List<VirtualServer> selectVirtualServersForPhysServer(int idPhysServer) throws SQLException, NamingException {
        List<VirtualServer> virtualServers = new ArrayList<>();
        connection = toConnection();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_VIRTUAL_SERVERS_FOR_PHYS_SERV)) {
            statement.setInt(1, idPhysServer);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                VirtualServer virtServer = new VirtualServer(
                        resultSet.getInt("id_virtualserver"),
                        resultSet.getInt("id_physicalServer"),
                        resultSet.getString("Name"),
                        resultSet.getString("IP"),
                        resultSet.getString("Mask"),
                        resultSet.getInt("Port")
                );
                virtualServers.add(virtServer);
            }

        } finally {
            close();
        }
        return virtualServers;
    }


    public VirtualServer selectVirtualServerById(int idVirtualServer) throws NullPointerException, SQLException, NamingException {
        connection = toConnection();
        VirtualServer virtServer = null;

        try (PreparedStatement statement = connection.prepareStatement(SELECT_VIRTUAL_SERVERS_BY_ID)) {
            statement.setInt(1, idVirtualServer);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                virtServer = new VirtualServer(
                        resultSet.getInt("id_virtualserver"),
                        resultSet.getInt("id_physicalServer"),
                        resultSet.getString("Name"),
                        resultSet.getString("IP"),
                        resultSet.getString("Mask"),
                        resultSet.getInt("Port")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return virtServer;
    }


    public String insertVirtualServer(VirtualServer virtualServer) throws SQLException, NamingException {
        connection = toConnection();

        try (PreparedStatement statement = connection.prepareStatement(INSERT_VIRTUAL_SERVER)) {
            statement.setInt(1, virtualServer.getIdPhysicalServer());
            statement.setString(2, virtualServer.getNameVM());
            statement.setString(3, virtualServer.getIP());
            statement.setString(4, virtualServer.getMask());
            statement.setInt(5, virtualServer.getPort());

            int resultInsert = statement.executeUpdate();
            if (resultInsert > 0) {
                return "Добавление виртуального сервера прошло успешно";
            }

        } finally {
            close();
        }
        return "Виртуальный сервер не добавлен";
    }


    public String deleteVirtualServer(int idVirtualServer) throws SQLException, NamingException {
        connection = toConnection();

        try (PreparedStatement statement = connection.prepareStatement(DELETE_VIRTUAL_SERVERS)) {
            statement.setInt(1, idVirtualServer);

            int resultInsert = statement.executeUpdate();
            if (resultInsert > 0) {
                return "Удаление виртуального сервера прошло успешно";
            }

        } finally {
            close();
        }
        return "Виртуальный сервер не удален";
    }


    public String updateVirtualServer(VirtualServer virtualServer) throws SQLException, NamingException {
        connection = toConnection();

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_VIRTUAL_SERVER)) {
            statement.setInt(1, virtualServer.getIdPhysicalServer());
            statement.setString(2, virtualServer.getNameVM());
            statement.setString(3, virtualServer.getIP());
            statement.setString(4, virtualServer.getMask());
            statement.setInt(5, virtualServer.getPort());

            statement.setInt(6, virtualServer.getId());

            int resultInsert = statement.executeUpdate();
            if (resultInsert > 0) {
                return "Изменение виртуального сервера прошло успешно";
            }

        } finally {
            close();
        }
        return "Виртуальный сервер не изменен";
    }

}
