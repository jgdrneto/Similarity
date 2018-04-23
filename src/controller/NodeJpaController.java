/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import similarity.entity.Node;
import similarity.entity.QueryDB;
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
public class NodeJpaController extends JPAController<Node> implements Serializable {

    public NodeJpaController(EntityManagerFactory emf) {
        super(emf,Node.class);
    }

    public void create(Node autoNode) {
        if (autoNode.getAutoScenarioList() == null) {
            autoNode.setAutoScenarioList(new ArrayList<Scenario>());
        }
        if (autoNode.getAutoNodeList() == null) {
            autoNode.setAutoNodeList(new ArrayList<Node>());
        }
        if (autoNode.getAutoScenarioList1() == null) {
            autoNode.setAutoScenarioList1(new ArrayList<Scenario>());
        }
        if (autoNode.getAutoQueryList() == null) {
            autoNode.setAutoQueryList(new ArrayList<QueryDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Node parentId = autoNode.getParentId();
            if (parentId != null) {
                parentId = em.getReference(parentId.getClass(), parentId.getId());
                autoNode.setParentId(parentId);
            }
            List<Scenario> attachedAutoScenarioList = new ArrayList<Scenario>();
            for (Scenario autoScenarioListAutoScenarioToAttach : autoNode.getAutoScenarioList()) {
                autoScenarioListAutoScenarioToAttach = em.getReference(autoScenarioListAutoScenarioToAttach.getClass(), autoScenarioListAutoScenarioToAttach.getId());
                attachedAutoScenarioList.add(autoScenarioListAutoScenarioToAttach);
            }
            autoNode.setAutoScenarioList(attachedAutoScenarioList);
            List<Node> attachedAutoNodeList = new ArrayList<Node>();
            for (Node autoNodeListAutoNodeToAttach : autoNode.getAutoNodeList()) {
                autoNodeListAutoNodeToAttach = em.getReference(autoNodeListAutoNodeToAttach.getClass(), autoNodeListAutoNodeToAttach.getId());
                attachedAutoNodeList.add(autoNodeListAutoNodeToAttach);
            }
            autoNode.setAutoNodeList(attachedAutoNodeList);
            List<Scenario> attachedAutoScenarioList1 = new ArrayList<Scenario>();
            for (Scenario autoScenarioList1AutoScenarioToAttach : autoNode.getAutoScenarioList1()) {
                autoScenarioList1AutoScenarioToAttach = em.getReference(autoScenarioList1AutoScenarioToAttach.getClass(), autoScenarioList1AutoScenarioToAttach.getId());
                attachedAutoScenarioList1.add(autoScenarioList1AutoScenarioToAttach);
            }
            autoNode.setAutoScenarioList1(attachedAutoScenarioList1);
            List<QueryDB> attachedAutoQueryList = new ArrayList<QueryDB>();
            for (QueryDB autoQueryListAutoQueryToAttach : autoNode.getAutoQueryList()) {
                autoQueryListAutoQueryToAttach = em.getReference(autoQueryListAutoQueryToAttach.getClass(), autoQueryListAutoQueryToAttach.getId());
                attachedAutoQueryList.add(autoQueryListAutoQueryToAttach);
            }
            autoNode.setAutoQueryList(attachedAutoQueryList);
            em.persist(autoNode);
            if (parentId != null) {
                parentId.getAutoNodeList().add(autoNode);
                parentId = em.merge(parentId);
            }
            for (Scenario autoScenarioListAutoScenario : autoNode.getAutoScenarioList()) {
                autoScenarioListAutoScenario.getAutoNodeList().add(autoNode);
                autoScenarioListAutoScenario = em.merge(autoScenarioListAutoScenario);
            }
            for (Node autoNodeListAutoNode : autoNode.getAutoNodeList()) {
                Node oldParentIdOfAutoNodeListAutoNode = autoNodeListAutoNode.getParentId();
                autoNodeListAutoNode.setParentId(autoNode);
                autoNodeListAutoNode = em.merge(autoNodeListAutoNode);
                if (oldParentIdOfAutoNodeListAutoNode != null) {
                    oldParentIdOfAutoNodeListAutoNode.getAutoNodeList().remove(autoNodeListAutoNode);
                    oldParentIdOfAutoNodeListAutoNode = em.merge(oldParentIdOfAutoNodeListAutoNode);
                }
            }
            for (Scenario autoScenarioList1AutoScenario : autoNode.getAutoScenarioList1()) {
                Node oldRootIdOfAutoScenarioList1AutoScenario = autoScenarioList1AutoScenario.getRootId();
                autoScenarioList1AutoScenario.setRootId(autoNode);
                autoScenarioList1AutoScenario = em.merge(autoScenarioList1AutoScenario);
                if (oldRootIdOfAutoScenarioList1AutoScenario != null) {
                    oldRootIdOfAutoScenarioList1AutoScenario.getAutoScenarioList1().remove(autoScenarioList1AutoScenario);
                    oldRootIdOfAutoScenarioList1AutoScenario = em.merge(oldRootIdOfAutoScenarioList1AutoScenario);
                }
            }
            for (QueryDB autoQueryListAutoQuery : autoNode.getAutoQueryList()) {
                Node oldNodeIdOfAutoQueryListAutoQuery = autoQueryListAutoQuery.getNodeId();
                autoQueryListAutoQuery.setNodeId(autoNode);
                autoQueryListAutoQuery = em.merge(autoQueryListAutoQuery);
                if (oldNodeIdOfAutoQueryListAutoQuery != null) {
                    oldNodeIdOfAutoQueryListAutoQuery.getAutoQueryList().remove(autoQueryListAutoQuery);
                    oldNodeIdOfAutoQueryListAutoQuery = em.merge(oldNodeIdOfAutoQueryListAutoQuery);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Node autoNode) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Node persistentAutoNode = em.find(Node.class, autoNode.getId());
            Node parentIdOld = persistentAutoNode.getParentId();
            Node parentIdNew = autoNode.getParentId();
            List<Scenario> autoScenarioListOld = persistentAutoNode.getAutoScenarioList();
            List<Scenario> autoScenarioListNew = autoNode.getAutoScenarioList();
            List<Node> autoNodeListOld = persistentAutoNode.getAutoNodeList();
            List<Node> autoNodeListNew = autoNode.getAutoNodeList();
            List<Scenario> autoScenarioList1Old = persistentAutoNode.getAutoScenarioList1();
            List<Scenario> autoScenarioList1New = autoNode.getAutoScenarioList1();
            List<QueryDB> autoQueryListOld = persistentAutoNode.getAutoQueryList();
            List<QueryDB> autoQueryListNew = autoNode.getAutoQueryList();
            if (parentIdNew != null) {
                parentIdNew = em.getReference(parentIdNew.getClass(), parentIdNew.getId());
                autoNode.setParentId(parentIdNew);
            }
            List<Scenario> attachedAutoScenarioListNew = new ArrayList<Scenario>();
            for (Scenario autoScenarioListNewAutoScenarioToAttach : autoScenarioListNew) {
                autoScenarioListNewAutoScenarioToAttach = em.getReference(autoScenarioListNewAutoScenarioToAttach.getClass(), autoScenarioListNewAutoScenarioToAttach.getId());
                attachedAutoScenarioListNew.add(autoScenarioListNewAutoScenarioToAttach);
            }
            autoScenarioListNew = attachedAutoScenarioListNew;
            autoNode.setAutoScenarioList(autoScenarioListNew);
            List<Node> attachedAutoNodeListNew = new ArrayList<Node>();
            for (Node autoNodeListNewAutoNodeToAttach : autoNodeListNew) {
                autoNodeListNewAutoNodeToAttach = em.getReference(autoNodeListNewAutoNodeToAttach.getClass(), autoNodeListNewAutoNodeToAttach.getId());
                attachedAutoNodeListNew.add(autoNodeListNewAutoNodeToAttach);
            }
            autoNodeListNew = attachedAutoNodeListNew;
            autoNode.setAutoNodeList(autoNodeListNew);
            List<Scenario> attachedAutoScenarioList1New = new ArrayList<Scenario>();
            for (Scenario autoScenarioList1NewAutoScenarioToAttach : autoScenarioList1New) {
                autoScenarioList1NewAutoScenarioToAttach = em.getReference(autoScenarioList1NewAutoScenarioToAttach.getClass(), autoScenarioList1NewAutoScenarioToAttach.getId());
                attachedAutoScenarioList1New.add(autoScenarioList1NewAutoScenarioToAttach);
            }
            autoScenarioList1New = attachedAutoScenarioList1New;
            autoNode.setAutoScenarioList1(autoScenarioList1New);
            List<QueryDB> attachedAutoQueryListNew = new ArrayList<QueryDB>();
            for (QueryDB autoQueryListNewAutoQueryToAttach : autoQueryListNew) {
                autoQueryListNewAutoQueryToAttach = em.getReference(autoQueryListNewAutoQueryToAttach.getClass(), autoQueryListNewAutoQueryToAttach.getId());
                attachedAutoQueryListNew.add(autoQueryListNewAutoQueryToAttach);
            }
            autoQueryListNew = attachedAutoQueryListNew;
            autoNode.setAutoQueryList(autoQueryListNew);
            autoNode = em.merge(autoNode);
            if (parentIdOld != null && !parentIdOld.equals(parentIdNew)) {
                parentIdOld.getAutoNodeList().remove(autoNode);
                parentIdOld = em.merge(parentIdOld);
            }
            if (parentIdNew != null && !parentIdNew.equals(parentIdOld)) {
                parentIdNew.getAutoNodeList().add(autoNode);
                parentIdNew = em.merge(parentIdNew);
            }
            for (Scenario autoScenarioListOldAutoScenario : autoScenarioListOld) {
                if (!autoScenarioListNew.contains(autoScenarioListOldAutoScenario)) {
                    autoScenarioListOldAutoScenario.getAutoNodeList().remove(autoNode);
                    autoScenarioListOldAutoScenario = em.merge(autoScenarioListOldAutoScenario);
                }
            }
            for (Scenario autoScenarioListNewAutoScenario : autoScenarioListNew) {
                if (!autoScenarioListOld.contains(autoScenarioListNewAutoScenario)) {
                    autoScenarioListNewAutoScenario.getAutoNodeList().add(autoNode);
                    autoScenarioListNewAutoScenario = em.merge(autoScenarioListNewAutoScenario);
                }
            }
            for (Node autoNodeListOldAutoNode : autoNodeListOld) {
                if (!autoNodeListNew.contains(autoNodeListOldAutoNode)) {
                    autoNodeListOldAutoNode.setParentId(null);
                    autoNodeListOldAutoNode = em.merge(autoNodeListOldAutoNode);
                }
            }
            for (Node autoNodeListNewAutoNode : autoNodeListNew) {
                if (!autoNodeListOld.contains(autoNodeListNewAutoNode)) {
                    Node oldParentIdOfAutoNodeListNewAutoNode = autoNodeListNewAutoNode.getParentId();
                    autoNodeListNewAutoNode.setParentId(autoNode);
                    autoNodeListNewAutoNode = em.merge(autoNodeListNewAutoNode);
                    if (oldParentIdOfAutoNodeListNewAutoNode != null && !oldParentIdOfAutoNodeListNewAutoNode.equals(autoNode)) {
                        oldParentIdOfAutoNodeListNewAutoNode.getAutoNodeList().remove(autoNodeListNewAutoNode);
                        oldParentIdOfAutoNodeListNewAutoNode = em.merge(oldParentIdOfAutoNodeListNewAutoNode);
                    }
                }
            }
            for (Scenario autoScenarioList1OldAutoScenario : autoScenarioList1Old) {
                if (!autoScenarioList1New.contains(autoScenarioList1OldAutoScenario)) {
                    autoScenarioList1OldAutoScenario.setRootId(null);
                    autoScenarioList1OldAutoScenario = em.merge(autoScenarioList1OldAutoScenario);
                }
            }
            for (Scenario autoScenarioList1NewAutoScenario : autoScenarioList1New) {
                if (!autoScenarioList1Old.contains(autoScenarioList1NewAutoScenario)) {
                    Node oldRootIdOfAutoScenarioList1NewAutoScenario = autoScenarioList1NewAutoScenario.getRootId();
                    autoScenarioList1NewAutoScenario.setRootId(autoNode);
                    autoScenarioList1NewAutoScenario = em.merge(autoScenarioList1NewAutoScenario);
                    if (oldRootIdOfAutoScenarioList1NewAutoScenario != null && !oldRootIdOfAutoScenarioList1NewAutoScenario.equals(autoNode)) {
                        oldRootIdOfAutoScenarioList1NewAutoScenario.getAutoScenarioList1().remove(autoScenarioList1NewAutoScenario);
                        oldRootIdOfAutoScenarioList1NewAutoScenario = em.merge(oldRootIdOfAutoScenarioList1NewAutoScenario);
                    }
                }
            }
            for (QueryDB autoQueryListOldAutoQuery : autoQueryListOld) {
                if (!autoQueryListNew.contains(autoQueryListOldAutoQuery)) {
                    autoQueryListOldAutoQuery.setNodeId(null);
                    autoQueryListOldAutoQuery = em.merge(autoQueryListOldAutoQuery);
                }
            }
            for (QueryDB autoQueryListNewAutoQuery : autoQueryListNew) {
                if (!autoQueryListOld.contains(autoQueryListNewAutoQuery)) {
                    Node oldNodeIdOfAutoQueryListNewAutoQuery = autoQueryListNewAutoQuery.getNodeId();
                    autoQueryListNewAutoQuery.setNodeId(autoNode);
                    autoQueryListNewAutoQuery = em.merge(autoQueryListNewAutoQuery);
                    if (oldNodeIdOfAutoQueryListNewAutoQuery != null && !oldNodeIdOfAutoQueryListNewAutoQuery.equals(autoNode)) {
                        oldNodeIdOfAutoQueryListNewAutoQuery.getAutoQueryList().remove(autoQueryListNewAutoQuery);
                        oldNodeIdOfAutoQueryListNewAutoQuery = em.merge(oldNodeIdOfAutoQueryListNewAutoQuery);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = autoNode.getId();
                if (findEntity(id) == null) {
                    throw new NonexistentEntityException("The Node with id " + id + " no longer exists.");
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
            Node autoNode;
            try {
                autoNode = em.getReference(entityClass, id);
                autoNode.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The Node with id " + id + " no longer exists.", enfe);
            }
            Node parentId = autoNode.getParentId();
            if (parentId != null) {
                parentId.getAutoNodeList().remove(autoNode);
                parentId = em.merge(parentId);
            }
            List<Scenario> autoScenarioList = autoNode.getAutoScenarioList();
            for (Scenario autoScenarioListAutoScenario : autoScenarioList) {
                autoScenarioListAutoScenario.getAutoNodeList().remove(autoNode);
                autoScenarioListAutoScenario = em.merge(autoScenarioListAutoScenario);
            }
            List<Node> autoNodeList = autoNode.getAutoNodeList();
            for (Node autoNodeListAutoNode : autoNodeList) {
                autoNodeListAutoNode.setParentId(null);
                autoNodeListAutoNode = em.merge(autoNodeListAutoNode);
            }
            List<Scenario> autoScenarioList1 = autoNode.getAutoScenarioList1();
            for (Scenario autoScenarioList1AutoScenario : autoScenarioList1) {
                autoScenarioList1AutoScenario.setRootId(null);
                autoScenarioList1AutoScenario = em.merge(autoScenarioList1AutoScenario);
            }
            List<QueryDB> autoQueryList = autoNode.getAutoQueryList();
            for (QueryDB autoQueryListAutoQuery : autoQueryList) {
                autoQueryListAutoQuery.setNodeId(null);
                autoQueryListAutoQuery = em.merge(autoQueryListAutoQuery);
            }
            em.remove(autoNode);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

}
