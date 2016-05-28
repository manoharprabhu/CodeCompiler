package com.codecompiler.services;

import com.codecompiler.vo.ProgramEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Manohar Prabhu on 5/29/2016.
 */
public interface ProgramRepository extends MongoRepository<ProgramEntity, String> {
    public ProgramEntity findByQueueId(String queueId);
}
