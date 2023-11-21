package modelo;

public class MonitoramentoProcesso extends Monitoramento {
    private Integer idMonitoramentoProcesso;
    private Integer fkProcesso;
    private Integer fkUnidadeMedida;
    private Integer fkTipoComponente;

    public MonitoramentoProcesso() {
    }

    public MonitoramentoProcesso(Double valor, Integer fkProcesso, Integer fkUnidadeMedida, Integer fkTipoComponente) {
        super(valor);
        this.fkProcesso = fkProcesso;
        this.fkUnidadeMedida = fkUnidadeMedida;
        this.fkTipoComponente = fkTipoComponente;
    }

    public Integer getIdMonitoramentoProcesso() {
        return idMonitoramentoProcesso;
    }

    public void setIdMonitoramentoProcesso(Integer idMonitoramentoProcesso) {
        this.idMonitoramentoProcesso = idMonitoramentoProcesso;
    }

    public Integer getFkProcesso() {
        return fkProcesso;
    }

    public void setFkProcesso(Integer fkProcesso) {
        this.fkProcesso = fkProcesso;
    }

    public Integer getFkUnidadeMedida() {
        return fkUnidadeMedida;
    }

    public void setFkUnidadeMedida(Integer fkUnidadeMedida) {
        this.fkUnidadeMedida = fkUnidadeMedida;
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
                %s
                Processo (FK): %d
                Unidade de Medida (FK): %d
                Tipo de Componente (FK): %d
                """.formatted(idMonitoramentoProcesso, super.toString(), fkUnidadeMedida, fkTipoComponente);
    }
}
