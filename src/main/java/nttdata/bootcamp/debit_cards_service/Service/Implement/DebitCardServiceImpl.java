package nttdata.bootcamp.debit_cards_service.Service.Implement;

import com.bank.debitcard.model.DebitCardAccountLink;
import com.bank.debitcard.model.DebitCardCreateRequest;
import com.bank.debitcard.model.DebitCardResponse;
import com.bank.debitcard.model.DebitCardUpdateRequest;
import com.bank.debitcard.model.LinkAccountRequest;
import com.bank.debitcard.model.UnlinkAccountRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import nttdata.bootcamp.debit_cards_service.Client.AccountClient;
import nttdata.bootcamp.debit_cards_service.Mapper.DebitCardMapper;
import nttdata.bootcamp.debit_cards_service.Repository.DebitCardRepository;
import nttdata.bootcamp.debit_cards_service.Rules.DebitCardAccountRulesPolicy;
import nttdata.bootcamp.debit_cards_service.Service.DebitCardNumberSequence;
import nttdata.bootcamp.debit_cards_service.Service.DebitCardService;
import nttdata.bootcamp.debit_cards_service.Validation.ValidationSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * Default {@link DebitCardService}: persistence, Redis cache, account-service integration and link rules.
 */
@Service
@RequiredArgsConstructor
public class DebitCardServiceImpl implements DebitCardService {

    private final DebitCardRepository repository;
    private final DebitCardMapper mapper;
    private final ValidationSupport validationSupport;
    private final AccountClient accountClient;
    private final DebitCardAccountRulesPolicy accountRulesPolicy;
    private final DebitCardNumberSequence debitCardNumberSequence;
    private final ReactiveStringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${redis.cache.ttl-seconds:900}")
    private long cacheTtlSeconds;

