package com.manev.quislisting.service;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Map;

@Component
public class TemplateEngineComponent {
    private final TemplateEngine templateEngine;

    public TemplateEngineComponent() {
        this.templateEngine = new TemplateEngine();
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolver);
    }

    public String getTemplateFromMap(String htmlContent, Map<String, Object> emailAttributesMap) {
        final Context ctx = new Context();
        if (!CollectionUtils.isEmpty(emailAttributesMap)) {
            emailAttributesMap.forEach(ctx::setVariable);
        }

        return templateEngine.process(htmlContent, ctx);
    }

}
