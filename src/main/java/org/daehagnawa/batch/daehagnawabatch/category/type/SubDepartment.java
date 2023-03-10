package org.daehagnawa.batch.daehagnawabatch.category.type;

import org.daehagnawa.batch.daehagnawabatch.category.Category;
import org.daehagnawa.batch.daehagnawabatch.domain.DepartmentInfoProxy;

public class SubDepartment extends Category {

    public SubDepartment(String categoryName, int categorySeq) {
        super(categoryName, categorySeq);
    }

    @Override
    public void setDepartmentProxyData(DepartmentInfoProxy template, boolean toLinked) {
        template.setSubDept(columnData);
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
