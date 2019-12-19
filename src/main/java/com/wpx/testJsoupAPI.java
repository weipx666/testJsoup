package com.wpx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.WildcardType;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class testJsoupAPI {

	//aaaa啊哈哈哈
	@Test
	public void test1() { //解析 http://www.zfsphp.com/ 的首页文章
		try {
			Document doc = Jsoup.connect("http://www.zfsphp.com/").get();
			Elements  urls = doc.select("article .entry-header h1 a[href]");
			
			for (int i = 0; i < urls.size(); i++) {
				String url = urls.get(i).attr("href");
				doc = Jsoup.connect(url).get();
				System.out.println(doc.select("article .entry-header h1 a").text());
				System.out.println(doc.select("article .entry-content").html());
				
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void test10(String imgurl) { //下载图片
		try {
			//String imgurl = "https://www.yudouyudou.com/uploads/allimg/191015/1-1910150U348.jpg";
			
			URL url = new URL(imgurl);
			InputStream in = url.openStream();
			String path = imgurl.substring(imgurl.indexOf("/", 38)+1,imgurl.length());
	
			File mks = new File("allimg/"+path.split("/")[0]);
			if(!mks.exists())mks.mkdirs();
			
			FileOutputStream fos = new FileOutputStream("allimg/"+path);
			byte[] data = new byte[10*1024];
			int leng = 0;
			while((leng = in.read(data))!=-1) {
				fos.write(data, 0, leng);
			}
			in.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Test
	public void test2() {    
		
		test22("https://www.yudouyudou.com/jiaochengheji/gongjutuijian/1867.html",0, getHeader());
		
	}

	public void test22(String url,int ceng,Map<String,String> headers) { 
		if(ceng>100)return;
		ceng++;
		System.out.println("*********:"+ceng);
		Document doc;
		try {
			doc = Jsoup.connect(url).headers(headers).get();
//			System.out.println(doc);
			System.out.println(doc.select("article .index_about .c_titile").text());
			System.out.println(doc.select("article ul#content").html().substring(0,100).trim());
			System.out.println(doc.select("article ul#content p:eq(1) img").attr("abs:src"));
			String nexturl = doc.select("article .nextinfo .pre a[href]").attr("abs:href");
			
			Elements imgnodes = doc.select("article ul#content img[src]");
			for (Element imgnode : imgnodes) {
				test10(imgnode.attr("abs:src"));
			}
			
			System.out.println(nexturl);
			System.out.println("**************************************************");
			headers.put("Referer", url);
			
			test22(doc.select("article .nextinfo .pre a[href]").attr("abs:href"),ceng,headers);
//			Elements  urls = doc.select(".ouvJEz ._1iTR78 li a[href]");
//			for (Element e : urls) {
//				//System.out.println("************:"+e.attr("abs:href"));
//				test22(e.attr("abs:href"),ceng);
//			}
			
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
		
		
	}
	
	public Map<String,String> getHeader(){
		Map<String,String> headerMap = new HashMap<String,String>();
		String headerStr ="Referer: https://www.yudouyudou.com/jiaochengheji/gongjutuijian/1914.html\r\n" + 
				"Cookie: Hm_lvt_db79ea291a2ec375b2f1fdbe09422217=1574147107,1574190873,1574191261; Hm_lpvt_db79ea291a2ec375b2f1fdbe09422217=1574192764\r\n" + 
				"Sec-Fetch-User: ?1\r\n" + 
				"Upgrade-Insecure-Requests: 1\r\n" + 
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36";
		
		
		String[] headerArray = headerStr.split("\r\n");
		for (String header : headerArray) {
			headerMap.put(header.split(":\\s")[0], header.split(":\\s")[1]);
			System.out.println(header.split(":\\s")[0]+":"+header.split(":\\s")[1]);
		}
		System.out.println(headerMap);
		return headerMap;
	}
	

	
	
	
}
