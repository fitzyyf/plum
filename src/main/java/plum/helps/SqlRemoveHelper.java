package plum.helps;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

/**
 * <p>
 * 去除SQL多余的无用的工具帮助类.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2013-01-02 12:37 PM
 * @since JDK 1.5
 */
public class SqlRemoveHelper {

	/** Order by 正则表达式 */
	public static final String ORDER_BY_REGEX = "order\\s*by[\\w|\\W|\\s|\\S]*";
	/** Xsql Order by 正则表达式 */
	public static final String XSQL_ORDER_BY_REGEX = "/~.*order\\s*by[\\w|\\W|\\s|\\S]*~/";
	/** From正则表达式 */
	public static final String FROM_REGEX = "\\sfrom\\s";

	private static int indexOfByRegex(String input, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		if (m.find()) {
			return m.start();
		}
		return -1;
	}

	/**
	 * 去除select 子句，未考虑union的情况
	 *
	 * @param sql sql
	 * @return 删除掉的selcet的子句
	 */
	public static String removeSelect(String sql) {
		Assert.hasText(sql);
		int beginPos = indexOfByRegex(sql.toLowerCase(), FROM_REGEX);
		Assert.isTrue(beginPos != -1, " sql : " + sql + " must has a keyword 'from'");
		return sql.substring(beginPos);
	}

	/**
	 * 去除orderby 子句
	 *
	 * @param sql sql
	 * @return 去掉order by sql
	 */
	public static String removeOrders(String sql) {
		Assert.hasText(sql);
		Pattern p = Pattern.compile(ORDER_BY_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public static String removeFetchKeyword(String sql) {
		return sql.replaceAll("(?i)fetch", "");
	}

	public static String removeXsqlBuilderOrders(String string) {
		Assert.hasText(string);
		Pattern p = Pattern.compile(XSQL_ORDER_BY_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(string);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return removeOrders(sb.toString());
	}
}
