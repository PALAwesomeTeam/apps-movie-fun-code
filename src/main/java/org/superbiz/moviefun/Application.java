package org.superbiz.moviefun;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.*;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletRegistrationBean actionServletRegistration(ActionServlet actionServlet) {
        return new ServletRegistrationBean(actionServlet, "/moviefun/*");
    }

    @Bean
    public DatabaseServiceCredentials databaseServiceCredentials(@Value("${VCAP_SERVICES}") String vcapServices){
        return new DatabaseServiceCredentials(vcapServices);
    }

    @Bean
    public DataSource albumsDataSource(DatabaseServiceCredentials serviceCredentials){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(serviceCredentials.jdbcUrl("albums-mysql", "p-mysql"));
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSource(dataSource);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public DataSource moviesDataSource(DatabaseServiceCredentials serviceCredentials){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(serviceCredentials.jdbcUrl("movies-mysql", "p-mysql"));
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSource(dataSource);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public HibernateJpaVendorAdapter hibernateJpaVendorAdapter(){
        HibernateJpaVendorAdapter test = new HibernateJpaVendorAdapter();
        test.setDatabase( Database.MYSQL);
        test.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        test.setGenerateDdl(true);

        return test;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean albumDatabaseEM(DataSource albumsDataSource, HibernateJpaVendorAdapter vendorAdaptor){
        LocalContainerEntityManagerFactoryBean beanFactory = new LocalContainerEntityManagerFactoryBean();
        beanFactory.setDataSource(albumsDataSource);
        beanFactory.setJpaVendorAdapter(vendorAdaptor);
        beanFactory.setPackagesToScan("org.superbiz.moviefun.albums");
        beanFactory.setPersistenceUnitName("albums-pu");

        return beanFactory;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean moviesDatabaseEM(DataSource moviesDataSource, HibernateJpaVendorAdapter vendorAdaptor){
        LocalContainerEntityManagerFactoryBean beanFactory = new LocalContainerEntityManagerFactoryBean();
        beanFactory.setDataSource(moviesDataSource);
        beanFactory.setJpaVendorAdapter(vendorAdaptor);
        beanFactory.setPackagesToScan("org.superbiz.moviefun.movies");
        beanFactory.setPersistenceUnitName("movies-pu");

        return beanFactory;
    }

    @Bean
    public PlatformTransactionManager albumsTransactionManager(EntityManagerFactory albumDatabaseEM){
        JpaTransactionManager tm = new JpaTransactionManager(albumDatabaseEM);
        return tm;
    }

    @Bean
    public PlatformTransactionManager moviesTransactionManager(EntityManagerFactory moviesDatabaseEM){
        JpaTransactionManager tm = new JpaTransactionManager(moviesDatabaseEM);
        return tm;
    }
}
