package com.springboot.project.service.transaction.model;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionFilterResponseModel {

    private List<TransactionDetailModel> data = new ArrayList<>();
    private Long foundItems;
    private Long totalItems;
    private Long previousPageToken;
    private Long nextPageToken;
}
