package com.springboot.project.businessrule.repository;

import com.springboot.project.businessrule.entity.BusinessRuleEntity;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRuleRepository extends MongoRepository<BusinessRuleEntity, UUID> {}
