package com.gobra.sistemagestaodeobras.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gobra.sistemagestaodeobras.model.ItemCompra;

public interface ItemCompraRepository extends JpaRepository<ItemCompra, Integer> {
  
}
