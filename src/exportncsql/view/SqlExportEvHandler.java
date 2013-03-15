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
			System.out.println("��Դ���ݿ�������Ӱ�ť");
			onBtnTestSourceDbConn();
		} else if(e.getSource() == this.m_ui.getBtnTestDestDbConn()) {
			System.out.println("Ŀ�����ݿ�������Ӱ�ť");
			onBtnTestDestDbConn();
		} else if (e.getSource() == this.m_ui.getBtnSqlExport()) {
			System.out.println("�����ű���ť");
			onBtnSqlExport();
		} else if (e.getSource() == this.m_ui.getBtnFilePath()) {
			System.out.println("����·����ť");
			onBtnFilePath();
		} else if (e.getSource() == this.m_ui.getChkCascExp()) {
			System.out.println("�Ƿ�������"
					+ this.m_ui.getChkCascExp().getSelection());
			onChkCascExp();
		} else if (e.getSource() == this.m_ui.getChkNodeMove()) {
			System.out.println("�Ƿ�ڵ�Ǩ��" + this.m_ui.getChkNodeMove().getSelection());
			onChkNodeMove();
		} else if (e.getSource() == this.m_ui.getChkUserDefExp()) {
			System.out.println("�Ƿ��Զ��嵼��" + this.m_ui.getChkUserDefExp().getSelection());
			onChkCascExp();
		} else if(e.getSource() == this.m_ui.getBtnSychronized()) {
			onBtnSychronized();
		}
	}

	private void onBtnSychronized() {
		ExportCondition condition = m_ui.getCondition();
		String fileFullName = condition.getFilepath() + condition.getFunCode() + "���ܽű�.sql";
		List<StringBuilder> sqlList = loadSql(fileFullName);
		ExportManager m = new ExportManager();
		Connection conn = null;
		try {
			conn = m.getConntion(condition.getDestDbAddress(), condition.getDestDbPort(), condition.getDestDbSid(), condition.getDestUserName(), condition.getDestPassword());
		} catch (Exception e) {
			MessageDialog.openError(m_ui.getShell(), "����", "������ݿ����ӳ���");
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
			MessageDialog.openError(m_ui.getShell(), "����", "ִ��SQL����");
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
	        // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
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
			MessageDialog.openError(m_ui.getShell(), "����", "δ�ҵ�������" + fileFullName + "�ļ�");
			return null;
		} catch (IOException e) {
			MessageDialog.openError(m_ui.getShell(), "����", "�ļ���ȡ����");
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
			System.out.println("���ݿ�����"
					+ this.m_ui.getCmbDbType().getSelectionIndex());
			onSelCmbDbType();
		} 
	}

	/**
	 * ѡ�񵼳�·��
	 */
	private void onBtnFilePath() {
		DirectoryDialog driSelect = new DirectoryDialog(this.m_ui.getShell(), 64);
		driSelect.setText("ѡ�񵼳�·��");
		DbConfTool tool = new DbConfTool();
		driSelect.setFilterPath(tool.getDbConfValue("FILE_PATH"));
		String path = driSelect.open();
		if (path != null)
			this.m_ui.getTxtFilePath().setText(path + "\\EXP_SQL\\");
	}

	/**
	 * �����ű�
	 */
	private void onBtnSqlExport() {
		try {
			saveDbConf(m_ui.getCondition());
			getExportManager().exportSQL(m_ui.getCondition());
			MessageDialog.openInformation(m_ui.getShell(), "��ʾ", "�������");
		} catch (Exception e) {
			MessageDialog.openError(m_ui.getShell(), "����", "�����ű�ʱ����" + e.getMessage());
			return;
		}
	} 

	/**
	 * ������Դ���ݿ�����
	 */
	private void onBtnTestSourceDbConn() {
		ExportCondition condition = m_ui.getCondition();
		String ip = condition.getSourceDbAddress();
		String port = condition.getSourceDbPort();
		String sid = condition.getSourceDbSid();
		String userName = condition.getSourceUserName();
		String password = condition.getSourcePassword();
		
		if(userName == null || "".equals(userName)) {
			MessageDialog.openError(m_ui.getShell(), "����", "�������û�����");
			m_ui.getTxtSourceUserName().forceFocus();
			return;
		}
		
		if(password == null || "".equals(password)) {
			MessageDialog.openError(m_ui.getShell(), "����", "���������룡");
			m_ui.getTxtSourcePwd().forceFocus();
			return;
		}
		
		onBtnTestDbConn(ip, port, sid, userName, password);
		saveDbConf(condition);
	}
	
	/**
	 * ����Ŀ�����ݿ�����
	 */
	private void onBtnTestDestDbConn() {
		ExportCondition condition = m_ui.getCondition();
		String ip = condition.getDestDbAddress();
		String port = condition.getDestDbPort();
		String sid = condition.getDestDbSid();
		String userName = condition.getDestUserName();
		String password = condition.getDestPassword();
		
		if(userName == null || "".equals(userName)) {
			MessageDialog.openError(m_ui.getShell(), "����", "�������û�����");
			m_ui.getTxtDestUserName().forceFocus();
			return;
		}
		
		if(password == null || "".equals(password)) {
			MessageDialog.openError(m_ui.getShell(), "����", "���������룡");
			m_ui.getTxtDestPwd().forceFocus();
			return;
		}
		
		onBtnTestDbConn(ip, port, sid, userName, password);
		saveDbConf(condition);
	}

	/**
	 * �������ݿ�����
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
			MessageDialog.openInformation(m_ui.getShell(), "��ʾ", "���ӳɹ�");
		} catch (ClassNotFoundException e) {
			MessageDialog.openError(m_ui.getShell(), "����", "�Ҳ������ݿ�������" + e.getMessage());
		} catch(SQLException e){
			MessageDialog.openError(m_ui.getShell(), "����", "���ݿ����Ӵ���" + e.getMessage());
		} catch (Exception e) {
			MessageDialog.openError(m_ui.getShell(), "����", "����ʧ��: " + e.getMessage());
		}
	}
	
	/**
	 * ���������ļ�
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