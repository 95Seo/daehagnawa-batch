package org.daehagnawa.batch.daehagnawabatch.excel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.daehagnawa.batch.daehagnawabatch.domain.department.DepartmentProxy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

@Getter
@Setter
public class ExcelData {

    private String universityName;

    private String degree;

    private String area;

    private String universityURL;

    private ExcelData(){}

    @Builder
    public ExcelData(
            String universityName,
            String universityURL,
            String degree,
            String area
    ) {
        this.universityName = universityName;
        this.universityURL = universityURL;
        this.degree = degree;
        this.area = area;
    }

    public DepartmentProxy toDepartmentProxy() {
        return new DepartmentProxy(universityName);
    }

    public Document getDocument() throws IOException {
        return Jsoup.connect(universityURL).get();
    }
}
