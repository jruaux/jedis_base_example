package com.redislabs.jedistest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.util.SafeEncoder;


public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Connecting to Redis!" );

        JedisPoolConfig poolConf = new JedisPoolConfig();

        poolConf.setMaxTotal(8);         // maximum active connections
        poolConf.setMaxIdle(8);          // The maximum number of connections that should be kept in the idle pool if isPoolSweeperEnabled() returns false.
        poolConf.setMinIdle(2);          // The minimum number of established connections that should be kept in the pool at all times.
        poolConf.setTestOnBorrow(false); // send a ping before when we attempt to grab a connection from the pool
        poolConf.setTestOnReturn(false); // don't send a ping when we close the pool resource - no idea why you would
        poolConf.setTestWhileIdle(true); // send ping from idle resources in the pool
        poolConf.setMaxWaitMillis(5000); // set max timeout for write operations

        JedisPool pool = new JedisPool(poolConf, "localhost", 6379);

        try {
            Jedis jedis = pool.getResource();

    	    Pipeline pipe = jedis.pipelined();
    	    Response<Object> bf = pipe.sendCommand(
                Command.ADD,  
                SafeEncoder.encode("People"), 
                SafeEncoder.encode("chris")
                );
    	    pipe.expire("People", 200);
            pipe.sync();

	Long result = (Long) bf.get();

	    if ( result > 0) {
            	System.out.println("Newly added");
	    } else {
            	System.out.println("Already present");
	    }


            jedis.close();

        } catch (Exception e) {
            System.out.println("Unable to connect to Redis Server");
        }

        pool.close();

    }
}
