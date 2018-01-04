package com.free.callable.process.version.strategy;


/**
 * Created by  on 2016/6/2.
 */
public class NumberVersionCompareStrategy extends VersionCompareStrategy{

	public int compare(String current, String target) {
		
		String[] currents = current.split("\\.");
		String[] targets = target.split("\\.");
		
		int currentLength = currents.length;
		int targetLength = targets.length;
		int compareLength = Math.min(currentLength, targetLength);
		int k = 0;
        while (k < compareLength) {
            int c1 = Integer.parseInt(currents[k]);
            int c2 = Integer.parseInt(targets[k]);
            if (c1 != c2) {
                return c1 - c2;
            }
            k++;
        }
		return currentLength - targetLength;
	}
}
