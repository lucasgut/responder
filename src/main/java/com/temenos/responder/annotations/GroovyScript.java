package com.temenos.responder.annotations;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Douglas Groves on 09/12/2016.
 */
@BindingAnnotation
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface GroovyScript {}
