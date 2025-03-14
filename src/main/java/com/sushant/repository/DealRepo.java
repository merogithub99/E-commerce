package com.sushant.repository;

import com.sushant.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepo extends JpaRepository<Deal,Long> {
}
