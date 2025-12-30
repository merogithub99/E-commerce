package com.sushant.repository;

import com.sushant.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
//address repo
public interface AddressRepository extends JpaRepository<Address,Long> {
}
