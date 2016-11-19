package com.jianglibo.vaadin.dashboard.repositories;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Software;

@RepositoryRestResource(collectionResourceRel = "BoxGroupHistories", path = "BoxGroupHistories")
public interface BoxGroupHistoryRepository extends JpaRepository<BoxGroupHistory, Long>,BoxGroupHistoryRepositoryCustom<BoxGroupHistory>, JpaSpecificationExecutor<BoxGroupHistory> ,RepositoryCommonMethod<BoxGroupHistory> {

	Page<BoxGroupHistory> findByArchivedEquals(boolean trashed, Pageable pageable);
	
	Page<BoxGroupHistory> findByBoxGroupEquals(BoxGroup bg, Pageable pageable);

	long countByBoxGroupEquals(BoxGroup bg);
	
	long countByArchivedEquals(boolean trashed);
	
	List<BoxGroupHistory> findBySoftware(Software software);
}
