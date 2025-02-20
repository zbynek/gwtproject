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
package org.gwtproject.user.client.ui;

import org.gwtproject.i18n.client.NumberFormat;
import org.gwtproject.text.client.NumberFormatRenderer;

/**
 * Extends {@link ValueLabel} for convenience when dealing with numbers and {@link NumberFormat},
 * especially in {@link org.gwtproject.uibinder.client.UiBinder UiBinder} templates. (Note that this
 * class does not accept renderers. To do so use {@link ValueLabel} directly.)
 *
 * <h3>Use in UiBinder Templates</h3>
 *
 * In {@link org.gwtproject.uibinder.client.UiBinder UiBinder} templates, the {@link NumberFormat}
 * can be specified with one of these attributes:
 *
 * <dl>
 *   <dt>format
 *   <dd>a reference to a {@link NumberFormat} instance.
 *   <dt>predefinedFormat
 *   <dd>a predefined format (see below for the list of acceptable values).
 *   <dt>customFormat
 *   <dd>a number format pattern that can be passed to {@link NumberFormat#getFormat(String)}. See
 *       below for a way of specifying a currency code.
 * </dl>
 *
 * The valid values for the {@code predefinedFormat} attributes are:
 *
 * <dl>
 *   <dt>DECIMAL
 *   <dd>the standard decimal format for the current locale, as given by {@link
 *       NumberFormat#getDecimalFormat()}.
 *   <dt>CURRENCY
 *   <dd>the standard currency format for the current locale, as given by {@link
 *       NumberFormat#getCurrencyFormat()}. See below for a way of specifying a currency code.
 *   <dt>PERCENT
 *   <dd>the standard percent format for the current locale, as given by {@link
 *       NumberFormat#getPercentFormat()}.
 *   <dt>SCIENTIFIC
 *   <dd>the standard scientific format for the current locale, as given by {@link
 *       NumberFormat#getScientificFormat()}.
 * </dl>
 *
 * When using {@code predefinedFormat="CURRENCY"} or a {@code customFormat}, you can specify a
 * currency code using either of the following attributes:
 *
 * <dl>
 *   <dt>currencyData
 *   <dd>a reference to a {@link org.gwtproject.i18n.client.CurrencyData CurrencyData} instance.
 *   <dt>currencyCode
 *   <dd>an ISO4217 currency code.
 * </dl>
 *
 * @param <T> The exact type of number
 */
public class NumberLabel<T extends Number> extends ValueLabel<T> {

  public NumberLabel() {
    super(new NumberFormatRenderer());
  }

  public NumberLabel(NumberFormat format) {
    super(new NumberFormatRenderer(format));
  }
}
