package org.springframework.samples.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.mvc.views.User;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebMvcSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("HibernateJPA");

    EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
    /*EntityManager entityManager = MyFactory.getEntityManager();*/
    EntityTransaction entityTransaction = null;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        try{
            List<User> users;
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            users = entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();

            for (User u: users) {
                auth
                        .inMemoryAuthentication()
                        .withUser(u.getUsername())  // #1
                        .password("{noop}" + u.getPassword())
                        .roles(u.getRole().getName());
            }
            entityTransaction.commit();

        } catch (Exception exception) {

            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            exception.printStackTrace();

        } finally {
            entityManager.close();
        }

    }

    /**
     * We need to ignore resources
     *
     */
    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/**"); // #3
    }

    /**
     * For this to work:
     * We need to return WebMvcSecurityConfig.class in getRootConfigClasses and create SecurityWebInitializer.java
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/sign-in/", "/api/*","/SoapWs").permitAll()
                    .antMatchers("/views/history").hasRole("admin")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/sign-in/")
                    .defaultSuccessUrl("/views/home")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll()
                    .logoutSuccessUrl("/sign-in/")
                    .and()
                /*.sessionManagement()
                    .invalidSessionUrl("/sign-in/")
                    .and()*/
                .csrf().disable();
    }
}
