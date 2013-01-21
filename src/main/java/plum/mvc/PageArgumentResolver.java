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

package plum.mvc;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import plum.mvc.annotation.PaginationParam;
import plum.utils.page.PageQuery;
import plum.utils.page.SortInfo;


/**
 * 分页条件参数解析器，适用于Bootstrap DataTables控件。
 * 将grid控件提交的pagesize、pageindex、order、sort参数转换为PaginationCriteria对象。
 * 这个类型需要在Spring-MVC配置文件中定义，参考jstd-samples->servlet-context.xml的配置。
 * <p/>
 * 使用示例
 * <pre>
 * &lt;mvc:annotation-driven>
 * 	 &lt;mvc:argument-resolvers>
 * 	 &lt;bean class="plum.mvc.PageArgumentResolver"/>
 * 	 &lt;/mvc:argument-resolvers>
 * 	&lt;/mvc:annotation-driven>
 * </pre>
 *
 * @author poplar.yfyang
 * @version 1.0 2013-01-01 9:59 PM
 * @since JDK 1.5
 */
public class PageArgumentResolver implements HandlerMethodArgumentResolver {

	/** Logger that is available to subclasses */
	private static final Log logger = LogFactory.getLog(PageArgumentResolver.class);

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(PaginationParam.class) != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
								  ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
								  WebDataBinderFactory binderFactory) throws Exception {

		final PaginationParam attr = parameter.getParameterAnnotation(PaginationParam.class);

		final Map<String, String[]> params = webRequest.getParameterMap();
		//转换KEY的大小写
		final Map<String, String[]> case_params = new CaseInsensitiveMap(params);
		String sPageIndex = case_params.get(attr.indexName())[0];
		String sPageSize = case_params.get(attr.sizeName())[0];
		//like sore=column desc,column2 asc
		String sort = case_params.get(attr.sortName())[0];


		final PageQuery pageQuery = new PageQuery();
		int pageSize;
		int pageIndex;
		try {
			pageIndex = Integer.valueOf(sPageIndex);
			pageSize = Integer.valueOf(sPageSize);
		} catch (NumberFormatException e) {
			logger.error("number paging is error!", e);
			if (!attr.required()) {
				return null;
			}
			pageSize = attr.pageSize();
			pageIndex = 0;
		} finally {
			try {
				case_params.clear();
			} catch (UnsupportedOperationException e) {
				logger.warn("param is not clear!");
			}
		}
		final List<SortInfo> sortFields = SortInfo.parseSortColumns(sort);

		pageQuery.setPageSize(pageSize);
		pageQuery.setSortInfoList(sortFields);
		pageQuery.setPage((pageIndex - 1) * pageSize);

		return pageQuery;

	}
}
