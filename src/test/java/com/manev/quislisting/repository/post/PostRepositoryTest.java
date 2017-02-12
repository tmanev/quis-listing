package com.manev.quislisting.repository.post;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.PostMeta;
import com.manev.quislisting.domain.post.discriminator.Post;
import com.manev.quislisting.domain.post.discriminator.builder.PostBuilder;
import com.manev.quislisting.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
@Transactional
public class PostRepositoryTest {

    @Inject
    PostRepository<Post> postPostRepository;

    @Inject
    UserRepository userRepository;

    @Test
    public void saveTest() {
        Optional<User> user = userRepository.findOneByLogin("user");

        PostMeta meta = new PostMeta();
        meta.setKey("_expiration_date");
        meta.setValue("1486908703");

        Post post = PostBuilder.aPost()
                .withTitle("First Post")
                .withContent("First content")
                .withName("First Name")
                .withCreated(ZonedDateTime.now())
                .withModified(ZonedDateTime.now())
                .withStatus("draft")
                .withUser(user.get()).build();

        meta.setAbstractPost(post);
        Set<PostMeta> metaSet = new HashSet<>();
        metaSet.add(meta);
        post.setPostMeta(metaSet);

        Post savedPost = postPostRepository.save(post);

        Post fromDb = postPostRepository.findOne(savedPost.getId());
        assertEquals(post.getTitle(), fromDb.getTitle());
        Set<PostMeta> savedPostMetaSet = post.getPostMeta();

        assertEquals(savedPostMetaSet.iterator().next().getKey(), "_expiration_date");
    }

}