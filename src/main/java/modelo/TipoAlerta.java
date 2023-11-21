package modelo;

public class TipoAlerta extends Tipo {
    private Integer idTipoAlerta;

    public TipoAlerta() {
    }

    public TipoAlerta(String nome) {
        super(nome);
    }

    public Integer getIdTipoAlerta() {
        return idTipoAlerta;
    }

    public void setIdTipoAlerta(Integer idTipoAlerta) {
        this.idTipoAlerta = idTipoAlerta;
    }

    @Override
    public String toString() {
        return """
                ID: %d
                """.formatted(idTipoAlerta, super.toString());
    }
}
