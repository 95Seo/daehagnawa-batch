package org.daehagnawa.batch.daehagnawabatch.excel;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.daehagnawa.batch.daehagnawabatch.domain.DepartmentInfoProxy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

@Data
@Builder
public class ExcelData {

    private String universityName;

    private String degree;

    private String area;

    private String universityURL;

    public ExcelData(){}

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

    public DepartmentInfoProxy toDepartmentProxy() {
        return new DepartmentInfoProxy(universityName);
    }

    public Document getDocument() throws IOException {
        return Jsoup.connect(universityURL).get();
    }
}
