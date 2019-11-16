
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfiguration {
//    @Bean
//    public ServletRegistrationBean registrationServlet() {
//        ServletRegistrationBean bean = new ServletRegistrationBean(new RegistationServlet(), "/user/register");
//        bean.setLoadOnStartup(1);
//        return bean;
//    }

    @Bean
    public ServletRegistrationBean<LoginServlet> loginServlet() {
        ServletRegistrationBean<LoginServlet> bean = new ServletRegistrationBean<>(new LoginServlet(), "/user/login");
        bean.setLoadOnStartup(1);
        return bean;
    }

//    @Bean
//    public FilterRegistrationBean securityServletFilter() {
//        FilterRegistrationBean bean = new FilterRegistrationBean();
//        bean.setFilter(new SecurityServletFilter());
//        bean.addUrlPatterns("/secured/*");
//        return bean;
//    }
}
