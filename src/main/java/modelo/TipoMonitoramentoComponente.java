package modelo;

public class TipoMonitoramentoComponente extends Tipo {
    private Integer idTipoComponente;
    private Integer fkTipoComponente;
    private Integer fkUnidadeMedida;

    public TipoMonitoramentoComponente() {
    }

    public TipoMonitoramentoComponente(String nome, Integer fkTipoComponente, Integer fkUnidadeMedida) {
        super(nome);
        this.fkTipoComponente = fkTipoComponente;
        this.fkUnidadeMedida = fkUnidadeMedida;
    }

    public Integer getIdTipoComponente() {
        return idTipoComponente;
    }

    public void setIdTipoComponente(Integer idTipoComponente) {
        this.idTipoComponente = idTipoComponente;
    }

    public Integer getFkTipoComponente() {
        return fkTipoComponente;
    }

    public void setFkTipoComponente(Integer fkTipoComponente) {
        this.fkTipoComponente = fkTipoComponente;
    }

    public Integer getFkUnidadeMedida() {
        return fkUnidadeMedida;
    }

    public void setFkUnidadeMedida(Integer fkUnidadeMedida) {
        this.fkUnidadeMedida = fkUnidadeMedida;
    }

    @Override
    public String toString() {
        return """
                ID: %d
                %s
                Tipo de Componente (FK): %d
                Unidade de Medida (FK): %d""".formatted(idTipoComponente, super.toString(), fkTipoComponente, fkUnidadeMedida);
    }
}
