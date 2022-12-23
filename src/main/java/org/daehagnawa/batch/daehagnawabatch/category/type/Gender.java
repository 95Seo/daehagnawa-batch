package org.daehagnawa.batch.daehagnawabatch.category.type;

import org.daehagnawa.batch.daehagnawabatch.category.Category;
import org.daehagnawa.batch.daehagnawabatch.domain.department.DepartmentProxy;

public class Gender extends Category {

    public Gender(
            String categoryName,
            int categorySeq
    ) {
        super(categoryName, categorySeq);
    }

    @Override
    public boolean isTarget() {
        return true;
    }

    @Override
    public void setDepartmentProxyData(DepartmentProxy template, boolean toLinked) {
        template.setGenter(columnData);
    }

    @Override
    public String getColumnData() {
        return columnData;
    }
}
