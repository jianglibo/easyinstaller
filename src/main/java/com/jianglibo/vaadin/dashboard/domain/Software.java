package com.jianglibo.vaadin.dashboard.domain;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import com.google.common.io.CharStreams;
import com.jianglibo.vaadin.dashboard.GlobalComboOptions;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGrid;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByYaml;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ScalarGridFieldDescription;
import com.jianglibo.vaadin.dashboard.util.SoftwareUtil;
import com.jianglibo.vaadin.dashboard.util.StrUtil;
import com.jianglibo.vaadin.dashboard.vo.FileToUploadVo;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Software is made of files to upload, code to execute, and custom
 * configurations. When install happen, also given an environment which take
 * information of box to install, and boxgroup target box belongs.
 * 
 * 
 * @author jianglibo@gmail.com
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "software", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "ostype", "sversion" }) })
@VaadinTable(multiSelect = true, footerVisible = true, messagePrefix = "domain.software.", styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true, defaultSort="-updatedAt")
@VaadinGrid(multiSelect = true, footerVisible = true, messagePrefix = "domain.software.", styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true, defaultSort="-updatedAt", selectMode = Grid.SelectionMode.MULTI)
public class Software extends BaseEntity implements HasUpdatedAt {

	@VaadinFormField(order = 10)
	@VaadinTableColumn(alignment = Align.LEFT)
	@NotNull
	@VaadinGridColumn
	private String name;

	@VaadinFormField(order = 15)
	@VaadinGridColumn
	@NotNull
	private String sversion;

	@ComboBoxBackByYaml(ymlKey = GlobalComboOptions.OS_TYPES)
	@VaadinFormField(fieldType = Ft.COMBO_BOX, order = 20)
	@VaadinTableColumn()
	@NotNull
	@VaadinGridColumn
	private String ostype;
	
	/**
	 * runas can be a single string, or json or anything else. It's up to consumer to explain it.
	 */
	@VaadinFormField(order = 25)
	private String runas;
	
	@VaadinFormField(order = 27)
	private String possibleRoles;

	@VaadinGridColumn
	@NotNull
	@VaadinFormField(order = 30)
	private String runner;
	
	@VaadinFormField(order = 35)
	private String timeouts;
	
	@OneToMany(fetch = FetchType.EAGER, cascade={CascadeType.REMOVE,CascadeType.PERSIST}, mappedBy = "software")
	private Set<TextFile> textfiles = Sets.newHashSet();
	
	@Temporal(TemporalType.TIMESTAMP)
	@VaadinTableColumn(order = 9995, sortable = true)
	@VaadinGridColumn(order = 9995, sortable = true)
	private Date updatedAt;

	@ElementCollection(fetch = FetchType.EAGER)
	@VaadinFormField(fieldType = Ft.HAND_MAKER, order = 100)
	@ScalarGridFieldDescription(columns = { "value", "!remove" }, clazz = String.class, rowNumber = 4)
	private Set<String> filesToUpload = Sets.newHashSet();

	@Lob
	@Column(length = 154112)
	@VaadinFormField(fieldType = Ft.TEXT_AREA, order = 120, rowNumber = 10)
	private String codeToExecute;

	/**
	 * some script language need file extension, for example powershell need ps1
	 * extension.
	 * 
	 */
	@VaadinFormField(order = 125)
	private String codeFileExt;

	@VaadinFormField(fieldType = Ft.COMBO_BOX, order = 130)
	@ComboBoxBackByYaml(ymlKey = GlobalComboOptions.LINE_SEPERATOR)
	private String codeLineSeperator = "LF";

	@Lob
	@Column(length = 154112)
	@VaadinFormField(fieldType = Ft.TEXT_AREA, order = 110)
	private String configContent;

	@VaadinFormField(fieldType = Ft.COMBO_BOX, order = 115)
	@ComboBoxBackByYaml(ymlKey = GlobalComboOptions.PREFERED_FORMAT)
	private String preferredFormat;

	@ManyToOne
	@NotNull
	private Person creator;

	@VaadinFormField(order = 200)
	private String actions = "install";

	@Lob
	@Column(length = 154112)
	@VaadinFormField(fieldType = Ft.TEXT_AREA, order = 140)
	private String actionDescriptions;

	@PreUpdate
	@Override
	public void preUpdate() {
		super.preUpdate();
		if (getActions() == null) {
			setActions("install");
		} else {
			if (getActions().trim().isEmpty()) {
				setActions("install");
			} else {
				setActions(StrUtil.commaJoiner.join(StrUtil.commaSplitter.split(getActions())));
			}
		}
		if (getPossibleRoles() != null) {
			setPossibleRoles(getPossibleRoles().toUpperCase());
		}
		try {
			setConfigContent(Joiner.on(SoftwareUtil.parseLs(getCodeLineSeperator())).join(CharStreams.readLines(new StringReader(getConfigContent()))));
		} catch (IOException e) {
		}
	}
	
	public Set<String> getPossibleRoleSetUpcase() {
		if (getPossibleRoles() == null || getPossibleRoles().trim().isEmpty()) {
			return Sets.newHashSet();
		} else {
			return Sets.newHashSet(StrUtil.commaSplitter.split(getPossibleRoles().toUpperCase()));
		}
	}
	
