package com.ulearn.rickle.repository;

import com.ulearn.rickle.domain.Route;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Sunand on 12/04/18.
 */
@Repository
public interface RouteRepository extends ReactiveMongoRepository<Route, String> {
}
