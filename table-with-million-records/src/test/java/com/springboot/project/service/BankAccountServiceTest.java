package com.springboot.project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.springboot.project.entity.BankAccountEntity;
import com.springboot.project.entity.AccountStatusEnumEntity;
import com.springboot.project.entity.AccountTypeEnumEntity;
import com.springboot.project.generated.model.BankAccountFilterRequestModel;
import com.springboot.project.generated.model.BankAccountFilterResponseModel;
import com.springboot.project.generated.model.PaginationRequestModel;
import com.springboot.project.generated.model.SortOrderEnumModel;
import com.springboot.project.repository.BankAccountRepository;
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
class BankAccountServiceTest {

        @Mock
        private BankAccountRepository bankAccountRepository;

        @InjectMocks
        private BankAccountService bankAccountService;

        // --- filterBankAccounts (offset pagination) ---

        @Test
        @SuppressWarnings("unchecked")
        void should_return_filtered_bank_accounts_when_valid_filter_request() {
                // given
                PaginationRequestModel pagination = new PaginationRequestModel().pageNumber(0).pageSize(10);
                BankAccountFilterRequestModel filterRequest = new BankAccountFilterRequestModel()
                                .firstName("John")
                                .pagination(pagination);

                BankAccountEntity entity = new BankAccountEntity();
                entity.setFirstName("John");
                entity.setLastName("Doe");
                entity.setSequenceNumber(1L);
                entity.setAccountType(AccountTypeEnumEntity.SAVINGS);
                entity.setStatus(AccountStatusEnumEntity.ACTIVE);
                List<BankAccountEntity> entities = List.of(entity);
                Page<BankAccountEntity> page = new PageImpl<>(entities);

                when(bankAccountRepository.findAll(any(Specification.class), any(Pageable.class)))
                                .thenReturn(page);

                // when
                BankAccountFilterResponseModel response = bankAccountService.filterBankAccounts(filterRequest);

                // then
                assertNotNull(response);
                assertEquals(1L, response.getFoundItems());
                assertEquals(1L, response.getTotalItems());
                assertEquals(1, response.getData().size());
                assertEquals("John", response.getData().get(0).getFirstName());

                verify(bankAccountRepository).findAll(any(Specification.class), any(Pageable.class));
        }

        @Test
        @SuppressWarnings("unchecked")
        void should_return_empty_results_when_no_bank_accounts_match_filter() {
                // given
                PaginationRequestModel pagination = new PaginationRequestModel().pageNumber(0).pageSize(10);
                BankAccountFilterRequestModel filterRequest = new BankAccountFilterRequestModel()
                                .pagination(pagination);

                Page<BankAccountEntity> emptyPage = new PageImpl<>(Collections.emptyList());

                when(bankAccountRepository.findAll(any(Specification.class), any(Pageable.class)))
                                .thenReturn(emptyPage);

                // when
                BankAccountFilterResponseModel response = bankAccountService.filterBankAccounts(filterRequest);

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
                                .sortBy("firstName")
                                .sortOrder(SortOrderEnumModel.ASC);
                BankAccountFilterRequestModel filterRequest = new BankAccountFilterRequestModel()
                                .pagination(pagination);

                BankAccountEntity entity = new BankAccountEntity();
                entity.setFirstName("Alice");
                entity.setSequenceNumber(1L);
                Page<BankAccountEntity> page = new PageImpl<>(List.of(entity));

                when(bankAccountRepository.findAll(any(Specification.class), any(Pageable.class)))
                                .thenReturn(page);

                // when
                BankAccountFilterResponseModel response = bankAccountService.filterBankAccounts(filterRequest);

                // then
                assertNotNull(response);
                assertEquals(1, response.getData().size());
                verify(bankAccountRepository).findAll(any(Specification.class), any(Pageable.class));
        }

        // --- filterBankAccountsWithCursor (cursor pagination) ---

        @Test
        @SuppressWarnings("unchecked")
        void should_return_cursor_paginated_results_when_no_token_provided() {
                // given
                PaginationRequestModel pagination = new PaginationRequestModel().pageSize(5);
                BankAccountFilterRequestModel filterRequest = new BankAccountFilterRequestModel()
                                .pagination(pagination);

                BankAccountEntity entity = new BankAccountEntity();
                entity.setFirstName("Alice");
                entity.setSequenceNumber(100L);
                Page<BankAccountEntity> page = new PageImpl<>(List.of(entity));

                when(bankAccountRepository.findAll(any(Specification.class), any(Pageable.class)))
                                .thenReturn(page);

                // when
                BankAccountFilterResponseModel response = bankAccountService
                                .filterBankAccountsWithCursor(filterRequest);

                // then
                assertNotNull(response);
                assertEquals(1, response.getData().size());
                assertEquals(100L, response.getNextPageToken());
                assertEquals(100L, response.getPreviousPageToken());
        }

        @Test
        @SuppressWarnings("unchecked")
        void should_apply_next_page_token_when_next_page_token_provided() {
                // given
                PaginationRequestModel pagination = new PaginationRequestModel().pageSize(5).nextPageToken(50L);
                BankAccountFilterRequestModel filterRequest = new BankAccountFilterRequestModel()
                                .pagination(pagination);

                BankAccountEntity entity = new BankAccountEntity();
                entity.setSequenceNumber(51L);
                Page<BankAccountEntity> page = new PageImpl<>(List.of(entity));

                when(bankAccountRepository.findAll(any(Specification.class), any(Pageable.class)))
                                .thenReturn(page);

                // when
                BankAccountFilterResponseModel response = bankAccountService
                                .filterBankAccountsWithCursor(filterRequest);

                // then
                assertNotNull(response);
                assertEquals(51L, response.getNextPageToken());
                assertEquals(51L, response.getPreviousPageToken());
        }

        @Test
        @SuppressWarnings("unchecked")
        void should_apply_previous_page_token_when_previous_page_token_provided() {
                // given
                PaginationRequestModel pagination = new PaginationRequestModel().pageSize(5).previousPageToken(100L);
                BankAccountFilterRequestModel filterRequest = new BankAccountFilterRequestModel()
                                .pagination(pagination);

                BankAccountEntity entity = new BankAccountEntity();
                entity.setSequenceNumber(99L);
                Page<BankAccountEntity> page = new PageImpl<>(List.of(entity));

                when(bankAccountRepository.findAll(any(Specification.class), any(Pageable.class)))
                                .thenReturn(page);

                // when
                BankAccountFilterResponseModel response = bankAccountService
                                .filterBankAccountsWithCursor(filterRequest);

                // then
                assertNotNull(response);
                assertEquals(99L, response.getNextPageToken());
                assertEquals(99L, response.getPreviousPageToken());
        }

        @Test
        @SuppressWarnings("unchecked")
        void should_return_null_tokens_when_cursor_result_is_empty() {
                // given
                PaginationRequestModel pagination = new PaginationRequestModel().pageSize(5);
                BankAccountFilterRequestModel filterRequest = new BankAccountFilterRequestModel()
                                .pagination(pagination);

                Page<BankAccountEntity> emptyPage = new PageImpl<>(Collections.emptyList());

                when(bankAccountRepository.findAll(any(Specification.class), any(Pageable.class)))
                                .thenReturn(emptyPage);

                // when
                BankAccountFilterResponseModel response = bankAccountService
                                .filterBankAccountsWithCursor(filterRequest);

                // then
                assertNotNull(response);
                assertEquals(0L, response.getFoundItems());
                assertNull(response.getNextPageToken());
                assertNull(response.getPreviousPageToken());
        }
}
