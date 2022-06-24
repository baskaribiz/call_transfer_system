package com.bank.callTransfer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.callTransfer.dao.entity.CallDistribution;

@Repository
public interface CallDistributionRepository extends JpaRepository<CallDistribution, Long> {

	public List<CallDistribution> findByReason(String reason);
}
