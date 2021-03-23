package org.springframework.data.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
annotation class Id