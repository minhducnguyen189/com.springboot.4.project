package com.springboot.project.businessrule.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.springboot.project.businessrule.service.IBusinessRuleService;
import com.springboot.project.common.exception.BadRequestException;
import com.springboot.project.common.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.json.JsonMapper;

class BusinessRuleControllerTest {

    private static final String TEST_RULE_PATH = "/v1/drools/business-rules/actions/test";
    private static final String TEST_RULE_REQUEST =
            """
            {"data": {}, "businessRule": {"name": "P", "rules": [{"rule": "R",
              "when": [{"identifier": "$x", "type": "Nope", "conditions": []}],
              "then": [{"actions": []}]}]}}
            """;

    private final IBusinessRuleService businessRuleService = mock(IBusinessRuleService.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc =
                MockMvcBuilders.standaloneSetup(
                                new BusinessRuleController(
                                        businessRuleService, JsonMapper.builder().build()))
                        .setControllerAdvice(new GlobalExceptionHandler())
                        .build();
    }

    @Test
    void should_respond_400_with_error_body_when_rule_does_not_compile() throws Exception {
        // given
        when(businessRuleService.testBusinessRule(any(), any()))
                .thenThrow(new BadRequestException("Business rule does not compile: Nope"));

        // when / then
        mockMvc.perform(
                        post(TEST_RULE_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TEST_RULE_REQUEST))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Business rule does not compile: Nope"));
    }
}
