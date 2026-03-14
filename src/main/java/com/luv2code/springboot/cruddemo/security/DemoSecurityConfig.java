package com.luv2code.springboot.cruddemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class DemoSecurityConfig {



    // add support for jdbc no  more hard coded users
    /*
   loads users from the database instead of RAM and follows spring default schema (uers and authorities)
    @Bean

    public UserDetailsManager userDetailsManager(DataSource dataSource){
         return new jdbcUserDetailsManager(dataSource);
    }
    /*
     */

    // custom queries using spring security

    @Bean
     public UserDetailsManager userDetailsManager(DataSource dataSource){

          JdbcUserDetailsManager jdbcUserDetailsManager =new JdbcUserDetailsManager(dataSource);

          jdbcUserDetailsManager.setUsersByUsernameQuery(
                  "select user_id,pw, active from members where user_id=?");


          jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                  "select user_id,role from roles where user_id=?");

          return jdbcUserDetailsManager;
    }
    // provides authorization based on the roles.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
         http.authorizeHttpRequests(configure->
                 configure
                         .requestMatchers(HttpMethod.GET,"/api/employees").hasRole("EMPLOYEE")
                         .requestMatchers(HttpMethod.GET,"/api/employees/**").hasRole("EMPLOYEE")
                         .requestMatchers(HttpMethod.POST,"/api/employees").hasRole("MANAGER")
                         .requestMatchers(HttpMethod.PUT,"/api/employees").hasRole("MANAGER")
                         .requestMatchers(HttpMethod.DELETE,"/api/employees/**").hasRole("ADMIN")

         );

         http.httpBasic(Customizer.withDefaults());

         http.csrf(csrf->csrf.disable());

         return http.build();
    }




  /*
     saves users in RAM not in database
      @Bean
    public InMemoryUserDetailsManager userDetailsManager(){

          UserDetails john= User.builder()
                  .username("john")
                  .password("{noop}test1234")
                  .roles("EMPLOYEE")
                  .build();
        UserDetails mike=User.builder()
                .username("mike")
                .password("{noop}test1234")
                .roles("EMPLOYEE","MANAGER")
                .build();
        UserDetails susan=User.builder()
                .username("susan")
                .password("{noop}test1234")
                .roles("EMPLOYEE","MANAGER","ADMIN")
                .build();

        return new InMemoryUserDetailsManager(john,mike,susan);

        }


   */




}
