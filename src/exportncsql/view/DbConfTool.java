package exportncsql.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * ���ݿ������ļ�
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
			System.out.println("��ȡ�����ļ�--->ʧ�ܣ�- ԭ���ļ�·����������ļ�������");   
	        e.printStackTrace();   
		} catch (IOException e) {
			System.out.println("װ���ļ�--->ʧ��!");
			e.printStackTrace();
		}  
	}

	/**
	 * ��ȡ��Դ���ݿ������ļ���ֵ
	 * @param column
	 * @return
	 */
	public String getDbConfValue(String column) {
		return prop.getProperty(column) == null ? "" : prop.getProperty(column);
	}

	/**
	 * ���������ļ���ֵ
	 * @param column
	 * @param value
	 */
	public void setDbConfValue(String column, String value) {
		prop.setProperty(column, value);
	}
	
	/**
	 * ���������ļ�
	 */
	public void save() {
		try {
			OutputStream outputStream = new FileOutputStream(readFile);  
			prop.store(outputStream, null);
			outputStream.close(); 
		} catch(FileNotFoundException e) {
			System.out.println("��ȡ�����ļ�--->ʧ�ܣ�- ԭ���ļ�·����������ļ�������");   
	        e.printStackTrace();   
		} catch (IOException e) {
			System.out.println("װ���ļ�--->ʧ��!");
			e.printStackTrace();
		}
	}
	
}
