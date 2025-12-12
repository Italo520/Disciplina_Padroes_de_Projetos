package br.com.todolist.repository.postgres;

import br.com.todolist.repository.ITarefaRepository;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.exception.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

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
            Tarefa tarefaParaRemover = em.find(Tarefa.class, entity.getId());
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
    public Tarefa atualizar(Tarefa entity) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            Tarefa merged = em.merge(entity);
            em.getTransaction().commit();
            return merged;
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
    public Tarefa buscarPorId(Long id) {
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

    @Override
    public List<Tarefa> buscarPorDia(java.time.LocalDate dia) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            TypedQuery<Tarefa> query = em.createQuery("SELECT t FROM Tarefa t WHERE t.deadline = :dia", Tarefa.class);
            query.setParameter("dia", dia);
            return query.getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Erro ao buscar tarefas por dia: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Tarefa> buscarTarefasCriticas() {
        List<Tarefa> all = buscarTodos();
        java.time.LocalDate hoje = java.time.LocalDate.now();
        return all.stream()
                .filter(t -> t.getDeadline() != null &&
                        java.time.temporal.ChronoUnit.DAYS.between(hoje, t.getDeadline()) - t.getPrioridade() < 0)
                .collect(java.util.stream.Collectors.toList());
    }
}