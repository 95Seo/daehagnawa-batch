package org.daehagnawa.batch.daehagnawabatch.scheduler;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class FirstJobRunner extends JobRunner {

    @Autowired
    private Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {

        JobDetail jobDetail = buildJobDetail(ScheduleJob.class, "FirstJob", "batch", new HashMap());
        Trigger trigger = buildJobTrigger("30 0/5 * * * ?");

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
