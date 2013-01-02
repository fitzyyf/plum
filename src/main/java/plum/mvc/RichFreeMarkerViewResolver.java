package plum.mvc;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

/**
 * <p>
 * ViewResolver for RichFreeMarkerView
 * <p/>
 * Override buildView, if viewName start with / , then ignore prefix.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2013-01-02 12:02 PM
 * @since JDK 1.5
 */
public class RichFreeMarkerViewResolver extends AbstractTemplateViewResolver {

	/** Set default viewClass */
	public RichFreeMarkerViewResolver() {
		setViewClass(RichFreeMarkerView.class);
	}

	/** if viewName start with / , then ignore prefix. */
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		AbstractUrlBasedView view = super.buildView(viewName);
		// start with / ignore prefix
		if (viewName.startsWith("/")) {
			view.setUrl(viewName + getSuffix());
		}
		return view;
	}
}
