/**   
 * @FileName: SharedPreferencesManager.java 
 * @Package com.application.base 
 * @author 馋猫   Email： eiffelyk@gmail.com
 * @date 2014年9月6日 下午3:47:36  
 */
package com.snscity.tools.debuger;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * SharedPreferencesManager：单例,putExtra,getXXX
 * @ClassName: SharedPreferencesManager  
 * @author Chenyuanming
 * @date 2014年11月7日 上午9:49:07 
 * @version V1.0
 */
public class SharedPreferencesManager {
	private static final int STRING_TYPE = 1;
	private static final int INT_TYPE = 2;
	private static final int BOOLEAN_TYPE = 3;
	private static final int FLOAT_TYPE = 4;
	private static final int LONG_TYPE = 5;
	private static String USER_TABLE_NAME = "shareprefrences.xml";
	private SharedPreferences sharedPreferences;
	private static SharedPreferencesManager instance;

	/**
	 * 获取SharedPreferencesManager实例
	 * 
	 * @MethodName: getInstance
	 * @param context
	 * @return
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:02:25	
	 */
	public static SharedPreferencesManager getInstance(Context context) {
		synchronized (SharedPreferencesManager.class) {
			if (instance == null) {
				synchronized (SharedPreferencesManager.class) {
					if (instance == null) {
						instance = new SharedPreferencesManager(context);
					}
				}
			}
		}
		return instance;
	}

	private SharedPreferencesManager(Context context) {
		initSpManager(context);
	}

	private void initSpManager(Context context) {
		sharedPreferences = context.getSharedPreferences(USER_TABLE_NAME, Context.MODE_PRIVATE);
	}

	/**
	 * 
	 * 插入单条数据，string int boolean 。
	 * 
	 * @MethodName: putExtra
	 * @param key
	 * @param content
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:13:23
	 */
	public void putExtra(String key, Object content) {
		remove(key);//防止数据类型不一致，安全起见，先remove掉
		Editor editor = sharedPreferences.edit();
		if (content instanceof String) {
			insertConfigure(editor, key, STRING_TYPE, content);
		} else if (content instanceof Integer) {
			insertConfigure(editor, key, INT_TYPE, content);
		} else if (content instanceof Boolean) {
			insertConfigure(editor, key, BOOLEAN_TYPE, content);
		} else if (content instanceof Float) {
			insertConfigure(editor, key, FLOAT_TYPE, content);
		} else if (content instanceof Long) {
			insertConfigure(editor, key, LONG_TYPE, content);
		}
		editor.commit();
	}

	/**
	 * 插入多条数据，string int boolean 。
	 * 
	 * @MethodName: putExtras
	 * @param map
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:13:38
	 */
	public void putExtras(HashMap<String, Object> map) {
		Editor editor = sharedPreferences.edit();
		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object content = entry.getValue();
			putExtra(key,content);
		}
		editor.commit();
	}

	/**
	 * 共用插入方法
	 * 
	 * @MethodName: insertConfigure
	 * @param editor
	 * @param key
	 * @param type
	 * @param content
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:13:49
	 */
	private void insertConfigure(Editor editor, String key, int type, Object content) {
		String value = String.valueOf(content);
		switch (type) {
		case STRING_TYPE:
			editor.putString(key, value);
			break;
		case BOOLEAN_TYPE:
			editor.putBoolean(key, Boolean.parseBoolean(value));
			break;
		case INT_TYPE:
			editor.putInt(key, Integer.parseInt(value));
			break;
		case FLOAT_TYPE:
			editor.putFloat(key, Float.parseFloat(value));
			break;
		case LONG_TYPE:
			editor.putLong(key, Long.parseLong(value));
			break;
		}
	}

	/**
	 * 通过key获取Boolean对象
	 * 
	 * @MethodName: getBoolean
	 * @param key
	 * @return
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:15:57
	 */
	public boolean getBoolean(String key) {
		return sharedPreferences.getBoolean(key, false);
	}

	/**
	 * 通过key获取int对象
	 * 
	 * @MethodName: getInt
	 * @param key
	 * @return
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:16:24
	 */
	public int getInt(String key) {
		return sharedPreferences.getInt(key, 0);
	}

	/**
	 * 通过key获取String对象
	 * 
	 * @MethodName: getString
	 * @param key
	 * @return
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:16:33
	 */
	public String getString(String key) {
		return sharedPreferences.getString(key, null);
	}
	/**
	 * 通过key获取Float对象
	 * 
	 * @MethodName: getFloat
	 * @param key
	 * @return
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:16:33
	 */
	public Float getFloat(String key) {
		return sharedPreferences.getFloat(key, 0);
	}
	/**
	 * 通过key获取Long对象
	 * 
	 * @MethodName: getLong
	 * @param key
	 * @return
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:16:33
	 */
	public Long getLong(String key) {
		return sharedPreferences.getLong(key, 0);
	}
	
	/**
	 * 通过key获取Boolean对象
	 * 
	 * @MethodName: getBoolean
	 * @param key
	 * @param defValue
	 * @return
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:15:57
	 */
	public boolean getBoolean(String key,boolean defValue) {
		return sharedPreferences.getBoolean(key, defValue);
	}
	
	/**
	 * 通过key获取int对象
	 * 
	 * @MethodName: getInt
	 * @param key
	 * @param defValue
	 * @return
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:16:24
	 */
	public int getInt(String key,int defValue) {
		return sharedPreferences.getInt(key, defValue);
	}
	
	/**
	 * 通过key获取String对象
	 * 
	 * @MethodName: getString
	 * @param key
	 * @param defValue
	 * @return
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:16:33
	 */
	public String getString(String key,String defValue) {
		return sharedPreferences.getString(key, defValue);
	}
	/**
	 * 通过key获取Float对象
	 * 
	 * @MethodName: getFloat
	 * @param key
	 * @param defValue
	 * @return
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:16:33
	 */
	public Float getFloat(String key,Float defValue) {
		return sharedPreferences.getFloat(key, defValue);
	}
	/**
	 * 通过key获取Long对象
	 * 
	 * @MethodName: getLong
	 * @param key
	 * @param defValue
	 * @return
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:16:33
	 */
	public Long getLong(String key,Long defValue) {
		return sharedPreferences.getLong(key, defValue);
	}

	/**
	 * 
	 * 获取SharedPreferences中所有的对象，返回数据是Map<String, ?>类型
	 * 
	 * @MethodName: getAllSharedPreferences
	 * @return
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:16:44
	 */
	public Map<String, ?> getAllSharedPreferences() {
		return sharedPreferences.getAll();
	}

	/**
	 * 清空SharedPreferences中所有的对象
	 * 
	 * @MethodName: clearAllSharedPreferences
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:17:39
	 */
	public void clearAllSharedPreferences() {

		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 清空SharedPreferences中指定key的对象
	 * 
	 * @MethodName: remove
	 * @param key
	 * @author Chenyuanming
	 * @date 2014年10月9日 下午2:17:47
	 */
	public void remove(String key) {
		Editor editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}
}
