package br.com.todolist.repository.json;
import br.com.todolist.repository.IUserRepository;

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

    /**
     * Construtor da classe UserRepositoryImpl.
     * Inicializa o gerenciador de persistência, cria a pasta de arquivos se necessário e carrega os usuários.
     */
    public UserRepositoryImpl() {
        criarPastaDeArquivos();
        this.persistencia = new GerenciadorDePersistenciaJson(ARQUIVO_USUARIOS);
        this.usuarios = carregarUsuarios();
    }

    /**
     * Cria a pasta "arquivos" caso ela não exista.
     */
    private void criarPastaDeArquivos() {
        File pasta = new File("arquivos");
        if (!pasta.exists()) {
            pasta.mkdir();
        }
    }

    /**
     * Carrega a lista de usuários do arquivo JSON.
     *
     * @return Uma lista de usuários, ou uma lista vazia se não houver dados.
     */
    private List<Usuario> carregarUsuarios() {
        Type tipoListaDeUsuarios = new TypeReference<List<Usuario>>() {}.getType();
        List<Usuario> lista = persistencia.carregar(tipoListaDeUsuarios);
        return lista != null ? lista : new ArrayList<>();
    }

    /**
     * Salva um novo usuário no arquivo JSON.
     * Se o usuário já existir (mesmo e-mail), não faz nada (embora a verificação de duplicidade devesse ocorrer antes).
     *
     * @param usuario O usuário a ser salvo.
     */
    @Override
    public void salvar(Usuario usuario) {
        if (buscarPorEmail(usuario.getEmail()) == null) {
            usuarios.add(usuario);
        }
        persistencia.salvar(this.usuarios);
    }

    /**
     * Exclui um usuário do sistema.
     *
     * @param usuario O usuário a ser excluído.
     */
    @Override
    public void excluir(Usuario usuario) {
        usuarios.remove(usuario);
        persistencia.salvar(this.usuarios);
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param usuario O usuário com os dados atualizados.
     */
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

    /**
     * Busca um usuário pelo ID (neste caso, o e-mail).
     *
     * @param id O e-mail do usuário.
     * @return O usuário encontrado, ou null.
     */
    @Override
    public Usuario buscarPorId(String id) {
        return buscarPorEmail(id);
    }

    /**
     * Retorna todos os usuários cadastrados.
     *
     * @return Uma nova lista com todos os usuários.
     */
    @Override
    public List<Usuario> buscarTodos() {
        return new ArrayList<>(usuarios);
    }

    /**
     * Busca um usuário pelo e-mail.
     *
     * @param email O e-mail do usuário.
     * @return O usuário encontrado, ou null se não existir.
     */
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
