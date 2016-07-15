package com.jianglibo.vaadin.dashboard.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import com.google.gwt.thirdparty.guava.common.collect.Lists;

public class ClassScanner {

	public static List<Class<?>> findAnnotatedBy(String basePackage, Class annoCls)
			throws IOException, ClassNotFoundException {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

		List<Class<?>> candidates = new ArrayList<Class<?>>();
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage)
				+ "/" + "**/*.class";
		Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
		for (Resource resource : resources) {
			if (resource.isReadable()) {
				MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
				if (isCandidate(metadataReader, annoCls)) {
					candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
				}
			}
		}
		return candidates;
	}

	public static List<Resource> findResourceInPackage(String basePackage, String suffix) {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage)
				+ suffix;
		Resource[] resources;
		List<Resource> matchedRs = Lists.newArrayList();
		try {
			resources = resourcePatternResolver.getResources(packageSearchPath);
		} catch (IOException e) {
			return matchedRs;
		}
		return Lists.newArrayList(resources);
	}

	private static String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}

	private static boolean isCandidate(MetadataReader metadataReader, Class annoCls) throws ClassNotFoundException {
		try {
			Class<?> c = Class.forName(metadataReader.getClassMetadata().getClassName());
			Annotation an = c.getAnnotation(annoCls);

			if (an != null) {
				return true;
			}
		} catch (Throwable e) {
		}
		return false;
	}
}
