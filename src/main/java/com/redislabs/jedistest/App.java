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

        poolConf.setMaxTotal(8);
        /* maximum active connections
	    Max number of connections in the pool should be sized with traffic expectations (high watermark of # concurrent users)
            Redis Enterprise can handle signfigantly more connections so make this number high
            If using threads 2-3x the thread count is probably a safe rule of thumb
            be sure to return connections to the pool
        */
        poolConf.setMaxIdle(8);
        /* The maximum number of connections that should be kept in the idle pool if isPoolSweeperEnabled() returns false.
           Connections start getting closed here if idle if you have long running idle connections consider matching setMaxTotal
        */
        poolConf.setMinIdle(2);
        /* The minimum number of established connections that should be kept in the pool at all times.
           If using threads 1.25-1.5x the number of threads is safe
           This will ensure that connections are kept to the back end so they will recycle quickly
        */
        poolConf.setTestOnBorrow(false); 
        /* when true - send a ping before when we attempt to grab a connection from the pool
           Generally not recommended as while the PING command (https://redis.io/commands/PING) is realtively lightweight
           if there is much borrowing happening this can increase traffic if the number of operations per connection is low
        */
        poolConf.setTestOnReturn(false);
        /* when true - send a ping upon returning a pool connection
           I cannot imagine a scenario where this would be useful
        */
        poolConf.setTestWhileIdle(true);
        /* when true - send ping from idle resources in the pool
           Again the ping is not expensive
           Reccommend setting this to true if you have a firewall between client and server that disconnects idle TCP connections
           Aso common issue on the cloud with load balancers (https://aws.amazon.com/blogs/aws/elb-idle-timeout-control/)
        */
        poolConf.setMaxWaitMillis(5000);
        /* set max timeout in milliseconds for write operations
           default is -1 which means wait forever
           Tune this carefully - often a good idea to slightly exceed your redis SLOWLOG settings,
           so you can view what is taking so long (https://redis.io/commands/slowlog)
        */

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
            System.out.println("Redis Error: " + e);
        }

        pool.close();

    }
}
