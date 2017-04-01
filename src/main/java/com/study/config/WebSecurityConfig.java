package com.study.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.study.security.MyFilterSecurityInterceptor;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
//	@Resource
//	private DataSource dataSource;
	
	@Resource(name="myUserDetailService")
	private UserDetailsService myUserDetailService;
	
	@Resource
	private MyFilterSecurityInterceptor myFilterSecurityInterceptor;
	
	
//	@Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {  
//		return super.authenticationManagerBean();  
//    }  
	
	@Override
	public void configure(WebSecurity web)throws Exception {
		// 设置不拦截规则
		//web.ignoring().antMatchers("/css/**","/js/**","/img/**","/font-awesome/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	/*	http.authorizeRequests()
		.anyRequest().authenticated()
		.and()
		.formLogin().and()
		.httpBasic();*/
		  http
	        .addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class)//在正确的位置添加我们自定义的过滤器  
	        .authorizeRequests()
	        .antMatchers("/css/**","/js/**","/img/**","/font-awesome/**").permitAll()
	        .anyRequest().authenticated();
//	       .and().formLogin().and()
//	        .httpBasic();
		// 自定义登录页面
		http.formLogin().loginPage("/jsp/login.jsp")
				.failureUrl("/jsp/login.jsp?error=1")
				.loginProcessingUrl("/spring_security_check")
				.usernameParameter("username")
				.passwordParameter("password").permitAll().defaultSuccessUrl("/index.do");
	}
	
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
	
		//启用内存用户存储
		/*auth.inMemoryAuthentication()
		.withUser("user1").password("123456").roles("USER").and()
		.withUser("admin").password("admin").roles("USER","ADMIN");*/
		//
		//给予数据库表认证
		/*auth.jdbcAuthentication().dataSource(dataSource)
		.usersByUsernameQuery("select username,password,enable from t_user where username=?")
		.authoritiesByUsernameQuery("select username,rolename from t_role where username=?");
		*/
		//配置自定义的用户服务
		auth.userDetailsService(myUserDetailService);
		
	}
	
	
	
}
