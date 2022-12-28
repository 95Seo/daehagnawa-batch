package org.daehagnawa.batch.daehagnawabatch.domain;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "university_department_info")
@EqualsAndHashCode(of = {"universityName", "admissionType", "departmentName"})
public class DepartmentInfo {

    @Id
    @Comment("기본키")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT", nullable = false)
    private Long id;

    @Comment("대학 이름")
    @Column(name = "university_name", columnDefinition = "varchar(100)", nullable = false)
    protected String universityName;

    @Comment("입학 전형 구분")
    @Column(name = "admission_type", columnDefinition = "varchar(100)", nullable = false)
    protected String admissionType;

    @Comment("학과 이름")
    @Column(name = "department_name", columnDefinition = "varchar(500)", nullable = false)
    protected String departmentName;

    @Comment("모집 인원")
    @Column(name = "recruitment_count", columnDefinition = "TEXT", nullable = false)
    private String recruitmentCount;

    @Comment("지원 인원")
    @Column(name = "applicants_count", columnDefinition = "TEXT", nullable = false)
    private String applicantsCount;        // String으로 변경

    @Comment("경쟁룰")
    @Column(name = "competition_ratio", columnDefinition = "FLOAT", nullable = false)
    private float competitionRatio;

    @Comment("지역")
    @Column(name = "university_area", columnDefinition = "CHAR(2)", nullable = false)
    private String universityArea;

    @Comment("학위")
    @Column(name = "university_degree", columnDefinition = "CHAR(3)", nullable = false)
    private String universityDegree;

    @Comment("입시년도")
    @Column(name = "entrance_exam_year", columnDefinition = "INT", nullable = false)
    private int entranceExamYear;

    @Comment("원서 접수 사이트 URL")
    @Column(name = "reception_url", columnDefinition = "TEXT", nullable = false)
    private String receptionUrl;

    @Comment("크롤링 타입 구분 (uway, jinhak)")
    @Column(name = "type", columnDefinition = "VARCHAR(6)", nullable = false)
    private String type;

    @Builder
    public DepartmentInfo(
            Long id,
            String universityName,
            String admissionType,
            String departmentName,
            String recruitmentCount,
            String applicantsCount,
            float competitionRatio,
            String universityArea,
            String universityDegree,
            int entranceExamYear,
            String receptionUrl,
            String type
    ) {
        this.id = id;
        this.universityName = universityName;
        this.admissionType = admissionType;
        this.departmentName = departmentName;
        this.recruitmentCount = recruitmentCount;
        this.applicantsCount = applicantsCount;
        this.competitionRatio = competitionRatio;
        this.universityArea = universityArea;
        this.universityDegree = universityDegree;
        this.receptionUrl = receptionUrl;
        this.entranceExamYear = entranceExamYear;
        this.receptionUrl = receptionUrl;
        this.type = type;
    }

    public void update(
            String recruitmentCount,
            String applicantsCount,
            float competitionRatio
    ) {
        if (!this.recruitmentCount.equals(recruitmentCount)) {
            this.recruitmentCount = recruitmentCount;
        }
        if (!this.applicantsCount.equals(applicantsCount)) {
            this.applicantsCount = applicantsCount;
        }
        if (this.competitionRatio != competitionRatio) {
            this.competitionRatio = competitionRatio;
        }
    }
}
