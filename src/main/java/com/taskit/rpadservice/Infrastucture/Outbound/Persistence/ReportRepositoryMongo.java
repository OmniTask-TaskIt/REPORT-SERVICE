package com.taskit.rpadservice.Infrastucture.Outbound.Persistence;

import com.taskit.rpadservice.Domain.Model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepositoryMongo
        extends MongoRepository<Report, String> {
}
