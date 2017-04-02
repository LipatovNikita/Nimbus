package Beans;


import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ManagedBean(name = "loginBean")
public class LoginBean {

    public LoginBean() {
    }

    private String username;
    private String password;
    String login = (String) FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();

    private Logger logger = Logger.getLogger(LoginBean.class);

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {
        Map<String, String> map = (Map<String, String>) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        setUsername(map.get("loginForm:login"));
        setPassword(map.get("loginForm:password"));
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(this.username, this.password);
        } catch (ServletException ex) {
            return "loginError";
        }
        if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("Superadmin")) {
            logger.info("Суперадминистратор " + login + " авторизовался в системе");
            return "superadmin";
        }
        else if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("Admin")) {
            logger.info("Администратор " + login + " авторизовался в системе");
            return "admin";
        }
        else {
            logger.info("Разработчик " + login + " авторизовался в системе");
            return "developer";
        }
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
            logger.info("Пользователь " + login + " покинул систему(");

        } catch (ServletException e) {
            logger.error("Ошибка логаута, не удаётся выйти из системы");
        }
        return "logout";
    }
}
