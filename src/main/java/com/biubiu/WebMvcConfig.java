package com.biubiu;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Value("${ffmpeg.video.folder}")
    private String videoFolder;


    /**
     *  允许跨域访问
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 可限制哪个请求可以通过跨域
                .allowedHeaders("*")  // 可限制固定请求头可以通过跨域
                .allowedMethods("*") // 可限制固定methods可以通过跨域
                .allowedOrigins("*")  // 可限制访问ip可以通过跨域
                .allowCredentials(true) // 是否允许发送cookie
                .exposedHeaders(HttpHeaders.SET_COOKIE);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/templates/dist/static/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:favicon.ico");
        registry.addResourceHandler("*.html").addResourceLocations("classpath:/templates/dist/");
        registry.addResourceHandler("*.js").addResourceLocations("classpath:/templates/dist/");
        registry.addResourceHandler("*.css").addResourceLocations("classpath:/templates/dist/");
        registry.addResourceHandler("/api/video/play/**").addResourceLocations("file:"+ videoFolder);
        super.addResourceHandlers(registry);
    }
}
