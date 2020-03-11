package com.repository.lite;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

public class RepositoryClassPathScanner extends ClassPathScanningCandidateComponentProvider {

    public RepositoryClassPathScanner(final boolean useDefaultFilters) {
        super(useDefaultFilters);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isIndependent();
    }

}
