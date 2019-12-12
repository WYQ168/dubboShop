package com.hgz.v17nettyspringboot.utils;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

public class ChannelUtils {

    private static Map<String, Channel> map = new HashMap<>();

    public static void add(String userId,Channel channel){
            map.put(userId,channel);
    }

    public static void remove(String userId){
            map.remove(userId);
    }

    public static Channel getChannel(String userId){
        return map.get(userId);
    }
}
