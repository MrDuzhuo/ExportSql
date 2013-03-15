package exportncsql.logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exportncsql.model.ExportCondition;

public class ExportManager {
//	private static final String KEY_SQLSERVER = "sqlserver";
//	private static final String KEY_ODBC = "jdbc:odbc:";
//	private static final String KEY_ORACLE = "oracle";
//	private static final String DIRVER_SQLSERVER = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
//	private static final String DIRVER_ODBC = "sun.jdbc.odbc.JdbcOdbcDriver";
//	private static final String DIRVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
//	private static final String DIRVER_DB2 = "com.ibm.db2.jcc.DB2Driver";
//	private static final String KEY_DB2 = "jdbc:db2";
	private BufferedWriter writer = null;

	public void testConntion(String ip, String port, String sid, String userName, String password) throws Exception {
		Connection con = getConntion(ip, port, sid, userName, password);
		con.close();
	}

	public String exportSQL(ExportCondition condition) throws Exception {

		Connection conn = null;
		try {
			try {
				conn = getConntion(condition.getSourceDbAddress(), condition.getSourceDbPort(),
						condition.getSourceDbSid(), condition.getSourceUserName(), condition.getSourcePassword());
				String filename = condition.getFunCode() + "汇总脚本" + ".sql";

				File dir = new File(condition.getFilepath());
				if (!(dir.exists())) {
					dir.mkdir();
				}

				File file = new File(condition.getFilepath() + filename);
				if (file.exists())
					file.delete();

				this.writer = new BufferedWriter(new FileWriter(file, true));

				StringBuffer msg = new StringBuffer();

				if (condition.isDetail()) {
					List<String> childNodes = getChildNodesInfo(conn, condition.getFunCode());
					Iterator<String> child = childNodes.iterator();
					while (child.hasNext()) {
						String funcode = (String) child.next();
						ExportCondition childCond = (ExportCondition) condition.clone();
						childCond.setFunCode(funcode);
						childCond.setBusiType(null);
						exportSqlByFunCode(msg, conn, childCond);
					}
				} else {
					exportSqlByFunCode(msg, conn, condition);
				}

				String str1 = msg.toString();
				try {
					if (conn != null)
						conn.close();
					if (this.writer != null) {
						this.writer.close();
						this.writer = null;
					}
				} catch (Exception localException1) {
				}
				return str1;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (this.writer != null) {
					this.writer.close();
					this.writer = null;
				}
			} catch (Exception localException2) {
			}
		}
	}

	private List<String> getChildNodesInfo(Connection conn, String funCode)
			throws Exception {
		String sql = "select fun_code from sm_funcregister\nwhere sm_funcregister.fun_code like '"
				+ funCode + "%'\n" + "order by sm_funcregister.fun_code";

		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		ArrayList<String> rtnValue = new ArrayList<String>();
		while (rs.next())
			rtnValue.add(rs.getString("fun_code"));

		return rtnValue;
	}

