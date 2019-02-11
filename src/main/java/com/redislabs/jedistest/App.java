package com.redislabs.jedistest;

import redis.clients.jedis.Jedis;
import java.util.Map;


public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Connecting to Redis!" );
        try {
            Jedis jedis = new Jedis("localhost"); 
            Map<String, String> dataMap = jedis.hgetAll("56714964282381505346596299146714128775465616245895347306009810006048170613624066755"); 
            System.out.println(dataMap);
        } catch (Exception e) {
            System.out.println("Unable to connect to Redis Server");
        }

    }
}
