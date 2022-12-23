package org.daehagnawa.batch.daehagnawabatch.category.type;


import org.daehagnawa.batch.daehagnawabatch.category.Category;
import org.daehagnawa.batch.daehagnawabatch.domain.department.DepartmentProxy;

public class ApplicantsCount extends Category {

    public ApplicantsCount(String categoryName, int categorySeq) {
        super(categoryName, categorySeq);
    }

    @Override
    public void setDepartmentProxyData(DepartmentProxy template, boolean toLinked) {
        if (toLinked)
            template.linkedApplicantsCount(columnData);
        else
            template.setApplicantsCount(columnData);
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
