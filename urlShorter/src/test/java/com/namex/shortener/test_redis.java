package com.namex.shortener;

import java.util.Set;

import redis.clients.jedis.Jedis;


public class test_redis {
	public static void main(String []args) {
		Jedis jedis = new Jedis("10.73.45.59");
//		jedis.set("pet", "popi");
		String key = jedis.get("auto");
		String newKey = null;
		if(key == null) {
			jedis.set("auto", "1");
		} else {
			newKey = String.valueOf((Integer.parseInt(key) + 1));
		}
		
		System.out.println(newKey);
	}
}
