package plum.utils.holder;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 用于持有Spring Validator,使调用Validator可以当静态方法使用.
 * <p/>
 * <pre>
 * static 方法调用:
 * SpringValidatorHolder.validate(object);
 * </pre>
 * <pre>
 * spring配置:
 * &lt;bean class="plum.utils.holder.SpringValidatorHolder">
 * 	 &lt;preperty name="validator" ref="validator"/>
 * &lt;/bean>
 * </pre>
 *
 * @author poplar.yfyang
 * @version 1.0 2013-01-02 1:17 PM
 * @since JDK 1.5
 */
public class SpringValidatorHolder implements InitializingBean {
	private static Validator validator;

	private static Validator getRequiredValidator() {
		if (validator == null)
			throw new IllegalStateException("'validator' property is null,SpringValidatorHolder not yet init.");
		return validator;
	}

	public static Validator getValidator() {
		return getRequiredValidator();
	}

	public void setValidator(Validator v) {
		if (validator != null) {
			throw new IllegalStateException("SpringValidatorHolder already holded 'validator'");
		}
		validator = v;
	}

	public static boolean supports(Class<?> type) {
		return getRequiredValidator().supports(type);
	}

	public static void validate(Object object, Errors errors) {
		getRequiredValidator().validate(object, errors);
	}

	/**
	 * 验证失败将抛出异常
	 *
	 * @param object 验证的对象
	 * @throws BindException
	 */
	public static void validate(Object object) throws BindException {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(object, object.getClass().getSimpleName());
		getRequiredValidator().validate(object, errors);
		if (errors.hasErrors()) {
			throw new BindException(errors);
		}
	}

	public static void cleanHolder() {
		validator = null;
	}

	public void afterPropertiesSet() throws Exception {
		if (validator == null) throw new BeanCreationException("not found spring 'validator' for SpringValidatorHolder ");
	}
}
