package org.daehagnawa.batch.daehagnawabatch.chunk;

import org.daehagnawa.batch.daehagnawabatch.domain.DepartmentInfo;
import org.daehagnawa.batch.daehagnawabatch.excel.ExcelData;
import org.springframework.batch.item.ItemProcessor;

import javax.transaction.Synchronization;
import java.util.List;

public class CountItemProcessor implements ItemProcessor<ExcelData, Integer> {

    int count = 0;

    @Override
    public Integer process(ExcelData item) throws Exception {

        for (int i = 0; i < 100; i++) {
            count++;
        }

        return count;
    }
}
