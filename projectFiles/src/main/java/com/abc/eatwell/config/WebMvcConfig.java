package com.abc.eatwell.config;

import com.abc.eatwell.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * set the static resources mapping
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/server/**").addResourceLocations("classpath:/server/");
        registry.addResourceHandler("/client/**").addResourceLocations("classpath:/client/");
    }

    /**
     * extend the message converter in Mvc
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // use Jackson to transform Java object to json
        messageConverter.setObjectMapper((new JacksonObjectMapper()));
        // add the above message converter to mvc converter set
        converters.add(0, messageConverter);
    }
}
