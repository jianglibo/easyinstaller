package com.jianglibo.vaadin.dashboard.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.jianglibo.vaadin.dashboard.ssh.StepConfig;
import com.vaadin.ui.themes.ValoTheme;


@Entity
@VaadinTable(multiSelect=true, messagePrefix="domain.steprun.",footerVisible=true, styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true)
@Table(name = "steprun")
public class StepRun extends BaseEntity implements HasPositionField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@VaadinTableColumn(order = 10)
	private String name;
	
	@VaadinTableColumn(order = 20)
	private String ostype;
	
	@ManyToOne
	private StepDefine stepDefine;
	
	@ManyToOne
	private Install install;
	
	private String uniqueFileName;
	
	@OneToOne
	private JschExecuteResult result;
	
	@VaadinTableColumn(sortable=true, order=20)
	private int position;
	
	public StepRun() {
	}
	
	public StepRun(OrderedStepDefine orderedStepDefine) {
		setStepDefine(orderedStepDefine.getStepDefine());
		setPosition(orderedStepDefine.getPosition());
		setName(orderedStepDefine.getStepDefine().getName());
		setOstype(orderedStepDefine.getStepDefine().getOstype());
	}
	
	@Lob
	@Column(length = 64000)
	@VaadinFormField(fieldType = Ft.TEXT_AREA, order = 40)
	private String ymlContent;

	@Lob
	@Column(length = 64000)
	@VaadinFormField(fieldType = Ft.TEXT_AREA, order = 30)
	private String codeContent;

	private boolean ifSuccessSkipNext;

	@Override
	public String getDisplayName() {
		return String.format("[%s,%s,%s]", getName(), getOstype(), getId());
	}
	
	@PrePersist
	public void beforeSave() {
		setUniqueFileName(UUID.randomUUID().toString());
	}

	public JschExecuteResult getResult() {
		return result;
	}

	public void setResult(JschExecuteResult result) {
		this.result = result;
	}

	public int getPosition() {
			return position;
	}


	public void setPosition(int position) {
		this.position = position;
	}

	public StepDefine getStepDefine() {
		return stepDefine;
	}

	public void setStepDefine(StepDefine stepDefine) {
		this.stepDefine = stepDefine;
	}

	public Install getInstall() {
		return install;
	}

	public void setInstall(Install install) {
		this.install = install;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOstype() {
		return ostype;
	}

	public void setOstype(String ostype) {
		this.ostype = ostype;
	}

	public String getYmlContent() {
		return ymlContent;
	}

	public void setYmlContent(String ymlContent) {
		this.ymlContent = ymlContent;
	}

	public String getCodeContent() {
		return codeContent;
	}

	public void setCodeContent(String codeContent) {
		this.codeContent = codeContent;
	}

	public boolean isIfSuccessSkipNext() {
		return ifSuccessSkipNext;
	}

	public void setIfSuccessSkipNext(boolean ifSuccessSkipNext) {
		this.ifSuccessSkipNext = ifSuccessSkipNext;
	}

	public String getUniqueFileName() {
		return uniqueFileName;
	}

	public void setUniqueFileName(String uniqueFileName) {
		this.uniqueFileName = uniqueFileName;
	}
}
