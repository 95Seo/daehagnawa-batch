package org.daehagnawa.batch.daehagnawabatch.domain;

import lombok.Setter;

@Setter
public class DepartmentInfoProxy {

    // 대학이름
    private String universityName;

    // 전형 구분
    private String admissionType;

    // 학과이름
    private String departmentName;

    // 모집인원
    private String recruitmentCount;

    // 지원인원
    private String applicantsCount;

    // 지역
    private String area;

    // 학위
    private String degree;

    // 입시 년도
    private int entranceExamYear;

    // 경쟁률
    private float competitionRatio;

    private DepartmentInfoProxy() {}

    public DepartmentInfoProxy(
            String universityName,
            String area,
            String degree
    ) {
        this.universityName = universityName;
        this.area = area;
        this.degree = degree;
    }

    public void setSubDept(String subDept) {
        departmentName += " [" + subDept + "]";
    }

    public void linkedDeptName(String str) {
        departmentName += " | " + str;
    }

    public void linkedRecruitmentCount(String str) {
        recruitmentCount += " | " + str;
    }

    public void linkedApplicantsCount(String str) {
        applicantsCount += " | " + str;
    }

    public void setEvent(String event) {
        departmentName += " " + event;
    }

    public void setGenter(String genter) {
        departmentName += " " + genter;
    }

    public DepartmentInfo toEntity() {
        return DepartmentInfo.builder()
                .universityName(universityName)
                .admissionType(admissionType)
                .departmentName(departmentName)
                .recruitmentCount(recruitmentCount)
                .applicantsCount(applicantsCount)
                .competitionRatio(competitionRatio)
                .area(area)
                .degree(degree)
                .entranceExamYear(entranceExamYear)
                .build();
    }
}
