package org.daehagnawa.batch.daehagnawabatch.category.type;

import lombok.extern.slf4j.Slf4j;
import org.daehagnawa.batch.daehagnawabatch.category.Category;
import org.daehagnawa.batch.daehagnawabatch.domain.department.DepartmentProxy;

@Slf4j
public class ExceptCategory extends Category {

    public ExceptCategory(String categoryName, int categorySeq) {
        super(categoryName, categorySeq);
    }

    @Override
    public void setDepartmentProxyData(DepartmentProxy template, boolean toLinked) {
        // 흠...
        log.info("쓰레기 값");
    }

    @Override
    public boolean isTarget() {
        return false;
    }

    @Override
    public String getColumnData() {
        return columnData;
    }
}
