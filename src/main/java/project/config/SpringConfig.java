package project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@ComponentScan("project")
@PropertySource("classpath:hibernate.properties")
@EnableTransactionManagement
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;
    private final Environment environment;

    @Autowired
    public SpringConfig(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver =
                new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        registry.viewResolver(resolver);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(
                Objects.requireNonNull(
                        environment.getProperty("hibernate.driver_class")));
        dataSource.setUrl(environment.getProperty("hibernate.connection.url"));
        dataSource.setUsername(environment.getProperty("hibernate.connection.username"));
        dataSource.setPassword(environment.getProperty("hibernate.connection.password"));
        return dataSource;
    }

    /** JdbcTemplate
    В данном проекте JdbcTemplate не нужен, но иногда в сложных проектах
    он используется вместе с Hibernate, поэтому оставлю код его подключения
    ----
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
     */

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect",
                environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql",
                environment.getRequiredProperty("hibernate.show_sql"));
        return properties;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("project");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}

/** @Configuration
Означает, что в это файле находится конфигурация проекта
Как я понимаю, это может быть не один файл
 */
/** @ComponentScan("project")
Указываем Спрингу, какие папки сканировать,
(находящиеся по уровню ниже java)
в которых есть классы и методы,
которые помечены разнми аннотациями
спринга например @Component @Controller
которые могут понадобится спрингу для внедрения
 */
/** @PropertySource("classpath:hibernate.properties")
Здесь указываем, где хранятся настройки и доступы
к  используемой базе данных.
В данном случае этот файл находится в папке resources
Далее в коде подключаем
import org.springframework.core.env.Environment;
для подключения переменной environment
к этим настройкам
*/
/** Интерфейс WebMvcConfigurer реализуется,
когда мы под себя хотим настроить Spring MVC
 */
/** @EnableTransactionManagement
 * Эту аннотацию мы используем когда не хотим сами управлять
 * транзакциями а доверяем Spring управлять ими
 * Он будет их сам открывть или закрывать, нам
 * остается в DAO только открывать или закрывать сессии
 */

/** @EnableWebMvc
эта аннотация разрешает нашему проекту использовать MVC;
Это аналог команды
<mvc:annotation-driven/>
в файле
applicationContextMVC.xml
когда настройка проекта идет через
web.xml и applicationContextMVC.xml
 */
/** @Autowired
Внедрение компонентов может (и рекомендовано) производить через конструктор,
с добавлением (по желанию) аннотации @Autowired (для наглядности)
В данном случае через конструктор мы внедряем applicationContext.
applicationContext мы используем, чтобы настроить в методе
templateResolver() Thymeleaf
*/
/** templateResolver(), templateEngine(), configureViewResolvers()
как я понимаю в этих методах мы подключаем шаблонизатор Thymeleaf
к проекту, с указанием некоторых дополнитеьных настроек.
В частности -
в  templateResolver() мы указываем

templateResolver.setPrefix("/WEB-INF/views/");
- где будут находится файлы представления

templateResolver.setSuffix(".html");
- с каким расширением представлений мы будем работать

templateResolver.setCharacterEncoding("UTF-8");
- желаемая кодировка файлов в представлении
(вкупе с настрйками из MySpringMvcDispatcherServletInitializer
это позволит нормально отображать русский язык)

также для этого в метод configureViewResolvers()
добавляется строка
resolver.setCharacterEncoding("UTF-8");
 */
/** После выполнения всех настроек
можно удалить файл web.xml из папки WEB-INF,
т.к. он не нужен
 */
/** dataSource()
Как я понимаю, данный бин содержит настройки базы данных, которые
будут использоваться JdbcTemplate
 */
/** hibernateProperties()
Метод просто забирает некоторые настройки из файла настроек
для следующего метода sessionFactory()
 */
/** sessionFactory()
С помощью sessionFactory мы получаем Hibernate сессию
в которой в дальнейшем запускаем все команды Hibernate
В данном случае её сосздает Спринг и мы ею пользуемся
когда нужно. Кроме того для этого метода мы указываем
область сканирования - где искать классы с аннотацией
@Entity - в голом Hibernate мы должны были бы
в конфиге указывать каждый такой класс отдельно
 */
/** hibernateTransactionManager()
Этот метод и этот бин нужны для того, чтобы самостоятельно
не создавать транзакции, а поручить это Спрингу
 */