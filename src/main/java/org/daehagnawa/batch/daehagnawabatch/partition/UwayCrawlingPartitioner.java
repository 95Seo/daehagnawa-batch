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
public class UwayCrawlingPartitioner implements Partitioner {

    private final EntityManager em;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Tuple result = em.createQuery("select min(u.universityId), max(u.universityId) " +
                "from university_document u " +
                "where u.type = 'uway'", Tuple.class).getSingleResult();

        int min = ((Long) result.get(0)).intValue();
        int max = ((Long) result.get(1)).intValue();
        int targetSize = ((max - min) / gridSize) + 1;

        Map<String, ExecutionContext> partition = new HashMap<>();

        int number = 0;
        int start = 0;
        int end = targetSize;

        for (int i = 0; i < gridSize; i++) {
            ExecutionContext value = new ExecutionContext();
            partition.put("partition" + number, value);
            value.put("start", start);  // offset
            value.put("end", end);  // limit

            // 다음 값을 셋팅하기
            start = end;
            end = end + targetSize;
            number++;
        }

        return partition;
    }
}
