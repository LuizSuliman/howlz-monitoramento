package dao;

import conexao.Conexao;
import modelo.Janela;
import org.springframework.jdbc.core.JdbcTemplate;

public class JanelaDao {
    public void salvar(Janela janela) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        con.update("INSERT INTO Janela (pid, idLocalJanela, comando, titulo, posicao, visibilidade, fkComputador, fkProcesso, dataHora) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", janela.getPid(), janela.getIdLocalJanela(), janela.getComando(), janela.getTitulo(), janela.getPosicao(), janela.getVisibilidade(), janela.getFkComputador(), janela.getFkProcesso(), janela.getDataHora());
    }
}
