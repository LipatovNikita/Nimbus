package DAO;

import Entities.Component;

import javax.naming.NamingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComponentDAO extends DAO {
    private final String INSERT_COMPONENT = "INSERT INTO component (`id_component`, `Name`, `Status`, `Version`, `id_infosystem`, `id_virtualServer`, `Load`, `Port`) VALUES (NULL,?,?,?,?,?,?,?)";
    private final String UPDATE_COMPONENT = "UPDATE component SET `Name` = ?, `Status` = ?, `Version` = ?, `id_infosystem` = ?, `id_virtualServer` = ?, `Load` = ?, `Port` = ? WHERE `id_component` = ?";
    private final String UPDATE_STATUS_COMPONENT = "UPDATE component SET `Status` = ? WHERE `id_component` = ?";
    private final String SELECT_COMPONENT = "SELECT * FROM component ";
    private final String SELECT_LOAD_COMPONENT = "SELECT `Load` FROM component WHERE `id_virtualServer` = ?";
    private final String SELECT_COMPONENT_FOR_VIRTUAL_SERVER = "SELECT `id_component`, `Name`, `Status`, `Version`, " +
            "`id_infosystem`, `id_virtualServer`, `Load`, `Port` FROM component WHERE `id_virtualServer` = ?";

    private final String SELECT_COMPONENT_FOR_INFO_SYSTEM = "SELECT `id_component`, `Name`, `Status`, `Version`, " +
            "`id_infosystem`, `id_virtualServer`, `Load`, `Port` FROM component WHERE `id_infosystem` = ?";

    private final String DELETE_COMPONENT = "DELETE FROM component WHERE id_component = ?";


    public ComponentDAO() {
        super();
    }

    public List<Component> selectComponents() throws SQLException, NamingException {
        List<Component> components = new ArrayList<>();
        connection = toConnection();

        try(PreparedStatement statement = connection.prepareStatement(SELECT_COMPONENT)){
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Component comp = new Component(
                        resultSet.getInt("id_component"),
                        resultSet.getString("Name"),
                        resultSet.getString("Status"),
                        resultSet.getString("Version"),
                        resultSet.getInt("id_infosystem"),
                        resultSet.getInt("id_virtualServer"),
                        resultSet.getInt("Load"),
                        resultSet.getInt("Port")
                );
                components.add(comp);
            }
        }finally {
            close();
        }
        return components;
    }

    public Integer selectLoadComponents(int idVirtualServer) throws SQLException, NamingException {
        int summLoad = 0;
        connection = toConnection();

        try(PreparedStatement statement = connection.prepareStatement(SELECT_LOAD_COMPONENT)){
            statement.setInt(1, idVirtualServer);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                summLoad += resultSet.getInt("Load");
            }
        }finally {
            close();
        }
        return summLoad;
    }


    private List<Component> selectComponentsForSomeone(int idSomeone, String SqlQuery) throws SQLException, NamingException {
        List<Component> components = new ArrayList<>();
        connection = toConnection();

        try(PreparedStatement statement = connection.prepareStatement(SqlQuery)){
            statement.setInt(1, idSomeone);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Component comp = new Component(
                        resultSet.getInt("id_component"),
                        resultSet.getString("Name"),
                        resultSet.getString("Status"),
                        resultSet.getString("Version"),
                        resultSet.getInt("id_infosystem"),
                        resultSet.getInt("id_virtualServer"),
                        resultSet.getInt("Load"),
                        resultSet.getInt("Port")
                );
                components.add(comp);
            }
        }finally {
            close();
        }
        return components;
    }

    public List<Component> selectComponentsForVirServ(int idVirtualServer) throws SQLException, NamingException {
        return selectComponentsForSomeone(idVirtualServer, SELECT_COMPONENT_FOR_VIRTUAL_SERVER);
    }

    public List<Component> selectComponentsForInfoSystem(int idInfoSystem) throws SQLException, NamingException {
        return selectComponentsForSomeone(idInfoSystem, SELECT_COMPONENT_FOR_INFO_SYSTEM);
    }


    public String insertComponent(Component component) throws SQLException, NamingException {
        connection = toConnection();

        try(PreparedStatement statement = connection.prepareStatement(INSERT_COMPONENT) ){
            statement.setString(1, component.getName());
            statement.setString(2, component.getStatus());
            statement.setString(3, component.getVersion());
            statement.setInt(4, component.getIdInformSystem());
            statement.setInt(5, component.getIdVirtualServer());
            statement.setInt(6, component.getLoad());
            statement.setInt(7, component.getPort());

            int resultInsert = statement.executeUpdate();
            if(resultInsert > 0){
                return "Добавление компонента прошло успешно";
            }

        }finally {
            close();
        }
        return "Компонент не добавлен";
    }


    public String deleteComponent(int idComponent) throws SQLException, NamingException {
        connection = toConnection();

        try(PreparedStatement statement = connection.prepareStatement(DELETE_COMPONENT) ){
            statement.setInt(1, idComponent);

            int resultInsert = statement.executeUpdate();
            if(resultInsert > 0){
                return "Удаление компонента прошло успешно";
            }
        }finally {
            close();
        }
        return "Компонент не удален";
    }


    public String updateComponent(Component component) throws SQLException, NamingException {
        connection = toConnection();

        try(PreparedStatement statement = connection.prepareStatement(UPDATE_COMPONENT) ) {
            statement.setString(1, component.getName());
            statement.setString(2, component.getStatus());
            statement.setString(3, component.getVersion());
            statement.setInt(4, component.getIdInformSystem());
            statement.setInt(5, component.getIdVirtualServer());
            statement.setInt(6, component.getLoad());
            statement.setInt(7, component.getPort());

            statement.setInt(8, component.getId());
            int resultInsert = statement.executeUpdate();
            if (resultInsert > 0) {
                return "Изменение компонента прошло успешно";
            }
        }finally {
            close();
        }
        return "Компонент не изменен";
    }

    public void updateStatusComponent(int idComponent, String status) throws SQLException, NamingException {
        connection = toConnection();

        try(PreparedStatement statement = connection.prepareStatement(UPDATE_STATUS_COMPONENT)){
            statement.setString(1,status);
            statement.setInt(2, idComponent);

            int resultInsert = statement.executeUpdate();
            if (resultInsert > 0) {
                System.out.println("Изменение компонента прошло успешно");
            }
        } finally {
            close();
        }
    }


}
