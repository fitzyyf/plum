package plum.encry;

/**
 * <p>
 * 加密用户密码接口.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2013-01-02 11:32 AM
 * @since JDK 1.5
 */
public interface Encryption {

	/**
	 * 加密密码
	 * @param password 需要加密的密码
	 * @return 加密后的字符串
	 */
	String encryption(String password);
}
