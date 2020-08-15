package com.mastertek.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mastertek.web.rest.util.sensetime.LoginVM;
import com.mastertek.web.rest.util.sensetime.SearchResult;

@Service
public class SenseTimeService {

	@Autowired
	ObjectMapper objectMapper;
	
	String endpoint = "http://sakm.sytes.net:8080";
	
	public String login() throws ClientProtocolException, IOException{
		
		HttpClient client = HttpClientBuilder.create().build();
		
		HttpPost httpPost = new HttpPost(endpoint+"/api/json");
		LoginVM loginVM = new LoginVM("Teknoloji", "Tekn0l0ji");  
		
		String json = objectMapper.writeValueAsString(loginVM);
	    StringEntity entity = new StringEntity(json);
	    httpPost.setEntity(entity);
	    httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");

		Long start = System.currentTimeMillis();
		HttpResponse response = client.execute(httpPost);
		Long end = System.currentTimeMillis();
		Long duration = end - start;

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {

			throw new RuntimeException("Terminal Connection Exception");
		}
		String result = EntityUtils.toString(response.getEntity());

		JsonNode actualObj = objectMapper.readTree(result);
		Long code = actualObj.get("code").asLong();
		if(code!=0)
			throw new RuntimeException(result);
		
		return actualObj.get("data").asText();
	}
	
	public Boolean hasFace(String path,String token) throws ClientProtocolException, IOException{
		
		HttpClient client = HttpClientBuilder.create().build();
		
		HttpPost httpPost = new HttpPost(endpoint+"/api/form");
		
		FileBody fileBody = new FileBody(new File(path), ContentType.DEFAULT_BINARY);
		StringBody msg_id = new StringBody("769", ContentType.MULTIPART_FORM_DATA);
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("img", fileBody);
		builder.addPart("msg_id", msg_id);
		HttpEntity entity = builder.build();
		httpPost.setEntity(entity);
		
		httpPost.setHeader("sessionid", token);
//	    httpPost.setHeader("Accept", "application/json");
//	    httpPost.setHeader("Content-type", "application/json");

		Long start = System.currentTimeMillis();
		HttpResponse response = client.execute(httpPost);
		Long end = System.currentTimeMillis();
		Long duration = end - start;

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {

			throw new RuntimeException("Terminal Connection Exception");
		}
		String result = EntityUtils.toString(response.getEntity());

		JsonNode actualObj = objectMapper.readTree(result);
		Long code = actualObj.get("code").asLong();
		if(code!=0)
			return false;
		else
			return true;
		
	}
	
	public SearchResult search(String path,String token) throws ClientProtocolException, IOException{
		
		HttpClient client = HttpClientBuilder.create().build();
		
		HttpPost httpPost = new HttpPost(endpoint+"/api/form");
		
		FileBody fileBody = new FileBody(new File(path), ContentType.DEFAULT_BINARY);
		StringBody msg_id = new StringBody("772", ContentType.MULTIPART_FORM_DATA);
		StringBody lib_ids = new StringBody("5", ContentType.MULTIPART_FORM_DATA);
		StringBody threshold = new StringBody("85", ContentType.MULTIPART_FORM_DATA);
		StringBody topk = new StringBody("1", ContentType.MULTIPART_FORM_DATA);
		StringBody n_topk = new StringBody("1", ContentType.MULTIPART_FORM_DATA);
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("img", fileBody);
		builder.addPart("msg_id", msg_id);
		builder.addPart("lib_ids", lib_ids);
		builder.addPart("threshold", threshold);
		builder.addPart("topk", topk);
		builder.addPart("n_topk", n_topk);
		HttpEntity entity = builder.build();
		httpPost.setEntity(entity);
		
		httpPost.setHeader("sessionid", token);
//	    httpPost.setHeader("Accept", "application/json");
//	    httpPost.setHeader("Content-type", "application/json");

		Long start = System.currentTimeMillis();
		HttpResponse response = client.execute(httpPost);
		Long end = System.currentTimeMillis();
		Long duration = end - start;

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {

			throw new RuntimeException("Terminal Connection Exception");
		}
		String result = EntityUtils.toString(response.getEntity());

 		JsonNode actualObj = objectMapper.readTree(result);
		Long code = actualObj.get("code").asLong();
		if(code!=0)
			throw new RuntimeException("Senstime Business Exception");
		
		JsonNode data =actualObj.get("data");
		JsonNode ntop =data.get("n_topn");
		
		Long id = null;
		Double similarity = 0d;
		if(ntop.has("id")){
			id = ntop.get("id").asLong();
			similarity = ntop.get("similarity").asDouble();;
		}
		
		return new SearchResult(id, similarity) ;
	}
}
