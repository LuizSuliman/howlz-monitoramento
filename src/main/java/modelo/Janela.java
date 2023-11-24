package modelo;

import java.time.LocalDateTime;

public class Janela {
    private Integer idJanela;
    private Integer pid;
    private String comando;
    private String titulo;
    private Integer visibilidade;
    private Integer fkComputador;
    private LocalDateTime dataHora;

    public Janela() {
    }

    public Janela(Integer pid, String comando, String titulo, Integer visibilidade, Integer fkComputador) {
        this.pid = pid;
        this.comando = comando;
        this.titulo = titulo;
        this.visibilidade = visibilidade;
        this.fkComputador = fkComputador;
        this.dataHora = LocalDateTime.now();
    }

    public Integer getIdJanela() {
        return idJanela;
    }

    public void setIdJanela(Integer idJanela) {
        this.idJanela = idJanela;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getVisibilidade() {
        return visibilidade;
    }

    public void setVisibilidade(Integer visibilidade) {
        this.visibilidade = visibilidade;
    }

    public Integer getFkComputador() {
        return fkComputador;
    }

    public void setFkComputador(Integer fkComputador) {
        this.fkComputador = fkComputador;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return """
                ID: %d
                PID: %d
                Comando: %s
                Título: %s
                Visibilidade: %s
                Data e Hora: %s
                Computador: %d""".formatted(idJanela, pid, comando, titulo, visibilidade, dataHora, fkComputador);
    }
}
