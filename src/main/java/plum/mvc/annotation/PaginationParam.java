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

package plum.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分页条件参数标注
 *
 * @author poplar.yfyang
 * @version 1.0 2013-01-01 10:11 PM
 * @since JDK 1.5
 */
@Target(value = {ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PaginationParam {

	/**
	 * Whether the parameter is required.
	 * <p>Default is <code>true</code>, leading to an exception thrown in case
	 * of the parameter missing in the request. Switch this to <code>false</code>
	 * if you prefer a <code>null</value> in case of the parameter missing.
	 */
	boolean required() default true;
}