	private void exportSqlByFunCode(StringBuffer rtnMsg, Connection conn, ExportCondition condition) throws Exception {
		String sql = "select * from sm_funcregister where fun_code='" + condition.getFunCode() + "' and (dr=0 or dr is null)";
		
		Map<String, String> pkField = new HashMap<String, String>();
		pkField.put("cfunid", "fun_name");

		String[] childSql = { "select * from sm_butnregister where parent_id = ? and (dr=0 or dr is null)" };

		exportSqlToFile(rtnMsg, conn, sql, pkField, childSql, condition);

		sql = "select * from pub_billtemplet where nodecode = '" + condition.getFunCode() + "' and (dr=0 or dr is null)"
				+ ((condition.getBillType() == null) ? "" : new StringBuilder(" and pk_billtypecode = '")
						.append(condition.getBillType()).append("'").toString());
		pkField.clear();
		pkField.put("pk_billtemplet", "bill_templetname");

		List<String> sqlList = new ArrayList<String>();
		sqlList.add("select * from pub_billtemplet_b where pk_billtemplet = ? and (dr=0 or dr is null)");
		sqlList.add("select * from pub_billtemplet_t where pk_billtemplet = ? and (dr=0 or dr is null)");
		exportSqlToFile(rtnMsg, conn, sql, pkField,	sqlList.toArray(new String[0]), condition);

		sql = "select * from pub_query_templet where node_code = '"	+ condition.getFunCode() + "' and (dr=0 or dr is null)";
		pkField.clear();
		pkField.put("id", "model_name");
		sqlList.clear();
		sqlList.add("select * from pub_query_condition where pk_templet = ? and (dr=0 or dr is null)");
		exportSqlToFile(rtnMsg, conn, sql, pkField, sqlList.toArray(new String[0]), condition);

		sql = "select * from pub_report_templet where parent_code = '" + condition.getFunCode() + "' and (dr=0 or dr is null)";
		pkField.clear();
		pkField.put("pk_templet", "node_name");

		sqlList.clear();
		sqlList.add("select * from pub_report_model where pk_templet = ? and (dr=0 or dr is null)");

		exportSqlToFile(rtnMsg, conn, sql, pkField,	sqlList.toArray(new String[0]), condition);

		sql = "select * from pub_print_template where vnodecode = '" + condition.getFunCode() + "' and (dr=0 or dr is null)";
		pkField.clear();
		pkField.put("ctemplateid", "vtemplatename");

		sqlList.clear();
		sqlList.add("select * from pub_print_cell where ctemplateid= ? and (dr=0 or dr is null)");
		sqlList.add("select * from pub_print_line where ctemplateid= ? and (dr=0 or dr is null)");
		sqlList.add("select * from pub_print_datasource where ctemplateid = ? and (dr=0 or dr is null)");

		exportSqlToFile(rtnMsg, conn, sql, pkField,	sqlList.toArray(new String[0]), condition);

		sql = "select * from pub_print_dataitem where vnodecode = '" + condition.getFunCode() + "' and (dr=0 or dr is null)";
		exportSqlToFile(rtnMsg, conn, sql, null, null, condition);

		sql = "select * from pub_systemplate where funnode = '"	+ condition.getFunCode() + "' and (dr=0 or dr is null)";
		exportSqlToFile(rtnMsg, conn, sql, null, null, condition);

		sql = "select * from pub_hotkeyregister where fun_code = '" + condition.getFunCode() + "' and (dr=0 or dr is null)";
		exportSqlToFile(rtnMsg, conn, sql, null, null, condition);

		sql = "select * from dap_dapsystem where funcode = '" + condition.getFunCode() + "' and (dr=0 or dr is null)";
		exportSqlToFile(rtnMsg, conn, sql, null, null, condition);

		sql = "select * from sm_codetocode where funccode = '" + condition.getFunCode() + "' and (dr=0 or dr is null)";
		exportSqlToFile(rtnMsg, conn, sql, null, null, condition);

		sql = "select * from bd_billtype where nodecode = '" + condition.getFunCode() + "' and (dr=0 or dr is null)";
		pkField.clear();
		pkField.put("pk_billtypecode", "billtypename");

		sqlList.clear();
		sqlList.add("select * from pub_billaction where pk_billtype = ? and (dr=0 or dr is null)");
		sqlList.add("select * from pub_busiclass where pk_billtype = ? and (dr=0 or dr is null)");
		sqlList.add("select * from pub_funccodetocode where billtype = ? and (dr=0 or dr is null)");
		sqlList.add("select * from pub_function where pk_billtype = ? and (dr=0 or dr is null)");
		sqlList.add("select * from pub_billactiongroup where pk_billtype = ? and (dr=0 or dr is null)");
		sqlList.add("select * from pub_billtobillrefer where billtype=? and (dr=0 or dr is null)");
		sqlList.add("select * from pub_votable where pk_billtype = ? and (dr=0 or dr is null)");
		sqlList.add("select * from dap_defitem where pk_billtype = ? and (dr=0 or dr is null)");
		sqlList.add("select * from pub_billcode_rule where pk_billtypecode = ? and (dr=0 or dr is null)");
		sqlList.add("select * from pub_billactionconfig where pk_billactiongroup in (select pk_billactiongroup from pub_billactiongroup where pk_billtype=? and (dr=0 or dr is null)) and (dr=0 or dr is null)");
		
		exportSqlToFile(rtnMsg, conn, sql, pkField, childSql, condition);
	}

