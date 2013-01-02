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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * <p>
 * 异常处理信息，支持Ajax提示.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2013-01-01 10:02 PM
 * @since JDK 1.5
 */
public class ExceptionHandler extends SimpleMappingExceptionResolver {
	/** ajax 异常view */
	private String ajaxErrorView;
	/** 默认的ajax提示信息 */
	private String ajaxDefaultErrorMessage = "An error has occurred";
	/** 是否提示信息 */
	private boolean ajaxShowTechMessage = true;

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		logger.error("exception!", ex);
		if (is_ajax(request)) {
			String exceptionMessage = ajaxDefaultErrorMessage;
			if (ajaxShowTechMessage)
				exceptionMessage += "\n" + get_exception_message(ex);
			ModelAndView m = new ModelAndView(ajaxErrorView);
			m.addObject("exceptionMessage", exceptionMessage);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return m;
		} else {
			return super.resolveException(request, response, handler, ex);
		}
	}

	/**
	 * 获取异常的堆栈信息，并分行显示，查看比较友好
	 *
	 * @param e 异常信息
	 * @return 友好的提示信息
	 */
	private String get_exception_message(Throwable e) {
		String message = "";
		while (e != null) {
			message += e.getMessage() + "\n";
			e = e.getCause();
		}
		return message;
	}

	/**
	 * 根据请求消息头，判断是否是ajax请求
	 *
	 * @param request 请求
	 * @return ture，是ajax请求
	 */
	private boolean is_ajax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

	/**
	 * ajax default message.
	 *
	 * @param ajaxDefaultErrorMessage default message.
	 */
	public void setAjaxDefaultErrorMessage(String ajaxDefaultErrorMessage) {
		this.ajaxDefaultErrorMessage = ajaxDefaultErrorMessage;
	}

	/**
	 * ajax error view.
	 *
	 * @param ajaxErrorView ajax view
	 */
	public void setAjaxErrorView(String ajaxErrorView) {
		this.ajaxErrorView = ajaxErrorView;
	}

	/**
	 * is show tech message.
	 *
	 * @param ajaxShowTechMessage show message.
	 */
	public void setAjaxShowTechMessage(boolean ajaxShowTechMessage) {
		this.ajaxShowTechMessage = ajaxShowTechMessage;
	}
}