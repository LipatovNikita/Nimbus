package DAO;

import Entities.User;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersDAO extends DAO {
    Connection connection;

    final String sqlDelete = "delete developer, users from developer left join users on developer.Login = users.Login" +
            " where developer.Login = ?";
    final String sqlSelect = "select developer.id_developer, developer.FirstName, developer.LastName, developer.MiddleName," +
            "developer.OfficialPosition, developer.id_supersystem, developer.Login, users.Login, users.Password" +
            " from developer, users where users.Login = developer.Login";
    final String sqlInsertOne = "insert into users (Login, Password) values (?,?)";
    final String sqlSelectLogins = "select Login from users";
    final String SqlInsertTwo = "insert into developer (id_developer, FirstName, LastName, MiddleName, OfficialPosition, " +
            "id_supersystem, Login) values (null, ?, ?, ?, ?, ?, ?)";
    final String SqlSelectSystems = "select id_supersystem, Name from informationsystem where id_supersystem = id_system";
    private final String GET_BY_LOGIN = "SELECT id_developer, FirstName, LastName, MiddleName, OfficialPosition, id_supersystem FROM developer WHERE login=?";
    private final String GET_BY_ID = "SELECT FirstName, LastName, MiddleName FROM developer WHERE id_developer=?";

    public UsersDAO() {
    }

    public List<User> selectUsers() throws SQLException, NamingException {
        connection = toConnection();
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id_developer"));
                user.setFirstname(resultSet.getString("FirstName"));
                user.setLastname(resultSet.getString("LastName"));
                user.setMiddleName(resultSet.getString("MiddleName"));
                user.setOfficialPosition(resultSet.getString("OfficialPosition"));
                user.setId_supersystem(resultSet.getInt("id_supersystem"));
                user.setLogin(resultSet.getString("Login"));
                user.setConfirmLogin(resultSet.getString("Login"));
                user.setPassword(resultSet.getString("Password"));
                users.add(user);
            }
        } finally {
            close();
        }
        return users;
    }
    public User getById(User user) {
        try {
            connection = toConnection();
            PreparedStatement prst = connection.prepareStatement(GET_BY_ID);
            prst.setInt(1, user.getId());
            ResultSet rs = prst.executeQuery();
            rs.next();
            user.setFirstname(rs.getString("FirstName"));
            user.setLastname(rs.getString("LastName"));
            user.setMiddleName(rs.getString("MiddleName"));
            connection.close();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        } finally {

            return user;
        }
    }
    public Map selectSystems() throws SQLException, NamingException {
        connection = toConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SqlSelectSystems);
        ResultSet resultSet = preparedStatement.executeQuery();
        Map systems = new HashMap<Integer, String>();
        while (resultSet.next()) {
            Integer id_system = resultSet.getInt("id_supersystem");
            String name = resultSet.getString("Name");
            systems.put(id_system, name);
        }
        connection.close();
        return systems;
    }

    public List<String> selectLogins() throws SQLException, NamingException {
        connection = toConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectLogins);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> logins = new ArrayList<>();
        while (resultSet.next()) {
            String login = resultSet.getString("Login");
            logins.add(login);
        }
        connection.close();
        return logins;
    }

    public String insertUserPartOne(User user) throws SQLException, NamingException {
        connection = toConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsertOne)) {
            preparedStatement.setString(1, user.getConfirmLogin());
            preparedStatement.setString(2, user.getPassword());
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

    public String insertUserPartTwo(User user) throws SQLException, NamingException {
        connection = toConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SqlInsertTwo)) {
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getMiddleName());
            preparedStatement.setString(4, user.getOfficialPosition());
            preparedStatement.setObject(5, user.getId_supersystem());
            preparedStatement.setString(6, user.getLogin());
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

    public String deleteUser(String login) throws SQLException, NamingException {
        connection = toConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete)) {
            preparedStatement.setString(1, login);
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                return "Пользователь удалён";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return "Не удалось удалить пользователя";
    }

    public User getByLogin(User user) {
        try {
            connection = toConnection();
            PreparedStatement prst = connection.prepareStatement(GET_BY_LOGIN);
            prst.setString(1, user.getLogin());
            ResultSet rs = prst.executeQuery();
            rs.next();
            user.setId(rs.getInt("id_developer"));
            user.setFirstname(rs.getString("FirstName"));
            user.setLastname(rs.getString("LastName"));
            user.setMiddleName(rs.getString("MiddleName"));
            user.setOfficialPosition(rs.getString("OfficialPosition"));
            user.setId_supersystem(rs.getInt("id_supersystem"));
            connection.close();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        } finally {

            return user;
        }
    }

}
