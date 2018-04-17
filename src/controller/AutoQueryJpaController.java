/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import similarity.entity.AutoNode;
import similarity.entity.AutoQuery;

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

    public void create(AutoQuery autoQuery) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AutoNode nodeId = autoQuery.getNodeId();
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

    public void edit(AutoQuery autoQuery) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AutoQuery persistentAutoQuery = em.find(AutoQuery.class, autoQuery.getId());
            AutoNode nodeIdOld = persistentAutoQuery.getNodeId();
            AutoNode nodeIdNew = autoQuery.getNodeId();
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
            AutoQuery autoQuery;
            try {
                autoQuery = em.getReference(AutoQuery.class, id);
                autoQuery.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The autoQuery with id " + id + " no longer exists.", enfe);
            }
            AutoNode nodeId = autoQuery.getNodeId();
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

    public List<AutoQuery> findAutoQueryEntities() {
        return findAutoQueryEntities(true, -1, -1);
    }

    public List<AutoQuery> findAutoQueryEntities(int maxResults, int firstResult) {
        return findAutoQueryEntities(false, maxResults, firstResult);
    }

    private List<AutoQuery> findAutoQueryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AutoQuery.class));
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

    public AutoQuery findAutoQuery(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AutoQuery.class, id);
        } finally {
            em.close();
        }
    }

    public int getAutoQueryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AutoQuery> rt = cq.from(AutoQuery.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
