package com.mastertek.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Facetracker.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	String ftpDirectory;
	String passowordFile;
	Long ftpPort;
	String passiveExternalAddress;
	String passivePorts;
	String ftpDefaultUser;
	String ftpDefaultPassord;
	String performanceTestDataDirectory;
	
	String dllPath;
	String faceRegPath;
	String licensePath;
	String capturer;
	String recognizer;
	
	public String getFtpDirectory() {
		return ftpDirectory;
	}
	public void setFtpDirectory(String ftpDirectory) {
		this.ftpDirectory = ftpDirectory;
	}
	public String getPassowordFile() {
		return passowordFile;
	}
	public void setPassowordFile(String passowordFile) {
		this.passowordFile = passowordFile;
	}
	public Long getFtpPort() {
		return ftpPort;
	}
	public void setFtpPort(Long ftpPort) {
		this.ftpPort = ftpPort;
	}
	public String getPassiveExternalAddress() {
		return passiveExternalAddress;
	}
	public void setPassiveExternalAddress(String passiveExternalAddress) {
		this.passiveExternalAddress = passiveExternalAddress;
	}
	public String getPassivePorts() {
		return passivePorts;
	}
	public void setPassivePorts(String passivePorts) {
		this.passivePorts = passivePorts;
	}
	public String getFtpDefaultUser() {
		return ftpDefaultUser;
	}
	public void setFtpDefaultUser(String ftpDefaultUser) {
		this.ftpDefaultUser = ftpDefaultUser;
	}
	public String getFtpDefaultPassord() {
		return ftpDefaultPassord;
	}
	public void setFtpDefaultPassord(String ftpDefaultPassord) {
		this.ftpDefaultPassord = ftpDefaultPassord;
	}
	public String getPerformanceTestDataDirectory() {
		return performanceTestDataDirectory;
	}
	public void setPerformanceTestDataDirectory(String performanceTestDataDirectory) {
		this.performanceTestDataDirectory = performanceTestDataDirectory;
	}
	public String getDllPath() {
		return dllPath;
	}
	public void setDllPath(String dllPath) {
		this.dllPath = dllPath;
	}
	public String getFaceRegPath() {
		return faceRegPath;
	}
	public void setFaceRegPath(String faceRegPath) {
		this.faceRegPath = faceRegPath;
	}
	public String getLicensePath() {
		return licensePath;
	}
	public void setLicensePath(String licensePath) {
		this.licensePath = licensePath;
	}
	public String getCapturer() {
		return capturer;
	}
	public void setCapturer(String capturer) {
		this.capturer = capturer;
	}
	public String getRecognizer() {
		return recognizer;
	}
	public void setRecognizer(String recognizer) {
		this.recognizer = recognizer;
	}
	
	
}
