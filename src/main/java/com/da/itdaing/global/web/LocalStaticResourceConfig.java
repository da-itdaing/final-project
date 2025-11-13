// src/main/java/com/da/itdaing/global/web/LocalStaticResourceConfig.java
package com.da.itdaing.global.web;

import com.da.itdaing.global.storage.StorageProps;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.provider", havingValue = "local", matchIfMissing = true)
public class LocalStaticResourceConfig implements WebMvcConfigurer {
    private final StorageProps props;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        var local = props.getLocal(); // root=./var, baseDir=uploads
        String handler = "/" + local.getBaseDir() + "/**";                 // "/uploads/**"
        String location = "file:" + local.getRoot() + "/" + local.getBaseDir() + "/"; // "file:./var/uploads/"

        registry.addResourceHandler(handler)
            .addResourceLocations(location)
            .setCachePeriod(31536000);
    }
}
