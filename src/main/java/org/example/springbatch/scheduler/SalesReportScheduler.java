package org.example.springbatch.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class SalesReportScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesReportScheduler.class);

    private final JobOperator jobOperator;
    private final Job salesReportJob;

    public SalesReportScheduler(JobOperator jobOperator, Job salesReportJob) {
        this.jobOperator = jobOperator;
        this.salesReportJob = salesReportJob;
    }

    @Scheduled(fixedRate = 30_000)
    public void run() throws JobInstanceAlreadyCompleteException, InvalidJobParametersException, JobExecutionAlreadyRunningException, JobRestartException {
        LOGGER.info("Iniciando execução diária");

        jobOperator.start(
                salesReportJob,
                new JobParametersBuilder()
                        .addLocalDateTime("startDate", LocalDateTime.now(ZoneId.systemDefault()))
                        .toJobParameters()
        );
    }
}