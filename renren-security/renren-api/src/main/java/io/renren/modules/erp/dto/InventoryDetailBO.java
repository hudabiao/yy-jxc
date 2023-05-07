package io.renren.modules.erp.dto;

import lombok.Data;

@Data
public class InventoryDetailBO {
    private Long id;
    private Long baseId;
    private String properties;
    private Integer amount;
}
