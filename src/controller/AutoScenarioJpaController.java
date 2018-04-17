/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.AutoExecution;
import entity.AutoNode;
import entity.AutoScenario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jullian
 */
public class AutoScenarioJpaController implements Serializable {

    public AutoScenarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AutoScenario autoScenario) {
        if (autoScenario.getAutoNodeList() == null) {
            autoScenario.setAutoNodeList(new ArrayList<AutoNode>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AutoExecution executionId = autoScenario.getExecutionId();
            if (executionId != null) {
                executionId = em.getReference(executionId.getClass(), executionId.getId());
                autoScenario.setExecutionId(executionId);
            }
            AutoNode rootId = autoScenario.getRootId();
            if (rootId != null) {
                rootId = em.getReference(rootId.getClass(), rootId.getId());
                autoScenario.setRootId(rootId);
            }
            List<AutoNode> attachedAutoNodeList = new ArrayList<AutoNode>();
            for (AutoNode autoNodeListAutoNodeToAttach : autoScenario.getAutoNodeList()) {
                autoNodeListAutoNodeToAttach = em.getReference(autoNodeListAutoNodeToAttach.getClass(), autoNodeListAutoNodeToAttach.getId());
                attachedAutoNodeList.add(autoNodeListAutoNodeToAttach);
            }
            autoScenario.setAutoNodeList(attachedAutoNodeList);
            em.persist(autoScenario);
            if (executionId != null) {
                executionId.getAutoScenarioList().add(autoScenario);
                executionId = em.merge(executionId);
            }
            if (rootId != null) {
                rootId.getAutoScenarioList().add(autoScenario);
                rootId = em.merge(rootId);
            }
            for (AutoNode autoNodeListAutoNode : autoScenario.getAutoNodeList()) {
                autoNodeListAutoNode.getAutoScenarioList().add(autoScenario);
                autoNodeListAutoNode = em.merge(autoNodeListAutoNode);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AutoScenario autoScenario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AutoScenario persistentAutoScenario = em.find(AutoScenario.class, autoScenario.getId());
            AutoExecution executionIdOld = persistentAutoScenario.getExecutionId();
            AutoExecution executionIdNew = autoScenario.getExecutionId();
            AutoNode rootIdOld = persistentAutoScenario.getRootId();
            AutoNode rootIdNew = autoScenario.getRootId();
            List<AutoNode> autoNodeListOld = persistentAutoScenario.getAutoNodeList();
            List<AutoNode> autoNodeListNew = autoScenario.getAutoNodeList();
            if (executionIdNew != null) {
                executionIdNew = em.getReference(executionIdNew.getClass(), executionIdNew.getId());
                autoScenario.setExecutionId(executionIdNew);
            }
            if (rootIdNew != null) {
                rootIdNew = em.getReference(rootIdNew.getClass(), rootIdNew.getId());
                autoScenario.setRootId(rootIdNew);
            }
            List<AutoNode> attachedAutoNodeListNew = new ArrayList<AutoNode>();
            for (AutoNode autoNodeListNewAutoNodeToAttach : autoNodeListNew) {
                autoNodeListNewAutoNodeToAttach = em.getReference(autoNodeListNewAutoNodeToAttach.getClass(), autoNodeListNewAutoNodeToAttach.getId());
                attachedAutoNodeListNew.add(autoNodeListNewAutoNodeToAttach);
            }
            autoNodeListNew = attachedAutoNodeListNew;
            autoScenario.setAutoNodeList(autoNodeListNew);
            autoScenario = em.merge(autoScenario);
            if (executionIdOld != null && !executionIdOld.equals(executionIdNew)) {
                executionIdOld.getAutoScenarioList().remove(autoScenario);
                executionIdOld = em.merge(executionIdOld);
            }
            if (executionIdNew != null && !executionIdNew.equals(executionIdOld)) {
                executionIdNew.getAutoScenarioList().add(autoScenario);
                executionIdNew = em.merge(executionIdNew);
            }
            if (rootIdOld != null && !rootIdOld.equals(rootIdNew)) {
                rootIdOld.getAutoScenarioList().remove(autoScenario);
                rootIdOld = em.merge(rootIdOld);
            }
            if (rootIdNew != null && !rootIdNew.equals(rootIdOld)) {
                rootIdNew.getAutoScenarioList().add(autoScenario);
                rootIdNew = em.merge(rootIdNew);
            }
            for (AutoNode autoNodeListOldAutoNode : autoNodeListOld) {
                if (!autoNodeListNew.contains(autoNodeListOldAutoNode)) {
                    autoNodeListOldAutoNode.getAutoScenarioList().remove(autoScenario);
                    autoNodeListOldAutoNode = em.merge(autoNodeListOldAutoNode);
                }
            }
            for (AutoNode autoNodeListNewAutoNode : autoNodeListNew) {
                if (!autoNodeListOld.contains(autoNodeListNewAutoNode)) {
                    autoNodeListNewAutoNode.getAutoScenarioList().add(autoScenario);
                    autoNodeListNewAutoNode = em.merge(autoNodeListNewAutoNode);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = autoScenario.getId();
                if (findAutoScenario(id) == null) {
                    throw new NonexistentEntityException("The autoScenario with id " + id + " no longer exists.");
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
            AutoScenario autoScenario;
            try {
                autoScenario = em.getReference(AutoScenario.class, id);
                autoScenario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The autoScenario with id " + id + " no longer exists.", enfe);
            }
            AutoExecution executionId = autoScenario.getExecutionId();
            if (executionId != null) {
                executionId.getAutoScenarioList().remove(autoScenario);
                executionId = em.merge(executionId);
            }
            AutoNode rootId = autoScenario.getRootId();
            if (rootId != null) {
                rootId.getAutoScenarioList().remove(autoScenario);
                rootId = em.merge(rootId);
            }
            List<AutoNode> autoNodeList = autoScenario.getAutoNodeList();
            for (AutoNode autoNodeListAutoNode : autoNodeList) {
                autoNodeListAutoNode.getAutoScenarioList().remove(autoScenario);
                autoNodeListAutoNode = em.merge(autoNodeListAutoNode);
            }
            em.remove(autoScenario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AutoScenario> findAutoScenarioEntities() {
        return findAutoScenarioEntities(true, -1, -1);
    }

    public List<AutoScenario> findAutoScenarioEntities(int maxResults, int firstResult) {
        return findAutoScenarioEntities(false, maxResults, firstResult);
    }

    private List<AutoScenario> findAutoScenarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AutoScenario.class));
            
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

    public AutoScenario findAutoScenario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AutoScenario.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<AutoScenario> findAutoScenarioFilter()
    {
        EntityManager em = getEntityManager();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery(AutoScenario.class);
        Root c = cq.from(AutoScenario.class);
        cq.select(c.get("name")).distinct(true);
        cq.select(c.get("requestUrl")).distinct(true);
        Query q = em.createQuery(cq);
        
        return q.getResultList();
    }

    public int getAutoScenarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AutoScenario> rt = cq.from(AutoScenario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
