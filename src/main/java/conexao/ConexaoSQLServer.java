package conexao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class ConexaoSQLServer {
    private JdbcTemplate conexaoDoBanco;

    public ConexaoSQLServer() {
        BasicDataSource dataSource = new BasicDataSource();
        /*
            Exemplo de driverClassName:
                org.h2.Driver
                com.mysql.cj.jdbc.Driver <- EXEMPLO PARA MYSQL
                com.microsoft.sqlserver.jdbc.SQLServerDriver <- EXEMPLO PARA SQL SERVER
        */
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        /*
            Exemplo de string de conexões:
                jdbc:h2:file:./teste_banco
                jdbc:mysql://localhost:3306/mydb <- EXEMPLO PARA MYSQL
                jdbc:sqlserver://localhost:1433;database=mydb <- EXEMPLO PARA SQL SERVER
        */
        dataSource.setUrl("jdbc:sqlserver://34.206.56.159:1433;databaseName=howlz;encrypt=false;trustServerCertificate=true");
        // ALTERAR VALORES ABAIXO PARA SUAS CREDENCIAIS DO SQL SERVER
        dataSource.setUsername("sa");
        dataSource.setPassword("urubu100");

        conexaoDoBanco = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getConexaoDoBanco() {
        return conexaoDoBanco;
    }
}
