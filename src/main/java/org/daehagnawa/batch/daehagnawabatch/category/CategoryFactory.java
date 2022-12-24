package org.daehagnawa.batch.daehagnawabatch.category;

import org.daehagnawa.batch.daehagnawabatch.category.type.*;
import org.daehagnawa.batch.daehagnawabatch.support.CategoryReg;

public class CategoryFactory {

    private CategoryFactory() {}

    public static Category create(String categoryName, int categorySeq) {
        // 신안산대학교 - 모집단위 -> 모집학과
        if (
                categoryName.matches(CategoryReg.DEPARTMENT_REG1.getReg()) ||
                        categoryName.matches(CategoryReg.DEPARTMENT_REG2.getReg())
        )
            return createDepartment(categorySeq);

        if (categoryName.matches(CategoryReg.SUB_DEPARTMENT_REG.getReg()))
            return createSubDepartment(categorySeq);

        if (categoryName.matches(CategoryReg.EVENT_REG.getReg()))
            return createEvent(categorySeq);

        if (categoryName.matches(CategoryReg.GENDER_REG.getReg()))
            return createGender(categorySeq);

        if (categoryName.matches(CategoryReg.RECURITMENT_COUNT_REG.getReg()))
            return createRecruitmentCount(categorySeq);

        if (
                categoryName.matches(CategoryReg.APPLICANTS_COUNT_REG1.getReg()) ||
                        categoryName.matches(CategoryReg.APPLICANTS_COUNT_REG2.getReg())
        )
            return createApplicantsCount(categorySeq);

        if (
                categoryName.matches(CategoryReg.COMPETITION_RATIO_REG1.getReg()) ||
                        categoryName.matches(CategoryReg.COMPETITION_RATIO_REG2.getReg()) ||
                        categoryName.matches(CategoryReg.COMPETITION_RATIO_REG3.getReg())
        )
            return createCompetitionRatio(categorySeq);

        return new ExceptCategory(categoryName, categorySeq);
    }

    private static Category createDepartment(int categorySeq) {
        return new Department("모집단위", categorySeq);
    }

    private static Category createSubDepartment(int categorySeq) {
        return new SubDepartment("서브모집단위", categorySeq);
    }

    private static Category createEvent(int categorySeq) {
        return new Event("종목", categorySeq);
    }

    private static Category createGender(int categorySeq) {
        return new Gender("성별", categorySeq);
    }

    private static Category createRecruitmentCount(int categorySeq) {
        return new RecruitmentCount("모집인원", categorySeq);
    }

    private static Category createApplicantsCount(int categorySeq) {
        return new ApplicantsCount("지원인원", categorySeq);
    }

    private static Category createCompetitionRatio(int categorySeq) {
        return new CompetitionRatio("경쟁률", categorySeq);
    }

    // 경쟁률 전용
    public static Category createRatio(int rowCount) {
        return new CompetitionRatio("경쟁률", 0, "-1", rowCount);
    }
}
