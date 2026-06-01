package com.devteria.identityservice.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/** Delegate rỗng để BPMN deploy được; thay bằng logic thật khi tích hợp nghiệp vụ. */
public class NoOpDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        // intentionally empty
    }
}
