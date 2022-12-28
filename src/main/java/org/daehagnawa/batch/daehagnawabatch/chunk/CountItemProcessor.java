package org.daehagnawa.batch.daehagnawabatch.chunk;

import org.daehagnawa.batch.daehagnawabatch.domain.DepartmentInfo;
import org.daehagnawa.batch.daehagnawabatch.domain.UniversityDocument;
import org.daehagnawa.batch.daehagnawabatch.excel.ExcelData;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import javax.transaction.Synchronization;
import java.util.List;

@Component
@StepScope
public class CountItemProcessor implements ItemProcessor<UniversityDocument, Integer> {

    int count = 0;

    @Override
    public Integer process(UniversityDocument item) throws Exception {

        for (int i = 0; i < 100; i++) {
            count++;
        }

        return count;
    }
}
