/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import similarity.entity.Execution;
import similarity.entity.Scenario;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jullian
 */
public class AutoExecutionJpaController implements Serializable {

    public AutoExecutionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Execution autoExecution) {
        if (autoExecution.getAutoScenarioList() == null) {
            autoExecution.setAutoScenarioList(new ArrayList<Scenario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Scenario> attachedAutoScenarioList = new ArrayList<Scenario>();
            for (Scenario autoScenarioListAutoScenarioToAttach : autoExecution.getAutoScenarioList()) {
                autoScenarioListAutoScenarioToAttach = em.getReference(autoScenarioListAutoScenarioToAttach.getClass(), autoScenarioListAutoScenarioToAttach.getId());
                attachedAutoScenarioList.add(autoScenarioListAutoScenarioToAttach);
            }
            autoExecution.setAutoScenarioList(attachedAutoScenarioList);
            em.persist(autoExecution);
            for (Scenario autoScenarioListAutoScenario : autoExecution.getAutoScenarioList()) {
                Execution oldExecutionIdOfAutoScenarioListAutoScenario = autoScenarioListAutoScenario.getExecutionId();
                autoScenarioListAutoScenario.setExecutionId(autoExecution);
                autoScenarioListAutoScenario = em.merge(autoScenarioListAutoScenario);
                if (oldExecutionIdOfAutoScenarioListAutoScenario != null) {
                    oldExecutionIdOfAutoScenarioListAutoScenario.getAutoScenarioList().remove(autoScenarioListAutoScenario);
                    oldExecutionIdOfAutoScenarioListAutoScenario = em.merge(oldExecutionIdOfAutoScenarioListAutoScenario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Execution autoExecution) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Execution persistentAutoExecution = em.find(Execution.class, autoExecution.getId());
            List<Scenario> autoScenarioListOld = persistentAutoExecution.getAutoScenarioList();
            List<Scenario> autoScenarioListNew = autoExecution.getAutoScenarioList();
            List<Scenario> attachedAutoScenarioListNew = new ArrayList<Scenario>();
            for (Scenario autoScenarioListNewAutoScenarioToAttach : autoScenarioListNew) {
                autoScenarioListNewAutoScenarioToAttach = em.getReference(autoScenarioListNewAutoScenarioToAttach.getClass(), autoScenarioListNewAutoScenarioToAttach.getId());
                attachedAutoScenarioListNew.add(autoScenarioListNewAutoScenarioToAttach);
            }
            autoScenarioListNew = attachedAutoScenarioListNew;
            autoExecution.setAutoScenarioList(autoScenarioListNew);
            autoExecution = em.merge(autoExecution);
            for (Scenario autoScenarioListOldAutoScenario : autoScenarioListOld) {
                if (!autoScenarioListNew.contains(autoScenarioListOldAutoScenario)) {
                    autoScenarioListOldAutoScenario.setExecutionId(null);
                    autoScenarioListOldAutoScenario = em.merge(autoScenarioListOldAutoScenario);
                }
            }
            for (Scenario autoScenarioListNewAutoScenario : autoScenarioListNew) {
                if (!autoScenarioListOld.contains(autoScenarioListNewAutoScenario)) {
                    Execution oldExecutionIdOfAutoScenarioListNewAutoScenario = autoScenarioListNewAutoScenario.getExecutionId();
                    autoScenarioListNewAutoScenario.setExecutionId(autoExecution);
                    autoScenarioListNewAutoScenario = em.merge(autoScenarioListNewAutoScenario);
                    if (oldExecutionIdOfAutoScenarioListNewAutoScenario != null && !oldExecutionIdOfAutoScenarioListNewAutoScenario.equals(autoExecution)) {
                        oldExecutionIdOfAutoScenarioListNewAutoScenario.getAutoScenarioList().remove(autoScenarioListNewAutoScenario);
                        oldExecutionIdOfAutoScenarioListNewAutoScenario = em.merge(oldExecutionIdOfAutoScenarioListNewAutoScenario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = autoExecution.getId();
                if (findAutoExecution(id) == null) {
                    throw new NonexistentEntityException("The Execution with id " + id + " no longer exists.");
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
            Execution autoExecution;
            try {
                autoExecution = em.getReference(Execution.class, id);
                autoExecution.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The Execution with id " + id + " no longer exists.", enfe);
            }
            List<Scenario> autoScenarioList = autoExecution.getAutoScenarioList();
            for (Scenario autoScenarioListAutoScenario : autoScenarioList) {
                autoScenarioListAutoScenario.setExecutionId(null);
                autoScenarioListAutoScenario = em.merge(autoScenarioListAutoScenario);
            }
            em.remove(autoExecution);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Execution> findAutoExecutionEntities() {
        return findAutoExecutionEntities(true, -1, -1);
    }

    public List<Execution> findAutoExecutionEntities(int maxResults, int firstResult) {
        return findAutoExecutionEntities(false, maxResults, firstResult);
    }

    private List<Execution> findAutoExecutionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Execution.class));
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

    public Execution findAutoExecution(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Execution.class, id);
        } finally {
            em.close();
        }
    }

    public int getAutoExecutionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Execution> rt = cq.from(Execution.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
