package com.springboot.project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.springboot.project.entity.BankAccountEntity;
import com.springboot.project.entity.TransactionDetailEntity;
import com.springboot.project.entity.TransactionStatusEnumEntity;
import com.springboot.project.entity.PaymentMethodEnumEntity;
import com.springboot.project.exception.ResourceNotFoundException;
import com.springboot.project.generated.model.DomainEnumModel;
import com.springboot.project.generated.model.PaginationRequestModel;
import com.springboot.project.generated.model.PaymentMethodEnumModel;
import com.springboot.project.generated.model.SortOrderEnumModel;
import com.springboot.project.generated.model.TransactionFilterRequestModel;
import com.springboot.project.generated.model.TransactionFilterResponseModel;
import com.springboot.project.generated.model.TransactionStatusEnumModel;
import com.springboot.project.generated.model.UpdateTransactionRequestModel;
import com.springboot.project.repository.BankAccountRepository;
import com.springboot.project.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

        @Mock
        private BankAccountRepository bankAccountRepository;

        @InjectMocks
        private TransactionService transactionService;

        // --- getTransaction ---

        @Test
        void should_return_transaction_when_valid_id() {
                // given
                UUID transactionId = UUID.randomUUID();
                UUID bankAccountId = UUID.randomUUID();

                BankAccountEntity bankAccountEntity = new BankAccountEntity();
                bankAccountEntity.setId(bankAccountId);

                TransactionDetailEntity entity = new TransactionDetailEntity();
                entity.setId(transactionId);
                entity.setLocation("Berlin");
                entity.setSequenceNumber(42L);
                entity.setBankAccount(bankAccountEntity);

                when(transactionRepository.findById(transactionId))
                                .thenReturn(Optional.of(entity));

                // when
                var response = transactionService.getTransaction(transactionId);

                // then
                assertNotNull(response);
                assertEquals(transactionId, response.getId());
                assertEquals("Berlin", response.getLocation());
                assertEquals(bankAccountId, response.getBankAccountId());
                verify(transactionRepository).findById(transactionId);
        }

        @Test
        void should_throw_exception_when_transaction_not_found_on_get() {
                // given
                UUID transactionId = UUID.randomUUID();

                when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

                // when & then
                assertThrows(
                                ResourceNotFoundException.class,
                                () -> transactionService.getTransaction(transactionId));

                verify(transactionRepository).findById(transactionId);
        }

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

                when(transactionRepository.findBy(any(Specification.class), any()))
                                .thenReturn(List.of(entity));

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

                when(transactionRepository.findBy(any(Specification.class), any()))
                                .thenReturn(List.of(entity));

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

                when(transactionRepository.findBy(any(Specification.class), any()))
                                .thenReturn(List.of(entity));

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

                when(transactionRepository.findBy(any(Specification.class), any()))
                                .thenReturn(Collections.emptyList());

                // when
                TransactionFilterResponseModel response = transactionService
                                .filterTransactionsWithCursor(filterRequest);

                // then
                assertNotNull(response);
                assertEquals(0L, response.getFoundItems());
                assertNull(response.getNextPageToken());
                assertNull(response.getPreviousPageToken());
        }

        // --- updateTransaction ---

        @Test
        void should_update_transaction_when_valid_request() {
                // given
                UUID transactionId = UUID.randomUUID();
                UUID bankAccountId = UUID.randomUUID();

                UpdateTransactionRequestModel updateRequest = new UpdateTransactionRequestModel()
                                .date(LocalDate.of(2026, 3, 7))
                                .domain(DomainEnumModel.RETAIL)
                                .location("Berlin")
                                .value(BigDecimal.valueOf(2500))
                                .status(TransactionStatusEnumModel.SUCCESS)
                                .paymentMethod(PaymentMethodEnumModel.CARD)
                                .taxAmount(BigDecimal.valueOf(250))
                                .netValue(BigDecimal.valueOf(2250))
                                .bankAccountId(bankAccountId);

                TransactionDetailEntity existingEntity = new TransactionDetailEntity();
                existingEntity.setId(transactionId);
                existingEntity.setLocation("Old Location");
                existingEntity.setSequenceNumber(42L);

                BankAccountEntity bankAccountEntity = new BankAccountEntity();
                bankAccountEntity.setId(bankAccountId);

                TransactionDetailEntity savedEntity = new TransactionDetailEntity();
                savedEntity.setId(transactionId);
                savedEntity.setLocation("Berlin");
                savedEntity.setSequenceNumber(42L);
                savedEntity.setBankAccount(bankAccountEntity);
                savedEntity.setValue(BigDecimal.valueOf(2500));

                when(transactionRepository.findById(transactionId))
                                .thenReturn(Optional.of(existingEntity));
                when(bankAccountRepository.findById(bankAccountId))
                                .thenReturn(Optional.of(bankAccountEntity));
                when(transactionRepository.save(any(TransactionDetailEntity.class)))
                                .thenReturn(savedEntity);

                // when
                var response = transactionService.updateTransaction(transactionId, updateRequest);

                // then
                assertNotNull(response);
                assertEquals("Berlin", response.getLocation());
                assertEquals(bankAccountId, response.getBankAccountId());
                verify(transactionRepository).findById(transactionId);
                verify(bankAccountRepository).findById(bankAccountId);
                verify(transactionRepository).save(any(TransactionDetailEntity.class));
        }

        @Test
        void should_throw_exception_when_transaction_not_found_on_update() {
                // given
                UUID transactionId = UUID.randomUUID();
                UUID bankAccountId = UUID.randomUUID();

                UpdateTransactionRequestModel updateRequest = new UpdateTransactionRequestModel()
                                .date(LocalDate.of(2026, 3, 7))
                                .domain(DomainEnumModel.RETAIL)
                                .location("Berlin")
                                .value(BigDecimal.valueOf(2500))
                                .status(TransactionStatusEnumModel.SUCCESS)
                                .paymentMethod(PaymentMethodEnumModel.CARD)
                                .bankAccountId(bankAccountId);

                when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

                // when & then
                assertThrows(
                                ResourceNotFoundException.class,
                                () -> transactionService.updateTransaction(transactionId, updateRequest));

                verify(transactionRepository).findById(transactionId);
                verify(bankAccountRepository, never()).findById(any());
                verify(transactionRepository, never()).save(any());
        }

        @Test
        void should_throw_exception_when_bank_account_not_found_on_update() {
                // given
                UUID transactionId = UUID.randomUUID();
                UUID bankAccountId = UUID.randomUUID();

                UpdateTransactionRequestModel updateRequest = new UpdateTransactionRequestModel()
                                .date(LocalDate.of(2026, 3, 7))
                                .domain(DomainEnumModel.RETAIL)
                                .location("Berlin")
                                .value(BigDecimal.valueOf(2500))
                                .status(TransactionStatusEnumModel.SUCCESS)
                                .paymentMethod(PaymentMethodEnumModel.CARD)
                                .bankAccountId(bankAccountId);

                TransactionDetailEntity existingEntity = new TransactionDetailEntity();
                existingEntity.setId(transactionId);

                when(transactionRepository.findById(transactionId))
                                .thenReturn(Optional.of(existingEntity));
                when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.empty());

                // when & then
                assertThrows(
                                ResourceNotFoundException.class,
                                () -> transactionService.updateTransaction(transactionId, updateRequest));

                verify(transactionRepository).findById(transactionId);
                verify(bankAccountRepository).findById(bankAccountId);
                verify(transactionRepository, never()).save(any());
        }

        // --- deleteTransaction ---

        @Test
        void should_soft_delete_transaction_when_valid_id() {
                // given
                UUID transactionId = UUID.randomUUID();

                TransactionDetailEntity existingEntity = new TransactionDetailEntity();
                existingEntity.setId(transactionId);
                existingEntity.setStatus(TransactionStatusEnumEntity.SUCCESS);

                when(transactionRepository.findById(transactionId))
                                .thenReturn(Optional.of(existingEntity));
                when(transactionRepository.save(any(TransactionDetailEntity.class)))
                                .thenReturn(existingEntity);

                // when
                transactionService.deleteTransaction(transactionId);

                // then
                assertEquals(TransactionStatusEnumEntity.DELETED, existingEntity.getStatus());
                verify(transactionRepository).findById(transactionId);
                verify(transactionRepository).save(existingEntity);
        }

        @Test
        void should_throw_exception_when_transaction_not_found_on_delete() {
                // given
                UUID transactionId = UUID.randomUUID();

                when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

                // when & then
                assertThrows(
                                ResourceNotFoundException.class,
                                () -> transactionService.deleteTransaction(transactionId));

                verify(transactionRepository).findById(transactionId);
                verify(transactionRepository, never()).save(any());
        }
}
