package br.com.todolist.repository;

import br.com.todolist.entity.Usuario;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private static final String ARQUIVO_USUARIOS = "arquivos/usuarios.json";
    private final GerenciadorDePersistenciaJson persistencia;
    private final List<Usuario> usuarios;

    public UserRepositoryImpl() {
        criarPastaDeArquivos();
        this.persistencia = new GerenciadorDePersistenciaJson(ARQUIVO_USUARIOS);
        this.usuarios = carregarUsuarios();
    }

    private void criarPastaDeArquivos() {
        File pasta = new File("arquivos");
        if (!pasta.exists()) {
            if (pasta.mkdir()) {
                System.out.println("Pasta 'arquivos' criada com sucesso.");
            } else {
                System.err.println("Erro ao criar a pasta 'arquivos'.");
            }
        }
    }

    private List<Usuario> carregarUsuarios() {
        Type tipoListaDeUsuarios = new TypeReference<List<Usuario>>() {}.getType();
        List<Usuario> lista = persistencia.carregar(tipoListaDeUsuarios);
        return lista != null ? lista : new ArrayList<>();
    }

    @Override
    public void salvar(Usuario usuario) {
        if (buscarPorEmail(usuario.getEmail()) == null) {
            usuarios.add(usuario);
        }
        persistencia.salvar(this.usuarios);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarios.stream()
                .filter(usuario -> usuario.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Usuario> buscarTodos() {
        return new ArrayList<>(usuarios);
    }
}