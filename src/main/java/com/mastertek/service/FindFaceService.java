package com.mastertek.service;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class FindFaceService {
	
	@Autowired
	ObjectMapper objectMapper;
	
	public String detectFace(String path) throws ClientProtocolException, IOException {
		HttpClient client = HttpClientBuilder.create().build();
		
		HttpPost post = new HttpPost("http://192.168.122.17/detect");
		FileBody fileBody = new FileBody(new File(path), ContentType.DEFAULT_BINARY);
		StringBody age = new StringBody("true", ContentType.MULTIPART_FORM_DATA);
		StringBody gender = new StringBody("true", ContentType.MULTIPART_FORM_DATA);
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("photo", fileBody);
		builder.addPart("age", age);
		builder.addPart("gender", gender);
		
		HttpEntity entity = builder.build();
		post.setEntity(entity);
		
		String encoding = Base64.getEncoder().encodeToString(("admin:105957Grk.").getBytes());
		post.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);

		Long start = System.currentTimeMillis();
		HttpResponse response = client.execute(post);
		Long end = System.currentTimeMillis();
		Long duration = end - start;

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {

			throw new RuntimeException("Terminal Connection Exception");
		}
		String result = EntityUtils.toString(response.getEntity());

		JsonNode actualObj = objectMapper.readTree(result);
		ArrayNode faces = (ArrayNode) actualObj.get("faces");

		String id = "";
		if(faces.size()>0) {
			JsonNode node  = faces.get(0);
			id = node.get("id").asText();
		}
		
		System.out.println("bitti");
		return id;
	}

	public void search() {
		
	}
}
