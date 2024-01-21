package com.jay.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author jay
 * @create 2024/1/21
 **/
@Component
public class RedisClient {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void pushToQueue(String queueName, String value) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        listOperations.rightPush(queueName, value);
    }

    public String popFromQueue(String queueName) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        return listOperations.leftPop(queueName);
    }

    public List<String> getAllFromQueue(String queueName) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        long size = listOperations.size(queueName);
        return listOperations.range(queueName, 0, size - 1);
    }


    public void addToSet(String setName, String value) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.add(setName, value);
    }

    public Set<String> getAllFromSet(String setName) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        return setOperations.members(setName);
    }

    public long getSetSize(String setName) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        return setOperations.size(setName);
    }

    public boolean isInSet(String setName, String value) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        return setOperations.isMember(setName, value);
    }

    public void removeFromSet(String setName, String value) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.remove(setName, value);
    }
}
