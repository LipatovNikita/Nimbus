package DAO;


import Entities.PhysicalServer;

import javax.naming.NamingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhysicalServerDAO extends DAO {
    private final String INSERT_PHYSICAL_SERVER = "INSERT INTO physicalserver (`id_physicalserver`, `HHD`, `RAM`, `Status`, `Processor`, `IP`, `MAC`) VALUES (NULL,?,?,?,?,?,?)";
    private final String UPDATE_PHYSICAL_SERVER = "UPDATE physicalserver SET `HHD` = ?, `RAM` = ?, `Status` = ?, `Processor` = ?, `IP` = ?, `MAC` = ? WHERE id_physicalserver = ?";
    private final String SELECT_PHYSICAL_SERVERS = "SELECT `id_physicalserver`, `HHD`, `RAM`, `Status`, `Processor`, `IP`, `MAC` FROM physicalserver";
    private final String SELECT_PHYSICAL_SERVERS_BY_ID = "SELECT `id_physicalserver`, `HHD`, `RAM`, `Status`, `Processor`, `IP`, `MAC` FROM physicalserver WHERE `id_physicalserver` = ?";
    private final String DELETE_PHYSICAL_SERVERS = "DELETE FROM physicalserver WHERE id_physicalserver = ?";


    public PhysicalServerDAO() {
        super();
    }

    public List<PhysicalServer> selectPhysicalServers() throws SQLException, NamingException {
        VirtualServerDAO virtServDAO = new VirtualServerDAO();
        List<PhysicalServer> physicalServers = new ArrayList<>();
        connection = toConnection();

        try(PreparedStatement statement = connection.prepareStatement(SELECT_PHYSICAL_SERVERS)){
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                PhysicalServer physicalServer = new PhysicalServer(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7)
                );
                physicalServer.setVirtualServers(virtServDAO.selectVirtualServersForPhysServer(physicalServer.getId()));

                physicalServers.add(physicalServer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close();
        }
        return physicalServers;
    }

    //Trows NullPointerEx
    //Выбрать физические сервера
    public PhysicalServer selectPhysicalServerById(int idPhysicalServer) throws NullPointerException, SQLException, NamingException {
        connection = toConnection();
        PhysicalServer physicalServer = null;

        try(PreparedStatement statement = connection.prepareStatement(SELECT_PHYSICAL_SERVERS_BY_ID)){
            statement.setInt(1,idPhysicalServer);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                physicalServer = new PhysicalServer(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7)
                );

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close();
        }
        return physicalServer;
    }

    //Добавить  физический сервер
    public String insertPhysicalServer(PhysicalServer physicalServer) throws SQLException, NamingException {
        connection = toConnection();

        try(PreparedStatement statement = connection.prepareStatement(INSERT_PHYSICAL_SERVER) ){
            statement.setInt(1, physicalServer.getHDD());
            statement.setInt(2, physicalServer.getRAM());
            statement.setString(3, physicalServer.getStatus());
            statement.setString(4, physicalServer.getProcessorName());
            statement.setString(5, physicalServer.getIP());
            statement.setString(6, physicalServer.getMAC());

            int resultInsert = statement.executeUpdate();
            if(resultInsert > 0){
                return "Добавление физического сервера прошло успешно";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close();
        }
        return "Физический сервер не добавлен";
    }

    //Удалить физический сервер
    public String deletePhysicalServer(int idPhysicalServer) throws SQLException, NamingException {
        connection = toConnection();

        try(PreparedStatement statement = connection.prepareStatement(DELETE_PHYSICAL_SERVERS) ){
            statement.setInt(1, idPhysicalServer);

            int resultInsert = statement.executeUpdate();
            if(resultInsert > 0){
                return "Удаление физического сервера прошло успешно";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close();
        }
        return "Физический сервер не удален";
    }


    public String updatePhysicalServer(PhysicalServer physicalServer) throws SQLException, NamingException {
        connection = toConnection();

        try(PreparedStatement statement = connection.prepareStatement(UPDATE_PHYSICAL_SERVER) ){
            statement.setInt(1, physicalServer.getHDD());
            statement.setInt(2, physicalServer.getRAM());
            statement.setString(3, physicalServer.getStatus());
            statement.setString(4, physicalServer.getProcessorName());
            statement.setString(5, physicalServer.getIP());
            statement.setString(6, physicalServer.getMAC());
            statement.setInt(7, physicalServer.getId());

            int resultInsert = statement.executeUpdate();
            if(resultInsert > 0){
                return "Изменение физического сервера прошло успешно";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close();
        }
        return "Физический сервер не изменен";
    }


}
