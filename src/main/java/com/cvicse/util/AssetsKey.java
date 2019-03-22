package com.cvicse.util;

import java.util.UUID;

public class AssetsKey {
	public static String keyGenerator() {
		return UUID.randomUUID().toString();
	}
}
