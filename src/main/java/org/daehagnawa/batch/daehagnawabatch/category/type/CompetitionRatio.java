package org.daehagnawa.batch.daehagnawabatch.category.type;


import org.daehagnawa.batch.daehagnawabatch.category.Category;
import org.daehagnawa.batch.daehagnawabatch.domain.department.DepartmentProxy;

public class CompetitionRatio extends Category {

    public CompetitionRatio(String categoryName, int categorySeq) {
        super(categoryName, categorySeq);
    }

    // 좀 애매하네..
    // slideRatio를 columnData에 적용 시킨 상태로 필드로 가지고 싶다..
    @Override
    public void setDepartmentProxyData(DepartmentProxy template, boolean toLinked) {
        template.setCompetitionRatio(toFloat(slideRatio(validationNoneData(columnData))));
    }

    public CompetitionRatio(String categoryName, int categorySeq, String columnData, int rowCount) {
        super(categoryName, categorySeq);
        super.columnData = columnData;
        super.rowCount = rowCount;
    }

    @Override
    public boolean isTarget() {
        return true;
    }

    @Override
    public String getColumnData() {
        return slideRatio(columnData);
    }

    private String validationNoneData(String columnData) {
        if (columnData.equals("-"))
            return "-1";

        return columnData;
    }

    private String slideRatio(String columnData) {
        return columnData.replace(" : 1", "");
    }

    private Float toFloat(String columnData) {
        return Float.valueOf(columnData);
    }
}
