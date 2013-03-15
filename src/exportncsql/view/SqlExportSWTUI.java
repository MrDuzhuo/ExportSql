package exportncsql.view;

import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.SWTEventListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import exportncsql.model.ExportCondition;

public class SqlExportSWTUI extends Composite {
	
	private Group grpParaSet = null;
	private Group group = null;
	
	private Label lblDbType = null;
	private Combo cmbDbType = null;
	
	private Label lblDbAddress = null;
	private Text txtSourceDbAddress = null;
	private Text txtDestDbAddress = null;
	
	private Label lblDbPort = null;
	private Text txtSourceDbPort = null;
	private Text txtDestDbPort = null;
	
	private Label lblDbSid = null;
	private Text txtSourceDbSid = null;
	private Text txtDestDbSid = null;
	
	private Button btnTestSourceDbConn = null;
	private Button btnTestDestDbConn = null;
	
	private Label lblUserName = null;
	private Text txtSourceUserName = null;
	private Text txtDestUserName = null;
	
	private Label lblPwd = null;
	private Text txtSourcePwd = null;
	private Text txtDestPwd = null;
	
	private Button btnSqlExport = null;
	private Button btnSychronized = null;
	
	private Label lblFilePath = null;
	private Text txtFilePath = null;
	private Button btnFilePath = null;
	
	private Button chkUserDefExp = null;
	private Text txaSqlText = null;
	private Label lblFunCode = null;
	private Text txtFunCode = null;
	private Button chkCascExp = null;
	private Button chkNodeMove = null;
	private Label lblDestNode = null;
	private Text txtDestNode = null;
	private Group grpMessage = null;
	private Text txaMessage = null;
	private MouseListener mouseEvHdl = null;
	private DbConfTool tool = new DbConfTool();

	/**
	 * ��Դ���ݿ����
	 */
	private void createSourceGrp() {
		this.grpParaSet = new Group(this, 4);
		this.grpParaSet.setText("��Դ���ݿ�");
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		this.grpParaSet.setLayout(gridLayout);
		
		RowData rowData = new RowData();
		rowData.height = 90;
		rowData.width = 535;
		this.grpParaSet.setLayoutData(rowData);
		
		DbConfTool tool = new DbConfTool();
		
		this.lblDbType = new Label(this.grpParaSet, 0);
		this.lblDbType.setText("���ݿ����ͣ�");
		
		GridData gridData2 = new GridData();
		gridData2.widthHint = 45;
		this.cmbDbType = new Combo(this.grpParaSet, 524);
		this.cmbDbType.setItems(new String[] { "Oracle" /*, "SQLSERVER", "DB2"*/ });
		this.cmbDbType.select(0);
		this.cmbDbType.setLayoutData(gridData2);
		
		this.lblDbAddress = new Label(this.grpParaSet, 0);
		this.lblDbAddress.setText("���ݿ��ַ��");
		
		this.txtSourceDbAddress = new Text(this.grpParaSet, 2048);
		this.txtSourceDbAddress.setText(tool.getDbConfValue("SOURCE_IP"));
		GridData dbAddressGridData = new GridData();
		dbAddressGridData.widthHint = 100;
		this.txtSourceDbAddress.setLayoutData(dbAddressGridData);
		
		this.lblDbPort = new Label(this.grpParaSet, 0);
		this.lblDbPort.setText("���ݿ�˿ڣ�");
		
		this.txtSourceDbPort = new Text(this.grpParaSet, 2048);
		this.txtSourceDbPort.setText(tool.getDbConfValue("SOURCE_PORT"));
		GridData dbPortGridData = new GridData();
		dbPortGridData.widthHint = 60;
		this.txtSourceDbPort.setLayoutData(dbPortGridData);
		
		this.lblDbSid = new Label(this.grpParaSet, 0);
		this.lblDbSid.setText("���ݿ�/ODBC��");
		
		this.txtSourceDbSid = new Text(this.grpParaSet, 2048);
		this.txtSourceDbSid.setText(tool.getDbConfValue("SOURCE_SID"));
		this.txtSourceDbSid.setLayoutData(dbPortGridData);
		
		this.lblUserName = new Label(this.grpParaSet, 0);
		this.lblUserName.setText("�û�����");
		
		this.txtSourceUserName = new Text(this.grpParaSet, 2048);
		this.txtSourceUserName.setText(tool.getDbConfValue("SOURCE_USER_NAME"));
		this.txtSourceUserName.setLayoutData(dbAddressGridData);
		
		this.lblPwd = new Label(this.grpParaSet, 0);
		this.lblPwd.setText("���룺");
		
		this.txtSourcePwd = new Text(this.grpParaSet, 2048);
		this.txtSourcePwd.setEchoChar('*');
		this.txtSourcePwd.setText(tool.getDbConfValue("SOURCE_PASSWORD"));
		this.txtSourcePwd.setLayoutData(dbPortGridData);
		
		this.btnTestSourceDbConn = new Button(this.grpParaSet, 0);
		this.btnTestSourceDbConn.setText("��������");
		GridData gridData11 = new GridData();
		gridData11.widthHint = 80;
		this.btnTestSourceDbConn.setLayoutData(gridData11);
		
	}

