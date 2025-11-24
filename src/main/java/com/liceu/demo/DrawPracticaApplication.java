package com.liceu.demo;

import com.liceu.demo.filters.SessionInterceptor;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@SpringBootApplication
@Configuration
public class DrawPracticaApplication  implements WebMvcConfigurer {
	@Value("${spring.datasource.url}")
	String datasourceUrl;
	@Value("${spring.datasource.username}")
	String DatasourceUser;
	@Value("${spring.datasource.password}")
	String DatasourcePassword;
	public static void main(String[] args) {
		SpringApplication.run(DrawPracticaApplication.class, args);
	}
	@Autowired
	SessionInterceptor sessionInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionInterceptor)
				.addPathPatterns("/galery");

	}

	@Bean
	public DataSource dataSource(){
		MysqlDataSource mds = new MysqlDataSource();
		mds.setURL(datasourceUrl);
		mds.setUser(DatasourceUser);
		mds.setPassword(DatasourcePassword);
		return mds;
	}
	@Bean
	public JdbcTemplate jdbcTemplate (DataSource dataSource){
		return new JdbcTemplate(dataSource);
	}
}
