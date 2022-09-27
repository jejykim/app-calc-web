package com.boras.rateCalc.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {
	
	// @ConfigurationProperties : 프로퍼티 설절파일을 읽어들이는 어노테이션
	// application.properties의 설정한 커스텀데이터소스 설정부분을 prefix의 값으로 설정한다.
	@Bean
	@ConfigurationProperties("spring.datasource.hikari.bootdb1")
	public DataSource bootdb1DataSource() {
	    return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}
	
}