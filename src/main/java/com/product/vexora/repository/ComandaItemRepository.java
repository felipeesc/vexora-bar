package com.product.vexora.repository;

import com.product.vexora.entity.ComandaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ComandaItemRepository extends JpaRepository<ComandaItem, UUID> {



}

