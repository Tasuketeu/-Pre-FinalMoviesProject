
import com.company.base.accenture.movies.interfaces.IContainUsers;
import com.company.base.accenture.movies.objModelClass.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "loginServlet", urlPatterns = "/user/login")
public class LoginServlet extends HttpServlet {

    @Autowired
    private IContainUsers icu;

    @Override
    protected void doGet( HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();

        String queryString = req.getQueryString();
        String decoded = URLDecoder.decode(queryString, "UTF-8");
        String[] pares = decoded.split("&");
        Map<String, String> parameters = new HashMap<String, String>();
        for(String pare : pares) {
            String[] nameAndValue = pare.split("=");
            parameters.put(nameAndValue[0], nameAndValue[1]);
        }

// Now you can get your parameter:
        String valueOfParam2 = parameters.get("param2");

          String name = req.getParameter("name");
//        String login = req.getParameter("login");
//        String pass = req.getParameter("pass");
//        String response=icu.loginOldUsers(name,login,pass);
        pw.println(name);
        pw.close();
    }
}
