package org.daehagnawa.batch.daehagnawabatch.domain.department;

import lombok.Setter;

@Setter
public class DepartmentProxy {

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

    private DepartmentProxy() {}

    public DepartmentProxy(
            String universityName
    ) {
        this.universityName = universityName;
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

    public Department toEntity() {
        return Department.builder()
                .universityName(universityName)
                .admissionType(admissionType)
                .departmentName(departmentName)
                .recruitmentCount(recruitmentCount)
                .applicantsCount(applicantsCount)
                .competitionRatio(competitionRatio)
                .build();
    }
}
