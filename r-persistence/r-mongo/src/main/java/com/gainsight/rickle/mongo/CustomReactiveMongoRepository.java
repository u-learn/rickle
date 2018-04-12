package com.gainsight.rickle.mongo;

import com.gainsight.rickle.common.annotation.MultiTenantEntity;
import com.gainsight.rickle.common.annotation.MultiTenantEntityType;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.util.StreamUtils;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Sunand on 12/04/18.
 */
@Slf4j
public class CustomReactiveMongoRepository<T, ID> implements ReactiveMongoRepository<T, ID> {
  private final @NonNull MongoEntityInformation<T, ID> entityInformation;
  private final @NonNull ReactiveMongoOperations mongoOperations;

  public CustomReactiveMongoRepository(@NonNull MongoEntityInformation<T, ID> entityInformation, @NonNull ReactiveMongoOperations mongoOperations) {
    if (entityInformation == null) {
      throw new IllegalArgumentException("entityInformation is null");
    } else if (mongoOperations == null) {
      throw new IllegalArgumentException("mongoOperations is null");
    } else {
      this.entityInformation = entityInformation;
      this.mongoOperations = mongoOperations;
    }
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#findById(java.lang.Object)
   */
  @Override
  public Mono<T> findById(ID id) {

    Assert.notNull(id, "The given id must not be null!");

    return mongoOperations.findOne(getIdQuery(id), getJavaType(), entityInformation.getCollectionName());
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#findById(org.reactivestreams.Publisher)
   */
  @Override
  public Mono<T> findById(Publisher<ID> publisher) {

    Assert.notNull(publisher, "The given id must not be null!");

    return Mono.from(publisher).flatMap(
            id -> mongoOperations.findOne(getIdQuery(id), getJavaType(), entityInformation.getCollectionName()));
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.query.ReactiveQueryByExampleExecutor#findOne(org.springframework.data.domain.Example)
   */
  @Override
  public <S extends T> Mono<S> findOne(Example<S> example) {

    Assert.notNull(example, "Sample must not be null!");

    Query q = new Query(new Criteria().alike(example));
    q.limit(2);

    return mongoOperations.find(q, example.getProbeType(), entityInformation.getCollectionName()).buffer(2)
            .map(vals -> {

              if (vals.size() > 1) {
                throw new IncorrectResultSizeDataAccessException(1);
              }
              return vals.iterator().next();
            }).next();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#existsById(java.lang.Object)
   */
  @Override
  public Mono<Boolean> existsById(ID id) {

    Assert.notNull(id, "The given id must not be null!");

    return mongoOperations.exists(getIdQuery(id), getJavaType(),
            entityInformation.getCollectionName());
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#existsById(org.reactivestreams.Publisher)
   */
  @Override
  public Mono<Boolean> existsById(Publisher<ID> publisher) {

    Assert.notNull(publisher, "The given id must not be null!");

    return Mono.from(publisher).flatMap(id -> mongoOperations.exists(getIdQuery(id), getJavaType(),
            entityInformation.getCollectionName()));

  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.query.ReactiveQueryByExampleExecutor#exists(org.springframework.data.domain.Example)
   */
  @Override
  public <S extends T> Mono<Boolean> exists(Example<S> example) {

    Assert.notNull(example, "Sample must not be null!");

    Query q = new Query(new Criteria().alike(example));
    return mongoOperations.exists(q, example.getProbeType(), entityInformation.getCollectionName());
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveSortingRepository#findAll()
   */
  @Override
  public Flux<T> findAll() {
    return findAll(new Query());
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#findAllById(java.lang.Iterable)
   */
  @Override
  public Flux<T> findAllById(Iterable<ID> ids) {

    Assert.notNull(ids, "The given Iterable of Id's must not be null!");

    return findAll(new Query(new Criteria(entityInformation.getIdAttribute())
            .in(Streamable.of(ids).stream().collect(StreamUtils.toUnmodifiableList()))));
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#findAllById(org.reactivestreams.Publisher)
   */
  @Override
  public Flux<T> findAllById(Publisher<ID> ids) {

    Assert.notNull(ids, "The given Publisher of Id's must not be null!");

    return Flux.from(ids).buffer().flatMap(this::findAllById);
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveSortingRepository#findAll(org.springframework.data.domain.Sort)
   */
  @Override
  public Flux<T> findAll(Sort sort) {

    Assert.notNull(sort, "Sort must not be null!");

    return findAll(new Query().with(sort));
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.mongodb.repository.ReactiveMongoRepository#findAll(org.springframework.data.domain.Example, org.springframework.data.domain.Sort)
   */
  @Override
  public <S extends T> Flux<S> findAll(Example<S> example, Sort sort) {

    Assert.notNull(example, "Sample must not be null!");
    Assert.notNull(sort, "Sort must not be null!");

    Query query = new Query(new Criteria().alike(example)).with(sort);

    return mongoOperations.find(query, example.getProbeType(), entityInformation.getCollectionName());
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.mongodb.repository.ReactiveMongoRepository#findAll(org.springframework.data.domain.Example)
   */
  @Override
  public <S extends T> Flux<S> findAll(Example<S> example) {

    Assert.notNull(example, "Example must not be null!");

    return findAll(example, Sort.unsorted());
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#count()
   */
  @Override
  public Mono<Long> count() {
    return mongoOperations.count(new Query(), entityInformation.getCollectionName());
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.query.ReactiveQueryByExampleExecutor#count(org.springframework.data.domain.Example)
   */
  @Override
  public <S extends T> Mono<Long> count(Example<S> example) {

    Assert.notNull(example, "Sample must not be null!");

    Query q = new Query(new Criteria().alike(example));
    return mongoOperations.count(q, example.getProbeType(), entityInformation.getCollectionName());
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.mongodb.repository.ReactiveMongoRepository#insert(java.lang.Object)
   */
  @Override
  public <S extends T> Mono<S> insert(S entity) {

    Assert.notNull(entity, "Entity must not be null!");

    return mongoOperations.insert(entity, entityInformation.getCollectionName());
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.mongodb.repository.ReactiveMongoRepository#insert(java.lang.Iterable)
   */
  @Override
  public <S extends T> Flux<S> insert(Iterable<S> entities) {

    Assert.notNull(entities, "The given Iterable of entities must not be null!");

    List<S> source = Streamable.of(entities).stream().collect(StreamUtils.toUnmodifiableList());

    return source.isEmpty() ? Flux.empty() : Flux.from(mongoOperations.insertAll(source));
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.mongodb.repository.ReactiveMongoRepository#insert(org.reactivestreams.Publisher)
   */
  @Override
  public <S extends T> Flux<S> insert(Publisher<S> entities) {

    Assert.notNull(entities, "The given Publisher of entities must not be null!");

    return Flux.from(entities).flatMap(entity -> mongoOperations.insert(entity, entityInformation.getCollectionName()));
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#save(java.lang.Object)
   */
  @Override
  public <S extends T> Mono<S> save(S entity) {

    Assert.notNull(entity, "Entity must not be null!");

    if (entityInformation.isNew(entity)) {
      return mongoOperations.insert(entity, entityInformation.getCollectionName());
    }

    return mongoOperations.save(entity, entityInformation.getCollectionName());
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#saveAll(java.lang.Iterable)
   */
  @Override
  public <S extends T> Flux<S> saveAll(Iterable<S> entities) {

    Assert.notNull(entities, "The given Iterable of entities must not be null!");

    Streamable<S> source = Streamable.of(entities);

    return source.stream().allMatch(it -> entityInformation.isNew(it)) ? //
            mongoOperations.insertAll(source.stream().collect(Collectors.toList())) : //
            Flux.fromIterable(entities).flatMap(this::save);
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#saveAll(org.reactivestreams.Publisher)
   */
  @Override
  public <S extends T> Flux<S> saveAll(Publisher<S> entityStream) {

    Assert.notNull(entityStream, "The given Publisher of entities must not be null!");

    return Flux.from(entityStream).flatMap(entity -> entityInformation.isNew(entity) ? //
            mongoOperations.insert(entity, entityInformation.getCollectionName()).then(Mono.just(entity)) : //
            mongoOperations.save(entity, entityInformation.getCollectionName()).then(Mono.just(entity)));
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#deleteById(java.lang.Object)
   */
  @Override
  public Mono<Void> deleteById(ID id) {

    Assert.notNull(id, "The given id must not be null!");

    return mongoOperations
            .remove(getIdQuery(id), getJavaType(), entityInformation.getCollectionName()).then();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#deleteById(org.reactivestreams.Publisher)
   */
  @Override
  public Mono<Void> deleteById(Publisher<ID> publisher) {

    Assert.notNull(publisher, "Id must not be null!");

    return Mono.from(publisher).flatMap(id -> mongoOperations.remove(getIdQuery(id), getJavaType(),
            entityInformation.getCollectionName())).then();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#delete(java.lang.Object)
   */
  @Override
  public Mono<Void> delete(T entity) {

    Assert.notNull(entity, "The given entity must not be null!");

    return deleteById(entityInformation.getRequiredId(entity));
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#deleteAll(java.lang.Iterable)
   */
  @Override
  public Mono<Void> deleteAll(Iterable<? extends T> entities) {

    Assert.notNull(entities, "The given Iterable of entities must not be null!");

    return Flux.fromIterable(entities).flatMap(entity -> deleteById(entityInformation.getRequiredId(entity))).then();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#deleteAll(org.reactivestreams.Publisher)
   */
  @Override
  public Mono<Void> deleteAll(Publisher<? extends T> entityStream) {

    Assert.notNull(entityStream, "The given Publisher of entities must not be null!");

    return Flux.from(entityStream)//
            .map(entityInformation::getRequiredId)//
            .flatMap(this::deleteById)//
            .then();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.repository.reactive.ReactiveCrudRepository#deleteAll()
   */
  @Override
  public Mono<Void> deleteAll() {
    return mongoOperations.remove(new Query(), entityInformation.getCollectionName()).then(Mono.empty());
  }

  private Query getIdQuery(Object id) {
    return new Query(getIdCriteria(id));
  }

  private Criteria getIdCriteria(Object id) {
    return where(entityInformation.getIdAttribute()).is(id);
  }

  private Flux<T> findAll(Query query) {

    return mongoOperations.find(query, getJavaType(), entityInformation.getCollectionName());
  }

  private Query injectCriteria(Query query) {
    return injectCriteria(query, false);
  }

  /**
   * Injecting custom Criteria for Multi-Tenancy and honouring deleted flag
   * @param query
   * @param includeDeleted
   * @return
   */
  private Query injectCriteria(Query query, boolean includeDeleted) {
    if (isMultiTenantEntity()) {
      if (isCollectionPerTenant()) {
        return query;
      } else {
        Optional<String> tenantId = Optional.of(getTenantId());
        // check already existing present group criteria
        //ToDo get the tenant identifier from the annotation
        boolean criteriaAlreadyExists = query.getQueryObject().containsKey("tenantId");

        //need inject criteria
        if (!criteriaAlreadyExists) {
          if (tenantId.isPresent()) {
            query.addCriteria(where("tenantId").is(tenantId.get()));
            log.debug("inject tenant id {} in query {}", tenantId.get(), query);
          } else {
            // no tenant found
            throw new RuntimeException();
          }
        } else {
          log.info("[MongoDB] Tenant Id criteria is already present : " + query.toString());
        }
      }
    }

    if(!includeDeleted) {
      //Adding deleted flag check at the framework level
      boolean deletedCriteriaAlreadyExists = query.getQueryObject().containsKey("deleted");
      if (!deletedCriteriaAlreadyExists) {
        query.addCriteria(where("deleted").is(false));
      }
    }

    return query;
  }

  private boolean isCollectionPerTenant() {
    final MultiTenantEntity annotation = getJavaType().getAnnotation(MultiTenantEntity.class);
    return (annotation.value() == MultiTenantEntityType.PER_TENANT);
  }

  private boolean isMultiTenantEntity() {
    boolean res = getJavaType().isAnnotationPresent(MultiTenantEntity.class);
    log.debug("isMultiTenantEntity {} ? {}", getJavaType(), res);
    return res;
  }

  private String getCollectionName() {
    final String collectionName = entityInformation.getCollectionName();
    if (isMultiTenantEntity()) {
      final MultiTenantEntity annotation = getJavaType().getAnnotation(MultiTenantEntity.class);
      if (annotation.value() == MultiTenantEntityType.PER_TENANT) {
        return StringUtils.remove(collectionName + "_" + getTenantId(), '-');
      }
    }

    return collectionName;
  }

  private String getTenantId() {
    return "sunand";
  }

  private Class<T> getJavaType() {
    return entityInformation.getJavaType();
  }
}
