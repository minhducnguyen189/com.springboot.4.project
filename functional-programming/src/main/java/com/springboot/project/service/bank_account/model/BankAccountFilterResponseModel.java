package com.springboot.project.service.bank_account.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccountFilterResponseModel {

  private List<BankAccountDetailModel> data = new ArrayList<>();
  private Long foundItems;
  private Long totalItems;
  private Long previousPageToken;
  private Long nextPageToken;

}
