package exportncsql.model;

public class ExportCondition {
	public static final String VERSION_NC3X = "NC3.x";
	public static final String VERSION_NC5X = "NC5.x";
	public static final String DBTYPE_ORA = "ORACLE";
	public static final String DBTYPE_SQLSERVER = "SQLSERVER";
	public static final String DBTYPE_DB2 = "DB2";
	public static String URL_ORACLE = "jdbc:oracle:thin:@127.0.0.1:1521:ncdb";
	public static String URL_SQLSERVER = "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=ncdb";
	public static String URL_DB2 = "jdbc:db2://127.0.0.1:50000/ncdb";
	public static final String MESSAGE = "���汾��Eclipse�����²��԰棬�緢���κ������뼰ʱ������ϵ�����䣺qbsqbs_009@163.com \n��.Ĭ�����·��Ϊ C:\\EXP_SQL\\ ������ȷ�ϴ��ļ�����û�������ű��ļ��Է�ֹ���ǣ�\n��.ϵͳ������δ�ṩ�Զ���������Ϊ���˾����ֳ����ø���һЩ��\n��.�ڵ�Ǩ��ʱ���Զ�����ԭ�ڵ��ɾ���ű�,��ע�⡣\n��.SQLSERVER��\n     ���ӷ�ʽһ��(SQL2000��ҪSP4������SQL2005δ���Թ�):\n     jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=���ݿ�����(ע�ⲻҪ�пո�)\n     ���ӷ�ʽ����jdbc:odbc: <����Դ����> ��\n��.ORACLE Ҳ���Բ��õڶ��� ODBC ���ӷ�ʽ��\n��.DB2��Ŀǰ���ӷ�ʽ��jdbc:db2\n��.������ϸ�����������޸ģ�\n\n��.0.7.0�������ܣ�\n��.1.�ڵ�Ǩ��ʱ����ͬʱ�����¾ɽڵ��ɾ���ű������㷴��Ǩ��\n��.2.������ɾ���ű���ɾ���ӱ����ݣ���ɾ���������ݣ���ֹ���Լ��\n";

	private String sourceDbAddress;
	private String sourceDbPort;
	private String sourceDbSid;
	private String sourceUserName;
	private String sourcePassword;

	private String destDbAddress;
	private String destDbPort;
	private String destDbSid;
	private String destUserName;
	private String destPassword;

	private String funCode;
	private String connStr;

	private boolean isDetail;// �Ƿ�������
	private String procVersion;
	private String busiType;
	private String billType;
	private String destFunCode;
	private String orgiNode;
	private String filepath;
	private String user_sql;
//	private boolean userApponit;
	private boolean isNodeMove;

