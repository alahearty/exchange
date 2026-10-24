package com.example.exchange.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.example.exchange.dao.UserDao;

/**
 * Performs the one-time plaintext-to-BCrypt migration before the application serves requests.
 * A failure is reported as a startup warning; login still falls back to the legacy check and
 * upgrades that user's password on the next successful sign-in.
 */
@WebListener
public class ApplicationStartupListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        int migrated = new UserDao().migrateLegacyPasswords();
        if (migrated > 0) {
            System.out.println("Password migration: hashed " + migrated + " legacy account(s).");
        } else if (migrated < 0) {
            System.err.println("Password migration could not run; legacy accounts will be upgraded on login.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}
