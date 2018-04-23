/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import similarity.entity.Scenario;

import java.io.Serializable;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jullian
 */
public abstract class JPAController<T> implements Serializable{

	protected EntityManagerFactory emf = null;
	protected Class<T> entityClass;
	
    public JPAController(EntityManagerFactory emf, Class<T> c) {
        this.emf = emf;
        this.entityClass = c;
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public abstract void create(T autoS);
    
    public abstract void edit(T autoS) throws NonexistentEntityException, Exception ; 
    
    public abstract void destroy(Long id) throws NonexistentEntityException ;
    
    public List<T> findEntities() {
        return findEntities(true, -1, -1);
    }

    public List<T> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<T> findEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from (entityClass));
            
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

    public T findEntity(Long id) {
        
    	EntityManager em = getEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }
    
    public List<Scenario> findEntityFilter()
    {
        EntityManager em = getEntityManager();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery(Scenario.class);
        Root c = cq.from(entityClass);
        cq.select(c.get("name")).distinct(true);
        cq.select(c.get("requestUrl")).distinct(true);
        Query q = em.createQuery(cq);
        
        return q.getResultList();
    }
    
    public int getEntityCount() {
    	EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<T> rt = cq.from(entityClass);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    };
    
}
