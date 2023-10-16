package dao;

import conexao.Conexao;
import modelo.Componente;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ComponenteDao {
    public void salvar(Componente novoComponente) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        con.update("INSERT INTO Componente (tipo, modelo, fkComputador) VALUES (?,?,?)", novoComponente.getTipo(), novoComponente.getModelo(), novoComponente.getFkComputador());
    }

    public List<Componente> buscarTodosPeloIdComputador(Integer idComputador) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        return con.query("SELECT * FROM Componente WHERE fkComputador = ?", new BeanPropertyRowMapper<>(Componente.class), idComputador);
    }

    public List<Componente> buscarPeloTipoEIdComputador(String nomeTipo, Integer idComputador) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        return con.query("SELECT * FROM Componente WHERE tipo = ? AND fkComputador = ?", new BeanPropertyRowMapper<>(Componente.class), nomeTipo, idComputador);
    }
}
