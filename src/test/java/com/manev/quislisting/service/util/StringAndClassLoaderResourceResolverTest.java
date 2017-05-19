package com.manev.quislisting.service.util;

import org.apache.commons.lang3.CharEncoding;
import org.junit.BeforeClass;
import org.junit.Test;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolver;

import static org.junit.Assert.assertEquals;


public class StringAndClassLoaderResourceResolverTest {
    private static SpringTemplateEngine templateEngine;

    @BeforeClass
    public static void setup() {
        TemplateResolver resolver = new TemplateResolver();
        resolver.setResourceResolver(new StringAndClassLoaderResourceResolver());
        resolver.setPrefix("mail/"); // src/test/resources/mail
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setCharacterEncoding(CharEncoding.UTF_8);
        resolver.setOrder(1);

        templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }

    @Test
    public void testStringResolution() {
        String expected = "<div>dave</div>";
        String input = "<div th:text=\"${userName}\">Some Username Here!</div>";
        IContext context = new StringAndClassLoaderResourceResolver.StringContext(input);
        context.getVariables().put("userName", "dave");
        String actual = templateEngine.process("redundant", context);
        assertEquals(expected, actual);
    }

    @Test
    public void testClasspathResolution() {
        IContext context = new Context();
        context.getVariables().put("message", "Hello Thymeleaf!");
        String actual = templateEngine.process("dummy", context);
        String expected = "<h1>Hello Thymeleaf!</h1>";
        assertEquals(expected, actual);
    }
}