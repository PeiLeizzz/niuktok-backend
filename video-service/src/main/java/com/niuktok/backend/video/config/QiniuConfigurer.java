package com.niuktok.backend.video.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "qiniu")
public class QiniuConfigurer {
    private String accessKey;

    private String secretKey;

    private String bucket;

    private Integer expireTimes;

    private Map<String, Object> putPolicy;

    @Bean
    public Auth qiniuAuth() {
        return Auth.create(accessKey, secretKey);
    }

    @Bean
    public StringMap putPolicy() {
        return new StringMap(putPolicy);
    }

    @Bean
    public Configuration qiniuCfg() {
        return new Configuration(Region.region0());
    }

    @Bean
    public BucketManager bucketManager() {
        return new BucketManager(qiniuAuth(), qiniuCfg());
    }
}