	private void createDestGrp() {
		this.grpParaSet = new Group(this, 4);
		this.grpParaSet.setText("Ŀ�����ݿ�");
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		this.grpParaSet.setLayout(gridLayout);
		
		RowData rowData = new RowData();
		rowData.height = 90;
		rowData.width = 535;
		this.grpParaSet.setLayoutData(rowData);
		
		DbConfTool tool = new DbConfTool();
		
		this.lblDbType = new Label(this.grpParaSet, 0);
		this.lblDbType.setText("���ݿ����ͣ�");
		
		GridData gridData2 = new GridData();
		gridData2.widthHint = 45;
		this.cmbDbType = new Combo(this.grpParaSet, 524);
		this.cmbDbType.setItems(new String[] { "Oracle" /*, "SQLSERVER", "DB2"*/ });
		this.cmbDbType.select(0);
		this.cmbDbType.setLayoutData(gridData2);
		
		this.lblDbAddress = new Label(this.grpParaSet, 0);
		this.lblDbAddress.setText("���ݿ��ַ��");
		
		this.txtDestDbAddress = new Text(this.grpParaSet, 2048);
		this.txtDestDbAddress.setText(tool.getDbConfValue("DEST_IP"));
		GridData dbAddressGridData = new GridData();
		dbAddressGridData.widthHint = 100;
		this.txtDestDbAddress.setLayoutData(dbAddressGridData);
		
		this.lblDbPort = new Label(this.grpParaSet, 0);
		this.lblDbPort.setText("���ݿ�˿ڣ�");
		
		this.txtDestDbPort = new Text(this.grpParaSet, 2048);
		this.txtDestDbPort.setText(tool.getDbConfValue("DEST_PORT"));
		GridData dbPortGridData = new GridData();
		dbPortGridData.widthHint = 60;
		this.txtDestDbPort.setLayoutData(dbPortGridData);
		
		this.lblDbSid = new Label(this.grpParaSet, 0);
		this.lblDbSid.setText("���ݿ�/ODBC��");
		
		this.txtDestDbSid = new Text(this.grpParaSet, 2048);
		this.txtDestDbSid.setText(tool.getDbConfValue("DEST_SID"));
		this.txtDestDbSid.setLayoutData(dbPortGridData);
		
		this.lblUserName = new Label(this.grpParaSet, 0);
		this.lblUserName.setText("�û�����");
		
		this.txtDestUserName = new Text(this.grpParaSet, 2048);
		this.txtDestUserName.setText(tool.getDbConfValue("DEST_USER_NAME"));
		this.txtDestUserName.setLayoutData(dbAddressGridData);
		
		this.lblPwd = new Label(this.grpParaSet, 0);
		this.lblPwd.setText("���룺");
		
		this.txtDestPwd = new Text(this.grpParaSet, 2048);
		this.txtDestPwd.setEchoChar('*');
		this.txtDestPwd.setText(tool.getDbConfValue("DEST_PASSWORD"));
		this.txtDestPwd.setLayoutData(dbPortGridData);
		
		this.btnTestDestDbConn = new Button(this.grpParaSet, 0);
		this.btnTestDestDbConn.setText("��������");
		GridData gridData11 = new GridData();
		gridData11.widthHint = 80;
		this.btnTestDestDbConn.setLayoutData(gridData11);
		
	}

