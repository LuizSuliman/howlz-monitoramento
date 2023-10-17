package dao;

import conexao.Conexao;
import modelo.Computador;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ComputadorDao {
    public void salvar(Computador novoComputador) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        con.update("INSERT INTO Computador (nome, sistemaOperacional, enderecoIP, numeroSerial, codigo, status, fkEmpresa) VALUES (?,?,?,?,?,?,?)",
                novoComputador.getNome(), novoComputador.getSistemaOperacional(), novoComputador.getEnderecoIP(), novoComputador.getNumeroSerial(), novoComputador.getCodigo(), novoComputador.getStatus(), novoComputador.getFkEmpresa());
    }

    public List<Computador> buscarTodosPeloIdEmpresa(Integer idEmpresa) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        return con.query("SELECT * FROM Computador WHERE fkEmpresa = ?", new BeanPropertyRowMapper<>(Computador.class), idEmpresa);
    }

    public List<Computador> buscarPeloId(Integer idComputador) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        return con.query("SELECT * FROM Computador WHERE idComputador = ?", new BeanPropertyRowMapper<>(Computador.class), idComputador);
    }

    public Computador buscarPeloSerial(String numeroSerial) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        return con.queryForObject("SELECT * FROM Computador WHERE numeroSerial = ?", new BeanPropertyRowMapper<>(Computador.class), numeroSerial);
    }

    public Integer contarPeloSerial(String numeroSerial) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        return con.queryForObject("SELECT COUNT(*) FROM Computador WHERE numeroSerial = ?", Integer.class, numeroSerial);
    }
}
