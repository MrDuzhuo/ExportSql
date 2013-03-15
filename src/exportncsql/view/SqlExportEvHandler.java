package exportncsql.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.DirectoryDialog;

import exportncsql.logic.ExportManager;
import exportncsql.model.ExportCondition;

public class SqlExportEvHandler implements MouseListener, SelectionListener,
		FocusListener {
	private SqlExportSWTUI m_ui = null;
	private ExportManager m_manager = null;

	public SqlExportEvHandler(SqlExportSWTUI ui) {
		if (ui == null)
			throw new NullPointerException("Composite not allow null value");
		this.m_ui = ui;
	}

	public void mouseDoubleClick(MouseEvent e) {
	}

	public void mouseDown(MouseEvent e) {
	}

	public void mouseUp(MouseEvent e) {
		if (e.getSource() == this.m_ui.getBtnTestSourceDbConn()) {
			System.out.println("来源数据库测试连接按钮");
			onBtnTestSourceDbConn();
		} else if(e.getSource() == this.m_ui.getBtnTestDestDbConn()) {
			System.out.println("目标数据库测试连接按钮");
			onBtnTestDestDbConn();
		} else if (e.getSource() == this.m_ui.getBtnSqlExport()) {
			System.out.println("导出脚本按钮");
			onBtnSqlExport();
		} else if (e.getSource() == this.m_ui.getBtnFilePath()) {
			System.out.println("导出路径按钮");
			onBtnFilePath();
		} else if (e.getSource() == this.m_ui.getChkCascExp()) {
			System.out.println("是否级联导出"
					+ this.m_ui.getChkCascExp().getSelection());
			onChkCascExp();
		} else if (e.getSource() == this.m_ui.getChkNodeMove()) {
			System.out.println("是否节点迁移" + this.m_ui.getChkNodeMove().getSelection());
			onChkNodeMove();
		} else if (e.getSource() == this.m_ui.getChkUserDefExp()) {
			System.out.println("是否自定义导出" + this.m_ui.getChkUserDefExp().getSelection());
			onChkCascExp();
		} else if(e.getSource() == this.m_ui.getBtnSychronized()) {
			onBtnSychronized();
		}
	}

	private void onBtnSychronized() {
		ExportCondition condition = m_ui.getCondition();
		String fileFullName = condition.getFilepath() + condition.getFunCode() + "汇总脚本.sql";
		List<StringBuilder> sqlList = loadSql(fileFullName);
		ExportManager m = new ExportManager();
		Connection conn = null;
		try {
			conn = m.getConntion(condition.getDestDbAddress(), condition.getDestDbPort(), condition.getDestDbSid(), condition.getDestUserName(), condition.getDestPassword());
		} catch (Exception e) {
			MessageDialog.openError(m_ui.getShell(), "错误", "获得数据库连接出错！");
			return;
		}
		int[] a = excuteSql(sqlList, conn);

	}

	private int[] excuteSql(List<StringBuilder> sqlList, Connection conn) {
		Statement stmt = null;
		int[] rows = null;
		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			for (StringBuilder sql : sqlList) {
				stmt.addBatch(sql.toString());
			}
			rows = stmt.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			MessageDialog.openError(m_ui.getShell(), "错误", "执行SQL出错！");
			try {
				conn.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return rows;
	}

	private List<StringBuilder> loadSql(String fileFullName) {
		List<StringBuilder> sqlList = new ArrayList<StringBuilder>();
		StringBuilder sql = new StringBuilder();
		try {
		    File file = new File(fileFullName);
		    BufferedReader reader =  new BufferedReader(new FileReader(file));
	        String tempString = null;
	        // 一次读入一行，直到读入null为文件结束
	        while ((tempString = reader.readLine()) != null) {

	           if(tempString.equals("") || tempString.startsWith("--"))
	        	   continue;

	           if(!tempString.endsWith(";"))
	        	   sql.append(tempString);
	           else 
	        	   sqlList.add(sql);
	        }
	        reader.close();
		} catch (FileNotFoundException e) {
			MessageDialog.openError(m_ui.getShell(), "错误", "未找到导出的" + fileFullName + "文件");
			return null;
		} catch (IOException e) {
			MessageDialog.openError(m_ui.getShell(), "错误", "文件读取错误");
			return null;
		} 

		return sqlList;
	}

	private ExportManager getExportManager() {
		if (this.m_manager == null)
			this.m_manager = new ExportManager();

		return this.m_manager;
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == this.m_ui.getCmbDbType()) {
			System.out.println("数据库类型"
					+ this.m_ui.getCmbDbType().getSelectionIndex());
			onSelCmbDbType();
		} 
	}

	/**
	 * 选择导出路径
	 */
	private void onBtnFilePath() {
		DirectoryDialog driSelect = new DirectoryDialog(this.m_ui.getShell(), 64);
		driSelect.setText("选择导出路径");
		DbConfTool tool = new DbConfTool();
		driSelect.setFilterPath(tool.getDbConfValue("FILE_PATH"));
		String path = driSelect.open();
		if (path != null)
			this.m_ui.getTxtFilePath().setText(path + "\\EXP_SQL\\");
	}

	/**
	 * 导出脚本
	 */
	private void onBtnSqlExport() {
		try {
			saveDbConf(m_ui.getCondition());
			getExportManager().exportSQL(m_ui.getCondition());
			MessageDialog.openInformation(m_ui.getShell(), "提示", "导出完成");
		} catch (Exception e) {
			MessageDialog.openError(m_ui.getShell(), "错误", "导出脚本时出错：" + e.getMessage());
			return;
		}
	} 

	/**
	 * 测试来源数据库连接
	 */
	private void onBtnTestSourceDbConn() {
		ExportCondition condition = m_ui.getCondition();
		String ip = condition.getSourceDbAddress();
		String port = condition.getSourceDbPort();
		String sid = condition.getSourceDbSid();
		String userName = condition.getSourceUserName();
		String password = condition.getSourcePassword();
		
		if(userName == null || "".equals(userName)) {
			MessageDialog.openError(m_ui.getShell(), "错误", "请输入用户名！");
			m_ui.getTxtSourceUserName().forceFocus();
			return;
		}
		
		if(password == null || "".equals(password)) {
			MessageDialog.openError(m_ui.getShell(), "错误", "请输入密码！");
			m_ui.getTxtSourcePwd().forceFocus();
			return;
		}
		
		onBtnTestDbConn(ip, port, sid, userName, password);
		saveDbConf(condition);
	}
	
	/**
	 * 测试目标数据库连接
	 */
	private void onBtnTestDestDbConn() {
		ExportCondition condition = m_ui.getCondition();
		String ip = condition.getDestDbAddress();
		String port = condition.getDestDbPort();
		String sid = condition.getDestDbSid();
		String userName = condition.getDestUserName();
		String password = condition.getDestPassword();
		
		if(userName == null || "".equals(userName)) {
			MessageDialog.openError(m_ui.getShell(), "错误", "请输入用户名！");
			m_ui.getTxtDestUserName().forceFocus();
			return;
		}
		
		if(password == null || "".equals(password)) {
			MessageDialog.openError(m_ui.getShell(), "错误", "请输入密码！");
			m_ui.getTxtDestPwd().forceFocus();
			return;
		}
		
		onBtnTestDbConn(ip, port, sid, userName, password);
		saveDbConf(condition);
	}

	/**
	 * 测试数据库连接
	 * @param ip
	 * @param port
	 * @param sid
	 * @param userName
	 * @param password
	 */
	private void onBtnTestDbConn(String ip, String port, String sid, String userName, String password) {
		try {
			ExportManager m = new ExportManager();
			m.testConntion(ip, port, sid, userName, password);
			MessageDialog.openInformation(m_ui.getShell(), "提示", "连接成功");
		} catch (ClassNotFoundException e) {
			MessageDialog.openError(m_ui.getShell(), "错误", "找不到数据库驱动：" + e.getMessage());
		} catch(SQLException e){
			MessageDialog.openError(m_ui.getShell(), "错误", "数据库连接错误：" + e.getMessage());
		} catch (Exception e) {
			MessageDialog.openError(m_ui.getShell(), "错误", "连接失败: " + e.getMessage());
		}
	}
	
	/**
	 * 保存配置文件
	 * @param condition
	 */
	private void saveDbConf(ExportCondition condition) {
		DbConfTool tool = new DbConfTool();
		tool.setDbConfValue("SOURCE_IP", condition.getSourceDbAddress());
		tool.setDbConfValue("SOURCE_PORT", condition.getSourceDbPort());
		tool.setDbConfValue("SOURCE_SID", condition.getSourceDbSid());
		tool.setDbConfValue("SOURCE_USER_NAME", condition.getSourceUserName());
		tool.setDbConfValue("SOURCE_PASSWORD", condition.getSourcePassword());
		
		tool.setDbConfValue("DEST_IP", condition.getDestDbAddress());
		tool.setDbConfValue("DEST_PORT", condition.getDestDbPort());
		tool.setDbConfValue("DEST_SID", condition.getDestDbSid());
		tool.setDbConfValue("DEST_USER_NAME", condition.getDestUserName());
		tool.setDbConfValue("DEST_PASSWORD", condition.getDestPassword());
		
		tool.setDbConfValue("FILE_PATH", condition.getFilepath());
		tool.setDbConfValue("FUN_CODE", condition.getFunCode());
		
		tool.save();
	}

	private void onChkCascExp() {
	}

	private void onSelCmbDbType() {
		int seleIndex = this.m_ui.getCmbDbType().getSelectionIndex();
		String dbType = this.m_ui.getCmbDbType().getItem(seleIndex);

//		if ("ORACLE".equalsIgnoreCase(dbType)) {
//			this.m_ui.getTxtConnStr().setText(ExportCondition.URL_ORACLE);
//		} else if ("SQLSERVER".equalsIgnoreCase(dbType)) {
//			this.m_ui.getTxtConnStr().setText(ExportCondition.URL_SQLSERVER);
//		} else if ("DB2".equalsIgnoreCase(dbType))
//			this.m_ui.getTxtConnStr().setText(ExportCondition.URL_DB2);
	}

	private void onChkNodeMove() {
		this.m_ui.getTxtDestNode().setEnabled(
				this.m_ui.getChkNodeMove().getSelection());
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
//		if (e.getSource() == this.m_ui.getTxtConnStr()) {
//			int index = this.m_ui.getCmbDbType().getSelectionIndex();
//			String dbType = this.m_ui.getCmbDbType().getItem(index);
//			if ("ORACLE".equalsIgnoreCase(dbType))
//				ExportCondition.URL_ORACLE = this.m_ui.getTxtConnStr()
//						.getText();
//			else if ("DB2".equalsIgnoreCase(dbType))
//				ExportCondition.URL_DB2 = this.m_ui.getTxtConnStr().getText();
//			else if ("SQLSERVER".equalsIgnoreCase(dbType))
//				ExportCondition.URL_SQLSERVER = this.m_ui.getTxtConnStr()
//						.getText();
//		}
	}
}