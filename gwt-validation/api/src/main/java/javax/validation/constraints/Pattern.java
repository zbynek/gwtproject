/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The annotated String must match the following regular expression. The regular expression follows
 * the Java regular expression conventions see {@link java.util.regex.Pattern}.
 *
 * <p>Accepts String. <code>null</code> elements are considered valid.
 *
 * @author Emmanuel Bernard
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {})
public @interface Pattern {
  /** @return The regular expression to match. */
  String regexp();

  /** @return Array of <code>Flag</code>s considered when resolving the regular expression. */
  Flag[] flags() default {};

  /** @return The error message template. */
  String message() default "{javax.validation.constraints.Pattern.message}";

  /** @return The groups the constraint belongs to. */
  Class<?>[] groups() default {};

  /** @return The payload associated to the constraint */
  Class<? extends Payload>[] payload() default {};

  int UNIX_LINES = 1;
  int CASE_INSENSITIVE = 2;
  int COMMENTS = 4;
  int MULTILINE = 8;
  int LITERAL = 16;
  int DOTALL = 32;
  int UNICODE_CASE = 64;
  int CANON_EQ = 128;
  int UNICODE_CHARACTER_CLASS = 256;

  /** Possible Regexp flags */
  enum Flag {

    /**
     * Enables Unix lines mode
     *
     * @see java.util.regex.Pattern#UNIX_LINES
     */
    UNIX_LINES(Pattern.UNIX_LINES),

    /**
     * Enables case-insensitive matching
     *
     * @see java.util.regex.Pattern#CASE_INSENSITIVE
     */
    CASE_INSENSITIVE(Pattern.CASE_INSENSITIVE),

    /**
     * Permits whitespace and comments in pattern
     *
     * @see java.util.regex.Pattern#COMMENTS
     */
    COMMENTS(Pattern.COMMENTS),

    /**
     * Enables multiline mode
     *
     * @see java.util.regex.Pattern#MULTILINE
     */
    MULTILINE(Pattern.MULTILINE),

    /**
     * Enables dotall mode
     *
     * @see java.util.regex.Pattern#DOTALL
     */
    DOTALL(Pattern.DOTALL),

    /**
     * Enables Unicode-aware case folding
     *
     * @see java.util.regex.Pattern#UNICODE_CASE
     */
    UNICODE_CASE(Pattern.UNICODE_CASE),

    /**
     * Enables canonical equivalence
     *
     * @see java.util.regex.Pattern#CANON_EQ
     */
    CANON_EQ(Pattern.CANON_EQ);

    // JDK flag value
    private final int value;

    private Flag(int value) {
      this.value = value;
    }

    /** @return flag value as defined in {@link java.util.regex.Pattern} */
    public int getValue() {
      return value;
    }
  }

  /**
   * Defines several <code>@Pattern</code> annotations on the same element
   *
   * @see Pattern
   * @author Emmanuel Bernard
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    Pattern[] value();
  }
}
