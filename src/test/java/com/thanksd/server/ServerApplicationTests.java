package com.thanksd.server;

import com.thanksd.server.config.S3Config;
import com.thanksd.server.service.PreSignedUrlService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ServerApplicationTests {
	@MockBean
	private S3Config s3config;
	@MockBean
	private PreSignedUrlService preSignedUrlService;
	@Test
	void contextLoads() {
	}

}
