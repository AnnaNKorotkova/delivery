package org.raif.delivery.infrastructure.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.raif.delivery.core.application.usecases.AssignOrdersUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssignOrdersJob implements Job {
    private final AssignOrdersUseCase useCase;

    @Autowired
    public AssignOrdersJob(AssignOrdersUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void execute(JobExecutionContext context) {
        useCase.handle();
    }
}