	public String getBillType() {
		return this.billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getBusiType() {
		return this.busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public boolean isDetail() {
		return this.isDetail;
	}

	public void setDetail(boolean isDetail) {
		this.isDetail = isDetail;
	}

	// public String getConnStr()
	// {
	// return this.connStr;
	// }

	// public void setConnStr(String connStr)
	// {
	// this.connStr = connStr;
	// }

	public String getFunCode() {
		return this.funCode;
	}

	public void setFunCode(String funCode) {
		this.funCode = funCode;
	}

	public String getUser_sql() {
		return this.user_sql;
	}

	public boolean isNodeMove() {
		return this.isNodeMove;
	}

	public void setNodeMove(boolean isNodeMove) {
		this.isNodeMove = isNodeMove;
	}

	public void setUser_sql(String user_sql) {
		this.user_sql = user_sql;
	}

//	public boolean isUserApponit() {
//		return this.userApponit;
//	}
//
//	public void setUserApponit(boolean userApponit) {
//		this.userApponit = userApponit;
//	}

	public String getFilepath() {
		return this.filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getProcVersion() {
		return this.procVersion;
	}

	public void setProcVersion(String procVersion) {
		this.procVersion = procVersion;
	}

	public String getDestFunCode() {
		return this.destFunCode;
	}

	public void setDestFunCode(String destFunCode) {
		this.destFunCode = destFunCode;
	}

	public String getOrgiNode() {
		return this.orgiNode;
	}

	public void setOrgiNode(String orgiNode) {
		this.orgiNode = orgiNode;
	}

	public String testConValidate() {
		StringBuffer errorMsg = new StringBuffer();

		if ((this.connStr == null) || (this.connStr.length() == 0))
			errorMsg.append("�����������ַ���\n ");

		if ((this.sourceUserName == null)
				|| (this.sourceUserName.length() == 0)) {
			errorMsg.append("���������ݿ��û���\n ");
		}

		return errorMsg.toString();
	}

	public String testExpValidate() {
		StringBuffer errorMsg = new StringBuffer();
//		if (this.userApponit) {
//			if ((this.user_sql == null) || (this.user_sql.length() == 0))
//				errorMsg.append("�������ѯSQL���\n ");
//		} else {
			if ((this.funCode == null) || (this.funCode.length() == 0))
				errorMsg.append("������Ҫ�����ű��Ľڵ��\n ");

			if ((this.isNodeMove)
					&& (((this.destFunCode == null) || (this.destFunCode
							.length() == 0)))) {
				errorMsg.append("������Ŀ��ڵ����\n ");
			}

//		}

		if ((this.connStr == null) || (this.connStr.length() == 0))
			errorMsg.append("�����������ַ���\n ");

		if ((this.sourceUserName == null)
				|| (this.sourceUserName.length() == 0)) {
			errorMsg.append("���������ݿ��û���\n ");
		}

		return errorMsg.toString();
	}

	public Object clone() throws CloneNotSupportedException {
		ExportCondition rtnValue = new ExportCondition();
		rtnValue.setBillType(this.billType);
		rtnValue.setBusiType(this.busiType);
		// rtnValue.setConnStr(this.connStr);
		rtnValue.setDetail(this.isDetail);
		rtnValue.setFunCode(this.funCode);
		
		rtnValue.setSourceDbAddress(this.sourceDbAddress);
		rtnValue.setSourceDbPort(this.sourceDbPort);
		rtnValue.setSourceDbSid(this.sourceDbSid);
		rtnValue.setSourcePassword(this.sourcePassword);
		rtnValue.setSourceUserName(this.sourceUserName);
		
		rtnValue.setDestDbAddress(this.destDbAddress);
		rtnValue.setDestDbPort(this.destDbPort);
		rtnValue.setDestDbSid(this.destDbSid);
		rtnValue.setDestUserName(this.destUserName);
		rtnValue.setDestPassword(this.destPassword);
		
		rtnValue.setProcVersion(this.procVersion);
		rtnValue.setUser_sql(this.user_sql);
//		rtnValue.setUserApponit(this.userApponit);
		rtnValue.setDestFunCode(this.destFunCode);
		rtnValue.setOrgiNode(this.orgiNode);
		return rtnValue;
	}

	public String getSourceDbAddress() {
		return sourceDbAddress;
	}

	public void setSourceDbAddress(String sourceDbAddress) {
		this.sourceDbAddress = sourceDbAddress;
	}

	public String getSourceDbPort() {
		return sourceDbPort;
	}

	public void setSourceDbPort(String sourceDbPort) {
		this.sourceDbPort = sourceDbPort;
	}

	public String getSourceDbSid() {
		return sourceDbSid;
	}

	public void setSourceDbSid(String sourceDbSid) {
		this.sourceDbSid = sourceDbSid;
	}

	public String getSourceUserName() {
		return sourceUserName;
	}

	public void setSourceUserName(String sourceUserName) {
		this.sourceUserName = sourceUserName;
	}

	public String getSourcePassword() {
		return sourcePassword;
	}

	public void setSourcePassword(String sourcePassword) {
		this.sourcePassword = sourcePassword;
	}

	public String getDestDbAddress() {
		return destDbAddress;
	}

	public void setDestDbAddress(String destDbAddress) {
		this.destDbAddress = destDbAddress;
	}

	public String getDestDbPort() {
		return destDbPort;
	}

	public void setDestDbPort(String destDbPort) {
		this.destDbPort = destDbPort;
	}

	public String getDestDbSid() {
		return destDbSid;
	}

	public void setDestDbSid(String destDbSid) {
		this.destDbSid = destDbSid;
	}

	public String getDestUserName() {
		return destUserName;
	}

	public void setDestUserName(String destUserName) {
		this.destUserName = destUserName;
	}

	public String getDestPassword() {
		return destPassword;
	}

	public void setDestPassword(String destPassword) {
		this.destPassword = destPassword;
	}
}