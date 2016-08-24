package com.jianglibo.vaadin.dashboard.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;

@Component
public class EntityCacheWrapper extends CacheLoader<ClassNameAndId, BaseEntity>{

	private LoadingCache<ClassNameAndId, BaseEntity> cacher;
	
	
	@Autowired
	private Domains domains;
	
	public EntityCacheWrapper() {
		this.cacher = CacheBuilder.newBuilder().maximumSize(1000)
				.build(this);
	}
	
//	public Object nextItemId(BaseEntity be) {
//		
//		if (indexAndOid.containsValue(be.getId())) {
//			int idx = indexAndOid.inverse().get(be.getId());
//			if (indexAndOid.containsKey(idx + 1)) {
//				try {
//					return (T) entities.get(indexAndOid.get(idx + 1));
//				} catch (ExecutionException e) {
//					return null;
//				}
//			} else {
//				return fetchFromOutSide(be);
//			}
//		} else {
//			return fetchFromOutSide(be);
//		}
//	}

	public LoadingCache<ClassNameAndId, BaseEntity> getCacher() {
		return cacher;
	}


	@Override
	public BaseEntity load(ClassNameAndId key) throws Exception {
		return null;
	}
	
}