	private void exportSqlToFile(StringBuffer retnMsg, Connection conn, String sql, Map<String, String> pkField, 
			String[] childSql, ExportCondition condition) throws Exception {
		String tableName = getTableNameFromSQL(sql);

		PreparedStatement stmt = null;
		ResultSet result = null;
		HashMap childTablePk = null;
		StringBuffer needToFile = new StringBuffer();
		StringBuffer deleteSql = new StringBuffer();
		try {
			stmt = conn.prepareStatement(sql);

			result = stmt.executeQuery();
			childTablePk = writeResultSetToFile(needToFile, result, tableName, pkField, 
					"--" + tableName + condition.getFunCode(),	condition, retnMsg);
		} finally {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();
			result = null;
			stmt = null;
		}

		if ((childSql != null) && (childSql.length > 0)	&& (childTablePk != null) && (!(childTablePk.isEmpty()))) {
			Iterator it = childTablePk.keySet().iterator();
			String pk = null;
			String name = null;
			while (it.hasNext()) {
				pk = (String) it.next();
				name = (String) childTablePk.get(pk);
				int iLen = childSql.length;
				for (int i = 0; i < iLen;) {
					try {
						String childTableName = getTableNameFromSQL(childSql[i]);
						stmt = conn.prepareStatement(childSql[i]);

						deleteSql.append(getDeleteSqlFromSelectSql(childSql[i],	new Object[] { pk }, condition));

						stmt.setString(1, pk);
						result = stmt.executeQuery();
						writeResultSetToFile(needToFile, result,
								childTableName, null, "--" + childTableName
										+ condition.getFunCode() + name,
								condition, retnMsg);
					} finally {
						if (result != null)
							result.close();
						if (stmt != null)
							stmt.close();
						result = null;
						stmt = null;
					}
					++i;
				}

			}

		}

		deleteSql.append(getDeleteSqlFromSelectSql(sql, null, condition));
		writeSQLToFile(deleteSql.toString());
		writeSQLToFile(needToFile.toString());
	}

	private String getDeleteSqlFromSelectSql(String sql, Object[] paras,
			ExportCondition condition) throws Exception {
		if (sql == null)
			return "";
		String tempSql = sql.toUpperCase();
		int formIdx = tempSql.indexOf("FROM");
		int whereIdx = tempSql.indexOf("WHERE");
		String tablename = tempSql.substring(formIdx + 4, whereIdx);
		if (tablename != null)
			tablename = tablename.trim();

		String info = "\n\n--" + tablename + "表的删除语句\n";

		String afterFromSql = tempSql.substring(formIdx + 4);
		String deleteSql = "delete " + afterFromSql;
		if ((paras != null) && (paras.length != 0)) {
			int iLen = paras.length;
			for (int i = 0; i < iLen; ++i) {
				int idx = deleteSql.indexOf("?");
				if (idx != -1)
					if (paras[i] instanceof String)
						deleteSql = deleteSql.replaceFirst("\\?", "'" + ((String) paras[i]) + "'");
					else if (paras[i] instanceof Character)
						deleteSql = deleteSql.replaceFirst("\\?", "'" + ((String) paras[i]) + "'");
					else
						deleteSql = deleteSql.replaceFirst("\\?", (String) paras[i]);
				else
					throw new RuntimeException("Sql中参数数目 与 传入参数数组的参数数目不一致");
			}

		}

		if ((condition.getDestFunCode() != null) && (!("".equalsIgnoreCase(condition.getDestFunCode())))) {
			String newtableDelete = new String(deleteSql);
			newtableDelete = newtableDelete.replaceAll(condition.getOrgiNode(), condition.getDestFunCode());
			deleteSql = deleteSql + ";\n" + newtableDelete;
		}

		return info + deleteSql + ";\n";
	}

	private HashMap writeResultSetToFile(StringBuffer needToFile,
			ResultSet result, String tableName, Map<String, String> mapFieldName,
			String info, ExportCondition condition, StringBuffer rtnMsg) throws Exception {
		HashMap pkFieldValue = new LinkedHashMap();
		String insertStr = getInsertStr(result, tableName, pkFieldValue, mapFieldName, rtnMsg, condition);
		if (insertStr == null)
			return null;
		
		if ((condition.getDestFunCode() != null) && (!("".equalsIgnoreCase(condition.getDestFunCode())))) {
			insertStr = insertStr.replaceAll("\\'" + condition.getOrgiNode(), "'" + condition.getDestFunCode());
			info = info.replaceAll(condition.getOrgiNode(),	condition.getDestFunCode());
		}
		needToFile.append(info);
		needToFile.append(insertStr);
		return pkFieldValue;
	}

