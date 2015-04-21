package main;

import base.GameMechanics;
import base.WebSocketService;
import frontend.WebSocketServiceImpl;
import frontend.servlets.*;
import game.GameMechanicsImpl;
import base.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import resourceSystem.ResourceFactory;
import resourceSystem.ServerConfig;

public class Main {
    public static void main(String[] args) throws Exception {
//        int port = 8080;
//        if (args.length == 1) {
//            String portString = args[0];
//            port = Integer.valueOf(portString);
//        }

        final Logger logger = LogManager.getLogger();

        ResourceFactory.getInstance().init();

        ServerConfig config = (ServerConfig) ResourceFactory.getInstance().get("data/server_config.xml");

        Context context = new Context();

        context.add(AccountService.class, new AccountServiceImpl());
        context.add(WebSocketService.class, new WebSocketServiceImpl());
        context.add(GameMechanics.class, new GameMechanicsImpl(context));

        Server server = new Server(config.getPort());
        logger.info("Starting server at port {}", config.getPort());

        ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContext.addServlet(new ServletHolder(new SignInServlet(context)), SignInServlet.url);
        servletContext.addServlet(new ServletHolder(new SignUpServlet(context)), "/api/v1/auth/signup");
        servletContext.addServlet(new ServletHolder(new ProfileServlet(context)), "/api/v1/auth/profile");
        servletContext.addServlet(new ServletHolder(new LogoutServlet(context)), "/api/v1/auth/logout");
        servletContext.addServlet(new ServletHolder(new AdminServlet(context)), "/admin");
        servletContext.addServlet(new ServletHolder(new WebSocketGameServlet(context)), "/api/v1/game");
        logger.info("Created servlets");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, servletContext});

        server.setHandler(handlers);
        logger.info("Handlers set up");

        server.start();
        server.join();

    }
}