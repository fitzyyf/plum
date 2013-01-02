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

package plum.freemarker.shiro;

import freemarker.template.SimpleHash;
import plum.freemarker.shiro.auth.AuthenticatedTag;
import plum.freemarker.shiro.auth.GuestTag;
import plum.freemarker.shiro.auth.NotAuthenticatedTag;
import plum.freemarker.shiro.auth.PrincipalTag;
import plum.freemarker.shiro.auth.UserTag;
import plum.freemarker.shiro.permission.HasPermissionTag;
import plum.freemarker.shiro.permission.LacksPermissionTag;
import plum.freemarker.shiro.role.HasAnyRolesTag;
import plum.freemarker.shiro.role.HasRoleTag;
import plum.freemarker.shiro.role.LacksRoleTag;

/**
 * <p>
 * Shirio权限验证 Freemarker 标签.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2012-10-27 10:42 AM
 * @since JDK 1.5
 */
public class ShiroTags extends SimpleHash {

	/**
	 * Constructs an empty hash that uses the default wrapper set in
	 * {@link freemarker.template.WrappingTemplateModel#setDefaultObjectWrapper(freemarker.template.ObjectWrapper)}.
	 */
	public ShiroTags() {
		put("authenticated", new AuthenticatedTag());
		put("guest", new GuestTag());
		put("hasAnyRoles", new HasAnyRolesTag());
		put("hasPermission", new HasPermissionTag());
		put("hasRole", new HasRoleTag());
		put("lacksPermission", new LacksPermissionTag());
		put("lacksRole", new LacksRoleTag());
		put("notAuthenticated", new NotAuthenticatedTag());
		put("principal", new PrincipalTag());
		put("user", new UserTag());
	}
}
