package com.nastenkapusechka.validation.util;

import com.nastenkapusechka.validation.analyzers.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * The class is designed for convenient storage,
 * search and retrieval of annotations and their analyzers
 *
 */
public class AnnotationRepository {

    private final Map<Class<? extends Annotation>, AnnotationAnalyzer> repository;

    public AnnotationRepository() {

        repository = new HashMap<>();

        /*
         * we register all our analyzers
         */

        registerAnalyser(new CardNumberAnalyzer());
        registerAnalyser(new EmailAnalyzer());
        registerAnalyser(new NotNullAnalyzer());
        registerAnalyser(new PasswordAnalyzer());
        registerAnalyser(new PhoneNumberAnalyzer());
        registerAnalyser(new ThreadSafeAnalyzer());
        registerAnalyser(new ValidateAnalyzer());
    }

    /**
     *
     * @param analyzer is a parser class for a specific annotation
     */
    public void registerAnalyser(AnnotationAnalyzer analyzer) {

        Class<?> cls = analyzer.getClass();
        AnnotationName annotation = cls.getAnnotation(AnnotationName.class);
        repository.put(annotation.value(), analyzer);

    }

    /**
     *
     * @param cls one of our annotations
     *                  @see AnnotationRepository#AnnotationRepository()
     * @return parser for this annotation
     */
    public AnnotationAnalyzer getForAnnotation(Class<? extends Annotation> cls) {

        return repository.get(cls);

    }


    /**
     * Here we can see the contents
     */
    @Deprecated
    public void showRepo() {

        repository.forEach((k, v) -> System.out.println("Annotation: " + k + " Analyzer: " + v));

    }

    /**
     * clean our repo
     */
    @Deprecated
    public void cleanRepo() {

        this.repository.clear();

    }
}
