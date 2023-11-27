package dao;

import conexao.ConexaoMySQL;
import conexao.ConexaoSQLServer;
import modelo.AssociacaoComputadorUsuario;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

public class AssociacaoComputadorUsuarioDao {
    public void salvar(AssociacaoComputadorUsuario associacaoComputadorUsuario) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "INSERT INTO AssociacaoComputadorUsuario (fkUsuario, fkComputador, dataAssociacao) VALUES (?, ?, ?)";

        try {
            // Salvando no banco local
            con.update(sql, associacaoComputadorUsuario.getFkUsuario(), associacaoComputadorUsuario.getFkComputador(), associacaoComputadorUsuario.getDataAssociacao());

            // Salvando no banco do servidor
            conServer.update(sql, associacaoComputadorUsuario.getFkUsuario(), associacaoComputadorUsuario.getFkComputador(), associacaoComputadorUsuario.getDataAssociacao());

        } catch (Exception e) {
            // Trate exceções (log, relatório de erro, etc.)
            e.printStackTrace();

        } finally

        {
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
