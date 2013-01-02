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
package plum.freemarker;

import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * Freemarker 的工具类.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2012-04-20 上午7:30
 * @since JDK 1.5
 */
public class DirectiveUtils {

	/** override的标记 */
	public static final String BLOCK = "__ftl_override__";
	public static final String OVERRIDE_CURRENT_NODE = "__ftl_override_current_node";

	/**
	 * 返回override的标记与对应的名称信息
	 *
	 * @param name 名称
	 * @return 标记名称信息
	 */
	public static String getOverrideVariableName(String name) {
		return BLOCK + name;
	}

	public static void exposeIflytekMacros(Configuration conf) {
		conf.setSharedVariable(BlockDirective.DIRECTIVE_NAME, new BlockDirective());
		conf.setSharedVariable(ExtendsDirective.DIRECTIVE_NAME, new ExtendsDirective());
		conf.setSharedVariable(OverrideDirective.DIRECTIVE_NAME, new OverrideDirective());
		conf.setSharedVariable(SuperDirective.DIRECTIVE_NAME, new SuperDirective());
	}

	/**
	 * 取得参数<code>params</code>中必须存在的指定Key的值，如果不存在，抛出异常
	 *
	 * @param params 参数Map
	 * @param key    必须存在的Key
	 * @return 对应key的值
	 * @throws freemarker.template.TemplateException 异常，如果key不存在params中则会出现异常
	 */
	@SuppressWarnings("rawtypes")
	static String getRequiredParam(Map params, String key) throws TemplateException {
		Object value = params.get(key);
		if (value == null || StringUtils.isEmpty(value.toString())) {
			throw new TemplateModelException("not found required parameter:" + key + " for directive");
		}
		return value.toString();
	}

	/**
	 * 取得<code>params</code>中指定Key的值，如果不存在返回默认信息
	 *
	 * @param params       参数Map
	 * @param key          key信息
	 * @param defaultValue 默认值
	 * @return 对应Key的值，不存在返回默认
	 */
	@SuppressWarnings("rawtypes")
	static String getParam(Map params, String key, String defaultValue) {
		Object value = params.get(key);
		return value == null ? defaultValue : value.toString();
	}

	/**
	 * 从Freemarker的运行环境中，取得指定名称的覆盖内容渲染器
	 *
	 * @param env  运行环境
	 * @param name 指定名称
	 * @return 覆盖内容渲染器
	 * @throws freemarker.template.TemplateModelException 模板模型异常
	 */
	static OverrideDirective.TemplateDirectiveBodyOverrideWraper getOverrideBody(Environment env, String name)
			throws TemplateModelException {
		return (OverrideDirective.TemplateDirectiveBodyOverrideWraper)
				env.getVariable(DirectiveUtils.getOverrideVariableName(name));
	}

	/**
	 * 对指定的Body的渲染器，指定其父亲的内容区域
	 *
	 * @param topBody      内容渲染器
	 * @param overrideBody 需要覆盖的内容渲染器
	 */
	static void setTopBodyForParentBody(OverrideDirective.TemplateDirectiveBodyOverrideWraper topBody,
										OverrideDirective.TemplateDirectiveBodyOverrideWraper overrideBody) {
		OverrideDirective.TemplateDirectiveBodyOverrideWraper parent = overrideBody;
		while (parent.parentBody != null) {
			parent = parent.parentBody;
		}
		parent.parentBody = topBody;
	}
}
