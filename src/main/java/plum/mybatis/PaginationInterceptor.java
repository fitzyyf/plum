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

package plum.mybatis;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.xml.bind.PropertyException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import plum.utils.page.PageQuery;
import plum.utils.page.Pager;
import plum.utils.reflex.Reflections;

/**
 * <p>
 * Mybatis数据库分页插件.
 * 拦截StatementHandler的prepare方法
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2011-11-18 下午12:31
 * @since JDK 1.5
 */
@Intercepts({@Signature(
		type = Executor.class,
		method = "query",
		args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PaginationInterceptor implements Interceptor, Serializable {
	/** if true paging interceptor. */
	private static final ThreadLocal<Boolean> PAGINATION_QUERY = new ThreadLocal<Boolean>();
	/** storage total page */
	private static final ThreadLocal<Pager> PAGINATION_COUNT = new ThreadLocal<Pager>();
	/** paging criteria */
	private static final ThreadLocal<PageQuery> PAGINATION_CRITERIA_THREAD_LOCAL = new ThreadLocal<PageQuery>();
	/** serial Version */
	private static final long serialVersionUID = -6075937069117597841L;
	/** logging */
	private static final Log LOG = LogFactory.getLog(PaginationInterceptor.class);
	/** mapped statement parameter index. */
	private static final int MAPPED_STATEMENT_INDEX = 0;
	/** parameter index. */
	private static final int PARAMETER_INDEX = 1;
	/** parameter index. */
	private static final int ROWBOUNDS_INDEX = 2;
	/** ResultHandler index. */
	private static final int RESULT_HANDLER_INDEX = 3;
	/** sql id , in the mapper xml file. */
	private static String _sql_regex = "[*]";
	/** DataBase dialect. */
	protected Dialect _dialect;

	/** clear total context. */
	public static void clean() {
		PAGINATION_COUNT.remove();
		PAGINATION_QUERY.remove();
	}

	/**
	 * get total context is page.
	 *
	 * @return total.if total is null ,return -1.
	 */
	public static Pager getTotal() {
		return PAGINATION_COUNT.get();
	}

	/**
	 * Set the paging information,to RowBuounds.
	 *
	 * @param rowBounds rowBounds.
	 * @return rowBounds.
	 */
	private static RowBounds offset_paging(RowBounds rowBounds) {
		// rowBuounds has offset.
		if (rowBounds.getOffset() == RowBounds.NO_ROW_OFFSET) {
			final PageQuery paginationCriteria = PAGINATION_CRITERIA_THREAD_LOCAL.get();
			if (paginationCriteria != null) {
				return new RowBounds(paginationCriteria.getPage(), paginationCriteria.getPageSize());
			}
		}
		return rowBounds;
	}

	/**
	 * perform paging intercetion.
	 *
	 * @param queryArgs Executor.query params.
	 */
	private void processIntercept(final Object[] queryArgs) {
		//queryArgs = query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler)
		final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];

		final Object parameter = queryArgs[PARAMETER_INDEX];
		//the need for paging intercept.
		boolean interceptor = ms.getId().matches(_sql_regex);
		//obtain paging information.
		final PageQuery pageQuery = interceptor ? PagingParametersFinder.getInstance().findCriteria(parameter) :
				new PageQuery(PageQuery.DEFAULT_PAGE_SIZE);
		if (interceptor) {
			PAGINATION_CRITERIA_THREAD_LOCAL.set(pageQuery);
		}
		final RowBounds rowBounds = (interceptor) ?
				offset_paging((RowBounds) queryArgs[ROWBOUNDS_INDEX]) : (RowBounds) queryArgs[ROWBOUNDS_INDEX];
		int offset = rowBounds.getOffset();
		int limit = rowBounds.getLimit();

		if (_dialect.supportsLimit() && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {
			final BoundSql boundSql = ms.getBoundSql(parameter);
			String sql = boundSql.getSql().trim();
			if (LOG.isDebugEnabled()) {
				LOG.debug("Pagination sql is <" + sql + ">");
			}
			//implementation of the access to the total number of SQL,to obtain  the total number and stored in the thread location

			Connection connection = null;
			try {
				//get connection
				connection = ms.getConfiguration().getEnvironment().getDataSource().getConnection();
				int count = SQLHelp.getCount(sql, connection, ms, parameter, boundSql, _dialect);
				final Pager pager = new Pager(pageQuery.getPage(), pageQuery.getPageSize(), count);
				PAGINATION_COUNT.set(pager);
			} catch (SQLException e) {
				LOG.error("The total number of access to the database failure.", e);
				PAGINATION_COUNT.set(null);
			} finally {
				try {
					if (connection != null && !connection.isClosed()) {
						connection.close();
					}
				} catch (SQLException e) {
					LOG.error("Close the database connection error.", e);
				}
			}
			if (_dialect.supportsLimit()) {
				sql = _dialect.getLimitString(sql, offset, limit);
				offset = RowBounds.NO_ROW_OFFSET;
			} else {
				sql = _dialect.getLimitString(sql, 0, limit);
			}
			limit = RowBounds.NO_ROW_LIMIT;

			queryArgs[ROWBOUNDS_INDEX] = new RowBounds(offset, limit);

			BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql);

			MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
			queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
		}
	}

	private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql,
									  String sql) {
		BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
		for (ParameterMapping mapping : boundSql.getParameterMappings()) {
			String prop = mapping.getProperty();
			if (boundSql.hasAdditionalParameter(prop)) {
				newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
			}
		}
		return newBoundSql;
	}

	//see: MapperBuilderAssistant
	private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
		MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		String[] keyProperties = ms.getKeyProperties();
		builder.keyProperty(keyProperties == null ? null : keyProperties[0]);

		//setStatementTimeout()
		builder.timeout(ms.getTimeout());

		//setStatementResultMap()
		builder.parameterMap(ms.getParameterMap());

		//setStatementResultMap()
		builder.resultMaps(ms.getResultMaps());
		builder.resultSetType(ms.getResultSetType());

		//setStatementCache()
		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());

		return builder.build();
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		processIntercept(invocation.getArgs());
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object o) {

		if (Executor.class.isAssignableFrom(o.getClass())) {
			return Plugin.wrap(new PaginationExecutor((Executor) o), this);
		}
		return Plugin.wrap(o, this);

	}

	/**
	 * 设置属性，支持自定义方言类和制定数据库的方式
	 * <p>
	 * <code>dialectClass</code>,自定义方言类。可以不配置这项
	 * <ode>dbms</ode> 数据库类型，插件支持的数据库
	 * <code>sqlRegex</code> 需要拦截的SQL ID
	 * </p>
	 * 如果同时配置了<code>dialectClass</code>和<code>dbms</code>,则以<code>dbms</code>为主
	 *
	 * @param p 属性
	 */
	@Override
	public void setProperties(Properties p) {
		String dialectClass = p.getProperty("dialectClass");
		DBMS dbms;
		if (!StringUtils.isEmpty(dialectClass)) {
			Dialect dialect1 = (Dialect) Reflections.instance(dialectClass);
			if (dialect1 == null) {
				throw new RuntimeException(new ClassNotFoundException("dialectClass is not found!"));
			}
			DialectClient.putEx(dialect1);
			dbms = DBMS.EX;
		}

		String dialect = p.getProperty("dbms");
		if (StringUtils.isEmpty(dialect)) {
			try {
				throw new PropertyException("dialect property is not found!");
			} catch (PropertyException e) {
				LOG.error("", e);
			}
		}
		//DBMS_THREAD_LOCAL.set(DBMS.valueOf(dialect.toUpperCase()));
		dbms = DBMS.valueOf(dialect.toUpperCase());
		if (dbms == null) {
			try {
				throw new PropertyException("插件无法支持该数据库");
			} catch (PropertyException e) {
				LOG.error("", e);
			}
		}
		_dialect = DialectClient.getDbmsDialect(dbms);

		String sql_regex = p.getProperty("sqlRegex");
		if (!StringUtils.isEmpty(sql_regex)) {
			_sql_regex = sql_regex;
		}
		clean();
	}

	public static class BoundSqlSqlSource implements SqlSource {
		BoundSql boundSql;

		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}

		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}
}
