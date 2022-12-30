package org.daehagnawa.batch.daehagnawabatch.chunk;

import lombok.RequiredArgsConstructor;
import org.daehagnawa.batch.daehagnawabatch.domain.DepartmentInfo;
import org.springframework.batch.item.database.JpaItemWriter;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.*;

@Transactional
@RequiredArgsConstructor
public class JpaItemListWriter<T> extends JpaItemWriter<List<DepartmentInfo>> {

    private final JpaItemWriter<DepartmentInfo> jpaItemWriter;
    private final EntityManager em;

    @Override
    public void write(List<? extends List<DepartmentInfo>> items) {
        if (!items.isEmpty()) {
            String universityName = items.get(0).get(0).getUniversityName();

            TypedQuery<DepartmentInfo> query = em.createQuery("select i " +
                    "from university_department_info i " +
                    "where i.universityName = :universityName ", DepartmentInfo.class);

            List<DepartmentInfo> departmentInfos = query
                    .setParameter("universityName", universityName)
                    .getResultList();

            for (List<DepartmentInfo> item : items) {
                if (departmentInfos.isEmpty()) {
                    save(item);
                } else {
                    update(item, departmentInfos);
                }
            }
        } else {
            System.out.println("없다 이눔아");
        }
    }

    private void save(List<DepartmentInfo> item) {
        List<DepartmentInfo> itemList = new ArrayList<>();
        itemList.addAll(item);
        jpaItemWriter.write(itemList);
    }

    private void update(List<DepartmentInfo> item, List<DepartmentInfo> departmentInfos) {
        for (DepartmentInfo i : item) {
            for (DepartmentInfo departmentInfo : departmentInfos) {
                if (departmentInfo.equals(i)) {
                    departmentInfo.update(
                            i.getRecruitmentCount(),
                            i.getApplicantsCount(),
                            i.getCompetitionRatio()
                    );
                }
            }
        }
        em.flush();
    }
}
