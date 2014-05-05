package de.eightbitboy.roo.envers.finder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Trigger annotation for this add-on.
 
 * @since 1.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface RooEnversFinder {
    /**
     * All view-related artifacts for a specific controller are stored in a
     * sub-directory under WEB-INF/views/<em>path</em>. The path parameter
     * defines the name of this sub-directory or path. This path is also used to
     * define the restful resource in the URL to which the controller is mapped.
     * 
     * @return The view path.
     */
    String path();
}

