package br.com.todolist.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;

@MappedSuperclass
public abstract class Itens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descricao;
    private String tipo;
    private String criadoPor;
    private LocalDate dataCadastro;
    private LocalDate deadline;

    protected Itens(String titulo, String descricao, String tipo, String criadoPor, LocalDate deadline) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.criadoPor = criadoPor;
        this.deadline = deadline;
        this.dataCadastro = LocalDate.now();
    }

    protected Itens() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(String criadoPor) {
        this.criadoPor = criadoPor;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadLine(LocalDate deadline) {
        this.deadline = deadline;
    }
}