	public Map<String, String> getActionDescriptionsMap() {
		Map<String, String> mp = Maps.newHashMap();
		try {
			JavaType jt = SoftwareUtil.ymlObjectMapper.getTypeFactory().constructParametrizedType(Map.class,Map.class, String.class, String.class);
			mp = SoftwareUtil.ymlObjectMapper.readValue(getActionDescriptions(),jt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mp;
	}
	
	public Map<String, Long> getTimeOutMaps() {
		Map<String, Long> mp = Maps.newHashMap();
		if (!Strings.isNullOrEmpty(getTimeouts())) {
			Splitter.on(',').omitEmptyStrings().trimResults().split(getTimeouts()).iterator().forEachRemaining(colonPair -> {
				String[] ss = colonPair.split(":");
				if (ss.length == 2) {
					String key = ss[0].trim();
					String value = ss[1].trim();
					String unit = "";
					if (value.endsWith("ms")) {
						value = value.substring(0, value.length() - 2);
						unit = "ms";
					} else if (value.endsWith("s")) {
						value = value.substring(0, value.length() - 1);
						unit = "s";
					} else if (value.endsWith("m")) {
						value = value.substring(0, value.length() - 1);
						unit = "m";
					} else if (value.endsWith("h")) {
						value = value.substring(0, value.length() - 1);
						unit = "h";
					}
					
					if (value.matches("^\\d+$")) {
						Long lvalue = Long.valueOf(value);
						switch (unit) {
						case "s":
							lvalue = lvalue * 1000;
							break;
						case "m":
							lvalue = lvalue * 60000;
							break;
						case "h":
							lvalue = lvalue * 3600000;
						default:
							break;
						}
						mp.put(key, lvalue);
					}
				}
			});
		}
		return mp;
	}

	public String getTimeouts() {
		return timeouts;
	}

	public void setTimeouts(String timeouts) {
		this.timeouts = timeouts;
	}

	public String getCodeFileName(String content) {
		String md5 = Hashing.md5().newHasher().putString(content, Charsets.UTF_8).hash().toString();
		if (Strings.isNullOrEmpty(getCodeFileExt())) {
			return md5;
		} else {
			if (getCodeFileExt().indexOf('.') != 0) {
				return md5 + "." + getCodeFileExt();
			} else {
				return md5 + getCodeFileExt();
			}
		}
	}


	public Software() {

	}

	public Set<FileToUploadVo> getFileToUploadVos() {
		return getFilesToUpload().stream().map(FileToUploadVo::new).collect(Collectors.toSet());
	}

	public Software(String name, String ostype) {
		setName(name);
		setOstype(ostype);
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

	public Set<String> getFilesToUpload() {
		return filesToUpload;
	}

	public void setFilesToUpload(Set<String> filesToUpload) {
		this.filesToUpload = filesToUpload;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("name", getName()).add("ostye", getOstype())
				.add("sversion", getSversion()).toString();
	}

	@Override
	public String getDisplayName() {
		return String.format("%s-%s-%s", getName(), getOstype(), getSversion());
	}

	public String getCodeToExecute() {
		return codeToExecute;
	}

	public void setCodeToExecute(String codeToExecute) {
		this.codeToExecute = codeToExecute;
	}

	public String getConfigContent() {
		return configContent;
	}

	public void setConfigContent(String configContent) {
		this.configContent = configContent;
	}

	public String getPreferredFormat() {
		return preferredFormat;
	}

	public void setPreferredFormat(String preferredFormat) {
		this.preferredFormat = preferredFormat;
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public String getRunner() {
		return runner;
	}

	public void setRunner(String runner) {
		this.runner = runner;
	}

	public String getSversion() {
		return sversion;
	}

	public void setSversion(String sversion) {
		this.sversion = sversion;
	}

	public String getCodeLineSeperator() {
		return codeLineSeperator;
	}

	public void setCodeLineSeperator(String codeLineSeperator) {
		this.codeLineSeperator = codeLineSeperator;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	

	public void copyFrom(Software vo) {
		setActions(vo.getActions());
		setArchived(vo.isArchived());
		setCodeFileExt(vo.getCodeFileExt());
		setCodeLineSeperator(vo.getCodeLineSeperator());
		setCodeToExecute(vo.getCodeToExecute());
		setConfigContent(vo.getConfigContent());
		setFilesToUpload(vo.getFilesToUpload());
		setPreferredFormat(vo.getPreferredFormat());
		setRunas(vo.getRunas());
		setRunner(vo.getRunner());
		setPossibleRoles(vo.getPossibleRoles());
//		setTextfiles(vo.getTextfiles());
	}

	public String getRunas() {
		return runas;
	}

	public void setRunas(String runas) {
		this.runas = runas;
	}

	public String getCodeFileExt() {
		return codeFileExt;
	}

	public void setCodeFileExt(String codeFileExt) {
		this.codeFileExt = codeFileExt;
	}

	public Set<TextFile> getTextfiles() {
		return textfiles;
	}

	public void setTextfiles(Set<TextFile> textfiles) {
		this.textfiles = textfiles;
	}

	public String getPossibleRoles() {
		return possibleRoles;
	}

	public void setPossibleRoles(String possibleRoles) {
		if (possibleRoles != null) {
			this.possibleRoles = possibleRoles.toUpperCase();
		} else {
			this.possibleRoles = possibleRoles;
		}
	}

	public String getActionDescriptions() {
		return actionDescriptions;
	}

	public void setActionDescriptions(String actionDescriptions) {
		this.actionDescriptions = actionDescriptions;
	}
}
