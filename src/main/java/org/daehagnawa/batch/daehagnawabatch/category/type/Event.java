package org.daehagnawa.batch.daehagnawabatch.category.type;


import org.daehagnawa.batch.daehagnawabatch.category.Category;
import org.daehagnawa.batch.daehagnawabatch.domain.department.DepartmentProxy;

public class Event extends Category {

    public Event(
            String categoryName,
            int categorySeq
    ) {
        super(categoryName, categorySeq);
    }

    @Override
    public void setDepartmentProxyData(DepartmentProxy template, boolean toLinked) {
        if (!columnData.equals("-"))
            template.setEvent(columnData);
    }

    @Override
    public boolean isTarget() {
        return true;
    }

    @Override
    public String getColumnData() {
        return columnData;
    }
}
