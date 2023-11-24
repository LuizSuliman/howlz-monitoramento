package dao;

import conexao.ConexaoMySQL;
import conexao.ConexaoSQLServer;
import modelo.Computador;
import modelo.Usuario;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

public class UsuarioDao {
    public Integer contarPeloEmailESenha(String email, String senha) {
        // ConexaoMySQL conexaoMySQL = new ConexaoMySQL();
        // JdbcTemplate con = conexaoMySQL.getConexaoDoBanco();
        ConexaoSQLServer conexaoSQLServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoSQLServer.getConexaoDoBanco();

        String sql = "SELECT COUNT(*) FROM Usuario WHERE email = ? AND senha = ?";

        try {
            // Contando no banco local
            // Integer countLocal = con.queryForObject(sql, Integer.class, email, senha);

            // Contando no banco do servidor
            Integer countServer = conServer.queryForObject(sql, Integer.class, email, senha);

            return countServer;

        } catch (Exception e) {
            // Trate exceções (log, relatório de erro, etc.)
            e.printStackTrace();
            return 0;

        } finally {
            // Certifique-se de fechar as conexões, mesmo se ocorrer uma exceção
            /*
            if (con != null) {
                try {
                    con.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão local
                }
            }
            */

            if (conServer != null) {
                try {
                    conServer.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão do servidor
                }
            }
        }
    }

    public Usuario buscarPeloEmailESenha(String email, String senha) {
        // ConexaoMySQL conexao = new ConexaoMySQL();
        // JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "SELECT * FROM Usuario WHERE email = ? AND senha = ?";

        try {
            // Buscando no banco local
            // Usuario usuarioLocal = con.queryForObject(sql, new BeanPropertyRowMapper<>(Usuario.class), email, senha);

            // Buscando no banco do servidor
            Usuario usuarioServer = conServer.queryForObject(sql, new BeanPropertyRowMapper<>(Usuario.class), email, senha);

            // Escolha qual usuário retornar (pode ser lógica de negócios específica)
            return usuarioServer;

        } catch (Exception e) {
            // Trate exceções (log, relatório de erro, etc.)
            e.printStackTrace();
            return null;

        } finally {
            // Certifique-se de fechar as conexões, mesmo se ocorrer uma exceção
            /*
            if (con != null) {
                try {
                    con.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão local
                }
            }
            */

            if (conServer != null) {
                try {
                    conServer.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão do servidor
                }
            }
        }
    }

    public void atualizarLocal(Usuario usuario) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        String sql = "INSERT INTO Usuario (idUsuario, nome, fkEmpresa, fkTipoUsuario) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE nome = ?, fkEmpresa = ?, fkTipoUsuario = ?";

        try {
            con.update(sql, usuario.getIdUsuario(), usuario.getNome(),usuario.getFkEmpresa(), usuario.getFkTipoUsuario(), usuario.getNome(),usuario.getFkEmpresa(), usuario.getFkTipoUsuario());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão local
                }
            }
        }
    }
}
