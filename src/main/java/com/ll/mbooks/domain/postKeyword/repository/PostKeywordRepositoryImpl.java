package com.ll.mbooks.domain.postKeyword.repository;

import com.ll.mbooks.domain.postKeyword.entity.PostKeyword;
import com.ll.mbooks.domain.postKeyword.entity.QPostKeyword;
import com.ll.mbooks.domain.postTag.entity.QPostTag;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PostKeywordRepositoryImpl implements PostKeywordRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostKeyword> getQslAllByAuthorId(Long authorId) {
        List<Tuple> fetch = jpaQueryFactory
                .select(QPostKeyword.postKeyword, QPostTag.postTag.count())
                .from(QPostKeyword.postKeyword)
                .innerJoin(QPostTag.postTag)
                .on(QPostKeyword.postKeyword.eq(QPostTag.postTag.postKeyword))
                .where(QPostTag.postTag.member.id.eq(authorId))
                .orderBy(QPostTag.postTag.post.id.desc())
                .groupBy(QPostKeyword.postKeyword.id)
                .fetch();

        return fetch.stream().
                map(tuple -> {
                    PostKeyword _postKeyword = tuple.get(QPostKeyword.postKeyword);
                    Long postTagsCount = tuple.get(QPostTag.postTag.count());

                    _postKeyword.getExtra().put("postTagsCount", postTagsCount);

                    return _postKeyword;
                })
                .collect(Collectors.toList());
    }
}
