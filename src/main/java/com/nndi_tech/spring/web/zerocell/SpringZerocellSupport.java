
package com.nndi_tech.spring.web.zerocell;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
// @org.springframework.web.servlet.config.annotation.EnableWebMvc;
public class SpringZerocellSupport implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ZerocellRequestBodyArgumentResolver());
    }
}