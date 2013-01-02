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

import plum.mvc.annotation.PaginationParam;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
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
	/** 页码Request中的值 */
	private String pageIndexName = "pageindex";
	/** 每页显示数量的Request Name值 */
	private String pageSizeName = "pagesize";
	/** 排序字段的Request Name值 */
	private String sortName = "sort";

	/**
	 * 注入 页码Request中的值
	 *
	 * @param pageIndexName 页码Request中的值
	 */
	public void setPageIndexName(String pageIndexName) {
		this.pageIndexName = pageIndexName;
	}

	/**
	 * 注入 每页显示数量的Request Name值
	 *
	 * @param pageSizeName 每页显示数量的Request Name值
	 */
	public void setPageSizeName(String pageSizeName) {
		this.pageSizeName = pageSizeName;
	}

	/**
	 * 注入 排序字段的Request Name值
	 *
	 * @param sortName 排序字段的Request Name值
	 */
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

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
		String sPageIndex = case_params.get(pageIndexName)[0];
		String sPageSize = case_params.get(pageSizeName)[0];
		//like sore=column desc,column2 asc
		String sort = case_params.get(sortName)[0];

//		WebUtils.getTargetPage(request, "_target", currentPage);

		final PageQuery pageQuery = new PageQuery();
		try {
			int pageIndex = Integer.valueOf(sPageIndex);
			int pageSize = Integer.valueOf(sPageSize);
			List<SortInfo> sortInfos = SortInfo.parseSortColumns(sort);

			pageQuery.setPageSize(pageSize);
			pageQuery.setSortInfoList(sortInfos);
			pageQuery.setPage((pageIndex - 1) * pageSize);

			return pageQuery;
		} catch (NumberFormatException e) {
			logger.error("number paging is error!", e);
			if (!attr.required()) {
				return null;
			}
			pageQuery.setPageSize(PageQuery.DEFAULT_PAGE_SIZE);
			pageQuery.setPage(0);
			return pageQuery;
		} finally {
			params.clear();
			case_params.clear();
		}
	}
}
