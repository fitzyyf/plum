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

package plum.mybatis;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;
import plum.utils.page.PageQuery;
import plum.utils.reflex.Classs;

/**
 * <p>
 * Paging <code>PaginationCriteria</code> finds.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2012-12-09 7:22 PM
 * @see PageQuery
 * @since JDK 1.5
 */
public class Pagings {

	/** The search parameters by use of interim storage of results. */
	private final Map<Object, String> search_map = new HashMap<Object, String>();

	/** private constructor */
	private Pagings() {
	}

	/**
	 * get pagings instance object.
	 *
	 * @return pagings.
	 */
	public static Pagings getInstance() {
		return App.pagings;
	}

	/**
	 * from the formulation of the objects found in the paging parameters object.
	 *
	 * @param object object.
	 * @return paging parameters.
	 */
	public PageQuery findCriteria(Object object) {
		if (object == null) {
			return null;
		}
		try {
			return findCriteriaFromObject(object);
		} finally {
			//clean serarchs map.
			search_map.clear();
		}
	}

	/**
	 * In the object to find whether contains <code>PaginationCriteria</code> objects.
	 *
	 * @param object paramter object.
	 * @return PaginationCriteria
	 */
	private PageQuery findCriteriaFromObject(Object object) {

		//如果已经寻找过这个对象，现在再来这里肯定是没找到。就直接返回NULL
		if (search_map.containsKey(object)) {
			return null;
		}
		//object class
		Class<?> obj_class = object.getClass();
		PageQuery pc;
		//primitive
		if (Classs.isPrimitiveType(obj_class)) {
			pc = null;
		} else if (obj_class.isAssignableFrom(PageQuery.class)) {
			pc = (PageQuery) object;
		} else if (object instanceof Map) {
			pc = findCriteriaFromMap((Map) object);
		} else if (object instanceof Collection) {
			pc = findCriteriaFromCollection((Collection) object);
		} else if (obj_class.isArray()) {
			pc = findCriteriaFromArray(object);
		} else {
			BeanMap map = new BeanMap(object);
			return findCriteriaFromMap(map);
		}

		search_map.put(object, StringUtils.EMPTY);
		return pc;
	}

	/**
	 * In the array to find whether it contains the <code>PaginationCriteria</code> object.
	 *
	 * @param array the array.
	 * @return PageQuery
	 */
	private PageQuery findCriteriaFromArray(Object array) {
		if (search_map.containsKey(array)) {
			return null;
		}

		for (int i = 0; i < Array.getLength(array); i++) {
			Object object = Array.get(array, i);
			PageQuery pc = findCriteriaFromObject(object);
			if (pc != null) {
				search_map.put(array, StringUtils.EMPTY);
				return pc;
			}
		}
		search_map.put(array, StringUtils.EMPTY);
		return null;
	}

	/**
	 * In the Collection to find whether contains <code>PaginationCriteria</code> objects.
	 *
	 * @param collection paramter collection.
	 * @return PageQuery
	 */
	private PageQuery findCriteriaFromCollection(Collection collection) {
		if (search_map.containsKey(collection)) {
			return null;
		}

		for (Object e : collection) {
			PageQuery pc = findCriteriaFromObject(e);
			if (pc != null) {
				search_map.put(collection, StringUtils.EMPTY);
				return pc;
			}
		}

		search_map.put(collection, StringUtils.EMPTY);
		return null;
	}

	/**
	 * In the Map to find whether contains <code>PaginationCriteria</code> objects.
	 *
	 * @param map paramter map.
	 * @return PaginationCriteria
	 */
	private PageQuery findCriteriaFromMap(Map map) {
		if (search_map.containsKey(map)) {
			return null;
		}

		for (Object value : map.values()) {
			PageQuery pc = findCriteriaFromObject(value);
			if (pc != null) {
				search_map.put(map, StringUtils.EMPTY);
				return pc;
			}
		}

		search_map.put(map, StringUtils.EMPTY);
		return null;
	}

	/** Single cased of associated object. */
	private static class App {
		/** Single pagings. */
		protected static Pagings pagings = new Pagings();
	}
}
