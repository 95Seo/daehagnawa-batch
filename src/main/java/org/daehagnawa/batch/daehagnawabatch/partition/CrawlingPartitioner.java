package org.daehagnawa.batch.daehagnawabatch.partition;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CrawlingPartitioner implements Partitioner {

    private final EntityManager em;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Tuple result = em.createQuery("select min(u.universityId), max(u.universityId) from university_document u", Tuple.class).getSingleResult();

        int min = ((Long) result.get(0)).intValue();
        int max = ((Long) result.get(1)).intValue();
        int targetSize = ((max - min) / gridSize) + 1;

        Map<String, ExecutionContext> partition = new HashMap<>();

        int number = 0;
        int start = min;
        int end = targetSize;

        for (int i = 0; i < gridSize; i++) {
            ExecutionContext value = new ExecutionContext();
            partition.put("partition" + number, value);
            value.put("start", (long) start);
            value.put("end", (long) end);

            // 다음 값을 셋팅하기
            start = end + 1;
            end = end + targetSize;
            number++;
        }


//        int end = start + targetSize - 1;

//        while (start <= max) {
//            ExecutionContext value = new ExecutionContext();
//            result.put("partition" + number, value);
//            value.put("product", list.get(number));
//
//            if (end >= max) {
//                end = max;
//            }
//            value.putInt("minValue", start);
//            value.putInt("maxValue", end);
//            start += targetSize;
//            end += targetSize;
//            number++;
//        }

        return partition;
    }
}
