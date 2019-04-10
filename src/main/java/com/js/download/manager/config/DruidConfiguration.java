package com.js.download.manager.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author du
 * @Title: DruidConfiguration
 * @ProjectName spring-boot-demo
 * @Description: TODO
 * @date 2019-03-11
 */
@Configuration
public class DruidConfiguration {
	private Logger logger = LoggerFactory.getLogger(DruidConfiguration.class);
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource druidDataSource() {
		return new DruidDataSource();
	}

	/**
	 * 注册一个StatViewServlet
	 * 
	 * @return
	 */
	@Bean
	public ServletRegistrationBean DruidStatViewServle() {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
				"/druid/*");
		servletRegistrationBean.addInitParameter("loginUsername", "jsdz");
		servletRegistrationBean.addInitParameter("loginPassword", "jsdz");
		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}

	/**
	 * 注册一个：filterRegistrationBean
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean druidStatFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		// 添加过滤规则.
		filterRegistrationBean.addUrlPatterns("/*");
		// 添加不需要忽略的格式信息.
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}
}
