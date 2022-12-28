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

    // 경쟁률
    private float competitionRatio;

    // 지역
    private String universityArea;

    // 학위
    private String universityDegree;

    // 입시 년도
    private int entranceExamYear;

    // 원서 접수 사이트 URL
    private String receptionUrl;

    // 크롤링 타입 (uway, jinhak)
    private String type;
    
    private DepartmentInfoProxy() {}

    public DepartmentInfoProxy(
            String universityName,
            String universityArea,
            String universityDegree,
            String receptionUrl,
            String type
    ) {
        this.universityName = universityName;
        this.universityArea = universityArea;
        this.universityDegree = universityDegree;
        this.receptionUrl = receptionUrl;
        this.type = type;
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
                .universityArea(universityArea)
                .universityDegree(universityDegree)
                .entranceExamYear(entranceExamYear)
                .receptionUrl(receptionUrl)
                .type(type)
                .build();
    }
}
