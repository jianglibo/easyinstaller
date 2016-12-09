package com.jianglibo.vaadin.dashboard.view.textfile;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import com.google.common.base.Strings;
import com.jianglibo.vaadin.dashboard.data.ManualPagable;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.domain.TextFile;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.repositories.TextFileRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;

public class TextFileContainer extends FreeContainer<TextFile> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TextFileContainer.class);
	
	private final TextFileRepository textFileRepository;
	
	private final SoftwareRepository softwareRepository;
	
	private Software software = null;

	public TextFileContainer(SoftwareRepository softwareRepository, TextFileRepository textFileRepository, Domains domains, int perPage, List<?> sortableContainerPropertyIds) {
		super(domains.getRepositoryCommonCustom(TextFile.class.getSimpleName()), domains.getDefaultSort(TextFile.class), TextFile.class, perPage, sortableContainerPropertyIds);
		this.textFileRepository = textFileRepository;
		this.softwareRepository = softwareRepository;
	}
	
	
	@Override
	public void whenUriFragmentChange(ListViewFragmentBuilder lvfb) {
		super.whenUriFragmentChange(lvfb);
		software = softwareRepository.findOne(lvfb.getLong("software"));
		notifyItemSetChanged();
	}

	@Override
	public int size() {
		if (software == null) {
			return 0;
		} else {
			if (Strings.isNullOrEmpty(getFilterString())) {
				return new Long(textFileRepository.countBySoftwareEquals(software)).intValue();
			} else {
				return new Long(textFileRepository.countBySoftwareEqualsAndNameContaining(software, getFilterString())).intValue();
			}
			
		}
	}
	
	@Override
	public void fetchPage() {
		ManualPagable pageable = new ManualPagable(getCurrentPage(), getPerPage(), getSort());
		LOGGER.info("fetch page with {}, {}, {}", getCurrentPage(), getPerPage(), getSort());
		Page<TextFile> tfs;
		if (Strings.isNullOrEmpty(getFilterString())) {
			tfs = textFileRepository.findBySoftwareEquals(software, pageable);
		} else {
			tfs = textFileRepository.findBySoftwareEqualsAndNameContaining(software, pageable, getFilterString());
		}
		setCurrentWindow(tfs.getContent());
	}

}
