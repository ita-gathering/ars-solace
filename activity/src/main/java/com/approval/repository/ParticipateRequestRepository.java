package com.approval.repository;

import com.approval.po.ParticipateRequest;
import com.approval.po.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by guowanyi on 2019/3/12.
 */
@Repository
public interface ParticipateRequestRepository extends MongoRepository<ParticipateRequest,String> {
    List<ParticipateRequest> findAllByUserName(String userName);
}
