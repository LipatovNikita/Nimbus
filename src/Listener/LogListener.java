package Listener;

import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class LogListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent arg) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg) {
    }
}
