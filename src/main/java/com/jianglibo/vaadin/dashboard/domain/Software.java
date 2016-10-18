package com.jianglibo.vaadin.dashboard.domain;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import com.google.common.io.CharStreams;
import com.jianglibo.vaadin.dashboard.GlobalComboOptions;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGrid;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByYaml;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ScalarGridFieldDescription;
import com.jianglibo.vaadin.dashboard.vo.FileToUploadVo;
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
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
@VaadinGrid(multiSelect = true, footerVisible = true, messagePrefix = "domain.software.", styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
public class Software extends BaseEntity {

	public static final Splitter commaSplitter = Splitter.on(',').trimResults().omitEmptyStrings();
	public static final Joiner commaJoiner = Joiner.on(',').skipNulls();
	
	public static final Pattern COMMON_SCRIPT_TAG = Pattern.compile("^.*insert-common-script-here:\\s*(\\S+)\\s*$");
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Software.class);

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

	@VaadinFormField(order = 25)
	private String runas;

	@VaadinGridColumn
	@NotNull
	@VaadinFormField(order = 30)
	private String runner;

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

	@PrePersist
	public void createCreatedAt() {
		setCreatedAt(Date.from(Instant.now()));
	}

	@PreUpdate
	public void normalizeComm() {
		setConfigContent(getConfigContent().replaceAll("\r", ""));
		if (getActions() == null) {
			setActions("install");
		} else {
			if (getActions().trim().isEmpty()) {
				setActions("install");
			} else {
				setActions(commaJoiner.join(commaSplitter.split(getActions())));
			}
		}
		try {
			setConfigContent(Joiner.on(parseLs()).join(CharStreams.readLines(new StringReader(getConfigContent()))));
		} catch (IOException e) {
		}
		
//		try {
//			setCodeToExecute(Joiner.on(parseLs()).join(CharStreams.readLines(new StringReader(getCodeToExecute()))));
//		} catch (IOException e) {
//		}

	}
	
	private String parseLs() {
		String cls = Strings.isNullOrEmpty(getCodeLineSeperator()) ? "LF" : getCodeLineSeperator().toUpperCase();
		return cls.replace("CR", "\r").replace("LF", "\n");
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
	
	// allow code snippet to insert. if a line in code contains "insert-common-script-here: spring resource url", then get resource content, and insert here.	
	public String getParsedCodeToExecute(ApplicationContext context) {
		try {
			List<String> lines = CharStreams.readLines(new StringReader(getCodeToExecute()));
			List<String> newlines = Lists.newArrayList();
			
			for(String line : lines) {
				Matcher m = COMMON_SCRIPT_TAG.matcher(line);
				if (m.matches()) {
					String rsptn = m.group(1);
					try {
						Resource[] rs = context.getResources(rsptn);
						for(Resource r : rs) {
							List<String> rlines = CharStreams.readLines(new InputStreamReader(r.getInputStream(), Charsets.UTF_8));
							newlines.addAll(rlines);
						}
					} catch (Exception e) {
						LOGGER.warn(e.getMessage());
						return getCodeToExecute();
					}
				} else {
					newlines.add(line);
				}
			}
			return Joiner.on(parseLs()).join(newlines);
		} catch (IOException e) {
			LOGGER.warn(e.getMessage());
			return getCodeToExecute();
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
		return String.format("%s--%s--%s", getName(), getOstype(), getSversion());
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
}
