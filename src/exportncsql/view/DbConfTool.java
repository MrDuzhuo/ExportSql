package exportncsql.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * 数据库配置文件
 * @author MrDu
 *
 */
public class DbConfTool {
	
	Properties prop = null; 
//	String readFile = "d:" + File.separator + "dbconf.properties";
	String readFile = this.getClass().getClassLoader().getResource("dbconf.properties").getPath();
	
	public DbConfTool() {
		prop = new Properties();
		try {
			InputStream inputStream = new FileInputStream(readFile);
			prop.load(inputStream);  
            inputStream.close();
		} catch(FileNotFoundException e) {
			System.out.println("读取属性文件--->失败！- 原因：文件路径错误或者文件不存在");   
	        e.printStackTrace();   
		} catch (IOException e) {
			System.out.println("装载文件--->失败!");
			e.printStackTrace();
		}  
	}

	/**
	 * 获取来源数据库配置文件的值
	 * @param column
	 * @return
	 */
	public String getDbConfValue(String column) {
		return prop.getProperty(column) == null ? "" : prop.getProperty(column);
	}

	/**
	 * 设置配置文件的值
	 * @param column
	 * @param value
	 */
	public void setDbConfValue(String column, String value) {
		prop.setProperty(column, value);
	}
	
	/**
	 * 保存配置文件
	 */
	public void save() {
		try {
			OutputStream outputStream = new FileOutputStream(readFile);  
			prop.store(outputStream, null);
			outputStream.close(); 
		} catch(FileNotFoundException e) {
			System.out.println("读取属性文件--->失败！- 原因：文件路径错误或者文件不存在");   
	        e.printStackTrace();   
		} catch (IOException e) {
			System.out.println("装载文件--->失败!");
			e.printStackTrace();
		}
	}
	
}
