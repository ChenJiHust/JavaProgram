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
		// 设置请求重试处理，用的是默认的重试处理：请求三次
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			/* 4 判断访问的状态码 */
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}

			/* 5 处理 HTTP 响应内容 */
			// HTTP响应头部信息，这里简单打印
			Header[] headers = getMethod.getResponseHeaders();
			for (Header h : headers)
				System.out.println(h.getName() + " " + h.getValue());
			
//			System.out.println(getMethod.getResponseHeader("Content-Type").getValue());
			// 读取 HTTP 响应内容，这里简单打印网页内容
			//byte[] responseBody = getMethod.getResponseBody();// 读取为字节数组
			//System.out.println(new String(responseBody));
			// 读取为 InputStream，在网页内容数据量大时候推荐使用
			InputStream response = getMethod.getResponseBodyAsStream();
	
			OutputStream out = new FileOutputStream("a.html");			
			byte[] buffer = new byte[4096];
			int byte_read;
			while((byte_read = response.read(buffer)) != -1)
				out.write(buffer, 0, byte_read);
	

		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			/* 6 .释放连接 */
			getMethod.releaseConnection();
		}
	}

}
