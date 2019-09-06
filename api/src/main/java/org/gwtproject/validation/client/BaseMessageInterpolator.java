/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwtproject.validation.client;

import java.util.Map;

import javax.validation.MessageInterpolator;

import org.gwtproject.i18n.shared.GwtLocale;
import org.gwtproject.regexp.shared.MatchResult;
import org.gwtproject.regexp.shared.RegExp;

/**
 * Base GWT {@link MessageInterpolator}.
 */
abstract class BaseMessageInterpolator implements MessageInterpolator {

    /**
     * Regular expression used to do message interpolation.
     */
    private static final RegExp MESSAGE_PARAMETER_PATTERN = RegExp.compile(
            "(\\{[^\\}]+?\\})", "g");
    /**
     * Replaces keys using the Default Validation Provider properties.
     */
    private final Function<String, String> providerReplacer =
            createReplacer(new ProviderValidationMessageResolverImpl());
    /**
     * Replaces keys using the Validation User custom properties.
     */
    private final Function<String, String> userReplacer;

    protected BaseMessageInterpolator(
            UserValidationMessagesResolver userValidationMessagesResolver) {
        userReplacer = createReplacer(userValidationMessagesResolver);
    }

    // Visible for testing
    static Function<String, String> createAnnotationReplacer(
            final Map<String, Object> map) {
        return from -> {
            Object object = map.get(from);
            return object == null ? null : object.toString();
        };
    }

    private static Function<String, String> createReplacer(
            final ValidationMessageResolver messageResolver) {
        return from -> {
            Object object = messageResolver.get(from);
            return object == null ? null : object.toString();
        };
    }

    @Override
    public final String interpolate(String messageTemplate, Context context) {
        return gwtInterpolate(messageTemplate, context, null);
    }

    @Override
    public String interpolate(String messageTemplate, Context context, GwtLocale locale) {
        return messageTemplate;
    }

    protected final String gwtInterpolate(String message, Context context,
                                          GwtLocale locale) {
        // see Section 4.3.1.1
        String resolvedMessage = message;
        String step1message;

        // TODO(nchalko) Add a safety to make sure this does not loop forever.
        do {
            do {
                step1message = resolvedMessage;

                // Step 1 Replace message parameters using custom user messages
                // repeat
                resolvedMessage = replaceParameters(resolvedMessage, userReplacer);
            } while (!step1message.equals(resolvedMessage));

            // Step2 Replace message parameter using the default provider messages.
            resolvedMessage = replaceParameters(resolvedMessage, providerReplacer);

            // Step 3 repeat from step 1 if step 2 made changes.
        } while (!step1message.equals(resolvedMessage));

        // step 4 resolve annotation attributes
        resolvedMessage = replaceParameters(
                resolvedMessage,
                createAnnotationReplacer(context.getConstraintDescriptor().getAttributes()));

        // Remove escapes (4.3.1)
        resolvedMessage = resolvedMessage.replace("\\{", "{");
        resolvedMessage = resolvedMessage.replace("\\}", "}");
        resolvedMessage = resolvedMessage.replace("\\\\", "\\");
        return resolvedMessage;
    }

    protected final String replaceParameters(String message,
                                             Function<String, String> replacer) {
        StringBuilder sb = new StringBuilder();
        int index = 0;

        MatchResult matcher;
        while ((matcher = MESSAGE_PARAMETER_PATTERN.exec(message)) != null) {
            String matched = matcher.getGroup(0);
            sb.append(message.substring(index, matcher.getIndex()));
            String value = replacer.apply(removeCurlyBrace(matched));
            sb.append(value == null ? matched : value);
            index = MESSAGE_PARAMETER_PATTERN.getLastIndex();
        }
        if (index < message.length()) {
            sb.append(message.substring(index));
        }
        return sb.toString();
    }

    private String removeCurlyBrace(String parameter) {
        return parameter.substring(1, parameter.length() - 1);
    }

    // local version because guava is not included.
    private interface Function<F, T> {

        T apply(F from);
    }
}
