package modelo;

import java.time.LocalDateTime;

public class AssociacaoComputadorUsuario {
    private Integer idAssociacao;
    private Integer fkUsuario;
    private Integer fkComputador;
    private LocalDateTime dataAssociacao;
    private LocalDateTime dataDesassociacao;

    public AssociacaoComputadorUsuario(Integer fkUsuario, Integer fkComputador, LocalDateTime dataAssociacao, LocalDateTime dataDesassociacao) {
        this.fkUsuario = fkUsuario;
        this.fkComputador = fkComputador;
        this.dataAssociacao = dataAssociacao;
        this.dataDesassociacao = dataDesassociacao;
    }

    public Integer getIdAssociacao() {
        return idAssociacao;
    }

    public void setIdAssociacao(Integer idAssociacao) {
        this.idAssociacao = idAssociacao;
    }

    public Integer getFkUsuario() {
        return fkUsuario;
    }

    public void setFkUsuario(Integer fkUsuario) {
        this.fkUsuario = fkUsuario;
    }

    public Integer getFkComputador() {
        return fkComputador;
    }

    public void setFkComputador(Integer fkComputador) {
        this.fkComputador = fkComputador;
    }

    public LocalDateTime getDataAssociacao() {
        return dataAssociacao;
    }

    public void setDataAssociacao(LocalDateTime dataAssociacao) {
        this.dataAssociacao = dataAssociacao;
    }

    public LocalDateTime getDataDesassociacao() {
        return dataDesassociacao;
    }

    public void setDataDesassociacao(LocalDateTime dataDesassociacao) {
        this.dataDesassociacao = dataDesassociacao;
    }

    @Override
    public String toString() {
        return """
                ID: %d
                Usuário (FK): %d
                Computador (FK): %d
                Data de Associação: %s
                Data de Desassociação: %s""".formatted(idAssociacao, fkComputador, fkComputador, dataAssociacao, dataDesassociacao);
    }
}
