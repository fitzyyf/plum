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

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * <p>
 * .
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2012-04-20 上午7:34
 * @since JDK 1.5
 */
public class SuperDirective implements TemplateDirectiveModel {
    public final static String DIRECTIVE_NAME = "super";

    @SuppressWarnings("rawtypes")
	@Override
    public void execute(Environment env,
                        Map params, TemplateModel[] loopVars,
                        TemplateDirectiveBody body) throws TemplateException, IOException {

        OverrideDirective.TemplateDirectiveBodyOverrideWraper current =
                (OverrideDirective.TemplateDirectiveBodyOverrideWraper) env.getVariable(DirectiveUtils.OVERRIDE_CURRENT_NODE);
        if (current == null) {
            throw new TemplateException("<@super/> direction must be child of override", env);
        }
        TemplateDirectiveBody parent = current.parentBody;
        if (parent == null) {
            throw new TemplateException("not found parent for <@super/>", env);
        }
        parent.render(env.getOut());

    }
}
