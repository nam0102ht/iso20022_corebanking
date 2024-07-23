package com.ntnn.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransferMoneyResponse {
    private String rspCode;
    private String rsnCode;
    private String rsnDesc;
}
