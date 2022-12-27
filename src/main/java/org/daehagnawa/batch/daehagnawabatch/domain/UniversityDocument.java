package org.daehagnawa.batch.daehagnawabatch.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.persistence.*;
import java.io.IOException;

@Getter
@Setter
@Entity(name = "university_document")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UniversityDocument {

    @Id @GeneratedValue
    @Comment("기본키")
    @Column(name = "university_id", columnDefinition = "BIGINT", nullable = false)
    private Long universityId;

    @Comment("크롤링 주소")
    @Column(name = "university_url", columnDefinition = "TEXT", nullable = false)
    private String universityUrl;

    @Comment("대학 이름")
    @Column(name = "university_name", columnDefinition = "TEXT", nullable = false)
    private String universityName;

    @Comment("학위 구분")
    @Column(name = "university_degree", columnDefinition = "varchar(3)", nullable = false)
    private String universityDegree;

    @Comment("지역 구분")
    @Column(name = "university_area", columnDefinition = "varchar(2)", nullable = false)
    private String universityArea;

    public DepartmentInfoProxy toDepartmentProxy() {
        return new DepartmentInfoProxy(
                universityName,
                universityArea,
                universityDegree
        );
    }

    public Document getDocument() throws IOException {
        return Jsoup.connect(universityUrl).get();
    }
}