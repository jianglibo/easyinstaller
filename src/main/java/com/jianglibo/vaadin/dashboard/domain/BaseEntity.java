package com.jianglibo.vaadin.dashboard.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	@Version
	private int version;

	@Temporal(TemporalType.TIMESTAMP)
	@VaadinTableColumn(order = 10000, sortable = true)
	@VaadinGridColumn(order = 10000, sortable = true)
	private Date createdAt;

	@VaadinTableColumn(order = 99990, sortable = true)
	@VaadinGridColumn(order = 10000, sortable = true)
	private boolean archived = false;

	public abstract String getDisplayName();

	@PrePersist
	public void createCreatedAt() {
		Date now = Date.from(Instant.now());
		setCreatedAt(now);
		if (this instanceof HasUpdatedAt) {
			((HasUpdatedAt)this).setUpdatedAt(now);
		}
	}
	
	@PreUpdate
	public void preUpdate() {
		if (this instanceof HasUpdatedAt) {
			((HasUpdatedAt)this).setUpdatedAt(Date.from(Instant.now()));	
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}
}
