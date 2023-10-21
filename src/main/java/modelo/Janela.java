package modelo;

import java.time.LocalDateTime;

public class Janela {
    private Integer idJanela;
    private Integer pid;
    private Integer idLocalJanela;
    private String comando;
    private String titulo;
    private String posicao;
    private Integer visibilidade;
    private Integer fkComputador;
    private Integer fkProcesso;
    private LocalDateTime dataHora;

    public Janela() {
    }

    public Janela(Integer pid, Integer idLocalJanela, String comando, String titulo, String posicao, Integer visibilidade, Integer fkComputador, LocalDateTime dataHora) {
        this.pid = pid;
        this.idLocalJanela = idLocalJanela;
        this.comando = comando;
        this.titulo = titulo;
        this.posicao = posicao;
        this.visibilidade = visibilidade;
        this.fkComputador = fkComputador;
        this.dataHora = dataHora;
    }

    public Janela(Integer pid, Integer idLocalJanela, String comando, String titulo, String posicao, Integer visibilidade, Integer fkComputador, Integer fkProcesso, LocalDateTime dataHora) {
        this.pid = pid;
        this.idLocalJanela = idLocalJanela;
        this.comando = comando;
        this.titulo = titulo;
        this.posicao = posicao;
        this.visibilidade = visibilidade;
        this.fkComputador = fkComputador;
        this.fkProcesso = fkProcesso;
        this.dataHora = dataHora;
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

    public Integer getIdLocalJanela() {
        return idLocalJanela;
    }

    public void setIdLocalJanela(Integer idLocalJanela) {
        this.idLocalJanela = idLocalJanela;
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

    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
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

    public Integer getFkProcesso() {
        return fkProcesso;
    }

    public void setFkProcesso(Integer fkProcesso) {
        this.fkProcesso = fkProcesso;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return "Janela{" +
                "idJanela=" + idJanela +
                ", pid=" + pid +
                ", idLocalJanela=" + idLocalJanela +
                ", comando='" + comando + '\'' +
                ", titulo='" + titulo + '\'' +
                ", posicao='" + posicao + '\'' +
                ", visibilidade=" + visibilidade +
                ", fkComputador=" + fkComputador +
                ", fkProcesso=" + fkProcesso +
                ", dataHora=" + dataHora +
                '}';
    }
}
