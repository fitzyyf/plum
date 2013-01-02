package plum.helps;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 对象的相关操作工具帮助类.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2013-01-02 12:54 PM
 * @since JDK 1.5
 */
public class ObjectHelper {


	public static boolean isNullOrEmptyString(Object o) {
		if(o == null)
			return true;
		if(String.class.isAssignableFrom(o.getClass())) {
			String str = (String)o;
			if(str.length() == 0)
				return true;
		}
		return false;
	}

	/**
	 * 可以用于判断 Map,Collection,String,Array是否为空
	 * @param o
	 * @return
	 */
	@SuppressWarnings("all")
	public static boolean isEmpty(Object o)  {
		if(o == null) return true;

		if(String.class.isAssignableFrom(o.getClass())) {
			if(((String)o).length() == 0){
				return true;
			}
		} else if(Collection.class.isAssignableFrom(o.getClass())) {
			if(((Collection)o).isEmpty()){
				return true;
			}
		} else if(o.getClass().isArray()) {
			if(Array.getLength(o) == 0){
				return true;
			}
		} else if(Map.class.isAssignableFrom(o.getClass())) {
			if(((Map)o).isEmpty()){
				return true;
			}
		}else {
			return false;
		}

		return false;
	}

	/**
	 * 可以用于判断 Map,Collection,String,Array是否不为空
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object c) throws IllegalArgumentException{
		return !isEmpty(c);
	}
}
