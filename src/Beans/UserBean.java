package Beans;

import DAO.InformationSystemDAO;
import DAO.UsersDAO;
import Entities.InformationSystem;
import Entities.User;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "userBean")
public class UserBean {
    User user = new User();
    List<User> users = new ArrayList<User>();
    List<String> logins = new ArrayList<String>();
    Map systems = new HashMap<Integer, String>();
    InformationSystem systemByDeveloper = new InformationSystem();
    Logger logger = Logger.getLogger(UserBean.class);
    UsersDAO usersDAO = new UsersDAO();

    public Map getSystems() {
        return systems;
    }

    public List<String> getLogins() {
        return logins;
    }

    public void setLogins(List logins) {
        this.logins = logins;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List users) {
        this.users = users;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public InformationSystem getSystemByDeveloper() {
        return systemByDeveloper;
    }

    @PostConstruct
    public void setData() {
        try {
            this.users = usersDAO.selectUsers();
            this.logins = usersDAO.selectLogins();
            this.systems = usersDAO.selectSystems();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void findSystemByDeveloper(String login) {
        try {
            InformationSystemDAO systemDAO = new InformationSystemDAO();
            user.setLogin(login);
            systemByDeveloper = systemDAO.getSystemByDeveloper(login);
        }
        catch (SQLException | NamingException e) {
            logger.error("Ошибка извлечения данных из БД");
        }
    }

    public void findUserByLogin(String login) {

        user.setLogin(login);
        user = usersDAO.getByLogin(user);
    }

    public User findUserById(int iduser){
        this.user = new User(iduser);
        this.user = usersDAO.getById(user);
        return user;
    }

    public String deleteUser(String login) {
        UsersDAO usersDAO = new UsersDAO();
        try {
            usersDAO.deleteUser(login);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "AllUsers";
    }

    public String insertUserPartOne() {
        UsersDAO usersDAO = new UsersDAO();
        try {
            usersDAO.insertUserPartOne(user);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "insertPartTwo";
    }

    public String insertUserPartTwo() {
        UsersDAO usersDAO = new UsersDAO();
        try {
            usersDAO.insertUserPartTwo(user);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "It'sDone";
    }

    public void searchUserByLogin() {
        UsersDAO usersDAO = new UsersDAO();
        try {
            user = usersDAO.getByLogin(user);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public User searchUserById(User user){
        return user;
    }

}
