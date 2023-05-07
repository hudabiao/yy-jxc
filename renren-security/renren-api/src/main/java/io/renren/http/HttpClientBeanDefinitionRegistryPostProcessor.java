package io.renren.http;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author hudaibiao
 */
@Component
public class HttpClientBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware {
//    private ResourcePatternResolver resourcePatternResolver;
//    private MetadataReaderFactory metadataReaderFactory;
    private ResourceLoader resourceLoader;
//    private static final String FULLTEXT_SCAN_PACKAGE_PATH = "fulltext.scan.package";

    @SneakyThrows
    @Override
    public void postProcessBeanDefinitionRegistry(@NotNull BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        final Set<Class<?>> classes = doScan("io.renren");
        // 扫描带有自定义注解的类
        for (Class clazz : classes) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
            AbstractBeanDefinition rawBeanDefinition = builder.getRawBeanDefinition();
            rawBeanDefinition.setBeanClass(HttpClientFactoryBean.class);
            rawBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(clazz);
            rawBeanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            beanDefinitionRegistry.registerBeanDefinition(clazz.getSimpleName(), rawBeanDefinition);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    /**
     * set注入对象
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 利用spring提供的扫描包下面的类信息,再通过classfrom反射获得类信息
     *
     * @param scanPath
     * @return
     * @throws IOException
     */
    public Set<Class<?>> doScan(String scanPath) throws IOException, ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                .concat(ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(scanPath))
                        .concat("/**/*.class"));
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        Resource[] resources = resolver.getResources(packageSearchPath);
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
        MetadataReader metadataReader = null;
        for (Resource resource : resources) {
            if (!resource.isReadable()) {
                continue;
            }
            metadataReader = metadataReaderFactory.getMetadataReader(resource);
            final ClassMetadata classMetadata = metadataReader.getClassMetadata();
            if(!classMetadata.isInterface()){
                continue;
            }
            Class<?> aClass = Class.forName(classMetadata.getClassName());
            if(aClass.getDeclaredAnnotation(HttpClient.class)!=null){
                classes.add(aClass);
            }
        }
        return classes;
    }
}
