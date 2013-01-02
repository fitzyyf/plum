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
import java.util.HashMap;
import java.util.Map;

import plum.mybatis.db.DB2Dialect;
import plum.mybatis.db.H2Dialect;
import plum.mybatis.db.HSQLDialect;
import plum.mybatis.db.MySQLDialect;
import plum.mybatis.db.OracleDialect;
import plum.mybatis.db.PostgreSQLDialect;
import plum.mybatis.db.SQLServer2005Dialect;
import plum.mybatis.db.SQLServerDialect;
import plum.mybatis.db.SybaseDialect;

/**
 * <p>
 * 数据库分页方言获取类.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2011-11-18 下午2:54
 * @since JDK 1.5
 */
public class DialectClient implements Serializable {
	private static final long serialVersionUID = 8107330250767760951L;
	private static final Map<DBMS, Dialect> DBMS_DIALECT = new HashMap<DBMS, Dialect>();

	/**
	 * 根据数据库名称获取数据库分页查询的方言实现。
	 *
	 * @param dbms 数据库名称
	 * @return 数据库分页方言实现
	 */
	public static Dialect getDbmsDialect(DBMS dbms) {
		if (DBMS_DIALECT.containsKey(dbms)) {
			return DBMS_DIALECT.get(dbms);
		}
		Dialect dialect = createDbmsDialect(dbms);
		DBMS_DIALECT.put(dbms, dialect);
		return dialect;
	}

	/**
	 * 插入自定义方言的实例
	 *
	 * @param exDialect 方言实现
	 */
	public static void putEx(Dialect exDialect) {
		DBMS_DIALECT.put(DBMS.EX, exDialect);
	}

	/**
	 * 创建数据库方言
	 *
	 * @param dbms 数据库
	 * @return 数据库
	 */
	private static Dialect createDbmsDialect(DBMS dbms) {
		switch (dbms) {
			case MYSQL:
				return new MySQLDialect();
			case ORACLE:
				return new OracleDialect();
			case DB2:
				return new DB2Dialect();
			case POSTGRE:
				return new PostgreSQLDialect();
			case SQLSERVER:
				return new SQLServerDialect();
			case SQLSERVER2005:
				return new SQLServer2005Dialect();
			case SYBASE:
				return new SybaseDialect();
			case H2:
				return new H2Dialect();
			case HSQL:
				return new HSQLDialect();
			default:
				throw new UnsupportedOperationException("Empty dbms dialect");
		}
	}


}
