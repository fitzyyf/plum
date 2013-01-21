package reflectasm;

import domain.Account;
import org.junit.Test;
import plum.reflectasm.FieldAccess;

import static junit.framework.Assert.assertEquals;

/**
 * <p>
 * .
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2013-01-02 3:05 PM
 * @since JDK 1.5
 */
public class ReflectAsmTest {

	@Test
	public void test_get() throws Exception {
		Account account = new Account();
		FieldAccess access = FieldAccess.get(Account.class);
		access.set(account, "name", "first");
		assertEquals("first", access.get(account, "name"));
	}
}
