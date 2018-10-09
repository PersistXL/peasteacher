package com.peas.hsf.schedule;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by duanyihui on 2017/2/22.
 */
public class HsfScheduler {

    private static final HScheduler h = new HScheduler();

    public static HScheduler addJob(Class<? extends Job> job, String cronExp) throws SchedulerException {
        return h.addJob(job, cronExp);
    }


    public static class HScheduler {
        private static final Scheduler scheduler = SchedulerBuilder.build();

        public HScheduler addJob(Class<? extends Job> job, String cronExp) throws SchedulerException {
            JobDetail jobDetail = JobBuilder.newJob()
                    .newJob(job)
                    .withIdentity(job.getName(), "hsf-jobDetail-default")
                    .build();
            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(job.getName() + "-trigger", "hsf-trigger-default")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            return this;
        }

        public void startJobs() throws SchedulerException {
            scheduler.start();
        }
    }

    private static class SchedulerBuilder {

        public static Scheduler build() {
            Scheduler scheduler = null;
            try {
                SchedulerFactory sf = new StdSchedulerFactory();
                scheduler = sf.getScheduler();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            return scheduler;
        }
    }

}
