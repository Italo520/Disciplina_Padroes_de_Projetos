package br.com.todolist.repository;

import br.com.todolist.entity.Evento;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Implementação do repositório de eventos utilizando PostgreSQL e JPA.
 */
public class EventoRepositoryPostgres implements IEventoRepository {

    @Override
    public List<Evento> buscarPorDia(LocalDate dia) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            TypedQuery<Evento> query = em.createQuery("SELECT e FROM Evento e WHERE e.deadline = :dia", Evento.class);
            query.setParameter("dia", dia);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Evento> buscarPorMes(YearMonth mes) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            TypedQuery<Evento> query = em.createQuery(
                    "SELECT e FROM Evento e WHERE MONTH(e.deadline) = :mes AND YEAR(e.deadline) = :ano", Evento.class);
            query.setParameter("mes", mes.getMonthValue());
            query.setParameter("ano", mes.getYear());
            return query.getResultList();
        } finally {
            em.close();
        }
    }

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
            throw e;
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
            throw e;
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
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Evento buscarPorId(String id) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            return em.find(Evento.class, id);
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
        } finally {
            em.close();
        }
    }
}
