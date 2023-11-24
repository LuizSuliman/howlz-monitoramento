package dao;

import conexao.ConexaoMySQL;
import conexao.ConexaoSQLServer;
import modelo.Computador;
import modelo.Empresa;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;

public class EmpresaDao {
    public Empresa buscarPeloIdNuvem(Integer idEmpresa) {
        ConexaoSQLServer conexaoServer = new ConexaoSQLServer();
        JdbcTemplate conServer = conexaoServer.getConexaoDoBanco();

        String sql = "SELECT * FROM Empresa WHERE idEmpresa = ?";

        try {
            return conServer.queryForObject(sql, new BeanPropertyRowMapper<>(Empresa.class), idEmpresa);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conServer != null) {
                try {
                    conServer.getDataSource().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Trate a exceção de fechamento da conexão do servidor
                }
            }
        }
    }

    public void atualizarLocal(Empresa empresa) {
        ConexaoMySQL conexao = new ConexaoMySQL();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        String sql = "INSERT INTO Empresa (idEmpresa, razaoSocial, nomeFantasia, apelido, cnpj) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE razaoSocial = ?, nomeFantasia = ?, apelido = ?, cnpj = ?";

        try {
            con.update(sql, empresa.getIdEmpresa(), empresa.getRazaoSocial(), empresa.getNomeFantasia(), empresa.getApelido(), empresa.getCnpj(), empresa.getRazaoSocial(), empresa.getNomeFantasia(), empresa.getApelido(), empresa.getCnpj());
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
