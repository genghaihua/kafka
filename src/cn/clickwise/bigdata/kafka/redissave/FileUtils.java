package cn.clickwise.bigdata.kafka.redissave;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {
	private static final int ADX_IMP_TIME = 0;
	private static final int ADX_IMP_USERID = 1;
	private static final int ADX_IMP_IP = 2;
	private static final int ADX_IMP_SID = 3;
	private static final int ADX_IMP_LOCID = 4;
	private static final int ADX_IMP_POSITION = 5;
	private static final int ADX_IMP_LOC = 6;
	private static final int ADX_IMP_FID = 7;
	private static final int ADX_IMP_HOST = 8;
	private static final int ADX_IMP_TITLE = 9;
	private static final int ADX_IMP_HOSTCATE = 10;
	private static final int ADX_IMP_PRICE = 11;
	private static final int ADX_IMP_UA = 12;
	private static final int ADX_IMP_LEN = 13;

	// private static final String LOCID="t6403";
	private static final String DATATYPE = "DEFAULT_NSTAT";

	private static String getTime(String timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(Long.parseLong(timestamp) * 1000l)).toString();
	}

	private static String formatLine1(String line) {
		String[] datas = line.split("\\t");
		if (datas.length != ADX_IMP_LEN) {
			return "";
		}
		String timestamp = datas[ADX_IMP_TIME];
		String time = getTime(timestamp);
		String uid = datas[ADX_IMP_USERID];
		String sip = datas[ADX_IMP_IP];
		String dip = sip;
		String loc = datas[ADX_IMP_LOC];
		String url = datas[ADX_IMP_HOST];
		String host = "";
		String uri = "";
		if (url.length() >= 9) {
			if (url.startsWith("http://")) {
				url = url.substring(7);
				int index = url.indexOf("/");
				if (index != -1) {
					host = url.substring(0, index);
					uri = url.substring(index);
				}
			} else if (url.startsWith("https://")) {
				url = url.substring(8);
				int index = url.indexOf("/");
				if (index != -1) {
					host = url.substring(0, index);
					uri = url.substring(index);
				}
			}
		}
		String refer = "NA";
		String cookie = "uid=" + uid;
		String locid = datas[ADX_IMP_FID];
		String ua = datas[ADX_IMP_UA];
		return locid + "\001" + time + "\001" + DATATYPE + "\001" + sip + "\001" + dip + "\001" + host + "\001" + uri
				+ "\001" + refer + "\001" + cookie + "\001" + loc + "\001" + ua;

	}
	private static final long nowstamp=System.currentTimeMillis()/1000;
	/**
	 * 过滤900秒以前的数据
	 * @param line
	 * @return
	 */
	private static String formatLine(String line) {
		String[] datas = line.split("\\t");
		if (datas.length != ADX_IMP_LEN) {
			return "";
		}
		String timestamp = datas[ADX_IMP_TIME];
		if((nowstamp-Long.parseLong(timestamp))>900){
			return "";
		}
		String time = getTime(timestamp);
		String uid = datas[ADX_IMP_USERID];
		String sip = datas[ADX_IMP_IP];
		String dip = sip;
		String loc = datas[ADX_IMP_LOC];
		String url = datas[ADX_IMP_HOST];
		String host = "";
		String uri = "";
		if (url.length() >= 9) {
			if (url.startsWith("http://")) {
				url = url.substring(7);
				int index = url.indexOf("/");
				if (index != -1) {
					host = url.substring(0, index);
					uri = url.substring(index);
				}
			} else if (url.startsWith("https://")) {
				url = url.substring(8);
				int index = url.indexOf("/");
				if (index != -1) {
					host = url.substring(0, index);
					uri = url.substring(index);
				}
			}
		}
		String refer = "NA";
		String cookie = "uid=" + uid;
		String locid = datas[ADX_IMP_FID];
		String ua = datas[ADX_IMP_UA];
		return locid + "\001" + time + "\001" + DATATYPE + "\001" + sip + "\001" + dip + "\001" + host + "\001" + uri
				+ "\001" + refer + "\001" + cookie + "\001" + loc + "\001" + ua;

	}
	
	private static void readFile() {
		// File ff = new File("C:\\Users\\genghaihua\\Downloads\\aaa.txt");
		File ff = new File("C:\\Users\\genghaihua\\Downloads\\BidImp_2016081818_192.168.10.68.log");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(ff));
			String tempString = null;
			// int line = 1;
			while ((tempString = reader.readLine()) != null) {
				// System.err.println(tempString);
				System.err.println(formatLine(tempString));
				// break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		readFile();
	}
}