    /**
     * Removes one debit card entry from Redis cache.
     *
     * @param debitCardId debit card identifier
     * @return empty completion when delete finishes
     */
    private Mono<Void> evictDebitCardCache(String debitCardId) {
        String key = "debitCard:" + debitCardId;
        return redisTemplate.delete(key).then();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<List<DebitCardResponse>> findAll() {
        return RxJava3Adapter.fluxToFlowable(repository.findByStatus("ACTIVE"))
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<DebitCardResponse> create(DebitCardCreateRequest request) {
        return RxJava3Adapter.monoToSingle(
                Mono.fromCallable(() -> validationSupport.validateOrThrow(request))
                        .flatMap(v -> debitCardNumberSequence.nextCardNumber()
                                .map(cardNumber -> mapper.toDocument(v, cardNumber)))
                        .flatMap(repository::save)
                        .map(mapper::toResponse)
        ).flatMap(card -> RxJava3Adapter.monoToSingle(
                evictDebitCardCache(card.getId()).thenReturn(card)
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Maybe<DebitCardResponse> getById(String id) {
        String cacheKey = "debitCard:" + id;
        return RxJava3Adapter.monoToMaybe(
                redisTemplate.opsForValue().get(cacheKey)
                        .flatMap(json -> {
                            try {
                                return Mono.just(objectMapper.readValue(json, DebitCardResponse.class));
                            } catch (JsonProcessingException e) {
                                return Mono.error(new RuntimeException("Failed to deserialize debit card from cache", e));
                            }
                        })
                        .switchIfEmpty(repository.findById(id)
                                .map(mapper::toResponse)
                                .flatMap(dto -> {
                                    try {
                                        String json = objectMapper.writeValueAsString(dto);
                                        return redisTemplate.opsForValue()
                                                .set(cacheKey, json, Duration.ofSeconds(cacheTtlSeconds))
                                                .thenReturn(dto);
                                    } catch (JsonProcessingException e) {
                                        return Mono.error(new RuntimeException("Failed to serialize debit card for cache", e));
                                    }
                                }))
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<DebitCardResponse> update(String id, DebitCardUpdateRequest request) {
        return RxJava3Adapter.monoToSingle(
                Mono.fromCallable(() -> validationSupport.validateOrThrow(request))
                        .then(repository.findById(id)
                                .switchIfEmpty(Mono.error(new RuntimeException("Debit card not found")))
                                .doOnNext(doc -> mapper.updateDocument(doc, request))
                                .flatMap(repository::save)
                                .map(mapper::toResponse)
                        )
        ).flatMap(card -> RxJava3Adapter.monoToSingle(
                evictDebitCardCache(card.getId()).thenReturn(card)
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Completable delete(String id) {
        return RxJava3Adapter.monoToMaybe(repository.findById(id))
                .switchIfEmpty(Single.error(new RuntimeException("Debit card not found")))
                .flatMapCompletable(doc -> RxJava3Adapter.monoToCompletable(
                        Mono.fromRunnable(() -> doc.setStatus("INACTIVE"))
                                .then(repository.save(doc))
                                .then(evictDebitCardCache(id))
                ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<DebitCardResponse> linkAccount(String id, LinkAccountRequest request) {
        return RxJava3Adapter.monoToSingle(
                Mono.fromCallable(() -> validationSupport.validateOrThrow(request))
                        .then(repository.findById(id)
                                .switchIfEmpty(Mono.error(new RuntimeException("Debit card not found")))
                                .flatMap(doc -> accountClient.getAccountById(request.getAccountId())
                                        .switchIfEmpty(Mono.error(new RuntimeException("Account not found")))
                                        .flatMap(account -> {
                                            accountRulesPolicy.validateAccountForLink(doc, account);
                                            List<nttdata.bootcamp.debit_cards_service.Entity.DebitCardAccountLink> links =
                                                    accountRulesPolicy.applyLinkRules(
                                                            doc.getLinkedAccounts(),
                                                            request.getAccountId(),
                                                            request.getIsPrimary()
                                                    );
                                            doc.setLinkedAccounts(links);
                                            return repository.save(doc).map(mapper::toResponse);
                                        })
                                )
                        )
        ).flatMap(card -> RxJava3Adapter.monoToSingle(
                evictDebitCardCache(card.getId()).thenReturn(card)
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<DebitCardResponse> unlinkAccount(String id, UnlinkAccountRequest request) {
        return RxJava3Adapter.monoToSingle(
                Mono.fromCallable(() -> validationSupport.validateOrThrow(request))
                        .then(repository.findById(id)
                                .switchIfEmpty(Mono.error(new RuntimeException("Debit card not found")))
                                .flatMap(doc -> {
                                    List<nttdata.bootcamp.debit_cards_service.Entity.DebitCardAccountLink> links =
                                            accountRulesPolicy.applyUnlinkRules(
                                                    doc.getLinkedAccounts(),
                                                    request.getAccountId()
                                            );
                                    doc.setLinkedAccounts(links);
                                    return repository.save(doc).map(mapper::toResponse);
                                })
                        )
        ).flatMap(card -> RxJava3Adapter.monoToSingle(
                evictDebitCardCache(card.getId()).thenReturn(card)
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<List<DebitCardAccountLink>> getAccounts(String id) {
        return RxJava3Adapter.monoToSingle(
                repository.findById(id)
                        .switchIfEmpty(Mono.error(new RuntimeException("Debit card not found")))
                        .map(doc -> doc.getLinkedAccounts() != null
                                ? doc.getLinkedAccounts()
                                : List.<nttdata.bootcamp.debit_cards_service.Entity.DebitCardAccountLink>of())
                        .map(links -> links.stream().map(link -> {
                            DebitCardAccountLink apiLink = new DebitCardAccountLink();
                            apiLink.setAccountId(link.getAccountId());
                            apiLink.setIsPrimary(Boolean.TRUE.equals(link.getIsPrimary()));
                            return apiLink;
                        }).toList())
        );
    }
}
