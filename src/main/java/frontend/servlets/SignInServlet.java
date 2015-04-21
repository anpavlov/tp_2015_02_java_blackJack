package frontend.servlets;

import base.AccountService;
import main.Context;
import base.UserProfile;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignInServlet extends HttpServlet {
    public static final String url = "/api/v1/auth/signin";
    private AccountService accountService;

    public SignInServlet(Context context) {
        this.accountService = (AccountService) context.get(AccountService.class);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();
        UserProfile profile = accountService.getUser(name);
        if (profile != null && profile.getPassword().equals(password)) {
            pageVariables.put("loginStatus", "Login passed");
            accountService.addSessions(request.getSession().getId(), profile);
        } else {
            pageVariables.put("loginStatus", "Wrong login/password");
        }

        response.getWriter().println(PageGenerator.getPage("authstatus.html", pageVariables));
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("email", email == null ? "" : email);
        pageVariables.put("password", password == null ? "" : password);

        response.getWriter().println(PageGenerator.getPage("authresponse.txt", pageVariables));
    }
}