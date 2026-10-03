package com.github.noeeekr.servicerized.product.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.github.noeeekr.servicerized.product.repository.references.KindReferenceManager;

@Repository
public class KindRepository {
    @Autowired
    private KindReferenceManager references;

    public KindReferenceManager getReferenceManager() {
        return this.references;
    }
}
