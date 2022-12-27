package org.daehagnawa.batch.daehagnawabatch.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@DynamicInsert
@Entity(name = "university_department_info")
public class DepartmentInfo {

    @Id
    @Comment("기본키")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT", nullable = false)
    private Long id;

    @Comment("대학 이름")
    @Column(name = "university_name", columnDefinition = "varchar(100)", nullable = false)
    private String universityName;

    @Comment("입학 전형 구분")
    @Column(name = "admission_type", columnDefinition = "varchar(100)", nullable = false)
    private String admissionType;

    @Comment("학과 이름")
    @Column(name = "department_name", columnDefinition = "TEXT", nullable = false)
    private String departmentName;

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
    @Column(name = "area", columnDefinition = "VARCHAR(2)", nullable = false)
    private String area;

    @Comment("학위")
    @Column(name = "degree", columnDefinition = "VARCHAR(3)", nullable = false)
    private String degree;

    @Comment("입시년도")
    @Column(name = "entrance_exam_year", columnDefinition = "INT", nullable = false)
    private int entranceExamYear;

    @CreatedDate
    @Comment("데이터 생성일")
    @Column(name = "created_at", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Comment("데이터 수정일")
    @Column(name = "updated_at", columnDefinition = "DATETIME")
    private LocalDateTime updatedAt;

    protected DepartmentInfo() {}

    public DepartmentInfo(
            Long id,
            String universityName,
            String admissionType,
            String departmentName,
            String recruitmentCount,
            String applicantsCount,
            float competitionRatio,
            String area,
            String degree,
            int entranceExamYear,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.universityName = universityName;
        this.admissionType = admissionType;
        this.departmentName = departmentName;
        this.recruitmentCount = recruitmentCount;
        this.applicantsCount = applicantsCount;
        this.competitionRatio = competitionRatio;
        this.area = area;
        this.degree = degree;
        this.entranceExamYear = entranceExamYear;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
