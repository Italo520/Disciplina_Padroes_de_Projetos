package br.com.todolist.repository.postgres;

import br.com.todolist.repository.IEventoRepository;

import br.com.todolist.entity.Evento;
import br.com.todolist.exception.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class EventoRepositoryPostgres implements IEventoRepository {

    @Override
    public void salvar(Evento entity) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new DatabaseException("Erro ao salvar evento: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void excluir(Evento entity) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            Evento eventoParaRemover = em.find(Evento.class, entity.getTitulo());
            if (eventoParaRemover != null) {
                em.remove(eventoParaRemover);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new DatabaseException("Erro ao excluir evento: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void atualizar(Evento entity) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new DatabaseException("Erro ao atualizar evento: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public Evento buscarPorId(Long id) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            return em.find(Evento.class, id);
        } catch (Exception e) {
            throw new DatabaseException("Erro ao buscar evento por ID: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Evento> buscarTodos() {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            TypedQuery<Evento> query = em.createQuery("SELECT e FROM Evento e", Evento.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Erro ao buscar todos os eventos: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}