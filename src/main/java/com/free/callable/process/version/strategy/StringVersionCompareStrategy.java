package com.free.callable.process.version.strategy;

/**
 * Created by  on 2016/6/2.
 */
public class StringVersionCompareStrategy extends VersionCompareStrategy{

	public int compare(String current, String target) {
		return current.compareTo(target);
	}
}
