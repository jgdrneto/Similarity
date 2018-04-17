/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package similarity.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jullian
 */
@Entity
@Table(name = "node")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Node.findAll", query = "SELECT a FROM Node a")
    , @NamedQuery(name = "Node.findById", query = "SELECT a FROM Node a WHERE a.id = :id")
    , @NamedQuery(name = "Node.findByException", query = "SELECT a FROM Node a WHERE a.exception = :exception")
    , @NamedQuery(name = "Node.findByTime", query = "SELECT a FROM Node a WHERE a.time = :time")
    , @NamedQuery(name = "Node.findByConstructor", query = "SELECT a FROM Node a WHERE a.constructor = :constructor")
    , @NamedQuery(name = "Node.findByMember", query = "SELECT a FROM Node a WHERE a.member = :member")
    , @NamedQuery(name = "Node.findByRealTime", query = "SELECT a FROM Node a WHERE a.realTime = :realTime")})
public class Node implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "exception")
    private String exception;
    @Column(name = "time")
    private BigInteger time;
    @Column(name = "constructor")
    private Boolean constructor;
    @Column(name = "member")
    private String member;
    @Column(name = "real_time")
    private BigInteger realTime;
    @ManyToMany(mappedBy = "autoNodeList")
    private List<Scenario> autoScenarioList;
    @OneToMany(mappedBy = "parentId")
    private List<Node> autoNodeList;
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @ManyToOne
    private Node parentId;
    @OneToMany(mappedBy = "rootId")
    private List<Scenario> autoScenarioList1;
    @OneToMany(mappedBy = "nodeId")
    private List<QueryDB> autoQueryList;

    public Node() {
    }

    public Node(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public BigInteger getTime() {
        return time;
    }

    public void setTime(BigInteger time) {
        this.time = time;
    }

    public Boolean getConstructor() {
        return constructor;
    }

    public void setConstructor(Boolean constructor) {
        this.constructor = constructor;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public BigInteger getRealTime() {
        return realTime;
    }

    public void setRealTime(BigInteger realTime) {
        this.realTime = realTime;
    }

    @XmlTransient
    public List<Scenario> getAutoScenarioList() {
        return autoScenarioList;
    }

    public void setAutoScenarioList(List<Scenario> autoScenarioList) {
        this.autoScenarioList = autoScenarioList;
    }

    @XmlTransient
    public List<Node> getAutoNodeList() {
        return autoNodeList;
    }

    public void setAutoNodeList(List<Node> autoNodeList) {
        this.autoNodeList = autoNodeList;
    }

    public Node getParentId() {
        return parentId;
    }

    public void setParentId(Node parentId) {
        this.parentId = parentId;
    }

    @XmlTransient
    public List<Scenario> getAutoScenarioList1() {
        return autoScenarioList1;
    }

    public void setAutoScenarioList1(List<Scenario> autoScenarioList1) {
        this.autoScenarioList1 = autoScenarioList1;
    }

    @XmlTransient
    public List<QueryDB> getAutoQueryList() {
        return autoQueryList;
    }

    public void setAutoQueryList(List<QueryDB> autoQueryList) {
        this.autoQueryList = autoQueryList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Node)) {
            return false;
        }
        Node other = (Node) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "similarity.entity.Node[ id=" + id + " ]";
    }
    
}
