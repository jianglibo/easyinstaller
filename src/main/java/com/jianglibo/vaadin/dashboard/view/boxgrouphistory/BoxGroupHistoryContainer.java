package com.jianglibo.vaadin.dashboard.view.boxgrouphistory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import com.jianglibo.vaadin.dashboard.data.ManualPagable;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupHistoryRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;

@SuppressWarnings("serial")
public class BoxGroupHistoryContainer extends FreeContainer<BoxGroupHistory> {
	
	private final BoxGroupHistoryRepository boxGroupHistoryRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BoxGroupHistoryContainer.class);

	public BoxGroupHistoryContainer(BoxGroupHistoryRepository boxGroupHistoryRepository, Domains domains, int perPage, List<?> sortableContainerPropertyIds) {
		super(domains.getRepositoryCommonCustom(BoxGroupHistory.class.getSimpleName()), domains.getDefaultSort(BoxGroupHistory.class), BoxGroupHistory.class, perPage, sortableContainerPropertyIds);
		this.boxGroupHistoryRepository = boxGroupHistoryRepository;
	}
	
	@Override
	public void whenUriFragmentChange(ListViewFragmentBuilder lvfb) {
		super.whenUriFragmentChange(lvfb);
		notifyItemSetChanged();
	}

	@Override
	public int size() {
		if (getLvfb() == null) {
			return 0;
		} else {
			return super.size();
		}
	}
	
	@Override
	public void fetchPage() {
		ManualPagable pageable = new ManualPagable(getCurrentPage(), getPerPage(), getSort());
		LOGGER.info("fetch page with {}, {}, {}", getCurrentPage(), getPerPage(), getSort());
		Page<BoxGroupHistory> gphs = boxGroupHistoryRepository.findAll(pageable);
		setCurrentWindow(gphs.getContent());
	}
}
