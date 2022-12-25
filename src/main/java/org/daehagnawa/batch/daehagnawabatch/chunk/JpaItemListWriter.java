package org.daehagnawa.batch.daehagnawabatch.chunk;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JpaItemWriter;

import java.util.ArrayList;
import java.util.List;

// jojoldu 참고
@RequiredArgsConstructor
public class JpaItemListWriter<T> extends JpaItemWriter<List<T>> {
    private final JpaItemWriter<T> jpaItemWriter;

    @Override
    public void write(List<? extends List<T>> items) {

        List<T> itemList = new ArrayList<>();

        for (List<T> item : items) {
            itemList.addAll(item);
            System.out.println(item);
        }

        jpaItemWriter.write(itemList);
    }
}
