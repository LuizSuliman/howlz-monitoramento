package dao;

import conexao.Conexao;
import modelo.Processo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class ProcessoDao {
    public void salvar(Processo processo) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        con.update("INSERT INTO Processo (pid, nome, usoCpu, usoRam, bytesUtilizados, memoriaVirtual, fkComputador, dataHora) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", processo.getPid(), processo.getNome(), processo.getUsoCpu(), processo.getUsoRam(), processo.getBytesUtilizados(), processo.getMemoriaVirtual(), processo.getFkComputador(), processo.getDataHora());
    }

    public Processo buscarPeloPid(Integer pid) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        return con.queryForObject("SELECT * FROM Processo WHERE pid = ? ORDER BY dataHora DESC LIMIT 1", new BeanPropertyRowMapper<>(Processo.class), pid);
    }
}
