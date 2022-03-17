package com.memorydb.redis.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.memorydb.redis.demo.PMSSource;
import com.memorydb.redis.demo.model.HotelMigrationStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.stereotype.Service;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisClusterOperationException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class RedisService {

    private static JedisCluster jedisCluster;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void connectToCluster() throws JedisClusterOperationException {
        log.info("Connecting to AWS MemoryDb for Redis......");
        final Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("clustercfg.my-cluster.scdruq.memorydb.eu-west-2.amazonaws.com", 6379));

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        int connectionTimeout = 30;
        int soTimeout = 30;
        int maxAttempts = 2;
        String clientName = "Jedis";
        boolean ssl = true;

        jedisCluster = new JedisCluster(jedisClusterNodes, connectionTimeout, soTimeout, maxAttempts, "test","Myclusterpassword123", clientName,poolConfig , true);

    }

    public void save(final HotelMigrationStatus hotelMigrationStatus){
        try{
            objectMapper.registerModule(new JavaTimeModule());
            final String value = objectMapper.writeValueAsString(hotelMigrationStatus);
            log.info("Going to save value :{} for key {} in AWS MemoryDb for Redis......",value, hotelMigrationStatus.getHotelId());
            jedisCluster.set(hotelMigrationStatus.getHotelId(), value);
            log.info("Successfully saved in AWS MemoryDb for Redis......");
        } catch (IOException e) {
            log.error("Error saving data in AWS MemoryDb for Redis......");
            e.printStackTrace();
        }
    }

    public String get(final String hotelId){
        String dataFromRedis = null;
        try {
            log.info("Going to fetch data from AWS MemoryDb for Redis......");
            dataFromRedis = jedisCluster.get(hotelId);
            log.info("Data from Redis cache:{}",dataFromRedis);
        } catch (Exception e) {
            log.error("Error fetching data from AWS MemoryDb for Redis......");
            e.printStackTrace();
        }
        return dataFromRedis;
    }

    public String saveNRetrieve(){
        final HotelMigrationStatus hotelMigrationStatus = new HotelMigrationStatus();
        final String hotelId = "Test1234";
        hotelMigrationStatus.setHotelId(hotelId);
        hotelMigrationStatus.setPmsSource(PMSSource.BART);
        hotelMigrationStatus.setUpdatedOn(LocalDate.now());
        String response = null;
        try {
            connectToCluster();
            save(hotelMigrationStatus);
            response = get(hotelId);
        }
        catch(Exception e){
            log.error("Received Exception : {}", e.getMessage());
            e.printStackTrace();
            response = e.getMessage();
        }
        return response;
    }

}
