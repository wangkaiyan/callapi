package com.free.callable.application;

import com.free.callable.call.Callable;
import com.free.callable.process.GlobalInstance;
import com.free.callable.process.pool.CallablePool;
import com.free.callable.process.pool.ExecutorPool;
import com.free.callable.process.pool.ProtocolPool;
import com.free.callable.process.print.JsonProtocolPrint;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

/**
 * Created by  on 2016/6/2.
 */
public abstract class CallableApplication extends ContextLoaderListener {

	
	private CallablePool callablePool = GlobalInstance.getCallablePool();
	
	private ExecutorPool executorPool = GlobalInstance.getExecutorPool();
	
	private ProtocolPool protocolPool = GlobalInstance.getProtocolPool();
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		this.init();
		
		this.registerSupportProtocol();
		
		executorPool.mount();
	}

	protected void mount(String alias, String version, Class<? extends Callable> handler) {
		
		callablePool.mount(alias, version, handler);
		
	}
	
	protected void mount(String alias, Class<? extends Callable> handler) {
		
		callablePool.mount(alias, "1.0.0", handler);
		
	}
	
	/*protected void mountValidate(Class<? extends Annotation> alias, AbstractMatchValidate<? extends Annotation> handler) {
		
		ValidateRulePool.mount(alias, handler);
	
	}*/

	protected abstract void init();
	
	protected void registerSupportProtocol() {
		protocolPool.register("json", JsonProtocolPrint.class);
		//protocolPool.register("xml", XmlProtocolPrint.class);
	
	}

	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
