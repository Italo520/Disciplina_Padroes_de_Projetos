package br.com.todolist.repository;

import br.com.todolist.entity.Tarefa;
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
            // Verifica se já existe para evitar erro de chave duplicada, ou usa merge se for intenção de "saveOrUpdate"
            // Mas o contrato de persistência geralmente separa salvar (novo) de atualizar.
            // Como o ID é atribuído manualmente (título), persist pode falhar se já existir.
            // Vamos tentar persist, se falhar, é erro de regra de negócio (título duplicado).
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
    public void excluir(Tarefa entity) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            // É preciso carregar a entidade no contexto antes de remover, ou usar referência
            Tarefa tarefaParaRemover = em.find(Tarefa.class, entity.getTitulo());
            if (tarefaParaRemover != null) {
                em.remove(tarefaParaRemover);
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
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Tarefa buscarPorId(String id) {
        EntityManager em = DatabaseConnection.getInstance().getEntityManager();
        try {
            return em.find(Tarefa.class, id);
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
        } finally {
            em.close();
        }
    }
}
