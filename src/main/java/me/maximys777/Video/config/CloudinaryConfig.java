package me.maximys777.Video.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = Map.of(
                "cloud_name", "dm73bvaxj",
                "api_key", "611499478443144",
                "api_secret", "AYWZmtl-r32ixB_5IVEX6201dmQ"
        );

        return new Cloudinary(config);
    }
}