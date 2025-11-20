package br.com.todolist.repository;

import br.com.todolist.entity.Usuario;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do repositório de usuários.
 * Responsável por persistir e recuperar dados de usuários em um arquivo JSON.
 */
public class UserRepositoryImpl implements IUserRepository {

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
            pasta.mkdir();
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
    public void excluir(Usuario usuario) {
        usuarios.remove(usuario);
        persistencia.salvar(this.usuarios);
    }

    @Override
    public void atualizar(Usuario usuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getEmail().equals(usuario.getEmail())) {
                usuarios.set(i, usuario);
                persistencia.salvar(this.usuarios);
                break;
            }
        }
    }

    @Override
    public Usuario buscarPorId(String id) {
        return buscarPorEmail(id);
    }

    @Override
    public List<Usuario> buscarTodos() {
        return new ArrayList<>(usuarios);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
    }
}
