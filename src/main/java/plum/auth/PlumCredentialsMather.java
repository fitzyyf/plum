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

package plum.auth;

import com.google.common.base.Objects;
import plum.encry.DefaultEncryption;
import plum.encry.Encryption;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * <p>
 * 身份密码校验规则.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2013-01-01 10:12 PM
 * @since JDK 1.5
 */
public class PlumCredentialsMather extends SimpleCredentialsMatcher {


	/** 混淆密码 加密方案 */
	private final Encryption encryption;

	/** 默认身份加密校验方案 */
	public PlumCredentialsMather() {
		this.encryption = App.DEFAULT_ENCRYPTION;
	}

	/**
	 * 给定加密方案进行密码校验
	 *
	 * @param encryption 加密方案，如果设定为<code>null</code>,则采用默认的加密方案
	 */
	public PlumCredentialsMather(Encryption encryption) {
		this.encryption = encryption == null ? App.DEFAULT_ENCRYPTION : encryption;
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		Object tokenCredential = encryption.encryption(toString(token.getCredentials()));
		Object userPassword = this.getCredentials(info);
		return Objects.equal(tokenCredential, userPassword);
	}

	/** 内部类，初始化默认方案 */
	protected static class App {
		/** 默认加密方案 */
		private static final Encryption DEFAULT_ENCRYPTION = new DefaultEncryption();
	}
}
