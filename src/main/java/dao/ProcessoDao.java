package dao;

import conexao.ConexaoMySQL;
import conexao.ConexaoSQLServer;
import modelo.Processo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;

public class ProcessoDao {
    public void salvar(Processo processo) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "INSERT INTO Processo (pid, nome, fkComputador, dataHora) VALUES (?, ?, ?, ?)";

        try {
            // Salvando no banco local
            con.update(sql, processo.getPid(), processo.getNome(), processo.getFkComputador(), processo.getDataHora());

            // Salvando no banco do servidor
            conServer.update(sql, processo.getPid(), processo.getNome(), processo.getFkComputador(), processo.getDataHora());

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

    public void salvarMultiplos(List<Processo> processos) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "INSERT INTO Processo (pid, nome, fkComputador, dataHora) VALUES (?, ?, ?, ?)";

        try {
            for (Processo processo : processos) {
                // Salvando no banco local
                con.update(sql, processo.getPid(), processo.getNome(), processo.getFkComputador(), processo.getDataHora());

                // Salvando no banco do servidor
                conServer.update(sql, processo.getPid(), processo.getNome(), processo.getFkComputador(), processo.getDataHora());
            }

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

    public Integer contarTodos() {
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "SELECT COUNT(idProcesso) FROM Processo";

        try {
            // Salvando no banco do servidor
            return conServer.queryForObject(sql, Integer.class);

        } catch (Exception e) {
            // Trate exceções (log, relatório de erro, etc.)
            e.printStackTrace();

        } finally {
            if (conServer != null) {
                try {
                    conServer.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão do servidor
                }
            }
        }
        return null;
    }

    public Processo buscarPeloPidEFkComputador(Integer pid, Integer fkComputador) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "SELECT * FROM Processo WHERE pid = ? AND fkComputador = ? ORDER BY dataHora DESC LIMIT 1";
        String sqlServer = "SELECT TOP 1 * FROM Processo WHERE pid = ? AND fkComputador = ? ORDER BY dataHora DESC";

        try {
            // Buscando no banco local
            Processo processoLocal = con.queryForObject(sql, new BeanPropertyRowMapper<>(Processo.class), pid, fkComputador);

            // Buscando no banco do servidor
            Processo processoServer = conServer.queryForObject(sqlServer, new BeanPropertyRowMapper<>(Processo.class), pid, fkComputador);

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
