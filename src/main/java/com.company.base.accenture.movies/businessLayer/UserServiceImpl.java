package com.company.base.accenture.movies.BL;

import com.company.base.accenture.movies.Interfaces.IContainUsers;
import com.company.base.accenture.movies.Interfaces.UserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Component
@RestController
@RequestMapping("/user")
@Consumes("application/json")
@Produces("application/json")
public class UserServiceImpl implements IContainUsers {

    @Autowired
    private UserAccessService icu;

    public static String activeUser = null;
    public static boolean adminMode = false;
    public static boolean exist;

    @Override
    public String registerUsers(String regName, String regLogin, String regPassword, String admin) {
        if(activeUser==null)
        {
            exist = false;
            icu.registerUsers(regName, regLogin, regPassword, admin);
            if (exist) {
                return "Пользователь с таким логином уже существует!";
            } else {
                return "Пользователь добавлен!";
            }
        }
        return "Вы не можете регистрироваться, находясь в системе!";
    }

    @Override
    @POST
    @RequestMapping("/register")
    public String registerUsers(@QueryParam("regName") String regName, @QueryParam("regLogin") String regLogin, @QueryParam("regPassword") String regPassword) {
        if(activeUser==null) {
            exist = false;
            icu.registerUsers(regName, regLogin, regPassword, "false");
            if (exist) {
                return "Пользователь с таким логином уже существует!";
            } else {
                return "Пользователь добавлен!";
            }
        }
        return "Вы не можете регистрироваться, находясь в системе!";
    }

    @Override
    @GET
    @RequestMapping("/login")
    @Consumes("application/json")
    @Produces("application/json")
    public String loginOldUsers(@QueryParam("name") String name, @QueryParam("login") String login, @QueryParam("password") String password) {
        if(activeUser==null) {
            icu.searchUser(name, login, password);
            if(activeUser!=null) {
                return "Вы вошли в систему!";
            } else {
                return "Неверно введённые данные!";
            }
        }
        return "Вы не можете логиниться, находясь в системе!";
    }

    @Override
    @POST
    @RequestMapping("/logout")
    @Consumes("application/json")
    @Produces("application/json")
    public String logout() {
        if(activeUser!=null) {
            activeUser=null;
            if (adminMode) { adminMode = false; }
            return "Вы вышли из системы!";
        }
        return "Вас не было в системе!";
    }
}
