package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Monitoramento {
    private enum Tipo {
        PORCENTAGEMUSO,
        FREQUENCIA,
        GBUSO,
        GBDISPONIVEL
    }

    private LocalDateTime dataHora;
    private Double valor;
    private Tipo tipo;
    private Integer fkComponente;

    public Monitoramento(Double valor, Integer fkComponente) {
        this.dataHora = LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss")));
        this.valor = valor;
        this.fkComponente = fkComponente;
    }
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Integer getFkComponente() {
        return fkComponente;
    }

    public void setFkComponente(Integer fkComponente) {
        this.fkComponente = fkComponente;
    }
}
