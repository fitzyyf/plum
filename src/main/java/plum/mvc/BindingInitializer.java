package plum.mvc;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;
import plum.mvc.types.DateTypeEditor;

/**
 * <p>
 * 数据绑定初始化类.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2013-01-02 12:07 PM
 * @since JDK 1.5
 */
public class BindingInitializer implements WebBindingInitializer {
	/** 初始化数据绑定 */
	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(Date.class, new DateTypeEditor());
	}
}