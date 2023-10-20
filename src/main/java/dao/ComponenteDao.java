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

        con.update("INSERT INTO Componente (tipo, modelo, fkComputador, numeroSerial) VALUES (?,?,?,?)", novoComponente.getTipo(), novoComponente.getModelo(), novoComponente.getFkComputador(), novoComponente.getNumeroSerial());
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

    public Componente buscarSerialDiscoPeloId(Integer id) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        return con.queryForObject("SELECT numeroSerial FROM Componente WHERE tipo = 'DISCO' AND idComponente = ?", (Componente.class), id);
    }
}
