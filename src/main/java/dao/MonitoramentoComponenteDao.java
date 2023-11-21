package dao;

import conexao.ConexaoMySQL;
import conexao.ConexaoSQLServer;
import modelo.MonitoramentoComponente;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

public class MonitoramentoComponenteDao {
    public void salvar(MonitoramentoComponente novoMonitoramento) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "INSERT INTO MonitoramentoComponente (dataHora, valor, fkTipoMonitoramentoComponente, fkComponente) VALUES (?, ?, ?, ?)";

        try {
            // Salvando no banco local
            con.update(sql, novoMonitoramento.getDataHora(), novoMonitoramento.getValor(), novoMonitoramento.getFkTipoMonitoramentoComponente(), novoMonitoramento.getFkComponente());

            // Salvando no banco do servidor
            conServer.update(sql, novoMonitoramento.getDataHora(), novoMonitoramento.getValor(), novoMonitoramento.getFkTipoMonitoramentoComponente(), novoMonitoramento.getFkComponente());

        } catch (Exception e) {
            // Trate exceções (log, relatório de erro, etc.)
            e.printStackTrace();

        } finally {
            // Certifique-se de fechar as conexões, mesmo se ocorrer uma exceção
            if (con != null) {
                try {
                    con.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão local
                }
            }

            if (conServer != null) {
                try {
                    conServer.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão do servidor
                }
            }
        }
    }
}
