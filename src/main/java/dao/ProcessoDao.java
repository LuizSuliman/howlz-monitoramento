package dao;

import conexao.Conexao;
import modelo.Processo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProcessoDao {
    public void salvar(Processo processo) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        con.update("INSERT INTO Processo (pid, nome, usoCpu, usoRam, bytesUtilizados, memoriaVirtual, fkComputador, dataHora) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", processo.getPid(), processo.getNome(), BigDecimal.valueOf(processo.getUsoCpu()).setScale(2, RoundingMode.HALF_UP), BigDecimal.valueOf(processo.getUsoRam()).setScale(2, RoundingMode.HALF_UP), processo.getBytesUtilizados(), processo.getMemoriaVirtual(), processo.getFkComputador(), processo.getDataHora());
    }

    public Processo buscarPeloPid(Integer pid) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        return con.queryForObject("SELECT * FROM Processo WHERE pid = ? ORDER BY dataHora DESC LIMIT 1", new BeanPropertyRowMapper<>(Processo.class), pid);
    }
}
