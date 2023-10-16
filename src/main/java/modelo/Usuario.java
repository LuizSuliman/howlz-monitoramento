package modelo;

import java.time.LocalDateTime;

public class Usuario {
    private enum Tipo {
        COLABORADOR,
        GESTOR,
        REPRESENTANTE
    }
    private String nome;
    private String cargo;
    private String email;
    private String senha;
    private Tipo tipo;
    private LocalDateTime dataCadastro;
    private Integer fkEmpresa;

    public Usuario() {
    }
    public Usuario(String nome, String cargo, String email, String senha, Tipo tipo, LocalDateTime dataCadastro, Integer fkEmpresa) {
        this.nome = nome;
        this.cargo = cargo;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
        this.dataCadastro = dataCadastro;
        this.fkEmpresa = fkEmpresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Integer fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }
}
