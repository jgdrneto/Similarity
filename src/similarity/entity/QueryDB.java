/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package similarity.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jullian
 */
@Entity
@Table(name = "query")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QueryDB.findAll", query = "SELECT a FROM QueryDB a")
    , @NamedQuery(name = "QueryDB.findById", query = "SELECT a FROM QueryDB a WHERE a.id = :id")
    , @NamedQuery(name = "QueryDB.findByQuery", query = "SELECT a FROM QueryDB a WHERE a.query = :query")
    , @NamedQuery(name = "QueryDB.findByType", query = "SELECT a FROM QueryDB a WHERE a.type = :type")})
public class QueryDB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "query")
    private String query;
    @Column(name = "type")
    private String type;
    @JoinColumn(name = "node_id", referencedColumnName = "id")
    @ManyToOne
    private Node nodeId;

    public QueryDB() {
    }

    public QueryDB(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Node getNodeId() {
        return nodeId;
    }

    public void setNodeId(Node nodeId) {
        this.nodeId = nodeId;
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
        if (!(object instanceof QueryDB)) {
            return false;
        }
        QueryDB other = (QueryDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "similarity.entity.QueryDB[ id=" + id + " ]";
    }
    
}
