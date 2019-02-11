package com.redislabs.jedistest;

import redis.clients.jedis.Jedis;



public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Connecting to Redis!" );
        try {
            Jedis jedis = new Jedis("localhost"); 
            System.out.println("Server is running: "+jedis.ping()); 
        } catch (Exception e) {
            System.out.println("Unable to connect to Redis Server");
        }

    }
}
