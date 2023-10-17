package modelo;

import java.util.ArrayList;
import java.util.List;

public class Componente {
    public enum Tipo {
        CPU,
        RAM,
        DISCO,
        GPU
    }
    private Tipo tipo;
    private String modelo;
    private Integer fkComputador;
    private List<Monitoramento> monitoramentos;

    public Componente(Tipo tipo, String modelo, Integer fkComputador) {
        this.tipo = tipo;
        this.modelo = modelo;
        this.fkComputador = fkComputador;
        this.monitoramentos = new ArrayList<>();
    }

    public String getTipo() {
        return tipo.name();
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getFkComputador() {
        return fkComputador;
    }

    public void setFkComputador(Integer fkComputador) {
        this.fkComputador = fkComputador;
    }

    public List<Monitoramento> getMonitoramentos() {
        return monitoramentos;
    }

    public void setMonitoramentos(List<Monitoramento> monitoramentos) {
        this.monitoramentos = monitoramentos;
    }

    @Override
    public String toString() {
        return "Componente{" +
                "tipo=" + tipo +
                ", modelo='" + modelo + '\'' +
                ", fkComputador=" + fkComputador +
                ", monitoramentos=" + monitoramentos +
                '}';
    }
}
