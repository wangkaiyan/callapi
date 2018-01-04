package com.free.callable.process;


import com.free.callable.process.pool.CallablePool;
import com.free.callable.process.pool.ExecutorPool;
import com.free.callable.process.pool.ProtocolPool;
import com.free.callable.process.security.callable.CallableSecurity;
import com.free.callable.process.security.callable.UserTokenCallableSecurity;
import com.free.callable.process.security.transport.EncryptionTransportSecurity;
import com.free.callable.process.security.transport.TransportSecurity;
import com.free.callable.process.version.strategy.NumberVersionCompareStrategy;
import com.free.callable.process.version.strategy.VersionCompareStrategy;

public class GlobalInstance {

	private static final CallablePool callablePool = new CallablePool();
	
	private static final ExecutorPool executorPool = new ExecutorPool();
	
	private static final ProtocolPool protocolPool = new ProtocolPool();
	
	private static UserTokenCallableSecurity  callableSecurity = new UserTokenCallableSecurity();

	private static VersionCompareStrategy versionCompareStrategy = new NumberVersionCompareStrategy();

	private static TransportSecurity transportSecurity = new EncryptionTransportSecurity();

	public static class GlobalSetting {
		
		public static void setVersionCompareStrategy(VersionCompareStrategy versionCompareStrategy) {
			GlobalInstance.versionCompareStrategy = versionCompareStrategy;
		}
		
		public static void setCallableSecurity(UserTokenCallableSecurity callableSecurity) {
			GlobalInstance.callableSecurity = callableSecurity;
		}
		
		public static void setTransportSecurity(TransportSecurity transportSecurity) {
			GlobalInstance.transportSecurity = transportSecurity;
		}
	}
	
	public static TransportSecurity getTransportSecurity() {
		
		return transportSecurity;
	}

	public static UserTokenCallableSecurity getCallableSecurity() {
		
		return callableSecurity;
	}
	public static CallablePool getCallablePool() {
		
		return callablePool;
	}
	
	public static ExecutorPool getExecutorPool() {
		
		return executorPool;
	}

	public static ProtocolPool getProtocolPool() {
		
		return protocolPool;
	}

	public static VersionCompareStrategy getVersioncomparestrategy() {
		return versionCompareStrategy;
	}
	
}
