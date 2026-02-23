package com.springboot.project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.springboot.project.entity.TransactionDetailEntity;
import com.springboot.project.entity.TransactionStatusEnumEntity;
import com.springboot.project.entity.PaymentMethodEnumEntity;
import com.springboot.project.generated.model.PaginationRequestModel;
import com.springboot.project.generated.model.SortOrderEnumModel;
import com.springboot.project.generated.model.TransactionFilterRequestModel;
import com.springboot.project.generated.model.TransactionFilterResponseModel;
import com.springboot.project.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

        @Mock
        private TransactionRepository transactionRepository;

        @InjectMocks
        private TransactionService transactionService;

        // --- filterTransactions (offset pagination) ---

        @Test
        @SuppressWarnings("unchecked")
        void should_return_filtered_transactions_when_valid_filter_request() {
                // given
                PaginationRequestModel pagination = new PaginationRequestModel().pageNumber(0).pageSize(10);
                TransactionFilterRequestModel filterRequest = new TransactionFilterRequestModel()
                                .location("New York")
                                .pagination(pagination);

                TransactionDetailEntity entity = new TransactionDetailEntity();
                entity.setLocation("New York");
                entity.setSequenceNumber(1L);
                entity.setTransactionNumber("TRN0000000001");
                entity.setValue(BigDecimal.valueOf(1000));
                entity.setStatus(TransactionStatusEnumEntity.SUCCESS);
                entity.setPaymentMethod(PaymentMethodEnumEntity.CARD);
                List<TransactionDetailEntity> entities = List.of(entity);
                Page<TransactionDetailEntity> page = new PageImpl<>(entities);

                when(transactionRepository.findAll(any(Specification.class), any(Pageable.class)))
                                .thenReturn(page);

                // when
                TransactionFilterResponseModel response = transactionService.filterTransactions(filterRequest);

                // then
                assertNotNull(response);
                assertEquals(1L, response.getFoundItems());
                assertEquals(1L, response.getTotalItems());
                assertEquals(1, response.getData().size());
                assertEquals("New York", response.getData().get(0).getLocation());

                verify(transactionRepository).findAll(any(Specification.class), any(Pageable.class));
        }

        @Test
        @SuppressWarnings("unchecked")
        void should_return_empty_results_when_no_transactions_match_filter() {
                // given
                PaginationRequestModel pagination = new PaginationRequestModel().pageNumber(0).pageSize(10);
                TransactionFilterRequestModel filterRequest = new TransactionFilterRequestModel()
                                .pagination(pagination);

                Page<TransactionDetailEntity> emptyPage = new PageImpl<>(Collections.emptyList());

                when(transactionRepository.findAll(any(Specification.class), any(Pageable.class)))
                                .thenReturn(emptyPage);

                // when
                TransactionFilterResponseModel response = transactionService.filterTransactions(filterRequest);

                // then
                assertNotNull(response);
                assertEquals(0L, response.getFoundItems());
                assertEquals(0L, response.getTotalItems());
                assertTrue(response.getData().isEmpty());
        }

        @Test
        @SuppressWarnings("unchecked")
        void should_apply_sorting_when_sort_parameters_provided() {
                // given
                PaginationRequestModel pagination = new PaginationRequestModel()
                                .pageNumber(0)
                                .pageSize(10)
                                .sortBy("date")
                                .sortOrder(SortOrderEnumModel.DESC);
                TransactionFilterRequestModel filterRequest = new TransactionFilterRequestModel()
                                .pagination(pagination);

                TransactionDetailEntity entity = new TransactionDetailEntity();
                entity.setSequenceNumber(1L);
                entity.setLocation("Tokyo");
                Page<TransactionDetailEntity> page = new PageImpl<>(List.of(entity));

                when(transactionRepository.findAll(any(Specification.class), any(Pageable.class)))
                                .thenReturn(page);

                // when
                TransactionFilterResponseModel response = transactionService.filterTransactions(filterRequest);

                // then
                assertNotNull(response);
                assertEquals(1, response.getData().size());
                verify(transactionRepository).findAll(any(Specification.class), any(Pageable.class));
        }

        // --- filterTransactionsWithCursor (cursor pagination) ---

        @Test
        @SuppressWarnings("unchecked")
        void should_return_cursor_paginated_results_when_no_token_provided() {
                // given
                PaginationRequestModel pagination = new PaginationRequestModel().pageSize(5);
                TransactionFilterRequestModel filterRequest = new TransactionFilterRequestModel()
                                .pagination(pagination);

                TransactionDetailEntity entity = new TransactionDetailEntity();
                entity.setSequenceNumber(200L);
                entity.setLocation("London");
                Page<TransactionDetailEntity> page = new PageImpl<>(List.of(entity));

                when(transactionRepository.findAll(any(Specification.class), any(Pageable.class)))
                                .thenReturn(page);

                // when
                TransactionFilterResponseModel response = transactionService
                                .filterTransactionsWithCursor(filterRequest);

                // then
                assertNotNull(response);
                assertEquals(1, response.getData().size());
                assertEquals(200L, response.getNextPageToken());
                assertEquals(200L, response.getPreviousPageToken());
        }

        @Test
        @SuppressWarnings("unchecked")
        void should_apply_next_page_token_when_next_page_token_provided() {
                // given
                PaginationRequestModel pagination = new PaginationRequestModel().pageSize(5).nextPageToken(100L);
                TransactionFilterRequestModel filterRequest = new TransactionFilterRequestModel()
                                .pagination(pagination);

                TransactionDetailEntity entity = new TransactionDetailEntity();
                entity.setSequenceNumber(101L);
                Page<TransactionDetailEntity> page = new PageImpl<>(List.of(entity));

                when(transactionRepository.findAll(any(Specification.class), any(Pageable.class)))
                                .thenReturn(page);

                // when
                TransactionFilterResponseModel response = transactionService
                                .filterTransactionsWithCursor(filterRequest);

                // then
                assertNotNull(response);
                assertEquals(101L, response.getNextPageToken());
                assertEquals(101L, response.getPreviousPageToken());
        }

        @Test
        @SuppressWarnings("unchecked")
        void should_apply_previous_page_token_when_previous_page_token_provided() {
                // given
                PaginationRequestModel pagination = new PaginationRequestModel().pageSize(5).previousPageToken(200L);
                TransactionFilterRequestModel filterRequest = new TransactionFilterRequestModel()
                                .pagination(pagination);

                TransactionDetailEntity entity = new TransactionDetailEntity();
                entity.setSequenceNumber(199L);
                Page<TransactionDetailEntity> page = new PageImpl<>(List.of(entity));

                when(transactionRepository.findAll(any(Specification.class), any(Pageable.class)))
                                .thenReturn(page);

                // when
                TransactionFilterResponseModel response = transactionService
                                .filterTransactionsWithCursor(filterRequest);

                // then
                assertNotNull(response);
                assertEquals(199L, response.getNextPageToken());
                assertEquals(199L, response.getPreviousPageToken());
        }

        @Test
        @SuppressWarnings("unchecked")
        void should_return_null_tokens_when_cursor_result_is_empty() {
                // given
                PaginationRequestModel pagination = new PaginationRequestModel().pageSize(5);
                TransactionFilterRequestModel filterRequest = new TransactionFilterRequestModel()
                                .pagination(pagination);

                Page<TransactionDetailEntity> emptyPage = new PageImpl<>(Collections.emptyList());

                when(transactionRepository.findAll(any(Specification.class), any(Pageable.class)))
                                .thenReturn(emptyPage);

                // when
                TransactionFilterResponseModel response = transactionService
                                .filterTransactionsWithCursor(filterRequest);

                // then
                assertNotNull(response);
                assertEquals(0L, response.getFoundItems());
                assertNull(response.getNextPageToken());
                assertNull(response.getPreviousPageToken());
        }
}
