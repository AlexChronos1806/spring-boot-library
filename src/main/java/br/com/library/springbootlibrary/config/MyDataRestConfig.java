package br.com.library.springbootlibrary.config;

import br.com.library.springbootlibrary.entity.Book;
import br.com.library.springbootlibrary.entity.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Value("${allowed.origins.endpoint}")
    private String theAllowedOrigins;

    private static final HttpMethod[] THE_UNSUPPORTED_ACTIONS = {
            HttpMethod.POST,
            HttpMethod.PATCH,
            HttpMethod.DELETE,
            HttpMethod.PUT
    };

    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration configuration,
                                                     CorsRegistry corsRegistry) {
        configuration.exposeIdsFor(Book.class);
        configuration.exposeIdsFor(Review.class);

        disableHttpMethods(Book.class, configuration);
        disableHttpMethods(Review.class, configuration);

        corsRegistry.addMapping(configuration.getBasePath() + "/**")
                .allowedOrigins(theAllowedOrigins);
    }

    private <T> void disableHttpMethods(Class<T> theClass, RepositoryRestConfiguration configuration) {
        configuration.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure(((metadata, httpMethods) -> httpMethods.disable(THE_UNSUPPORTED_ACTIONS)))
                .withCollectionExposure(((metadata, httpMethods) -> httpMethods.disable(THE_UNSUPPORTED_ACTIONS)));
    }
}
