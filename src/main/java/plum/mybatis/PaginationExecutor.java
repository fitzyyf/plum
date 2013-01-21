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

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;
import plum.utils.page.PageList;
import plum.utils.page.Pager;


/**
 * Paging executor.
 * Dependence of mybatis agent.
 *
 * @author poplar.yfyang
 * @author htlu
 * @version 1.0 2012-09-09 7:22 PM
 * @since JDK 1.5
 */
public class PaginationExecutor implements Executor {

	/** logging */
	private static final Log LOG = LogFactory.getLog(PaginationExecutor.class);
	/** mybatis executor interface */
	private final Executor executor;

	/**
	 * Paging Constructor.
	 *
	 * @param executor Trim executor.
	 */
	public PaginationExecutor(Executor executor) {
		this.executor = executor;
	}



	@Override
	public int update(MappedStatement ms, Object parameter) throws SQLException {

		return executor.update(ms, parameter);
	}

	@Override
	public <E> List<E> query(MappedStatement ms, Object parameter,
							 RowBounds rowBounds, ResultHandler resultHandler,
							 CacheKey cacheKey, BoundSql boundSql) throws SQLException {

		final List<E> rows = executor.query(ms, parameter, rowBounds, resultHandler);
		Pager total = PaginationInterceptor.getTotal();
		try {
			if (total != null) {
				final PageList<E> result = new PageList<E>(rows, total);
				// if the current of the executor is for CachingExecutor
				final Cache cache = ms.getCache();
				System.out.println(cache);
				// Determine whether the current query cache.
				if (executor.getClass().isAssignableFrom(CachingExecutor.class) && cache != null) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("cache executor the cache's kye  is " + cacheKey);
					}

					cache.putObject(cacheKey, result);
				}
				return result;
			} else {
				return rows;
			}
		} finally {
			PaginationInterceptor.clean();
		}
	}

	@Override
	public <E> List<E> query(MappedStatement ms, Object parameter,
							 RowBounds rowBounds, ResultHandler resultHandler)
			throws SQLException {

		final List<E> rows = executor.query(ms, parameter, rowBounds, resultHandler);
		Pager total = PaginationInterceptor.getTotal();
		try {
			if (total != null) {
				final PageList<E> result = new PageList<E>(rows, total);
				// if the current of the executor is for CachingExecutor
				final Cache cache = ms.getCache();
				System.out.println(cache);
				// Determine whether the current query cache.
				if (executor.getClass().isAssignableFrom(CachingExecutor.class) && cache != null) {
					BoundSql boundSql = ms.getBoundSql(parameter);
					final CacheKey cacheKey = createCacheKey(ms, parameter, rowBounds, boundSql);
					if (LOG.isDebugEnabled()) {
						LOG.debug("cache executor the cache's kye  is " + cacheKey);
					}

					cache.putObject(cacheKey, result);
				}
				return result;
			} else {
				return rows;
			}
		} finally {
			PaginationInterceptor.clean();
		}

	}

	@Override
	public List<BatchResult> flushStatements() throws SQLException {
		return executor.flushStatements();
	}

	@Override
	public void commit(boolean required) throws SQLException {
		executor.commit(required);
	}

	@Override
	public void rollback(boolean required) throws SQLException {
		executor.rollback(required);
	}

	@Override
	public CacheKey createCacheKey(MappedStatement ms, Object parameterObject,
								   RowBounds rowBounds, BoundSql boundSql) {
		return executor.createCacheKey(ms, parameterObject, rowBounds, boundSql);
	}

	@Override
	public boolean isCached(MappedStatement ms, CacheKey key) {
		return executor.isCached(ms, key);
	}

	@Override
	public void clearLocalCache() {
		executor.clearLocalCache();
	}

	@Override
	public void deferLoad(MappedStatement ms, MetaObject resultObject,
						  String property, CacheKey key) {
		executor.deferLoad(ms, resultObject, property, key);
	}

	@Override
	public Transaction getTransaction() {
		return executor.getTransaction();
	}

	@Override
	public void close(boolean forceRollback) {
		//clear
		PaginationInterceptor.clean();
		executor.close(forceRollback);
	}

	@Override
	public boolean isClosed() {
		return executor.isClosed();
	}

}
