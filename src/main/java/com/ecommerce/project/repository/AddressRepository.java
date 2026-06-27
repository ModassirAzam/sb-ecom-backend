package com.ecommerce.project.repository;

import com.ecommerce.project.model.Address;
import com.ecommerce.project.payload.AddressDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
