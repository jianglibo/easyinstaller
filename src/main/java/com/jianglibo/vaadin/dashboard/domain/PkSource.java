package com.jianglibo.vaadin.dashboard.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pksource", uniqueConstraints = { @UniqueConstraint(columnNames = "fileMd5") })
public class PkSource extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
    @Column(nullable = false)
	private String fileMd5;

    private String pkname;
    
    private String originFrom;
    
    private Long length;
    

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public String getPkname() {
		return pkname;
	}

	public void setPkname(String pkname) {
		this.pkname = pkname;
	}

	public String getOriginFrom() {
		return originFrom;
	}

	public void setOriginFrom(String originFrom) {
		this.originFrom = originFrom;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}
}
