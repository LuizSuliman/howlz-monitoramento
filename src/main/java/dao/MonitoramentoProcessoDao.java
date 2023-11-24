package dao;

import conexao.ConexaoMySQL;
import conexao.ConexaoSQLServer;
import modelo.MonitoramentoProcesso;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

public class MonitoramentoProcessoDao {
    public void salvar(MonitoramentoProcesso novoMonitoramento) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "INSERT INTO MonitoramentoProcesso (dataHora, valor, fkProcesso, fkUnidadeMedida, fkTipoComponente) VALUES (?, ?, ?, ?, ?)";

        try {
            // Salvando no banco local
            con.update(sql, novoMonitoramento.getDataHora(), novoMonitoramento.getValor(), novoMonitoramento.getFkProcesso(), novoMonitoramento.getFkUnidadeMedida(), novoMonitoramento.getFkTipoComponente());

            // Salvando no banco do servidor
            conServer.update(sql, novoMonitoramento.getDataHora(), novoMonitoramento.getValor(), novoMonitoramento.getFkProcesso(), novoMonitoramento.getFkUnidadeMedida(), novoMonitoramento.getFkTipoComponente());

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
