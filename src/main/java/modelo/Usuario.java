package modelo;

import java.time.LocalDateTime;

public class Usuario {
    private Integer idUsuario;
    private String nome;
    private String email;
    private String senha;
    private LocalDateTime dataCadastro;
    private Integer fkEmpresa;
    private Integer fkTipoUsuario;
    private Integer fkGestor;

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha, LocalDateTime dataCadastro, Integer fkEmpresa, Integer fkTipoUsuario, Integer fkGestor) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataCadastro = dataCadastro;
        this.fkEmpresa = fkEmpresa;
        this.fkTipoUsuario = fkTipoUsuario;
        this.fkGestor = fkGestor;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Integer getFkTipoUsuario() {
        return fkTipoUsuario;
    }

    public void setFkTipoUsuario(Integer fkTipoUsuario) {
        this.fkTipoUsuario = fkTipoUsuario;
    }

    public Integer getFkGestor() {
        return fkGestor;
    }

    public void setFkGestor(Integer fkGestor) {
        this.fkGestor = fkGestor;
    }

    @Override
    public String toString() {
        return """
                ID: %d
                Nome: %s
                Email: %s
                Senha: %s
                Data de Cadastro: %s
                Empresa (FK): %d
                Tipo de Usuário (FK): %d
                Gestor (FK): %d""".formatted(idUsuario, nome, email, senha, dataCadastro, fkEmpresa, fkTipoUsuario, fkGestor);
    }
}
