package dtri.com.config;

/*
@Configuration
@ComponentScan(basePackages = "dtri.com.tw.config")
@PropertySource(value = { "classpath:application.properties" })

 
    @Autowired
    private Environment envMssql;
 
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(envMssql.getRequiredProperty("jdbc.mssql.driverClassName"));
        dataSource.setUrl(envMssql.getRequiredProperty("jdbc.mssql.url"));
        dataSource.setUsername(envMssql.getRequiredProperty("jdbc.mssql.username"));
        dataSource.setPassword(envMssql.getRequiredProperty("jdbc.mssql.password"));
        return dataSource;
    }
 
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        return jdbcTemplate;
    }
 */
public class ApplicationConfig {
}
