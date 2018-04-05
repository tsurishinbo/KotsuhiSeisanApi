package jp.co.stcinc.api.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 認証情報エンティティ
 */
@Entity
@Table(name = "t_auth")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TAuth.findAll", query = "SELECT t FROM TAuth t")
    , @NamedQuery(name = "TAuth.findByEmpNo", query = "SELECT t FROM TAuth t WHERE t.empNo = :empNo")
    , @NamedQuery(name = "TAuth.findByToken", query = "SELECT t FROM TAuth t WHERE t.token = :token")
    , @NamedQuery(name = "TAuth.findByExpire", query = "SELECT t FROM TAuth t WHERE t.expire = :expire")})
public class TAuth implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "emp_no")
    private Integer empNo;
    @Size(max = 100)
    @Column(name = "token")
    private String token;
    @Size(max = 14)
    @Column(name = "expire")
    private String expire;

    public TAuth() {
    }

    public TAuth(Integer empNo) {
        this.empNo = empNo;
    }

    public Integer getEmpNo() {
        return empNo;
    }

    public void setEmpNo(Integer empNo) {
        this.empNo = empNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empNo != null ? empNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TAuth)) {
            return false;
        }
        TAuth other = (TAuth) object;
        if ((this.empNo == null && other.empNo != null) || (this.empNo != null && !this.empNo.equals(other.empNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jp.co.stcinc.api.entity.TAuth[ empNo=" + empNo + " ]";
    }
    
}
