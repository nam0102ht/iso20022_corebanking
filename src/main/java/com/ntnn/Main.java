package com.ntnn;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.security.Security;

@SpringBootApplication
@EnableR2dbcRepositories
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
}