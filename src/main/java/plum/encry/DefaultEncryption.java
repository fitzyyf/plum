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

package plum.encry;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * <p>
 * 系统默认加密方案.
 * </p>
 * 加密方案描述:
 * <ul>
 * <li>先对明文密码进行Base64转码</li>
 * <li>再在Base64码的基础上增加一个混淆码,<code>默认混淆码为 plum</code></li>
 * <li>对增加混淆码后的字符串进行sha_1的方式加密得到字符串</li>
 * </ul>
 *
 * @author poplar.yfyang
 * @version 1.0 2013-01-02 10:16 AM
 * @since JDK 1.5
 */
public class DefaultEncryption implements Encryption {
	/** 默认混淆码，plum */
	private static final String DEFAULT_CONFUSE = "plum";
	/** 混淆码 */
	private String confuse = DEFAULT_CONFUSE;

	/**
	 * sha-1 encrption base64 chars.
	 *
	 * @param base64 base64 char
	 * @return sha-1
	 */
	private static String sha_1(String base64) {
		return DigestUtils.sha1Hex(base64);
	}

	/**
	 * base64 string.
	 *
	 * @param password password
	 * @return base64 string
	 */
	private static String base64(String password) {
		return Base64.encodeBase64String(password.getBytes());
	}

	/**
	 * 可以通过注入的方式改变混淆码，加密使用
	 *
	 * @param confuse 混淆码
	 */
	public void setConfuse(String confuse) {
		this.confuse = confuse;
	}

	/**
	 * encryption password
	 *
	 * @param password password
	 * @return encryption string
	 */
	@Override
	public String encryption(String password) {
		//plaintext with base64
		String base64 = base64(password);
		//add  encryption be confused string.
		base64 += confuse;
		//sha_1 value
		return sha_1(base64);
	}
}
