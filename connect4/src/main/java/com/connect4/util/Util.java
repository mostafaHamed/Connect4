package com.connect4.util;

import java.util.UUID;

/**
 * Util class
 * @author mhamed
 *
 */
public class Util {
	
	/**
	 * Utility method generates a UUID with a specific prefix
	 * @param prefix
	 * @return the generatedID
	 */
	public static String generateId(String prefix){
		String generatedId = prefix + "_" + UUID.randomUUID().toString();
		return generatedId;
	}
	
}