	/**
	 * �����ű�����
	 */
	private void createGroup() {
		
		GridData gridData7 = new GridData();
		gridData7.widthHint = 80;
		gridData7.horizontalSpan = 2;
		
		GridData gridData6 = new GridData();
		gridData6.horizontalSpan = 6;
		gridData6.widthHint = 500;
		gridData6.heightHint = 50;
		GridData gridData5 = new GridData();
		gridData5.horizontalSpan = 2;
		
		GridData gridData4 = new GridData();
		gridData4.horizontalSpan = 2;
		gridData4.widthHint = 350;
		
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 4;
		
		RowData rowData1 = new RowData();
		rowData1.height = 125;
		rowData1.width = 535;
		
		this.group = new Group(this, 0);
		this.group.setText("�ű�ͬ���뵼��");
		this.group.setLayout(gridLayout1);
		this.group.setLayoutData(rowData1);
		
		this.chkCascExp = new Button(this.group, 32);
		this.chkCascExp.setText("��������");
		this.chkCascExp.setSelection(true);
		this.lblFunCode = new Label(this.group, 0);
		this.lblFunCode.setText("��   ��   �ţ�");
		this.txtFunCode = new Text(this.group, 2048);
		this.txtFunCode.setText(tool.getDbConfValue("FUN_CODE"));
		this.txtFunCode.setLayoutData(gridData7);
		
		this.chkNodeMove = new Button(this.group, 32);
		this.chkNodeMove.setText("�ڵ�Ǩ��");
		this.lblDestNode = new Label(this.group, 0);
		this.lblDestNode.setText("Ŀ��ڵ�ţ�");
		this.txtDestNode = new Text(this.group, 2048);
		this.txtDestNode.setEnabled(false);
		this.txtDestNode.setLayoutData(gridData7);
		
		this.lblFilePath = new Label(this.group, 0);
		this.lblFilePath.setText("�ļ�·����");
		this.txtFilePath = new Text(this.group, 2048);
		this.txtFilePath.setText(tool.getDbConfValue("FILE_PATH"));
		this.txtFilePath.setEnabled(false);
		this.txtFilePath.setLayoutData(gridData4);
		this.btnFilePath = new Button(this.group, 0);
		this.btnFilePath.setText("ѡ��...");
		
		this.btnSqlExport = new Button(this.group, 0);
		this.btnSqlExport.setText("�����ű�");
		
		this.btnSychronized = new Button(this.group, 0);
		this.btnSychronized.setText("ͬ����Ŀ�����ݿ�");
	}

	private void createGrpMessage() {
		GridData gridData9 = new GridData();
		gridData9.widthHint = 500;
		gridData9.heightHint = 130;
		RowData rowData2 = new RowData();
		rowData2.width = 535;
		rowData2.height = 140;
		this.grpMessage = new Group(this, 0);
		this.grpMessage.setLayout(new GridLayout());
		this.grpMessage.setText("��Ϣ��ʾ");
		this.grpMessage.setLayoutData(rowData2);
		this.txaMessage = new Text(this.grpMessage, 2626);
		this.txaMessage.setLayoutData(gridData9);
		this.txaMessage
				.append("���汾��Eclipse�����²��԰棬�緢���κ������뼰ʱ������ϵ�����䣺qbsqbs_009@163.com \n��.Ĭ�����·��Ϊ C:\\EXP_SQL\\ ������ȷ�ϴ��ļ�����û�������ű��ļ��Է�ֹ���ǣ�\n��.ϵͳ������δ�ṩ�Զ���������Ϊ���˾����ֳ����ø���һЩ��\n��.�ڵ�Ǩ��ʱ���Զ�����ԭ�ڵ��ɾ���ű�,��ע�⡣\n��.SQLSERVER��\n     ���ӷ�ʽһ��(SQL2000��ҪSP4������SQL2005δ���Թ�):\n     jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=���ݿ�����(ע�ⲻҪ�пո�)\n     ���ӷ�ʽ����jdbc:odbc: <����Դ����> ��\n��.ORACLE Ҳ���Բ��õڶ��� ODBC ���ӷ�ʽ��\n��.DB2��Ŀǰ���ӷ�ʽ��jdbc:db2\n��.������ϸ�����������޸ģ�\n\n��.0.7.0�������ܣ�\n��.1.�ڵ�Ǩ��ʱ����ͬʱ�����¾ɽڵ��ɾ���ű������㷴��Ǩ��\n��.2.������ɾ���ű���ɾ���ӱ����ݣ���ɾ���������ݣ���ֹ���Լ��\n");
	}

