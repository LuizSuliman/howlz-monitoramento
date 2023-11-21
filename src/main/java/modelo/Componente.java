package modelo;

import java.util.ArrayList;
import java.util.List;

public class Componente {
    private Integer idComponente;
    private String modelo;
    private String identificador;
    private Integer fkComputador;
    private Integer fkTipoComponente;
    private List<Monitoramento> monitoramentos;

    public Componente() {
    }

    public Componente(String modelo, String identificador, Integer fkComputador, Integer fkTipoComponente) {
        this.modelo = modelo;
        this.identificador = identificador;
        this.fkComputador = fkComputador;
        this.fkTipoComponente = fkTipoComponente;
        this.monitoramentos = new ArrayList<>();
    }

    public Integer getIdComponente() {
        return idComponente;
    }

    public void setIdComponente(Integer idComponente) {
        this.idComponente = idComponente;
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

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Integer getFkTipoComponente() {
        return fkTipoComponente;
    }

    public void setFkTipoComponente(Integer fkTipoComponente) {
        this.fkTipoComponente = fkTipoComponente;
    }

    @Override
    public String toString() {
        return """
                ID: %d
                Modelo: %s
                Identificador: %s
                Computador (FK): %d
                Tipo de Componente: %d""".formatted(idComponente, modelo, identificador, fkComputador, fkTipoComponente);
    }
}
