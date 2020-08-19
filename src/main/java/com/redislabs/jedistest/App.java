package com.redislabs.jedistest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.commands.ProtocolCommand;
import redis.clients.jedis.util.SafeEncoder;



public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Connecting to Redis!" );
        try {
            Jedis jedis = new Jedis("localhost", 6379); 
	    //jedis.auth("password"); // if you need a password
	    Pipeline pipe = jedis.pipelined();
	    pipe.sendCommand(Command.ADD,  SafeEncoder.encode("People"), SafeEncoder.encode("chris"));
	    pipe.expire("People", 200);
	    pipe.sync();
        } catch (Exception e) {
            System.out.println("Unable to connect to Redis Server");
        }

    }
}
