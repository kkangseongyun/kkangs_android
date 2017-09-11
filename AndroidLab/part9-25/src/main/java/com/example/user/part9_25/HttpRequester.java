package com.example.user.part9_25;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class HttpRequester {
	
	HttpTask http;
	
	public void request(String url, HashMap<String, String> param, HttpCallback callback) {
		http = new HttpTask(url, param, callback);
		http.execute();
	}
	
	public void cancel() {
		if(http != null)
			http.cancel(true);
	}
	
	private class HttpTask extends AsyncTask<Void, Void, String> {
		String url;
		HashMap<String, String> param;
		HttpCallback callback;
		
		public HttpTask(String url, HashMap<String, String> param, HttpCallback callback) {
			this.url = url;
			this.param = param;
			this.callback = callback;
		}
		
		@Override
		protected String doInBackground(Void... nothing) {
			String response = "";
			String postData = "";
			PrintWriter pw = null;
			BufferedReader in = null;
			
			//add~~~~~~~~~~~~~~
			try {
				URL text=new URL(url);
				HttpURLConnection http=(HttpURLConnection)text.openConnection();
				http.setRequestProperty("Content-type","application/x-www-form-urlencoded;charset=UTF-8");
				http.setConnectTimeout(10000);
				http.setReadTimeout(10000);
				http.setRequestMethod("POST");
				http.setDoInput(true);
				http.setDoOutput(true);

				if(param != null && param.size()>0){
					Iterator<Map.Entry<String, String>> entries=param.entrySet().iterator();
					int index=0;
					while (entries.hasNext()){
						if(index != 0){
							postData=postData+"&";
						}
						Map.Entry<String, String> mapEntry=(Map.Entry<String, String>) entries.next();
						postData= postData+mapEntry.getKey()+"="+ URLEncoder.encode(mapEntry.getValue(),"UTF-8");
						index++;
					}
					pw=new PrintWriter(new OutputStreamWriter(http.getOutputStream(), "UTF-8"));
					pw.write(postData);
					pw.flush();
				}

				in=new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
				StringBuffer sb=new StringBuffer();
				String inputLine;
				while((inputLine = in.readLine()) != null){
					sb.append(inputLine);
				}
				response=sb.toString();



			}catch (Exception e){
				e.printStackTrace();
			} finally {
				{
					try{
						if(pw != null) pw.close();
						if(in != null) in.close();
					}catch (Exception e){}
				}
			}

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			//add~~~~~~~~~~~
			this.callback.onResult(result);
			
		}
	}

}
