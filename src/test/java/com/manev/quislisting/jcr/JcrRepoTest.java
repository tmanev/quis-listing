package com.manev.quislisting.jcr;

import com.manev.QuisListingApp;
import com.manev.quislisting.config.JcrConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jcr.GuestCredentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class JcrRepoTest {

    @Autowired
    private JcrConfiguration jcrConfiguration;

    @Test
    public void testRepo() throws RepositoryException, IOException {
        Repository repository = jcrConfiguration.repository();
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

}
