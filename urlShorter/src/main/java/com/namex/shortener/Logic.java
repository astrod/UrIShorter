package com.namex.shortener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import redis.clients.jedis.Jedis;

public class Logic {
	public Jedis getConnection() {
		Jedis jedis = new Jedis("10.73.45.59");
		return jedis;
	}

	public String getId(String longUrl) throws Exception {

		Jedis jedis = null;
		String id;
		try {
			try {
				jedis = getConnection();
				id = jedis.get(longUrl);

			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			throw e;
		}
		
		return id;
	}

	public String autoIncrement() {
		Jedis jedis = getConnection();
		String key = jedis.get("auto");
		String newKey = null;
		if (key == null) {
			jedis.set("auto", "1");
			return "1";
		} else {
			newKey = String.valueOf((Integer.parseInt(key) + 1));
			jedis.set("auto", newKey);
		}
		return newKey;
	}

	public String getShort(String serverName, int port, String contextPath,
			String longUrl) throws Exception {

		Jedis jedis = getConnection();;

		String id = getId(longUrl);// check if URL has been shorten already
		
		if (id != null) {
			// if id is not null, this link has been shorten already.
			// nothing to do

		} else {
			// at this point id is null, make it shorter
			id = autoIncrement();
			try {
				jedis.set(id, longUrl);
				jedis.set(longUrl, id);
			}catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				System.out.println(jedis.get(id) + " and id : " + jedis.get(longUrl));
				jedis.close();
			}
			// after we insert the record, we obtain the ID as identifier of our
			// new short link
		}
		
		return "http://" + serverName + ":" + port + contextPath + "/" + id;
	}

	public String getLongUrl(String urlId) throws Exception {
		if (urlId.startsWith("/")) {
			urlId = urlId.replace("/", "");
		}
		String query = "SELECT long_url FROM url_data where id=" + urlId;
		Jedis jedis = null;
		String longUrl = null;
		try {
			jedis = getConnection();
			longUrl = jedis.get(urlId);
		} finally {
			jedis.close();
		}

		return longUrl;
	}

}