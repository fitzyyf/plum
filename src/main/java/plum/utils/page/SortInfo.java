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

package plum.utils.page;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

/**
 * 排序的列
 *
 * @author poplar.yfyang
 * @version 1.0 2013-01-01 9:59 PM
 * @since JDK 1.5
 */
public class SortInfo implements Serializable {

	private static final long serialVersionUID = 6959974032209696722L;
	/** 排序的列，以,号隔开表示多个 */
	private String columnName;
	/** 排序字段的映射关系，倒序还是顺序 */
	private SortOrder sortOrder;

	public SortInfo() {
	}

	public SortInfo(String columnName, SortOrder sortOrder) {
		super();
		this.columnName = columnName;
		this.sortOrder = sortOrder;
	}

	public static List<SortInfo> parseSortColumns(String sortColumns) {
		if (sortColumns == null) {
			return Lists.newArrayList();
		}

		List<SortInfo> results = Lists.newArrayList();
		String[] sortSegments = sortColumns.trim().split(",");
		for (String sortSegment : sortSegments) {
			String[] array = sortSegment.split("\\s+");

			SortInfo sortInfo = new SortInfo();
			sortInfo.setColumnName(array[0]);
			sortInfo.setSortOrder(array.length == 2 ? (StringUtils.equalsIgnoreCase(array[1], "ASC") ? SortOrder.ASC : SortOrder.DESC) : null);
			results.add(sortInfo);
		}
		return results;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String toString() {
		return columnName + (sortOrder == null ? "" : " " + sortOrder);
	}

	/**
	 * 排序顺序
	 *
	 * @author poplar.yfyang
	 */
	public enum SortOrder {
		/** 正序 */
		ASC,

		/** 倒序 */
		DESC
	}
}
