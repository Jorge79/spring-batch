package org.example.springbatch.batch.listener;

import org.example.springbatch.batch.decider.InputFilesDecider;
import org.example.springbatch.integration.SftpDownloadService;
import org.example.springbatch.integration.SftpUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class SalesReportSftpJobListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesReportSftpJobListener.class);

    private final SftpDownloadService sftpDownloadService;
    private final SftpUploadService sftpUploadService;

    public SalesReportSftpJobListener(SftpDownloadService sftpDownloadService,
                                      SftpUploadService sftpUploadService) {
        this.sftpDownloadService = sftpDownloadService;
        this.sftpUploadService = sftpUploadService;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOGGER.info("Iniciando download de arquivos no SFTP antes do job");
        int downloadedFilesCount = sftpDownloadService.downloadNewFiles();
        jobExecution.getExecutionContext()
                .putInt(InputFilesDecider.DOWNLOADED_FILES_COUNT_CONTEXT_KEY, downloadedFilesCount);
        LOGGER.info("Download SFTP finalizado com {} arquivo(s) novo(s).", downloadedFilesCount);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() != BatchStatus.COMPLETED) {
            LOGGER.warn("Job finalizado com status {}. Upload/movimentacao SFTP nao sera executado.",
                    jobExecution.getStatus());
            return;
        }

        if (InputFilesDecider.NO_INPUT.equals(jobExecution.getExitStatus().getExitCode())) {
            LOGGER.info("Job finalizado com status {}. Upload/movimentacao SFTP nao sera executado.",
                    InputFilesDecider.NO_INPUT);
            return;
        }

        LOGGER.info("Job concluído com sucesso. Iniciando upload e movimentação no SFTP");
        sftpUploadService.uploadAndMoveProcessedFiles();
    }
}