package deu.cse.spring_webmail;

import java.io.IOException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;

@ComponentScan
@SpringBootApplication
@Slf4j
public class SpringWebmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebmailApplication.class, args);
    }
    @Bean(name="systemProperties")
    public PropertiesFactoryBean systemProperties() {
        log.debug("systemProperties() called...");
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("/system.properties"));
        try {
            Properties props = bean.getObject();
        } catch (IOException ex) {
            log.error("configProperties: 예외 = {}", ex.getMessage());
        }
        
        return bean;
    }

}
