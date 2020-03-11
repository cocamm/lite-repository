package com.repository.lite;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.util.Assert;

public class MapRepositoryFactorySupport<T extends MapRepository<S, ID>, S, ID> implements InitializingBean, FactoryBean<T>, BeanClassLoaderAware, BeanFactoryAware {

    private final Class<? extends T> repositoryInterface;
    private ClassLoader classLoader;
    private BeanFactory beanFactory;

    protected MapRepositoryFactorySupport(Class<? extends T> repositoryInterface) {
        Assert.notNull(repositoryInterface, "Repository interface must not be null!");
        this.repositoryInterface = repositoryInterface;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public T getObject() throws Exception {
        Object target = new SimpleMapRepository<T, ID>();

        // Create proxy
        ProxyFactory result = new ProxyFactory();
        result.setTarget(target);
        result.setInterfaces(repositoryInterface, MapRepository.class);
        T repository = (T) result.getProxy(classLoader);

        return repository;
    }

    @Override
    public Class<?> getObjectType() {
        return repositoryInterface;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
