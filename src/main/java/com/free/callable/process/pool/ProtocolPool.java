package com.free.callable.process.pool;


import com.free.callable.exception.CallableException;
import com.free.callable.process.print.AbstractProtocolPrint;
import com.free.callable.spring.util.ApplicationContextUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by  on 2016/6/2.
 */
public class ProtocolPool extends AliasPool<AbstractProtocolPrint>{

	private static Map<String, Class<? extends AbstractProtocolPrint>> protocolPool = new HashMap<>();

	public ProtocolPool() {
		super(protocolPool);
	}
	
	public AbstractProtocolPrint getProtocolPrint(String alias) throws CallableException {
		
		Class<? extends AbstractProtocolPrint> printClass = getExecutor(alias);
    	
    	if(printClass == null) {
			
    		throw new CallableException("only support [json,xml] ");
    	}
    	else {
    		return ApplicationContextUtil.getBeansOfType(printClass);
    	}
    	
	}
	
}
