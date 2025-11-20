package br.com.todolist.repository;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.exception.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Implementação do repositório de tarefas utilizando PostgreSQL e JPA.
 */
public class TarefaRepositoryPostgres implements ITarefaRepository {

    @Override
    public void salvar(Tarefa entity) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new DatabaseException("Erro ao salvar tarefa: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void excluir(Tarefa entity) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            Tarefa tarefaParaRemover = em.find(Tarefa.class, entity.getTitulo());
            if (tarefaParaRemover != null) {
                em.remove(tarefaParaRemover);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new DatabaseException("Erro ao excluir tarefa: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void atualizar(Tarefa entity) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new DatabaseException("Erro ao atualizar tarefa: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public Tarefa buscarPorId(String id) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            return em.find(Tarefa.class, id);
        } catch (Exception e) {
            throw new DatabaseException("Erro ao buscar tarefa por ID: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Tarefa> buscarTodos() {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            TypedQuery<Tarefa> query = em.createQuery("SELECT t FROM Tarefa t", Tarefa.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Erro ao buscar todas as tarefas: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}
