package ro.sci.bookwormscommunity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.BookServiceImpl;

@Configuration
@ComponentScan("ro.sci.bookwormscommunity")
public class BeanConfig {

    @Bean
    public BookService bookService() {
        return new BookServiceImpl();
    }

}
