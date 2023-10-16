package dao;

import conexao.Conexao;
import modelo.Monitoramento;
import modelo.Usuario;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UsuarioDao {
    public Integer contarPeloEmailESenha(String email, String senha) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        return con.queryForObject("SELECT COUNT(*) FROM Usuario WHERE email = ? AND senha = ?", Integer.class, email, senha);
    }

    public Usuario buscarPeloEmailESenha(String email, String senha) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        return con.queryForObject("SELECT * FROM Usuario WHERE email = ? AND senha = ?", new BeanPropertyRowMapper<>(Usuario.class), email, senha);
    }
}
