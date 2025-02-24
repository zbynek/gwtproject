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
package org.gwtproject.user.cellview.client;

import com.google.j2cl.junit.apt.J2clTestInput;
import java.util.Locale;
import org.gwtproject.cell.client.TextCell;
import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.client.TableCellElement;
import org.gwtproject.dom.client.TableElement;
import org.gwtproject.dom.client.TableRowElement;
import org.gwtproject.dom.client.TableSectionElement;
import org.gwtproject.dom.style.shared.Unit;
import org.gwtproject.resources.client.ImageResource;
import org.gwtproject.user.cellview.client.AbstractHasData.RedrawEvent.Handler;
import org.gwtproject.user.cellview.client.CellTable.Resources;
import org.gwtproject.user.cellview.client.CellTable.Style;
import org.gwtproject.user.client.DOM;
import org.gwtproject.user.client.ui.RootPanel;
import org.gwtproject.user.client.ui.Widget;

/** Tests for {@link CellTable}. */
@J2clTestInput(CellTableTest.class)
public class CellTableTest extends AbstractCellTableTestBase<CellTable<String>> {

  /** Test headers that span multiple columns. */
  public void testMultiColumnHeader() {
    Resources res = new CellTable_ResourcesImpl();
    CellTable<String> table = new CellTable<>(10, res);
    TextHeader header = new TextHeader("Hello");

    // Get the style information.
    Style style = res.cellTableStyle();
    String styleHeader = style.cellTableHeader();
    String styleFirstColumn = style.cellTableFirstColumnHeader();
    String styleLastColumn = style.cellTableLastColumnHeader();

    // No header.
    table.redraw();
    table.getPresenter().flush();
    assertEquals(0, getHeaderCount(table));

    // Single column.
    table.addColumn(
        new TextColumn<String>() {
          @Override
          public String getValue(String object) {
            return null;
          }
        },
        header);
    table.getPresenter().flush();
    assertEquals(1, getHeaderCount(table));
    assertEquals(1, getHeaderElement(table, 0).getColSpan());
    assertTrue(getHeaderElement(table, 0).getClassName().contains(styleHeader));
    assertTrue(getHeaderElement(table, 0).getClassName().contains(styleFirstColumn));
    assertTrue(getHeaderElement(table, 0).getClassName().contains(styleLastColumn));

    // Header spans both columns.
    table.addColumn(
        new TextColumn<String>() {
          @Override
          public String getValue(String object) {
            return null;
          }
        },
        header);
    table.getPresenter().flush();
    assertEquals(1, getHeaderCount(table));
    assertEquals(2, getHeaderElement(table, 0).getColSpan());
    assertTrue(getHeaderElement(table, 0).getClassName().contains(styleHeader));
    assertTrue(getHeaderElement(table, 0).getClassName().contains(styleFirstColumn));
    assertTrue(getHeaderElement(table, 0).getClassName().contains(styleLastColumn));

    // Header spans all three columns.
    table.addColumn(
        new TextColumn<String>() {
          @Override
          public String getValue(String object) {
            return null;
          }
        },
        header);
    table.getPresenter().flush();
    assertEquals(1, getHeaderCount(table));
    assertEquals(3, getHeaderElement(table, 0).getColSpan());
    assertTrue(getHeaderElement(table, 0).getClassName().contains(styleHeader));
    assertTrue(getHeaderElement(table, 0).getClassName().contains(styleFirstColumn));
    assertTrue(getHeaderElement(table, 0).getClassName().contains(styleLastColumn));

    // New header at fourth column.
    table.addColumn(
        new TextColumn<String>() {
          @Override
          public String getValue(String object) {
            return null;
          }
        },
        "New Header");
    table.getPresenter().flush();
    assertEquals(2, getHeaderCount(table));
    assertEquals(3, getHeaderElement(table, 0).getColSpan());
    assertTrue(getHeaderElement(table, 0).getClassName().contains(styleHeader));
    assertTrue(getHeaderElement(table, 0).getClassName().contains(styleFirstColumn));
    assertEquals(1, getHeaderElement(table, 1).getColSpan());
    assertTrue(getHeaderElement(table, 1).getClassName().contains(styleHeader));
    assertTrue(getHeaderElement(table, 1).getClassName().contains(styleLastColumn));

    // Two separate spans of same header: HHHXHH.
    table.addColumn(
        new TextColumn<String>() {
          @Override
          public String getValue(String object) {
            return null;
          }
        },
        header);
    table.addColumn(
        new TextColumn<String>() {
          @Override
          public String getValue(String object) {
            return null;
          }
        },
        header);
    table.getPresenter().flush();
    assertEquals(3, getHeaderCount(table));
    assertEquals(3, getHeaderElement(table, 0).getColSpan());
    assertTrue(getHeaderElement(table, 0).getClassName().contains(styleHeader));
    assertTrue(getHeaderElement(table, 0).getClassName().contains(styleFirstColumn));
    assertEquals(1, getHeaderElement(table, 1).getColSpan());
    assertTrue(getHeaderElement(table, 1).getClassName().contains(styleHeader));
    assertEquals(2, getHeaderElement(table, 2).getColSpan());
    assertTrue(getHeaderElement(table, 2).getClassName().contains(styleHeader));
    assertTrue(getHeaderElement(table, 2).getClassName().contains(styleLastColumn));
  }

