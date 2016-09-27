package org.agoncal.application.conference.commons.registry;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Qualifier
@Retention(RUNTIME)
@Target({FIELD, METHOD, TYPE, PARAMETER})
public @interface TalkMicroService {
}
