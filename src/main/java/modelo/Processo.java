package modelo;

import java.time.LocalDateTime;

public class Processo {
    private Integer idProcesso;
    private Integer pid;
    private String nome;
    private Double usoCpu;
    private Double usoRam;
    private String bytesUtilizados;
    private String memoriaVirtual;
    private Integer fkComputador;

    private LocalDateTime dataHora;

    public Processo() {
    }

    public Processo(Integer pid, String nome, Double usoCpu, Double usoRam, String bytesUtilizados, String memoriaVirtual, Integer fkComputador, LocalDateTime dataHora) {
        this.pid = pid;
        this.nome = nome;
        this.usoCpu = usoCpu;
        this.usoRam = usoRam;
        this.bytesUtilizados = bytesUtilizados;
        this.memoriaVirtual = memoriaVirtual;
        this.fkComputador = fkComputador;
        this.dataHora = dataHora;
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

    public Double getUsoCpu() {
        return usoCpu;
    }

    public void setUsoCpu(Double usoCpu) {
        this.usoCpu = usoCpu;
    }

    public Double getUsoRam() {
        return usoRam;
    }

    public void setUsoRam(Double usoRam) {
        this.usoRam = usoRam;
    }

    public String getBytesUtilizados() {
        return bytesUtilizados;
    }

    public void setBytesUtilizados(String bytesUtilizados) {
        this.bytesUtilizados = bytesUtilizados;
    }

    public String getMemoriaVirtual() {
        return memoriaVirtual;
    }

    public void setMemoriaVirtual(String memoriaVirtual) {
        this.memoriaVirtual = memoriaVirtual;
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
        return "Processo{" +
                "idProcesso=" + idProcesso +
                ", pid=" + pid +
                ", nome='" + nome + '\'' +
                ", usoCpu=" + usoCpu +
                ", usoRam=" + usoRam +
                ", bytesUtilizados='" + bytesUtilizados + '\'' +
                ", memoriaVirtual='" + memoriaVirtual + '\'' +
                ", fkComputador=" + fkComputador +
                ", dataHora=" + dataHora +
                '}';
    }
}
