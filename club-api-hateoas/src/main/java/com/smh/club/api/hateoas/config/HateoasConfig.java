package com.smh.club.api.hateoas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class HateoasConfig {

    /**
     * Corrects links when calls are forwarded.
     *
     * @return {@link ForwardedHeaderFilter}
     */
    @Bean
    ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
}
