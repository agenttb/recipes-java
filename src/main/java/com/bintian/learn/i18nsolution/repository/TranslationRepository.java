package com.bintian.learn.i18nsolution.repository;

import com.bintian.learn.i18nsolution.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

    /**
     * 根据key和locale查询翻译
     */
    Optional<Translation> findByMessageKeyAndLocale(String messageKey, String locale);

    /**
     * 批量查询翻译（性能优化）
     */
    @Query("SELECT t FROM Translation t WHERE t.messageKey IN :keys AND t.locale = :locale")
    List<Translation> findByMessageKeysAndLocale(@Param("keys") List<String> keys,
                                                 @Param("locale") String locale);

    /**
     * 根据分类和locale查询
     */
    List<Translation> findByCategoryAndLocale(String category, String locale);

    /**
     * 查询所有支持的语言
     */
    @Query("SELECT DISTINCT t.locale FROM Translation t")
    List<String> findAllLocales();
}