	private void writeSQLToFile(String sql) throws Exception {
		if (sql == null)
			return;

		BufferedWriter tempwriter = null;
		tempwriter = this.writer;
		tempwriter.write(sql);
		tempwriter.flush();
	}

	private String getInsertStr(ResultSet result, String tableName,
			HashMap rtnValues, Map<String, String> mapFieldName, StringBuffer rtnMsg,
			ExportCondition condition) throws Exception {
		String pkField = null;
		String nameField = null;
		if ((mapFieldName != null) && (!(mapFieldName.isEmpty()))) {
			Set key = mapFieldName.keySet();
			Iterator it = key.iterator();
			if (it.hasNext()) {
				pkField = (String) it.next();
				nameField = (String) mapFieldName.get(pkField);
			}
		}

		ResultSetMetaData metadata = result.getMetaData();
		int count = metadata.getColumnCount();
		if (count == 0) {
			return null;
		}

		String[] columnNames = new String[count];
		int[] columnTypes = new int[count];
		StringBuffer sameStr = new StringBuffer();
		sameStr.append("\nInsert into " + tableName);
		sameStr.append("\n(");
		for (int i = 1; i <= count; ++i) {
			columnNames[(i - 1)] = metadata.getColumnName(i);
			columnTypes[(i - 1)] = metadata.getColumnType(i);
			sameStr.append(columnNames[(i - 1)]);
			if (i != count)
				sameStr.append(",");
		}

		sameStr.append(")\n");

		StringBuffer insertStr = new StringBuffer(4096);
		insertStr.append("\n\n\n");
		int rowcount = 0;
		while (result.next()) {
			insertStr.append(sameStr);
			insertStr.append("Values(");
			String pk = null;
			String name = null;
			for (int i = 0; i < count; ++i) {
				Object o = result.getObject(columnNames[i]);
				if (columnNames[i].equalsIgnoreCase(pkField))
					pk = (String) o;

				if (columnNames[i].equalsIgnoreCase(nameField)) {
					name = (String) o;
				}

				if (o == null) {
					insertStr.append("null");
				} else {
					int type = columnTypes[i];
					if ((type == -5) || (type == 3) || (type == 8)
							|| (type == 6) || (type == 4) || (type == 2)
							|| (type == 7) || (type == 5) || (type == -6)) {
						insertStr.append(o.toString());
					} else {
						insertStr.append("'");
						insertStr.append(o.toString());
						insertStr.append("'");
					}
				}

				if (i != count - 1)
					insertStr.append(",");
			}

			insertStr.append(");\n");
			if ((pk != null) && (name != null))
				rtnValues.put(pk, name);

			++rowcount;
		}
		rtnMsg.append(tableName + "表已导出\t\t记录" + rowcount + "条！\t 节点号:" + condition.getFunCode() + "\n");
		System.out.println("导出的记录条数：" + rowcount);
		
		return ((insertStr.toString().trim().length() == 0) ? null : insertStr.toString());
	}

	private String getTableNameFromSQL(String sql) {
		String tempsql = sql.toUpperCase();
		int start = tempsql.indexOf("FROM") + 4;
		int end = tempsql.indexOf("WHERE");
		String tableName = tempsql.substring(start, end);
		return tableName.trim();
	}

	public Connection getConntion(String ip, String port, String sid, String userName, String password) throws Exception {


//		if (connStr.indexOf("sqlserver") >= 0)
//			Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
//		else if (connStr.indexOf("oracle") >= 0)
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//		else if (connStr.indexOf("jdbc:odbc:") >= 0)
//			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
//		else if (connStr.indexOf("jdbc:db2") >= 0)
//			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
//		else
//			throw new Exception("驱动解析失败！");
		
		Class.forName("oracle.jdbc.driver.OracleDriver");

		String connStr = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + sid;
		Connection conn = DriverManager.getConnection(connStr, userName, password);
		return conn;
	}
}