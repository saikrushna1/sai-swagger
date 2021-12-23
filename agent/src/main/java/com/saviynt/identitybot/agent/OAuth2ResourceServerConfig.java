package com.saviynt.identitybot.agent;
//package com.peddle.digitals.cobot.agent;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//
//@Configuration
//@EnableResourceServer
//public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
//    
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//        .authorizeRequests()
//        .antMatchers("/agent/api/executescript").authenticated();
//    }
//
//    
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//         resources.resourceId(null);
//    }
//}