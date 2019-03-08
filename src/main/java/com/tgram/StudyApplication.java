package com.tgram;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(FdfsClientConfig.class)
@EnableAutoConfiguration
public class StudyApplication {
	public static void main(String[] args) {
		new SpringApplication(StudyApplication.class).run(args);
	}
}