package com.redislabs.jedistest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import org.apache.commons.pool2.PooledObject;

import java.util.Map;


public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Connecting to Redis!" );
	JedisPoolConfig poolConf = new JedisPoolConfig();

        poolConf.setMaxTotal(8);         // maximum active connections
        poolConf.setMaxIdle(8);          // The maximum number of connections that should be kept in the idle pool if isPoolSweeperEnabled() returns false.
        poolConf.setMinIdle(2);          // The minimum number of established connections that should be kept in the pool at all times.
        poolConf.setTestOnBorrow(true);  // send a ping before when we attempt to grab a connection from the pool
        poolConf.setTestOnReturn(false); // don't send a ping when we close the pool resource - no idea why you would
        poolConf.setTestWhileIdle(true); // send ping from idle resources in the pool
        poolConf.setMaxWaitMillis(5000); // set max timeout for write operations

	JedisPool pool = new JedisPool(poolConf, "localhost", 6379);

        try {
            Jedis jedis = pool.getResource();
	    //jedis.auth("password");
            Map<String, String> dataMap = jedis.hgetAll("test123"); 
            System.out.println(dataMap);
	    jedis.close();
        } catch (Exception e) {
            System.out.println("Error:" + e);
        }

    }
}
