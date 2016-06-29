package com.carnival.mm;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import java.util.Arrays;
import java.util.List;

/**
 * This class contains the Couchbase configuration for the application
 */
@Configuration
@EnableCouchbaseRepositories
@ConfigurationProperties(prefix = "couchbase")
public class DatabaseConfig extends AbstractCouchbaseConfiguration {

    private String host;
    private String bucketName;
    private String bucketPassword;

    @Override
    protected List<String> bootstrapHosts() {
        return Arrays.asList(getHost());
    }

    @Override
    protected String getBucketName() {
        return bucketName;
    }

    @Override
    protected String getBucketPassword() {
        return bucketPassword;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setBucketPassword(String bucketPassword) {
        this.bucketPassword = bucketPassword;
    }
}