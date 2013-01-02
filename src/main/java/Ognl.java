/*
 * Copyright (c) 2012-2013, Poplar Yfyang 杨友峰 (poplar1123@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * Ognl工具类，主要是为了在ognl表达式访问静态方法时可以减少长长的类名称编写.
 * </p>
 * Ognl访问静态方法的表达式为: @class@method(args)
 * 示例使用:
 * <pre>
 * 	&lt;if test="@Ognl@isNotEmpty(userId)">
 * 		and user_id = #{userId}
 * 	&lt;/if>
 * </pre>
 *
 * @author poplar.yfyang
 * @version 1.0 2012-10-30 7:46 PM
 * @since JDK 1.5
 */
@SuppressWarnings("UnusedDeclaration")
public final class Ognl {

	public static boolean equal(String str_one ,String str_other){
		return StringUtils.equals(str_one,str_other);
	}

	/**
	 * 可以用于判断String,Map,Collection,Array是否为空
	 *
	 * @param o 参数
	 * @return true，为空，false不为空
	 */
	public static boolean isEmpty(Object o) {
		if (o == null) return true;
		boolean empty = false;
		if (o instanceof String) {
			empty = ((String) o).length() == 0;
		} else if (o instanceof Collection) {
			empty = ((Collection) o).isEmpty();
		} else if (o.getClass().isArray()) {
			empty = Array.getLength(0) == 0;
		} else if (o instanceof Map) {
			empty = ((Map) o).isEmpty();
		} else {
			return empty;
		}
		return empty;
	}

	/**
	 * 可以用于判断 Map,Collection,String,Array是否不为空
	 *
	 * @param o 参数
	 * @return true，参数为空，否则不为空
	 */
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}

	/**
	 * 判断参数不为空
	 *
	 * @param o 参数
	 * @return true参数不为空
	 */
	public static boolean isNotBlank(Object o) {
		return !isBlank(o);
	}

	/**
	 * 判断参数是否为数字
	 *
	 * @param o 参数
	 * @return true，为数字
	 */
	public static boolean isNumber(Object o) {
		if (o == null) return false;
		if (o instanceof Number) {
			return true;
		}
		if (o instanceof String) {
			String str = (String) o;
			return str.length() != 0 && str.trim().length() != 0 && StringUtils.isNumeric(str);
		}
		return false;
	}

	/**
	 * 判断字符串是否为空白
	 *
	 * @param o 参数对象
	 * @return true，为空
	 */
	public static boolean isBlank(Object o) {
		if (o == null)
			return true;
		if (o instanceof String) {
			String str = (String) o;
			return isBlank(str);
		}
		return false;
	}

	/**
	 * 判断字符串是否为空白
	 *
	 * @param str 参数字符串
	 * @return true，为空
	 */
	public static boolean isBlank(String str) {
		if (str == null || str.length() == 0) {
			return true;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}



}