	public ExportCondition getCondition() {
		ExportCondition condition = new ExportCondition();
		
		
		condition.setSourceDbAddress(getTxtSourceDbAddress().getText());
		condition.setSourceDbPort(getTxtSourceDbPort().getText());
		condition.setSourceDbSid(getTxtSourceDbSid().getText());
		condition.setSourceUserName(getTxtSourceUserName().getText());
		condition.setSourcePassword(getTxtSourcePwd().getText());
		
		condition.setDestDbAddress(getTxtDestDbAddress().getText());
		condition.setDestDbPort(getTxtDestDbPort().getText());
		condition.setDestDbSid(getTxtDestDbSid().getText());
		condition.setDestUserName(getTxtDestUserName().getText());
		condition.setDestPassword(getTxtDestPwd().getText());
		
		condition.setFunCode(getTxtFunCode().getText());

		condition.setDetail(getChkCascExp().getSelection());
		condition.setNodeMove(getChkNodeMove().getSelection());
		condition.setDestFunCode(getTxtDestNode().getText());
		condition.setOrgiNode(getTxtFunCode().getText());

		condition.setFilepath(this.txtFilePath.getText());

		return condition;
	}

	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display, 64);
		shell.setLayout(new FillLayout());
		shell.setSize(new Point(560, 650));
		new SqlExportSWTUI(shell, 0);
		shell.open();

		while (!(shell.isDisposed()))
			if (!(display.readAndDispatch()))
				display.sleep();

		display.dispose();
	}

	public SqlExportSWTUI(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = 512;
		setLayout(rowLayout);
		createSourceGrp();
		createDestGrp();
		createGroup();
		createGrpMessage();
		initConnection();
		setSize(new Point(550, 418));
	}

	private void initConnection() {
		getBtnFilePath().addMouseListener((MouseListener) getEvHandler());
		getBtnSqlExport().addMouseListener((MouseListener) getEvHandler());
		getBtnTestSourceDbConn().addMouseListener((MouseListener) getEvHandler());
		getBtnTestDestDbConn().addMouseListener((MouseListener) getEvHandler());
		getChkCascExp().addMouseListener((MouseListener) getEvHandler());
		getChkNodeMove().addMouseListener((MouseListener) getEvHandler());
		getCmbDbType().addSelectionListener((SelectionListener) getEvHandler());
		getBtnSychronized().addMouseListener((MouseListener) getEvHandler());
	}
	


	protected SWTEventListener getEvHandler() {
		if (this.mouseEvHdl == null)
			this.mouseEvHdl = new SqlExportEvHandler(this);

		return this.mouseEvHdl;
	}

	public Button getBtnFilePath() {
		return this.btnFilePath;
	}

	public Button getBtnSqlExport() {
		return this.btnSqlExport;
	}

	public Button getBtnTestSourceDbConn() {
		return this.btnTestSourceDbConn;
	}

	public Button getChkCascExp() {
		return this.chkCascExp;
	}

	public Button getChkNodeMove() {
		return this.chkNodeMove;
	}

	public Button getChkUserDefExp() {
		return this.chkUserDefExp;
	}

	public Combo getCmbDbType() {
		return this.cmbDbType;
	}


	public Text getTxaSqlText() {
		return this.txaSqlText;
	}

	public Text getTxtDestNode() {
		return this.txtDestNode;
	}

	public Text getTxtFunCode() {
		return this.txtFunCode;
	}

	public Text getTxtSourcePwd() {
		return this.txtSourcePwd;
	}

	public Text getTxaMessage() {
		return this.txaMessage;
	}

	public Text getTxtSourceUserName() {
		return this.txtSourceUserName;
	}

	public Text getTxtFilePath() {
		return this.txtFilePath;
	}

	public Text getTxtSourceDbAddress() {
		return txtSourceDbAddress;
	}

	public Text getTxtSourceDbPort() {
		return txtSourceDbPort;
	}

	public Text getTxtSourceDbSid() {
		return txtSourceDbSid;
	}

	public Button getBtnTestDestDbConn() {
		return btnTestDestDbConn;
	}

	public Text getTxtDestDbAddress() {
		return txtDestDbAddress;
	}

	public Text getTxtDestDbPort() {
		return txtDestDbPort;
	}

	public Text getTxtDestDbSid() {
		return txtDestDbSid;
	}

	public Text getTxtDestUserName() {
		return txtDestUserName;
	}

	public Text getTxtDestPwd() {
		return txtDestPwd;
	}

	public Label getLblFilePath() {
		return lblFilePath;
	}

	public Button getBtnSychronized() {
		return btnSychronized;
	}
}