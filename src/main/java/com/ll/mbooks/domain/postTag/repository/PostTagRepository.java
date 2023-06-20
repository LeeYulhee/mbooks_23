package com.ll.mbooks.domain.postTag.repository;

import com.ll.mbooks.domain.postTag.entity.PostTag;
import com.ll.mbooks.domain.productTag.entity.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    Optional<PostTag> findByPostIdAndPostKeywordId(Long postId, Long keywordId);

    List<PostTag> findAllByPostId(Long postId);

    List<PostTag> findAllByPostIdIn(long[] ids);

    List<PostTag> findAllByMemberId(Long memberId);

    List<PostTag> findAllByMemberIdAndPostKeyword_contentOrderByPost_idDesc(Long id, String postKeywordContent);

    List<PostTag> findAllByMemberIdAndPostKeywordIdOrderByPost_idDesc(long memberId, long postKeywordId);

    List<ProductTag> findAllByPostKeyword_contentOrderByPost_idDesc(String productKeywordContent);
}
