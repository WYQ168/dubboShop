package com.hgz.v17center;


import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


@SpringBootTest
@RunWith(SpringRunner.class)
public class V17CenterApplicationTests {


	@Autowired
	private FastFileStorageClient client;

	@Test
	public void uploadTest() throws FileNotFoundException {
		File file = new File("D:\\aaa.png");
		StorePath storePath = client.uploadFile(
				new FileInputStream(file), file.length(), "png", null);

		System.out.println(storePath.getFullPath());
	}

    @Test
    public void delTest(){
        client.deleteFile("group1/M00/00/00/rBAABF260syARDl4AQhIjt6rXQQ186.png");
        System.out.println("删除成功！");
    }

	@Test
	public void contextLoads() {
	}


}
