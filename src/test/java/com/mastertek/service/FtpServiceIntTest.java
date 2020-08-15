package com.mastertek.service;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mastertek.FacetrackerApp;
import com.mastertek.config.ApplicationProperties;
import com.mastertek.domain.Device;
import com.mastertek.domain.Record;
import com.mastertek.domain.RecordSense;
import com.mastertek.domain.enumeration.RecordStatus;
import com.mastertek.repository.DeviceRepository;
import com.mastertek.repository.RecordRepository;
import com.mastertek.repository.RecordSenseRepository;
import com.mastertek.service.util.CountBuddyUtil;
import com.mastertek.service.util.FtpWorkerThread;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = FacetrackerApp.class)
@Transactional
@DirtiesContext
public class FtpServiceIntTest {

	@Autowired
    private ApplicationProperties applicationProperties;
	
	@Autowired
    private RecordRepository recordRepository;
	
	@Autowired
    private RecordSenseRepository recordSenseRepository;
	
	@Autowired
    private DeviceRepository deviceRepository;
	
	
    @Before
    public void setup() {
    	Device device = new Device();
    	device.setDeviceId("733935");
     	deviceRepository.save(device);
    }	
   
    
    @Test
    public void performanceTest() throws Exception {
    	
    	
    }
   
    
    @Test
    public void ftpTest() throws Exception {

    	FileUtils.cleanDirectory(new File(applicationProperties.getFtpDirectory()));
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("faceimages/Face_733935_42839_1566849042090.jpg").getFile());
  
    	CountBuddyUtil.sendFtpFile("localhost", applicationProperties.getFtpPort().intValue(), file,applicationProperties.getFtpDefaultUser(),applicationProperties.getFtpDefaultPassord());
    	
    	List<Record> list = recordRepository.findAll();
    	assertThat(list.size()).isEqualTo(1);
    	assertThat(list.get(0).getStatus()).isEqualTo(RecordStatus.NO_FACE_DETECTED);
    	
    	List<RecordSense> list2 = recordSenseRepository.findAll();
    	assertThat(list2.size()).isEqualTo(1);
    	assertThat(list2.get(0).getStatus()).isEqualTo(RecordStatus.NO_MATCHING);
    }	
    
    @Test
    public void ftpTest2() throws Exception {

    	FileUtils.cleanDirectory(new File(applicationProperties.getFtpDirectory()));
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("faceimages/Face_733935_42839_1566849042091.jpg").getFile());
  
    	CountBuddyUtil.sendFtpFile("localhost", applicationProperties.getFtpPort().intValue(), file,applicationProperties.getFtpDefaultUser(),applicationProperties.getFtpDefaultPassord());
    	
    	List<Record> list = recordRepository.findAll();
    	assertThat(list.size()).isEqualTo(1);
    	assertThat(list.get(0).getStatus()).isEqualTo(RecordStatus.NO_MATCHING);
    	
    	List<RecordSense> list2 = recordSenseRepository.findAll();
    	assertThat(list2.size()).isEqualTo(1);
    	assertThat(list2.get(0).getStatus()).isEqualTo(RecordStatus.WHITE_LIST_DETECTED);
    }	
    
    @Test
	public void performanceTest_2() throws Exception {
    	
    	FileUtils.cleanDirectory(new File(applicationProperties.getFtpDirectory()));
    	ExecutorService executor = Executors.newFixedThreadPool(10);

		String path4= applicationProperties.getPerformanceTestDataDirectory();

		File[] files = CountBuddyUtil.getFileList(path4+"\\input");
		long count = 0;
		long count2;
		for (int i = 0; i < 10000; i++) {
			if (i >= files.length)
				continue;

			Runnable worker = new FtpWorkerThread(files[i],"localhost",applicationProperties.getFtpPort().intValue(),applicationProperties.getFtpDefaultUser(),applicationProperties.getFtpDefaultPassord(),i);
			executor.execute(worker);
			count++;
			System.out.println("i-->" + i + ",count-->" + count);

		}
		
		executor.shutdown();
		executor.awaitTermination(30, TimeUnit.SECONDS);
		
		List<Record> list = recordRepository.findAll();
		assertThat(list.size()).isEqualTo(1813);
	}
   
}



