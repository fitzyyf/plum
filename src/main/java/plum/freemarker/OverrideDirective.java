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
import java.io.Writer;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * <p>
 * Freemarker的覆盖自定义标记，用来覆盖模板中的指定区域.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2012-04-20 上午7:32
 * @since JDK 1.5
 */
public class OverrideDirective implements TemplateDirectiveModel {
    /** 覆盖模板的自定义指令名称 */
    public final static String DIRECTIVE_NAME = "override";

    @SuppressWarnings("rawtypes")
	@Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        String name = DirectiveUtils.getRequiredParam(params, "name");
        String overrideVariableName = DirectiveUtils.getOverrideVariableName(name);

        TemplateDirectiveBodyOverrideWraper override = DirectiveUtils.getOverrideBody(env, name);
        TemplateDirectiveBodyOverrideWraper current = new TemplateDirectiveBodyOverrideWraper(body, env);
        if (override == null) {
            env.setVariable(overrideVariableName, current);
        } else {
            DirectiveUtils.setTopBodyForParentBody(current, override);
        }
    }

    /** 重写指令的内容渲染包装器 */
    static class TemplateDirectiveBodyOverrideWraper implements TemplateDirectiveBody, TemplateModel {
        /** 当前内容渲染 */
        private final TemplateDirectiveBody body;
        /** 内容渲染包装器 */
        public TemplateDirectiveBodyOverrideWraper parentBody;
        /** 运行环境 */
        public final Environment env;

        /**
         * 构造一个包装器
         *
         * @param body 内容渲染
         * @param env  运行环境
         */
        public TemplateDirectiveBodyOverrideWraper(TemplateDirectiveBody body,
                                                   Environment env) {
            super();
            this.body = body;
            this.env = env;
        }

        @Override
        public void render(Writer out) throws TemplateException, IOException {
            if (body == null) return;
            TemplateDirectiveBodyOverrideWraper preOverridy = (TemplateDirectiveBodyOverrideWraper) env.getVariable(DirectiveUtils.OVERRIDE_CURRENT_NODE);
            try {
                env.setVariable(DirectiveUtils.OVERRIDE_CURRENT_NODE, this);
                body.render(out);
            } finally {
                env.setVariable(DirectiveUtils.OVERRIDE_CURRENT_NODE, preOverridy);
            }
        }
    }
}
