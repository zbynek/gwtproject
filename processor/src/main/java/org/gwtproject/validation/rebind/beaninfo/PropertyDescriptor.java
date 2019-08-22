package org.gwtproject.validation.rebind.beaninfo;

/**
 * @author Dmitrii Tikhomirov
 * Created by treblereel 8/20/19
 */

public interface PropertyDescriptor extends ElementDescriptor {
    /**
     * Is the property marked by the <code>@Valid</code> annotation.
     * @return <code>true</code> if the annotation is present, <code>false</code> otherwise.
     */
    boolean isCascaded();

    /**
     * Name of the property acording to the Java Bean specification.
     * @return property name.
     */
    String getPropertyName();
}

