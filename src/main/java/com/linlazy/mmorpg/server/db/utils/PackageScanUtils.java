package com.linlazy.mmorpg.server.db.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * 扫描包下的所有类
 * @author linlazy
 */
public class PackageScanUtils{


    private static final Logger logger = LoggerFactory.getLogger(PackageScanUtils.class);

    /**
     * 路径资匹配解析器
     */
    private static final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private static final MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();

    /**
     * 默认资源匹配模式
     */
    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    /**
     * 扫描包名
     * @param packageName
     * @return
     */
    public static <T> Collection<Class<T>> scanPackage(String packageName) {
        Collection<Class<T>> classCollection = new HashSet<>();


        String packageSearchPattern ="classpath*:" + resolvePackageName(packageName) + DEFAULT_RESOURCE_PATTERN;
        Resource[] resources = new Resource[0];
        try {
            resources = resourcePatternResolver.getResources(packageSearchPattern);
        } catch (IOException e) {
            logger.error("{}",e);
        }
        for(Resource resource: resources){
            String className = null;
            try {
                className = metadataReaderFactory.getMetadataReader(resource).getClassMetadata().getClassName();
            } catch (IOException e) {
                logger.error("{}",e);
            }
            Class<T> aClass = null;
            try {
                aClass = (Class<T>) Class.forName(className);
            } catch (ClassNotFoundException e) {
                logger.error("{}",e);
            }
            classCollection.add(aClass);
        }
        return classCollection;
    }

    /**
     * 解析包名称路径字符串
     * @param packageName
     * @return
     */
    private static String resolvePackageName(String packageName) {
        String placeholder = SystemPropertyUtils.resolvePlaceholders(packageName);
        return ClassUtils.convertClassNameToResourcePath(placeholder);
    }
}
