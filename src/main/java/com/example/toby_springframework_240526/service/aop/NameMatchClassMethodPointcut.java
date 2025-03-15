package com.example.toby_springframework_240526.service.aop;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.util.PatternMatchUtils;

public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut {

    public void setMappedClassName(String mappedNamePattern) {
        this.setClassFilter(new SimpleClassFilter(mappedNamePattern));
    }

    private static class SimpleClassFilter implements ClassFilter {
        String mappedName;

        public SimpleClassFilter(String mappedNamePattern) {
            this.mappedName = mappedNamePattern;
        }

        @Override
        public boolean matches(Class<?> clazz) {
            return PatternMatchUtils.simpleMatch(this.mappedName, clazz.getSimpleName());
        }
    }
}
