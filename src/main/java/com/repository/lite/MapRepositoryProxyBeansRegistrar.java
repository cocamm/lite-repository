package com.repository.lite;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Configuration
public class MapRepositoryProxyBeansRegistrar implements ImportBeanDefinitionRegistrar, BeanClassLoaderAware, ResourceLoaderAware, EnvironmentAware {

    private RepositoryClassPathScanner classpathScannerCustomRepository;
    private ClassLoader classLoader;
    private ResourceLoader resourceLoader;
    private Environment environment;

    public MapRepositoryProxyBeansRegistrar() {
        classpathScannerCustomRepository = new RepositoryClassPathScanner(false);
        classpathScannerCustomRepository.addIncludeFilter(new AnnotationTypeFilter(Repository.class));
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        String[] basePackages = getBasePackages(importingClassMetadata);
        if (!CollectionUtils.isEmpty(Arrays.asList(basePackages))) {
            for (String basePackage : basePackages) {
                createWebServicProxies(basePackage, registry);
            }
        }
    }

    private String[] getBasePackages(AnnotationMetadata importingClassMetadata) {

        String[] basePackages = null;

        MultiValueMap<String, Object> allAnnotationAttributes =
                importingClassMetadata.getAllAnnotationAttributes(EnableMapRepositories.class.getName());

        if (!allAnnotationAttributes.isEmpty()) {
            basePackages = (String[]) allAnnotationAttributes.getFirst("basePackages");
        }

        return basePackages;
    }

    private void createWebServicProxies(String basePackage, BeanDefinitionRegistry registry) {
        try {

            for (BeanDefinition beanDefinition : classpathScannerCustomRepository.findCandidateComponents(basePackage)) {

                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());

                Repository repository = clazz.getAnnotation(Repository.class);

                String beanName = !StringUtils.isEmpty(repository.value())
                        ? repository.value() : ClassUtils.getShortNameAsProperty(clazz);

                Class<?> repositoryInterface = ClassUtils.forName(beanDefinition.getBeanClassName(), classLoader);

                BeanDefinitionBuilder builder = BeanDefinitionBuilder
                        .rootBeanDefinition("com.study.customrepository.MapRepositoryFactorySupport");
                builder.addConstructorArgValue(repositoryInterface);

                AbstractBeanDefinition proxyBeanDefinition = builder.getBeanDefinition();
                registry.registerBeanDefinition(beanName, proxyBeanDefinition);
            }
        } catch (Exception e) {
            System.out.println("Exception while createing proxy");
            e.printStackTrace();
        }

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
