package dao;

import conexao.ConexaoMySQL;
import conexao.ConexaoSQLServer;
import modelo.Janela;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class JanelaDao {
    public void salvar(Janela janela) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "INSERT INTO Janela (pid, comando, titulo, visibilidade, fkComputador, dataHora) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            // Salvando no banco local
            con.update(sql, janela.getPid(), janela.getComando(), janela.getTitulo(), janela.getVisibilidade(), janela.getFkComputador(), janela.getDataHora());

            // Salvando no banco do servidor
            conServer.update(sql, janela.getPid(), janela.getComando(), janela.getTitulo(), janela.getVisibilidade(), janela.getFkComputador(), janela.getDataHora());

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

    public void salvarMultiplos(List<Janela> janelas) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "INSERT INTO Janela (pid, comando, titulo, visibilidade, fkComputador, dataHora) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            for (Janela janela : janelas) {
                // Adiciona o lote de inserção para o banco local
                con.update(sql, janela.getPid(), janela.getComando(), janela.getTitulo(), janela.getVisibilidade(), janela.getFkComputador(), janela.getDataHora());

                // Adiciona o lote de inserção para o banco do servidor
                conServer.update(sql, janela.getPid(), janela.getComando(), janela.getTitulo(), janela.getVisibilidade(), janela.getFkComputador(), janela.getDataHora());
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
}
