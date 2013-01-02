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

package plum.freemarker.shiro.permission;

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;
import plum.freemarker.shiro.auth.SecureTag;

/**
 * <p>
 * .
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2012-10-27 10:37 AM
 * @since JDK 1.5
 */
public abstract class PermissionTag extends SecureTag {

	String getName(Map params) {
		return getParam(params, "name");
	}

	@Override
	protected void verifyParameters(Map params) throws TemplateModelException {
		String permission = getName(params);

		if (permission == null || permission.length() == 0) {
			throw new TemplateModelException("The 'name' tag attribute must be set.");
		}
	}

	@Override
	public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
		String p = getName(params);

		boolean show = showTagBody(p);
		if (show) {
			renderBody(env, body);
		}
	}

	protected boolean isPermitted(String p) {
		return getSubject() != null && getSubject().isPermitted(p);
	}

	protected abstract boolean showTagBody(String p);
}