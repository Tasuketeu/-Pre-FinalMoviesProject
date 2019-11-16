package com.company.base.accenture.movies.servlets;


import com.company.base.accenture.movies.interfaces.IContainUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "registrationServlet", urlPatterns = "/user/register")
public class RegistationServlet extends HttpServlet {

    @Autowired
    private IContainUsers bl;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    @Override
    protected void doPost( HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain");
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();

        String name = req.getParameter("name");
        String login = req.getParameter("login");
        String pass = req.getParameter("pass");
        String response=bl.registerUsers(name,login,pass);
        pw.println(response);
        pw.close();
    }
}
