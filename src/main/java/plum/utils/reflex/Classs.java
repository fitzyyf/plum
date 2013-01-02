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

package plum.utils.reflex;

/**
 * <p>
 * 相关类的工具类.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2012-12-09 7:00 PM
 * @since JDK 1.5
 */
public class Classs {


	/**
	 * 返回指定类型所对应的primitive类型。包含String类
	 *
	 * fixed:paramter string type.
	 *
	 * @param clazz 要检查的类型
	 * @return 如果指定类型为<code>null</code>或不是primitive类型的包装类，则返回<code>false</code>，否则返回<code>true</code>。
	 */
	public static boolean isPrimitiveType(Class clazz) {
		return clazz != null && (clazz.isPrimitive() || clazz.equals(Long.class) || clazz.equals(Integer.class)
				|| clazz.equals(Short.class) || clazz.equals(Byte.class) || clazz.equals(Double.class)
				|| clazz.equals(Float.class) || clazz.equals(Boolean.class) || clazz.equals(Character.class) || clazz.equals(String.class));

	}

	/**
	 * 返回指定类型所对应的非primitive类型。
	 *
	 * @param clazz 要检查的类型
	 * @return 如果指定类型为<code>null</code>，则返回<code>true</code>，如果是primitive类型，则返回<code>false</code>。
	 */
	public static boolean isNonPrimitiveType(Class clazz) {
		return clazz == null || !clazz.isPrimitive() || !clazz.equals(long.class)
				&& !clazz.equals(int.class) && !clazz.equals(short.class) && !clazz.equals(byte.class)
				&& !clazz.equals(double.class) && !clazz.equals(float.class) && !clazz.equals(boolean.class)
				&& !clazz.equals(char.class) && !clazz.equals(String.class);

	}
}
