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

package plum.freemarker.shiro.role;

import org.apache.shiro.subject.Subject;

/**
 * <p>
 * 多个角色的验证.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2012-10-27 10:35 AM
 * @since JDK 1.5
 */
public class HasAnyRolesTag extends RoleTag {
	// Delimeter that separates role names in tag attribute
	private static final String ROLE_NAMES_DELIMETER = ",";

	@Override
	protected boolean showTagBody(String roleName) {
		boolean hasAnyRole = false;
		Subject subject = getSubject();

		if (subject != null) {
			// Iterate through roles and check to see if the user has one of the roles
			for (String role : roleName.split(ROLE_NAMES_DELIMETER)) {
				if (subject.hasRole(role.trim())) {
					hasAnyRole = true;
					break;
				}
			}
		}

		return hasAnyRole;
	}
}
