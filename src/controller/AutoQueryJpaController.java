/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import similarity.entity.Node;
import similarity.entity.QueryDB;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jullian
 */
public class AutoQueryJpaController implements Serializable {

    public AutoQueryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(QueryDB autoQuery) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Node nodeId = autoQuery.getNodeId();
            if (nodeId != null) {
                nodeId = em.getReference(nodeId.getClass(), nodeId.getId());
                autoQuery.setNodeId(nodeId);
            }
            em.persist(autoQuery);
            if (nodeId != null) {
                nodeId.getAutoQueryList().add(autoQuery);
                nodeId = em.merge(nodeId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(QueryDB autoQuery) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            QueryDB persistentAutoQuery = em.find(QueryDB.class, autoQuery.getId());
            Node nodeIdOld = persistentAutoQuery.getNodeId();
            Node nodeIdNew = autoQuery.getNodeId();
            if (nodeIdNew != null) {
                nodeIdNew = em.getReference(nodeIdNew.getClass(), nodeIdNew.getId());
                autoQuery.setNodeId(nodeIdNew);
            }
            autoQuery = em.merge(autoQuery);
            if (nodeIdOld != null && !nodeIdOld.equals(nodeIdNew)) {
                nodeIdOld.getAutoQueryList().remove(autoQuery);
                nodeIdOld = em.merge(nodeIdOld);
            }
            if (nodeIdNew != null && !nodeIdNew.equals(nodeIdOld)) {
                nodeIdNew.getAutoQueryList().add(autoQuery);
                nodeIdNew = em.merge(nodeIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = autoQuery.getId();
                if (findAutoQuery(id) == null) {
                    throw new NonexistentEntityException("The autoQuery with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            QueryDB autoQuery;
            try {
                autoQuery = em.getReference(QueryDB.class, id);
                autoQuery.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The autoQuery with id " + id + " no longer exists.", enfe);
            }
            Node nodeId = autoQuery.getNodeId();
            if (nodeId != null) {
                nodeId.getAutoQueryList().remove(autoQuery);
                nodeId = em.merge(nodeId);
            }
            em.remove(autoQuery);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<QueryDB> findAutoQueryEntities() {
        return findAutoQueryEntities(true, -1, -1);
    }

    public List<QueryDB> findAutoQueryEntities(int maxResults, int firstResult) {
        return findAutoQueryEntities(false, maxResults, firstResult);
    }

    private List<QueryDB> findAutoQueryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(QueryDB.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public QueryDB findAutoQuery(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(QueryDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getAutoQueryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<QueryDB> rt = cq.from(QueryDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
