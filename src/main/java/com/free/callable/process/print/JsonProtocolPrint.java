package com.free.callable.process.print;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.free.callable.struts.Response;
import com.free.callable.struts.ResponseData;

/**
 * Created by  on 2016/6/2.
 */
public class JsonProtocolPrint extends AbstractProtocolPrint {

	@Override
	public <IN> IN in(String input, Class<IN> clazz) {
		return JSON.parseObject(input, clazz);
	}

	@Override
	public <OUT> String out(OUT out) {
		return JSON.toJSONString(out,
				SerializerFeature.PrettyFormat,
				SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullNumberAsZero,
				SerializerFeature.WriteNullListAsEmpty,
				SerializerFeature.WriteNullBooleanAsFalse, 
				SerializerFeature.WriteNullStringAsEmpty );
	}

	@Override
	public String error(int code, String message) {
		Response<ResponseData> response = new Response<ResponseData>();
		ResponseData data = new ResponseData();
		data.setMessage(message);
		response.setCode(code);
		response.setData(data);
		return out(response);
	}

	@Override
	public String getContentType() {
		return "text/json;charset=utf-8";
	}

}
