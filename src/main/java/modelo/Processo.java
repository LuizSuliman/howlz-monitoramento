package modelo;

import java.time.LocalDateTime;

public class Processo {
    private Integer idProcesso;
    private Integer pid;
    private String nome;
    private Integer fkComputador;
    private LocalDateTime dataHora;

    public Processo() {
    }

    public Processo(Integer pid, String nome, Integer fkComputador) {
        this.pid = pid;
        this.nome = nome;
        this.fkComputador = fkComputador;
        this.dataHora = LocalDateTime.now();
    }

    public Integer getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(Integer idProcesso) {
        this.idProcesso = idProcesso;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
                Nome: %s
                Computador (FK):
                Data e Hora: %s
                """.formatted(idProcesso, pid, nome, fkComputador, dataHora);
    }
}
