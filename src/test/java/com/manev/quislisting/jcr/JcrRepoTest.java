package com.manev.quislisting.jcr;

import com.manev.QuisListingApp;
import com.manev.quislisting.config.JcrConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jcr.*;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class JcrRepoTest {

    @Autowired
    private JcrConfiguration jcrConfiguration;

    @Test
    public void testRepo() throws RepositoryException, IOException {
        Repository repository = jcrConfiguration.getRepositoryImpl();
        Session session = repository.login(new GuestCredentials());
        try {
            String user = session.getUserID();
            String name = repository.getDescriptor(Repository.REP_NAME_DESC);
            System.out.println(
                    "Logged in as " + user + " to a " + name + " repository.");
        } finally {
            session.logout();
        }
    }

    @Test
    public void testFirstHop() throws RepositoryException, IOException {
        Repository repository = jcrConfiguration.getRepositoryImpl();
        Session session = repository.login(
                new SimpleCredentials("admin", "admin".toCharArray()));
        try {
            Node root = session.getRootNode();

            // Store content
            Node hello = root.addNode("hello");
            Node world = hello.addNode("world");
            world.setProperty("message", "Hello, World!");
            session.save();

            // Retrieve content
            Node node = root.getNode("hello/world");
            System.out.println(node.getPath());
            System.out.println(node.getProperty("message").getString());

            // Remove content
            root.getNode("hello").remove();
            session.save();
        } finally {
            session.logout();
        }
    }

}
