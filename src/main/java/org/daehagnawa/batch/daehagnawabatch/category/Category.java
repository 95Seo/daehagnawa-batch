package org.daehagnawa.batch.daehagnawabatch.category;

import lombok.Getter;
import org.daehagnawa.batch.daehagnawabatch.domain.DepartmentInfoProxy;
import org.jsoup.nodes.Element;

@Getter
public abstract class Category {
    protected String categoryName;
    protected int categorySeq;
    protected int rowCount = 0;
    protected String columnData;

    protected Category(
            String categoryName,
            int categorySeq
    ) {
        this.categoryName = categoryName;
        this.categorySeq = categorySeq;
    }

    public void setColumn(Element column) {
        columnData = column.text();
        rowCount = toInteger(column.attr("rowspan"));
    }

    private static Integer toInteger(String str) {
        return Integer.valueOf(str);
    }

    public boolean rowCountIsZero() {
        return rowCount == 0;
    }

    public void downRowCount() {
        rowCount--;
    }

    public abstract void setDepartmentProxyData(DepartmentInfoProxy template, boolean toLinked);

    public abstract boolean isTarget();

    public abstract String getColumnData();
}
