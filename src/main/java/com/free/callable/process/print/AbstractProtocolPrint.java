package com.free.callable.process.print;

/**
 * Created by  on 2016/6/2.
 */
public abstract class AbstractProtocolPrint{

	public abstract <IN> IN in(String input, Class<IN> clazz);

	public abstract <OUT> String out(OUT out);
	
	public abstract String error(int code,String message);
	
	public abstract String getContentType();
}
