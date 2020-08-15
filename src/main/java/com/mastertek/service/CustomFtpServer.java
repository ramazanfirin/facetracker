package com.mastertek.service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.ftpserver.ConnectionConfig;
import org.apache.ftpserver.DataConnectionConfiguration;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.impl.DefaultConnectionConfig;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.SaltedPasswordEncryptor;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.springframework.stereotype.Service;

import com.mastertek.config.ApplicationProperties;

@Service
public class CustomFtpServer {
	
	FaceRecognitionService faceRecognitionService;
	
	FaceRecognitionSenseTimeService faceRecognitionSenseTimeService;
	
	ApplicationProperties applicationProperties;
	
	FtpServer server;
	
	LinkedBlockingQueue<String> quene = new LinkedBlockingQueue<String>(10000);
	 
	public CustomFtpServer(FaceRecognitionService faceRecognitionService,FaceRecognitionSenseTimeService faceRecognitionSenseTimeService,ApplicationProperties applicationProperties) {
		super();
		// TODO Auto-generated constructor stub
		this.faceRecognitionService = faceRecognitionService;
		this.applicationProperties = applicationProperties;
		this.faceRecognitionSenseTimeService = faceRecognitionSenseTimeService;
	}

@PostConstruct
public void init() throws FtpException, IOException {

    FtpServerFactory serverFactory = new FtpServerFactory();
    serverFactory.getFtplets().put("uploadNOtify",  new MyFtplet(faceRecognitionService,applicationProperties.getFtpDirectory(),applicationProperties,faceRecognitionSenseTimeService));
	
    PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
	
	File ftpDirectory = new File(applicationProperties.getFtpDirectory());
	if(!ftpDirectory.exists())
		ftpDirectory.mkdir();

	File passwordFile = new File(applicationProperties.getPassowordFile());
	if(!passwordFile.exists())
		passwordFile.createNewFile();
	
	userManagerFactory.setFile(passwordFile);
	userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
	UserManager um = userManagerFactory.createUserManager();
	if(um.getUserByName(applicationProperties.getFtpDefaultUser())==null) {
	
		BaseUser user = new BaseUser();
		user.setName(applicationProperties.getFtpDefaultUser());
		user.setPassword(applicationProperties.getFtpDefaultPassord());
		user.setHomeDirectory(applicationProperties.getFtpDirectory());
		user.setEnabled(true);
	
		
		um.save(user);
	}
	
	 serverFactory.setUserManager(userManagerFactory.createUserManager());
	 
	 
	
	 
	 DataConnectionConfigurationFactory dataConnectionConfigurationFactory = new DataConnectionConfigurationFactory();
	 //dataConnectionConfigurationFactory.setPassiveAddress(applicationProperties.getPassiveAddress());
	 dataConnectionConfigurationFactory.setPassiveExternalAddress(applicationProperties.getPassiveExternalAddress());
	 dataConnectionConfigurationFactory.setPassivePorts(applicationProperties.getPassivePorts());
	 DataConnectionConfiguration dataConnectionConfig = dataConnectionConfigurationFactory.createDataConnectionConfiguration();

	 ListenerFactory listenerFactory = new ListenerFactory();
	 listenerFactory.setPort(applicationProperties.getFtpPort().intValue());
	 listenerFactory.setDataConnectionConfiguration(dataConnectionConfig);
	 
	 
	  org.apache.ftpserver.listener.Listener listener = listenerFactory.createListener();
	 
	  serverFactory.addListener("default", listener);
	
	  
	  server = serverFactory.createServer();
	  ConnectionConfig connectionConfig = new DefaultConnectionConfig(true,500,600,10,3,1000);
		 
	  serverFactory.setConnectionConfig(connectionConfig);
	 
	
	  server.start();
	  System.out.println("ftp server calisti");
}

public LinkedBlockingQueue<String> getQuene() {
	return quene;
}

public void setQuene(LinkedBlockingQueue<String> quene) {
	this.quene = quene;
}

@PreDestroy
public void stop() {
	server.stop();
}
}
