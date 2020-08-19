package com.redislabs.jedistest;

import redis.clients.jedis.commands.ProtocolCommand;
import redis.clients.jedis.util.SafeEncoder;

// Add in some customer commands used in bloomfilter


public enum Command implements ProtocolCommand {
    RESERVE("BF.RESERVE"),
    ADD("BF.ADD"),
    MADD("BF.MADD"),
    EXISTS("BF.EXISTS"),
    MEXISTS("BF.MEXISTS"),
    INSERT("BF.INSERT");

    private final byte[] raw;

    Command(String alt) {
        raw = SafeEncoder.encode(alt);
    }

    public byte[] getRaw() {
        return raw;
    }
}
