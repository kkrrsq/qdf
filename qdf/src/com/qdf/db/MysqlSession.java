package com.qdf.db;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.qdf.core.Qdf;
import com.qdf.core.TypeConverter;
import com.qdf.util.LogUtil;
import com.qdf.util.StringUtil;

public class MysqlSession implements Session {

	private static final MysqlSession _me = new MysqlSession();

	public static MysqlSession me() {
		return _me;
	}

	private MysqlSession() {
	}

	public int save(Object obj) {

		Class<?> clazz = obj.getClass();

		Method[] methods = clazz.getMethods();

		Map<String, Object> map = new HashMap<>();

		for (Method method : methods) {

			if (method.getName().startsWith("get") && method.getName().length() > 3
					&& method.getParameters().length == 0 && !method.getName().equals("getClass")) {

				try {

					String field = StringUtil.firstCharToLowerCase(method.getName().substring(3));
					Object value = method.invoke(obj);
					map.put(field, value);

				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			}

		}

		StringBuffer filesString = new StringBuffer("(");
		StringBuffer valuesString = new StringBuffer("(");

		for (String key : map.keySet()) {
			filesString.append(key).append(",");
			valuesString.append("?").append(",");
		}

		filesString.deleteCharAt(filesString.length() - 1);
		valuesString.deleteCharAt(valuesString.length() - 1);

		filesString.append(")");
		valuesString.append(")");

		String tableName = Qdf.me().getTable().getTableMap().get(clazz);

		String sql = "insert into " + tableName + filesString + " values" + valuesString;

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = Qdf.me().getPool().getConnection();
			ps = conn.prepareStatement(sql);
			int index = 1;
			for (String key : map.keySet()) {
				ps.setObject(index, map.get(key));
				index++;
			}
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (null != ps) {
					ps.close();
				}
				if (null != conn && conn.getAutoCommit()) {
					conn.close();
				}
			} catch (Exception e2) {
				LogUtil.error(e2.getMessage(),e2);
			}
		}

	}

	public int delete(Object obj) {

		Class<?> clazz = obj.getClass();
		String tableName = Qdf.me().getTable().getTableMap().get(clazz);
		String keyName = Qdf.me().getTable().getPkMap().get(clazz);

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = Qdf.me().getPool().getConnection();
			Object id = clazz.getMethod("get" + StringUtil.firstCharToUpperCase(keyName)).invoke(obj);
			String sql = "delete from " + tableName + " where " + keyName + "= ?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, id);
			return ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (null != ps) {
					ps.close();
				}
				if (null != conn && conn.getAutoCommit()) {
					conn.close();
				}
			} catch (Exception e2) {
				LogUtil.error(e2.getMessage(),e2);
			}
		}

	}

	public int update(Object obj) {

		Class<?> clazz = obj.getClass();

		String tableName = Qdf.me().getTable().getTableMap().get(clazz);
		String keyName = Qdf.me().getTable().getPkMap().get(clazz);
		Method[] methods = clazz.getMethods();

		Map<String, Object> map = new HashMap<>();

		for (Method method : methods) {

			if (method.getName().startsWith("get") && method.getName().length() > 3
					&& method.getParameters().length == 0 && !method.getName().equals("getClass")) {

				try {

					String field = StringUtil.firstCharToLowerCase(method.getName().substring(3));
					Object value = method.invoke(obj);
					map.put(field, value);

				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			}

		}

		StringBuffer sql = new StringBuffer("update ").append(tableName).append(" set ");
		for (String key : map.keySet()) {
			if (!key.equals(keyName)) {
				sql.append(key).append(" = ?,");
			}
		}

		sql.deleteCharAt(sql.length() - 1);

		sql.append(" where ").append(keyName).append(" = ?");

		System.out.println(sql);

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = Qdf.me().getPool().getConnection();
			ps = conn.prepareStatement(sql.toString());
			int index = 1;
			for (String key : map.keySet()) {
				if (key.equals(keyName))
					continue;
				ps.setObject(index, map.get(key));
				index++;
			}
			ps.setObject(index, map.get(keyName));
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (null != ps) {
					ps.close();
				}
				if (null != conn && conn.getAutoCommit()) {
					conn.close();
				}
			} catch (Exception e2) {
				LogUtil.error(e2.getMessage(),e2);
			}
		}

	}

	public Object findById(Class<?> clazz, Object id) {

		Object obj = null;

		String tableName = Qdf.me().getTable().getTableMap().get(clazz);
		String keyName = Qdf.me().getTable().getPkMap().get(clazz);
		String sql = "select * from " + tableName + " where " + keyName + " = ?";

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = Qdf.me().getPool().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setObject(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				obj = clazz.newInstance();

				Method[] methods = clazz.getMethods();

				for (Method method : methods) {

					if (method.getName().startsWith("set") && method.getName().length() > 3
							&& method.getParameters().length == 1) {

						Class<?> paramType = method.getParameterTypes()[0];
						String field = StringUtil.firstCharToLowerCase(method.getName().substring(3));
						String value = rs.getString(field);
						if (null != value) {
							method.invoke(obj, TypeConverter.convert(paramType, value));
						}
					}
				}
			}

			return obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (null != rs) {
					rs.close();
				}
				if (null != ps) {
					ps.close();
				}
				if (null != conn && conn.getAutoCommit()) {
					conn.close();
				}
			} catch (Exception e2) {
				LogUtil.error(e2.getMessage(),e2);
			}
		}
	}

	public List<Object> queryList(String sql, Object... objects) {

		SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, Qdf.me().getPool().getDbType());
		SQLSelectStatement sqlStatement = parser.parseSelect();
		SQLSelect sqlSelect = sqlStatement.getSelect();
		SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) sqlSelect.getQuery();
		SQLTableSource tableSource = queryBlock.getFrom();
		SQLExpr expr1 = ((SQLExprTableSource) tableSource).getExpr();
		SQLIdentifierExpr identExpr = (SQLIdentifierExpr) expr1;
		String table = identExpr.getName();

		Class<?> clazz = null;

		Map<Class<?>, String> map = Qdf.me().getTable().getTableMap();
		for (Class<?> c : map.keySet()) {
			if (map.get(c).equals(table)) {
				clazz = c;
				break;
			}
		}

		List<Object> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = Qdf.me().getPool().getConnection();
			ps = conn.prepareStatement(sql.toString());
			if (null != objects) {
				for (int i = 0; i < objects.length; i++) {
					ps.setObject(i + 1, objects[i]);
				}
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				Object obj = clazz.newInstance();

				Method[] methods = clazz.getMethods();

				for (Method method : methods) {

					if (method.getName().startsWith("set") && method.getName().length() > 3
							&& method.getParameters().length == 1) {

						Class<?> paramType = method.getParameterTypes()[0];
						String field = StringUtil.firstCharToLowerCase(method.getName().substring(3));
						String value = rs.getString(field);
						if (null != value) {
							method.invoke(obj, TypeConverter.convert(paramType, value));
						}

					}
				}
				list.add(obj);
			}

			return list;

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (null != rs) {
					rs.close();
				}
				if (null != ps) {
					ps.close();
				}
				if (null != conn && conn.getAutoCommit()) {
					conn.close();
				}
			} catch (Exception e2) {
				LogUtil.error(e2.getMessage(),e2);
			}
		}

	}
	
	public List<Object> queryPage(Class<?> clazz,int page,int pageSize) {
		
		String tableName = Qdf.me().getTable().getTableMap().get(clazz);
		String sql = "select * from " + tableName + " limit " + ((page-1)*pageSize)+","+pageSize;;
		return queryList(sql, new Object[]{});
	}
	
	public List<Object> queryPage(String sql,int page,int pageSize) {
		
		sql = sql + " limit " + ((page-1)*pageSize)+","+pageSize;;
		return queryList(sql, new Object[]{});
	}

}
