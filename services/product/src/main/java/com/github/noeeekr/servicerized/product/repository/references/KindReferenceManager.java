package com.github.noeeekr.servicerized.product.repository.references;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.noeeekr.servicerized.product.repository.models.entities.KindEntity;
import jakarta.persistence.EntityManager;

@Component 
public final class KindReferenceManager {
    @Autowired 
    private EntityManager entityManager;

    public final KindEntity getVirtualServiceKind() {
        Long id = KindEntity.Default.getVirtualServiceKind().getKindId();
        return entityManager.getReference(KindEntity.class, id);
    }
}
