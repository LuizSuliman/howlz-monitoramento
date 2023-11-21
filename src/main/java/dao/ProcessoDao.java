package dao;

import conexao.ConexaoMySQL;
import conexao.ConexaoSQLServer;
import modelo.Processo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

public class ProcessoDao {
    public void salvar(Processo processo) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "INSERT INTO Processo (pid, nome, usoCpu, usoRam, bytesUtilizados, memoriaVirtual, fkComputador, dataHora) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            // Salvando no banco local
            con.update(sql, processo.getPid(), processo.getNome(), BigDecimal.valueOf(processo.getUsoCpu()).setScale(2, RoundingMode.HALF_UP), BigDecimal.valueOf(processo.getUsoRam()).setScale(2, RoundingMode.HALF_UP), processo.getBytesUtilizados(), processo.getMemoriaVirtual(), processo.getFkComputador(), processo.getDataHora());

            // Salvando no banco do servidor
            conServer.update(sql, processo.getPid(), processo.getNome(), BigDecimal.valueOf(processo.getUsoCpu()).setScale(2, RoundingMode.HALF_UP), BigDecimal.valueOf(processo.getUsoRam()).setScale(2, RoundingMode.HALF_UP), processo.getBytesUtilizados(), processo.getMemoriaVirtual(), processo.getFkComputador(), processo.getDataHora());

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

    public Processo buscarPeloPid(Integer pid) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "SELECT * FROM Processo WHERE pid = ? ORDER BY dataHora DESC LIMIT 1";

        try {
            // Buscando no banco local
            Processo processoLocal = con.queryForObject(sql, new BeanPropertyRowMapper<>(Processo.class), pid);

            // Buscando no banco do servidor
            Processo processoServer = conServer.queryForObject(sql, new BeanPropertyRowMapper<>(Processo.class), pid);

            // Escolha qual processo retornar (pode ser lógica de negócios específica)
            return (processoLocal != null) ? processoLocal : processoServer;

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