  public void testNullLoadingImage() {
    // Create a Resources instance that does not include a loading image.
    CellTable.Resources resources =
        new CellTable.Resources() {
          private final CellTable.Resources defaultRes = new CellTable_ResourcesImpl();

          @Override
          public ImageResource cellTableFooterBackground() {
            return defaultRes.cellTableFooterBackground();
          }

          @Override
          public ImageResource cellTableHeaderBackground() {
            return defaultRes.cellTableHeaderBackground();
          }

          @Override
          public ImageResource cellTableLoading() {
            return null;
          }

          @Override
          public ImageResource cellTableSelectedBackground() {
            return defaultRes.cellTableSelectedBackground();
          }

          @Override
          public ImageResource cellTableSortAscending() {
            return defaultRes.cellTableSortAscending();
          }

          @Override
          public ImageResource cellTableSortDescending() {
            return defaultRes.cellTableSortDescending();
          }

          @Override
          public Style cellTableStyle() {
            return defaultRes.cellTableStyle();
          }
        };

    CellTable<String> table = new CellTable<String>(25, resources);
    assertNull(table.getLoadingIndicator());
  }

  /** Using a null sort icon should not cause any errors if none of the columns are sortable. */
  public void testNullSortIcons() {
    // Create a Resources instance that does not include sort images.
    CellTable.Resources resources =
        new CellTable.Resources() {
          private final CellTable.Resources defaultRes = new CellTable_ResourcesImpl();

          @Override
          public ImageResource cellTableFooterBackground() {
            return defaultRes.cellTableFooterBackground();
          }

          @Override
          public ImageResource cellTableHeaderBackground() {
            return defaultRes.cellTableHeaderBackground();
          }

          @Override
          public ImageResource cellTableLoading() {
            return defaultRes.cellTableLoading();
          }

          @Override
          public ImageResource cellTableSelectedBackground() {
            return defaultRes.cellTableSelectedBackground();
          }

          @Override
          public ImageResource cellTableSortAscending() {
            return null;
          }

          @Override
          public ImageResource cellTableSortDescending() {
            return null;
          }

          @Override
          public Style cellTableStyle() {
            return defaultRes.cellTableStyle();
          }
        };

    CellTable<String> table = new CellTable<String>(10, resources);
    populateData(table);
    table.getPresenter().flush();
  }

  /**
   * CellTable should not throw any errors if all of the icons are null.
   *
   * <p>Sort icons are only used if a column is sorted. Background icons are not used in the default
   * styles, and are optional. The sorting icon is specially handled.
   */
  public void testNullIcons() {
    // Create a Resources instance that does not include sort images.
    CellTable.Resources resources =
        new CellTable.Resources() {
          private final CellTable.Resources defaultRes = new CellTable_ResourcesImpl();

          @Override
          public ImageResource cellTableFooterBackground() {
            return null;
          }

          @Override
          public ImageResource cellTableHeaderBackground() {
            return null;
          }

          @Override
          public ImageResource cellTableLoading() {
            return null;
          }

          @Override
          public ImageResource cellTableSelectedBackground() {
            return null;
          }

          @Override
          public ImageResource cellTableSortAscending() {
            return null;
          }

          @Override
          public ImageResource cellTableSortDescending() {
            return null;
          }

          @Override
          public Style cellTableStyle() {
            return defaultRes.cellTableStyle();
          }
        };

    CellTable<String> table = new CellTable<String>(10, resources);
    populateData(table);
    table.getPresenter().flush();
  }

  /** Test that removing a column sets its width to zero and the display to none. */
  public void testRemoveColumnWithWidth_withRemoveColumnsOnHideEnabled() {
    CellTable<String> table = createAbstractHasData(new TextCell());
    table.setRemoveColumnsOnHide(true);
    Column<String, ?> column1 = table.getColumn(1);
    table.setColumnWidth(column1, "100px");
    Element col0 = table.colgroup.getFirstChildElement();
    Element col1 = col0.getNextSiblingElement();
    assertEquals("100px", col1.getStyle().getWidth().toLowerCase(Locale.ROOT));
    assertEquals(2, table.colgroup.getChildCount());

    // Remove column 1.
    table.removeColumn(column1);
    table.getPresenter().flush();

    assertNull(col0.getNextSiblingElement());
    assertEquals(1, table.colgroup.getChildCount());
  }

