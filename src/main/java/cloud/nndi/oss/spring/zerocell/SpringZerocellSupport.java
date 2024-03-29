
package cloud.nndi.oss.spring.zerocell;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Implements support for spring-zerocell
 */
@Configuration
public class SpringZerocellSupport implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ZerocellRequestBodyArgumentResolver());
    }
}