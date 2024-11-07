package com.example.demo.cache;

import com.example.demo.entity.Config;
import com.example.demo.repo.ConfigRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum Keys{
        weather_api
    }

    private final ConfigRepo configRepo;
    public Map<String,String> APP_CACHE;

    public AppCache(ConfigRepo configRepo) {
        this.configRepo = configRepo;
    }

    @PostConstruct
    public void init() {
        APP_CACHE = new HashMap<>();
        List<Config> all=configRepo.findAll();
        for(Config config:all) {
            APP_CACHE.put(config.getKey(),config.getValue());
        }
    }

}
