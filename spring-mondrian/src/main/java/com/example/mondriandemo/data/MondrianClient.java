package com.example.mondriandemo.data;

import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Query;
import mondrian.olap.Result;
import mondrian.spi.impl.CatalogLocatorImpl;
import mondrian.spi.impl.IdentityCatalogLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.PrintWriter;

@Service
public class MondrianClient {

    @Autowired
    DataSource dataSource;

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    public void runQuery() {

        System.out.println("Connecting..." +dataSource.toString() );

        getJdbcTemplate().queryForList("select * from salespeople");

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection connection = DriverManager.getConnection(
                "Provider=mondrian;" +
                        "Jdbc=jdbc:h2:mem:testdb;" +
                        "JdbcUser=sa;" +
                        "JdbcPassword=;" +
                        "Catalog=DemoSchema.xml;",
                new ClasspathCatalogLocator()
                 );

        System.out.println("Connected");

        Query query = connection.parseQuery(
                "SELECT {[Measures].[ItemsSold]} on columns," +
                        " {[SalesPerson].children} on rows " +
                        "FROM [Sales] ");


        Result result = connection.execute(query);

        System.out.println("Query finished " + result.getAxes().length + "  ");

        result.print(new PrintWriter(System.out));
        System.out.println("0----------------------------------");
        for (int i = -1; i < result.getAxes().length; i++) {
            System.out.println("Axis #" + (i + 1) + ":");
            //printAxis(pw, i < 0 ? slicerAxis : axes[i]);
        }
    }
}
