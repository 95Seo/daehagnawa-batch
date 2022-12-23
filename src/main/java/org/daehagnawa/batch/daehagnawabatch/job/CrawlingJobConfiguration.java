package org.daehagnawa.batch.daehagnawabatch.job;

import lombok.RequiredArgsConstructor;
import org.daehagnawa.batch.daehagnawabatch.listener.StopWatchJobListener;
import org.daehagnawa.batch.daehagnawabatch.tasklet.CrawlingTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CrawlingJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CrawlingTasklet crawlingTasklet;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("crawlingJob")
                .start(crawlingStep())
                .incrementer(new RunIdIncrementer())
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Step crawlingStep() {
        return stepBuilderFactory.get("crawlingStep")
                .tasklet(crawlingTasklet)
                .build();
    }

}
