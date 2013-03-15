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
	public static final String MESSAGE = "本版本是Eclipse插件版β测试版，如发现任何问题请及时与我联系，邮箱：qbsqbs_009@163.com \n■.默认输出路径为 C:\\EXP_SQL\\ ，事先确认此文件夹内没有其他脚本文件以防止覆盖！\n■.系统参数表未提供自动导出，因为本人觉得现场配置更快一些。\n■.节点迁移时会自动生成原节点的删除脚本,请注意。\n■.SQLSERVER：\n     连接方式一、(SQL2000需要SP4补丁，SQL2005未测试过):\n     jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=数据库名称(注意不要有空格)\n     连接方式二、jdbc:odbc: <数据源名称> 。\n■.ORACLE 也可以采用第二种 ODBC 连接方式。\n■.DB2：目前连接方式：jdbc:db2\n■.具体详细导出请自行修改！\n\n■.0.7.0新增功能：\n■.1.节点迁移时可以同时生成新旧节点的删除脚本，方便反复迁移\n■.2.导出的删除脚本先删除子表数据，后删除主表数据，防止外键约束\n";

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

	private boolean isDetail;// 是否级联导出
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
			errorMsg.append("请输入连接字符串\n ");

		if ((this.sourceUserName == null)
				|| (this.sourceUserName.length() == 0)) {
			errorMsg.append("请输入数据库用户名\n ");
		}

		return errorMsg.toString();
	}

	public String testExpValidate() {
		StringBuffer errorMsg = new StringBuffer();
//		if (this.userApponit) {
//			if ((this.user_sql == null) || (this.user_sql.length() == 0))
//				errorMsg.append("请输入查询SQL语句\n ");
//		} else {
			if ((this.funCode == null) || (this.funCode.length() == 0))
				errorMsg.append("请输入要导出脚本的节点号\n ");

			if ((this.isNodeMove)
					&& (((this.destFunCode == null) || (this.destFunCode
							.length() == 0)))) {
				errorMsg.append("请输入目标节点编码\n ");
			}

//		}

		if ((this.connStr == null) || (this.connStr.length() == 0))
			errorMsg.append("请输入连接字符串\n ");

		if ((this.sourceUserName == null)
				|| (this.sourceUserName.length() == 0)) {
			errorMsg.append("请输入数据库用户名\n ");
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