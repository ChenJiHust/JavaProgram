package cj.crawler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class FileDownLoader {

	/**
	 * ���� url ����ҳ����������Ҫ�������ҳ���ļ��� ȥ���� url �з��ļ����ַ�
	 */
	public String getFileNameByUrl(String url, String contentType) {
		url = url.substring(7);// remove http://
		if (contentType.indexOf("html") != -1)// text/html
		{
			url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
			return url;
		} else// ��application/pdf
		{
			return url.replaceAll("[\\?/:*|<>\"]", "_") + "."
					+ contentType.substring(contentType.lastIndexOf("/") + 1);
		}
	}

	/**
	 * ������ҳ�ֽ����鵽�����ļ� filePath ΪҪ������ļ�����Ե�ַ
	 */
	@SuppressWarnings("unused")
	private void saveToLocal(byte[] data, String filePath) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					new File(filePath)));
			for (int i = 0; i < data.length; i++)
				out.write(data[i]);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* ���� url ָ�����ҳ */
	public String downloadFile(String url) {
		String filePath = null;
		/* 1.���� HttpClinet �������ò��� */
		HttpClient httpClient = new HttpClient();
		// ���� Http ���ӳ�ʱ 5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				5000);

		/* 2.���� GetMethod �������ò��� */
		GetMethod getMethod = new GetMethod(url);
		// ���� get ����ʱ 5s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// �����������Դ���
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());

		/* 3.ִ�� HTTP GET ���� */
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			// �жϷ��ʵ�״̬��
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
				filePath = null;
			}

			/* 4.���� HTTP ��Ӧ���� */
			//byte[] responseBody = getMethod.getResponseBody();// ��ȡΪ�ֽ�����
			// ������ҳ url ���ɱ���ʱ���ļ���
			filePath = "D:\\Java\\program\\workspace\\Crawler\\result\\"
					+ getFileNameByUrl(url, getMethod.getResponseHeader(
							"Content-Type").getValue());
			
			InputStream response = getMethod.getResponseBodyAsStream();			
			OutputStream out = new FileOutputStream(filePath);			
			byte[] buffer = new byte[4096];
			int byte_read;
			while((byte_read = response.read(buffer)) != -1)
				out.write(buffer, 0, byte_read);
			
			//saveToLocal(responseBody, filePath);
		} catch (HttpException e) {
			// �����������쳣��������Э�鲻�Ի��߷��ص�����������
			System.out.println("Please check your provided httpaddress!");
			e.printStackTrace();
		} catch (IOException e) {
			// ���������쳣
			e.printStackTrace();
		} finally {
			// �ͷ�����
			getMethod.releaseConnection();
		}
		return filePath;
	}

	// ���Ե� main ����
	public static void main(String[] args) {
		FileDownLoader downLoader = new FileDownLoader();
		downLoader.downloadFile("http://delivery.acm.org/10.1145/1690000/1687731/p922-abouzeid.pdf?ip=166.111.64.252&acc=ACTIVE%20SERVICE&CFID=164420840&CFTOKEN=21513937&__acm__=1348717613_8c54f09ef9b007013d06219244260f06");
	}
}