/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package similarity.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "execution")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Execution.findAll", query = "SELECT a FROM Execution a")
    , @NamedQuery(name = "Execution.findById", query = "SELECT a FROM Execution a WHERE a.id = :id")
    , @NamedQuery(name = "Execution.findByDate", query = "SELECT a FROM Execution a WHERE a.date = :date")
    , @NamedQuery(name = "Execution.findBySystemName", query = "SELECT a FROM Execution a WHERE a.systemName = :systemName")
    , @NamedQuery(name = "Execution.findBySystemVersion", query = "SELECT a FROM Execution a WHERE a.systemVersion = :systemVersion")})
public class Execution implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "system_name")
    private String systemName;
    @Column(name = "system_version")
    private String systemVersion;
    @OneToMany(mappedBy = "executionId")
    private List<Scenario> autoScenarioList;

    public Execution() {
    }

    public Execution(Long id) {
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

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    @XmlTransient
    public List<Scenario> getAutoScenarioList() {
        return autoScenarioList;
    }

    public void setAutoScenarioList(List<Scenario> autoScenarioList) {
        this.autoScenarioList = autoScenarioList;
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
        if (!(object instanceof Execution)) {
            return false;
        }
        Execution other = (Execution) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "similarity.entity.Execution[ id=" + id + " ]";
    }
    
}
