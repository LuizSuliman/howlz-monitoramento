package dao;

import conexao.ConexaoMySQL;
import conexao.ConexaoSQLServer;
import modelo.Monitoramento;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

public class MonitoramentoDao {
    public void salvar(Monitoramento novoMonitoramento) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "INSERT INTO Monitoramento (dataHora, valor, tipo, fkComponente) VALUES (?, ?, ?, ?)";

        try {
            // Salvando no banco local
            con.update(sql, novoMonitoramento.getDataHora(), BigDecimal.valueOf(novoMonitoramento.getValor()).setScale(2, RoundingMode.HALF_UP), novoMonitoramento.getTipo(), novoMonitoramento.getFkComponente());

            // Salvando no banco do servidor
            conServer.update(sql, novoMonitoramento.getDataHora(), BigDecimal.valueOf(novoMonitoramento.getValor()).setScale(2, RoundingMode.HALF_UP), novoMonitoramento.getTipo(), novoMonitoramento.getFkComponente());

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

    public List<Monitoramento> buscarTodosPeloIdComponente(Integer idComponente) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "SELECT * FROM Monitoramento WHERE fkComponente = ?";

        try {
            // Buscando no banco local
            List<Monitoramento> monitoramentosLocal = con.query(sql, new BeanPropertyRowMapper<>(Monitoramento.class), idComponente);

            // Buscando no banco do servidor
            List<Monitoramento> monitoramentosServer = conServer.query(sql, new BeanPropertyRowMapper<>(Monitoramento.class), idComponente);

            return (monitoramentosLocal != null) ? monitoramentosLocal : monitoramentosServer;

        } catch (Exception e) {
            // Trate exceções (log, relatório de erro, etc.)
            e.printStackTrace();
            return null;

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

    public List<Monitoramento> buscarUltimos15PeloIdComponente(Integer idComponente) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "SELECT * FROM Monitoramento WHERE fkComponente = ? LIMIT 15";
        try {
            // Buscando no banco local
            List<Monitoramento> monitoramentosLocal = con.query(sql, new BeanPropertyRowMapper<>(Monitoramento.class), idComponente);

            // Buscando no banco do servidor
            List<Monitoramento> monitoramentosServer = conServer.query(sql, new BeanPropertyRowMapper<>(Monitoramento.class), idComponente);

            return (monitoramentosLocal != null) ? monitoramentosLocal : monitoramentosServer;

        } catch (Exception e) {
            // Trate exceções (log, relatório de erro, etc.)
            e.printStackTrace();
            return null;

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