  public void testRemoveColumnWithWidth_withRemoveColumnsOnHideDisabled() {
    CellTable<String> table = createAbstractHasData(new TextCell());
    table.setRemoveColumnsOnHide(false);
    Column<String, ?> column1 = table.getColumn(1);
    table.setColumnWidth(column1, "100px");
    Element col0 = table.colgroup.getFirstChildElement();
    Element col1 = col0.getNextSiblingElement();
    assertEquals("100px", col1.getStyle().getWidth().toLowerCase(Locale.ROOT));
    assertEquals(2, table.colgroup.getChildCount());

    // Remove column 1.
    table.removeColumn(column1);
    table.getPresenter().flush();

    assertEquals("0px", col1.getStyle().getWidth());
    assertEquals("none", col1.getStyle().getDisplay().toLowerCase(Locale.ROOT));
  }

  public void testEmptyTableWidgetAttachDetach() {
    class AttachDetachAwareWidget extends Widget {
      int attachCount = 0;
      int detachCount = 0;

      public AttachDetachAwareWidget() {
        setElement(DOM.createAnchor());
      }

      @Override
      protected void onAttach() {
        attachCount++;
      }

      @Override
      protected void onDetach() {
        detachCount++;
      }
    }

    AttachDetachAwareWidget widget = new AttachDetachAwareWidget();
    CellTable<String> table = createAbstractHasData(new TextCell());
    table.setEmptyTableWidget(widget);
    assertEquals(0, widget.attachCount);
    RootPanel.get().add(table);
    RootPanel.get().remove(table);
    RootPanel.get().add(table);
    assertEquals(2, widget.attachCount);
    assertEquals(1, widget.detachCount);
  }

  @Override
  public void testSetColumnWidth() {
    CellTable<String> table = createAbstractHasData(new TextCell());
    Column<String, ?> column0 = table.getColumn(0);
    Column<String, ?> column1 = table.getColumn(1);

    // Set the width.
    table.setColumnWidth(column1, "100px");
    Element col0 = table.colgroup.getFirstChildElement();
    Element col1 = col0.getNextSiblingElement();
    assertEquals("", col0.getStyle().getWidth());
    assertEquals("100px", col1.getStyle().getWidth().toLowerCase(Locale.ROOT));

    // Clear the width.
    table.clearColumnWidth(column1);
    assertEquals("", col0.getStyle().getWidth());
    assertEquals("", col1.getStyle().getWidth());

    // Set the width again.
    table.setColumnWidth(column0, 30.1, Unit.PCT);
    assertEquals("30.1%", col0.getStyle().getWidth().toLowerCase(Locale.ROOT));
    assertEquals("", col1.getStyle().getWidth());
  }

  public void testSetTableLayoutFixed() {
    CellTable<String> table = createAbstractHasData(new TextCell());
    assertNotSame("fixed", table.getElement().getStyle().getTableLayout());

    table.setTableLayoutFixed(true);
    assertEquals("fixed", table.getElement().getStyle().getTableLayout());

    table.setTableLayoutFixed(false);
    assertNotSame("fixed", table.getElement().getStyle().getTableLayout());
  }

  /** Test if the tableWidget style is applied. */
  public void testTableWidgetStyle() {
    Resources res = new CellTable_ResourcesImpl();
    CellTable<String> table = new CellTable<String>(10, res);

    String tableWidgetStyle = res.cellTableStyle().cellTableWidget();

    TableElement tableElem = table.getElement().cast();
    assertTrue(tableElem.getClassName().contains(tableWidgetStyle));
  }

  public void testTableRenderedEvent() {
    final Counter counter = new Counter();
    CellTable<String> table = createAbstractHasData(new TextCell());
    table.addRedrawHandler(
        new Handler() {
          @Override
          public void onRedraw() {
            counter.increment();
          }
        });

    // Cause table to redraw by removing a column.
    Column<String, ?> column1 = table.getColumn(1);
    table.removeColumn(column1);
    table.getPresenter().flush();
    assertEquals(1, counter.getCount());

    // Cause the table to redraw by resetting the {@link CellTableBuilder}.
    table.setTableBuilder(new DefaultCellTableBuilder<>(table));
    table.getPresenter().flush();
    assertEquals(2, counter.getCount());
  }

  @Override
  protected CellTable<String> createAbstractHasData() {
    return new CellTable<>();
  }

  @Override
  protected TableCellElement getBodyElement(CellTable<String> table, int row, int column) {
    TableElement tableElem = table.getElement().cast();
    TableSectionElement tbody = tableElem.getTBodies().getItem(0);
    TableRowElement tr = tbody.getRows().getItem(row);
    return tr.getCells().getItem(column);
  }

  @Override
  protected int getHeaderCount(CellTable<String> table) {
    TableElement tableElem = table.getElement().cast();
    TableSectionElement thead = tableElem.getTHead();
    if (thead.getRows().getLength() == 0) {
      return 0;
    }
    TableRowElement tr = thead.getRows().getItem(0);
    return tr.getCells().getLength();
  }

  @Override
  protected TableCellElement getHeaderElement(CellTable<String> table, int column) {
    TableElement tableElem = table.getElement().cast();
    TableSectionElement thead = tableElem.getTHead();
    TableRowElement tr = thead.getRows().getItem(0);
    return tr.getCells().getItem(column);
  }
}
