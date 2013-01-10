package cj.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class TestGetMethod {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// URL url = new URL("http://www.baidu.com");
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				5000);
		GetMethod getMethod = new GetMethod("http://www.baidu.com");
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// �����������Դ����õ���Ĭ�ϵ����Դ�����������
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			/* 4 �жϷ��ʵ�״̬�� */
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}

			/* 5 ���� HTTP ��Ӧ���� */
			// HTTP��Ӧͷ����Ϣ������򵥴�ӡ
			Header[] headers = getMethod.getResponseHeaders();
			for (Header h : headers)
				System.out.println(h.getName() + " " + h.getValue());
			
//			System.out.println(getMethod.getResponseHeader("Content-Type").getValue());
			// ��ȡ HTTP ��Ӧ���ݣ�����򵥴�ӡ��ҳ����
			//byte[] responseBody = getMethod.getResponseBody();// ��ȡΪ�ֽ�����
			//System.out.println(new String(responseBody));
			// ��ȡΪ InputStream������ҳ������������ʱ���Ƽ�ʹ��
			InputStream response = getMethod.getResponseBodyAsStream();
	
			OutputStream out = new FileOutputStream("a.html");			
			byte[] buffer = new byte[4096];
			int byte_read;
			while((byte_read = response.read(buffer)) != -1)
				out.write(buffer, 0, byte_read);
	

		} catch (HttpException e) {
			// �����������쳣��������Э�鲻�Ի��߷��ص�����������
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// ���������쳣
			e.printStackTrace();
		} finally {
			/* 6 .�ͷ����� */
			getMethod.releaseConnection();
		}
	}

}
