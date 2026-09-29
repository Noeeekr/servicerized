package com.github.noeeekr.servicerized.product.repository.models.dto;

import com.github.noeeekr.servicerized.product.repository.interfaces.entities.KindInterface;
import lombok.Getter;

@Getter
public class KindDto implements KindInterface {
    private String kindName;
    private Long kindId;

    public KindDto(String kindName, Long kindId) {
        this.kindName = kindName;
        this.kindId = kindId;
    }

    public KindDto(KindInterface kind) {
        this.kindName = kind.getKindName();
        this.kindId = kind.getKindId();
    }
}
