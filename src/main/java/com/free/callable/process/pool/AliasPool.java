package com.free.callable.process.pool;

import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by  on 2016/6/2.
 */
public class AliasPool<T> {

	private Map<String, Class<? extends T>> pool;
	
	private static Logger logger = Logger.getLogger("alias");
	
	public AliasPool(Map<String, Class<? extends T>> pool) {
		this.pool = pool;
	}

	public void register(String alias, Class<? extends T> executor) {
		synchronized (pool) {
			if(pool.containsKey(alias)) {
				logger.error("duplicate alias : " + alias);
			}
			pool.put(alias, executor);
		}
	}
	
	public Class<? extends T> getExecutor(String alias) {
		
		Class<? extends T> service = pool.get(alias);
		
		return service;
		
	}

}
