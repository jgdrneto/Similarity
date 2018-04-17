/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import entity.AutoExecution;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.AutoScenario;
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

    public void create(AutoExecution autoExecution) {
        if (autoExecution.getAutoScenarioList() == null) {
            autoExecution.setAutoScenarioList(new ArrayList<AutoScenario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<AutoScenario> attachedAutoScenarioList = new ArrayList<AutoScenario>();
            for (AutoScenario autoScenarioListAutoScenarioToAttach : autoExecution.getAutoScenarioList()) {
                autoScenarioListAutoScenarioToAttach = em.getReference(autoScenarioListAutoScenarioToAttach.getClass(), autoScenarioListAutoScenarioToAttach.getId());
                attachedAutoScenarioList.add(autoScenarioListAutoScenarioToAttach);
            }
            autoExecution.setAutoScenarioList(attachedAutoScenarioList);
            em.persist(autoExecution);
            for (AutoScenario autoScenarioListAutoScenario : autoExecution.getAutoScenarioList()) {
                AutoExecution oldExecutionIdOfAutoScenarioListAutoScenario = autoScenarioListAutoScenario.getExecutionId();
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

    public void edit(AutoExecution autoExecution) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AutoExecution persistentAutoExecution = em.find(AutoExecution.class, autoExecution.getId());
            List<AutoScenario> autoScenarioListOld = persistentAutoExecution.getAutoScenarioList();
            List<AutoScenario> autoScenarioListNew = autoExecution.getAutoScenarioList();
            List<AutoScenario> attachedAutoScenarioListNew = new ArrayList<AutoScenario>();
            for (AutoScenario autoScenarioListNewAutoScenarioToAttach : autoScenarioListNew) {
                autoScenarioListNewAutoScenarioToAttach = em.getReference(autoScenarioListNewAutoScenarioToAttach.getClass(), autoScenarioListNewAutoScenarioToAttach.getId());
                attachedAutoScenarioListNew.add(autoScenarioListNewAutoScenarioToAttach);
            }
            autoScenarioListNew = attachedAutoScenarioListNew;
            autoExecution.setAutoScenarioList(autoScenarioListNew);
            autoExecution = em.merge(autoExecution);
            for (AutoScenario autoScenarioListOldAutoScenario : autoScenarioListOld) {
                if (!autoScenarioListNew.contains(autoScenarioListOldAutoScenario)) {
                    autoScenarioListOldAutoScenario.setExecutionId(null);
                    autoScenarioListOldAutoScenario = em.merge(autoScenarioListOldAutoScenario);
                }
            }
            for (AutoScenario autoScenarioListNewAutoScenario : autoScenarioListNew) {
                if (!autoScenarioListOld.contains(autoScenarioListNewAutoScenario)) {
                    AutoExecution oldExecutionIdOfAutoScenarioListNewAutoScenario = autoScenarioListNewAutoScenario.getExecutionId();
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
                    throw new NonexistentEntityException("The autoExecution with id " + id + " no longer exists.");
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
            AutoExecution autoExecution;
            try {
                autoExecution = em.getReference(AutoExecution.class, id);
                autoExecution.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The autoExecution with id " + id + " no longer exists.", enfe);
            }
            List<AutoScenario> autoScenarioList = autoExecution.getAutoScenarioList();
            for (AutoScenario autoScenarioListAutoScenario : autoScenarioList) {
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

    public List<AutoExecution> findAutoExecutionEntities() {
        return findAutoExecutionEntities(true, -1, -1);
    }

    public List<AutoExecution> findAutoExecutionEntities(int maxResults, int firstResult) {
        return findAutoExecutionEntities(false, maxResults, firstResult);
    }

    private List<AutoExecution> findAutoExecutionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AutoExecution.class));
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

    public AutoExecution findAutoExecution(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AutoExecution.class, id);
        } finally {
            em.close();
        }
    }

    public int getAutoExecutionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AutoExecution> rt = cq.from(AutoExecution.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}