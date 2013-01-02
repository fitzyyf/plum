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

import java.io.IOException;
import java.util.Map;

import freemarker.cache.TemplateCache;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * <p>
 * 定义模板继承的自定义指令.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2012-04-20 上午7:36
 * @since JDK 1.5
 */
public class ExtendsDirective implements TemplateDirectiveModel {
    /** 自定义指令名称 */
    public final static String DIRECTIVE_NAME = "extends";

    @SuppressWarnings("rawtypes")
	@Override
    public void execute(Environment env, Map params,
                        TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        String name = DirectiveUtils.getRequiredParam(params, "name");
        params.remove("name");
        String encoding = DirectiveUtils.getParam(params, "encoding", "UTF-8");
        String includeTemplateName = TemplateCache.getFullTemplatePath(env, getTemplatePath(env), name);
        Configuration configuration = env.getConfiguration();
        Template template = configuration.getTemplate(includeTemplateName, env.getLocale(), encoding, true);
        for (Object key : params.keySet()) {
            TemplateModel paramModule = new SimpleScalar(params.get(key).toString());
            env.setVariable(key.toString(),paramModule);
        }
        env.include(template);
    }

    /**
     * 取得模板路径的地址
     *
     * @param env Freemarker的运行环境
     * @return 模板路径地址
     */
    private String getTemplatePath(Environment env) {
        String templateName = env.getTemplate().getName();
        return templateName.lastIndexOf('/') == -1 ? "" : templateName.substring(0, templateName.lastIndexOf('/') + 1);
    }

}
