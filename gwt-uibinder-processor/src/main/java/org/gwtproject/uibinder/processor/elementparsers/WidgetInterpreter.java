package org.gwtproject.uibinder.processor.elementparsers;


import org.gwtproject.uibinder.processor.FieldManager;
import org.gwtproject.uibinder.processor.FieldWriter;
import org.gwtproject.uibinder.processor.UiBinderClasses;
import org.gwtproject.uibinder.processor.UiBinderWriter;
import org.gwtproject.uibinder.processor.XMLElement;
import org.gwtproject.uibinder.processor.ext.UnableToCompleteException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Used by {@link HTMLPanelParser} to interpret elements that call for widget instances. Declares
 * the appropriate widget, and replaces them in the markup with a &lt;span&gt;.
 */
class WidgetInterpreter implements XMLElement.Interpreter<String> {

  private static final Map<String, String> LEGAL_CHILD_ELEMENTS;
  private static final String DEFAULT_CHILD_ELEMENT = "span";

  static {
    // Other cases?
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("table", "tbody");
    map.put("thead", "tr");
    map.put("tbody", "tr");
    map.put("tfoot", "tr");
    map.put("ul", "li");
    map.put("ol", "li");
    map.put("dl", "dt");
    LEGAL_CHILD_ELEMENTS = Collections.unmodifiableMap(map);
  }

  private static String getLegalPlaceholderTag(XMLElement elem) {
    XMLElement parent = elem.getParent();
    String tag = null;
    if (parent != null) {
      tag = LEGAL_CHILD_ELEMENTS.get(parent.getLocalName());
    }
    if (tag == null) {
      tag = DEFAULT_CHILD_ELEMENT;
    }
    return tag;
  }

  private final String fieldName;

  private final UiBinderWriter uiWriter;

  public WidgetInterpreter(String fieldName, UiBinderWriter writer) {
    this.fieldName = fieldName;
    this.uiWriter = writer;
  }

  public String interpretElement(XMLElement elem)
      throws UnableToCompleteException {
    if (!uiWriter.isWidgetElement(elem)) {
      return null;
    }

    FieldManager fieldManager = uiWriter.getFieldManager();
    FieldWriter fieldWriter = fieldManager.require(fieldName);

    // Allocate a local variable to hold the dom id for this widget. Note
    // that idHolder is a local variable reference, not a string id. We
    // have to generate the ids at runtime, not compile time, or else
    // we'll reuse ids for any template rendered more than once.
    String idHolder = uiWriter.declareDomIdHolder(null);
    uiWriter.ensureCurrentFieldAttached();

    FieldWriter childFieldWriter = uiWriter.parseElementToField(elem);
    String elementPointer = idHolder + "Element";
    uiWriter.addInitStatement(
        "Element %s = com.google.gwt.dom.client.Document.get().getElementById(%s);",
        elementPointer, idHolder);

    if (uiWriter.useLazyWidgetBuilders()) {

      // Register a DOM id field.
      String lazyDomElementPath = UiBinderClasses.LAZYDOMELEMENT;
      FieldWriter elementWriter = fieldManager.registerField(lazyDomElementPath, elementPointer);
      elementWriter.setInitializer(String.format("new %s<Element>(%s)",
          lazyDomElementPath, fieldManager.convertFieldToGetter(idHolder)));

      // Add attach/detach sections for this element.
      fieldWriter.addAttachStatement("%s.get();",
          fieldManager.convertFieldToGetter(elementPointer));
      fieldWriter.addDetachStatement(
          "%s.addAndReplaceElement(%s, %s.get());",
          fieldName,
          fieldManager.convertFieldToGetter(childFieldWriter.getName()),
          fieldManager.convertFieldToGetter(elementPointer));
    } else {

      // Delay replacing the placeholders with the widgets until after
      // detachment so as not to end up attaching the widget to the DOM
      // unnecessarily
      uiWriter.addDetachStatement(
          "%1$s.addAndReplaceElement(%2$s, %3$s);",
          fieldName,
          childFieldWriter.getName(),
          elementPointer);
    }

    // Create an element to hold the widget.
    String tag = getLegalPlaceholderTag(elem);
    idHolder = fieldManager.convertFieldToGetter(idHolder);
    if (uiWriter.useSafeHtmlTemplates()) {
      idHolder = uiWriter.tokenForStringExpression(elem, idHolder);
    } else {
      idHolder = "\" + " + idHolder + " + \"";
    }
    return "<" + tag + " id='" + idHolder + "'></" + tag + ">";
  }
}
