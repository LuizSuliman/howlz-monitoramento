package dao;

import conexao.Conexao;
import modelo.Monitoramento;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class MonitoramentoDao {
    public void salvar(Monitoramento novoMonitoramento) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        con.update("INSERT INTO Monitoramento (dataHora, valor, tipo, fkComponente) VALUES (?,?,?,?)", novoMonitoramento.getDataHora(), novoMonitoramento.getValor(), novoMonitoramento.getTipo(), novoMonitoramento.getFkComponente());
    }

    public List<Monitoramento> buscarTodosPeloIdComponente(Integer idComponente) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        return con.query("SELECT * FROM Monitoramento WHERE fkComponente = ?", new BeanPropertyRowMapper<>(Monitoramento.class), idComponente);
    }

    public List<Monitoramento> buscarUltimos15PeloIdComponente(Integer idComponente) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        return con.query("SELECT * FROM Monitoramento WHERE fkComponente = ? LIMIT 15", new BeanPropertyRowMapper<>(Monitoramento.class), idComponente);
    }
}
