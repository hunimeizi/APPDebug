package com.snscity.common.debug.activity;

public class DevConstants {
	public static final int ENVIRONMENT_PRODUCTION = 1;
	public static final int ENVIROMENT_DEVELOPMENT = 2;
	public static final int ENVIROMENT_MOCKSERVER = 3;
	
	public static int environment() 
	{
		return ENVIROMENT_DEVELOPMENT;
	}
}
