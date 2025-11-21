package br.com.todolist.repository.postgres;
import br.com.todolist.repository.IUserRepository;

import br.com.todolist.entity.Usuario;
import br.com.todolist.exception.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Implementação do repositório de usuários utilizando PostgreSQL e JPA.
 */
public class UserRepositoryPostgres implements IUserRepository {

    @Override
    public void salvar(Usuario entity) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new DatabaseException("Erro ao salvar usuário: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void excluir(Usuario entity) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            Usuario usuarioParaRemover = em.find(Usuario.class, entity.getEmail());
            if (usuarioParaRemover != null) {
                em.remove(usuarioParaRemover);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new DatabaseException("Erro ao excluir usuário: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void atualizar(Usuario entity) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new DatabaseException("Erro ao atualizar usuário: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public Usuario buscarPorId(String id) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } catch (Exception e) {
            throw new DatabaseException("Erro ao buscar usuário por ID: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Usuario> buscarTodos() {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Erro ao buscar todos os usuários: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return buscarPorId(email);
    }
}
