package com.devteria.identityservice.service;

import com.devteria.identityservice.entity.Order;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CamundaService {
    RuntimeService runtimeService;
    TaskService taskService;
    IdentityService identityService;

    public void startOrderProcess(Order order) {
        try {


        identityService.setAuthenticatedUserId(order.getUser().getId());

        Map<String, Object> variables = new HashMap<>();
        variables.put("orderId", order.getId().toString());
        variables.put("customerId", order.getUser().getId());
        variables.put("orderTotal", order.getTotalAmount());
        variables.put("paymentStatus", order.getPaymentStatus());

        runtimeService.startProcessInstanceByKey(
                "order-process",
                order.getId().toString(), // businessKey
                variables
        );
        log.info("Started Camunda process for order {}", order.getId());


    }finally {
        identityService.clearAuthentication();

    }}
    public void completeTaskForOrder(Long orderId, Map<String, Object> variables) {
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceBusinessKey(orderId.toString())
                .list();

        if (!tasks.isEmpty()) {
            taskService.complete(tasks.get(0).getId(), variables);
            log.info("Completed task for order {}", orderId);
        } else {
            log.warn("No active tasks found for order {}", orderId);
        }
    }

    public String getCurrentTaskName(Long orderId) {
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(orderId.toString())
                .singleResult();

        return task != null ? task.getName() : "NO_ACTIVE_TASK";
    }
}