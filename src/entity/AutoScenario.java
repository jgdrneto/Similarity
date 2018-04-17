/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jullian
 */
@Entity
@Table(name = "scenario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AutoScenario.findAll", query = "SELECT a FROM AutoScenario a")
    , @NamedQuery(name = "AutoScenario.findById", query = "SELECT a FROM AutoScenario a WHERE a.id = :id")
    , @NamedQuery(name = "AutoScenario.findByDate", query = "SELECT a FROM AutoScenario a WHERE a.date = :date")
    , @NamedQuery(name = "AutoScenario.findByName", query = "SELECT a FROM AutoScenario a WHERE a.name = :name")
    , @NamedQuery(name = "AutoScenario.findByRequestParameters", query = "SELECT a FROM AutoScenario a WHERE a.requestParameters = :requestParameters")
    , @NamedQuery(name = "AutoScenario.findByRequestUrl", query = "SELECT a FROM AutoScenario a WHERE a.requestUrl = :requestUrl")
    , @NamedQuery(name = "AutoScenario.findByThreadId", query = "SELECT a FROM AutoScenario a WHERE a.threadId = :threadId")})
public class AutoScenario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "name")
    private String name;
    @Column(name = "request_parameters")
    private String requestParameters;
    @Column(name = "request_url")
    private String requestUrl;
    @Column(name = "thread_id")
    private BigInteger threadId;
    @JoinTable(name = "node_scenario", joinColumns = {
        @JoinColumn(name = "scenario_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "node_id", referencedColumnName = "id")})
    @ManyToMany
    private List<AutoNode> autoNodeList;
    @JoinColumn(name = "execution_id", referencedColumnName = "id")
    @ManyToOne
    private AutoExecution executionId;
    @JoinColumn(name = "root_id", referencedColumnName = "id")
    @ManyToOne
    private AutoNode rootId;

    public AutoScenario() {
    }

    public AutoScenario(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(String requestParameters) {
        this.requestParameters = requestParameters;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public BigInteger getThreadId() {
        return threadId;
    }

    public void setThreadId(BigInteger threadId) {
        this.threadId = threadId;
    }

    @XmlTransient
    public List<AutoNode> getAutoNodeList() {
        return autoNodeList;
    }

    public void setAutoNodeList(List<AutoNode> autoNodeList) {
        this.autoNodeList = autoNodeList;
    }

    public AutoExecution getExecutionId() {
        return executionId;
    }

    public void setExecutionId(AutoExecution executionId) {
        this.executionId = executionId;
    }

    public AutoNode getRootId() {
        return rootId;
    }

    public void setRootId(AutoNode rootId) {
        this.rootId = rootId;
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
        if (!(object instanceof AutoScenario)) {
            return false;
        }
        AutoScenario other = (AutoScenario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AutoScenario[ id=" + id + " ]";
    }
    
}
