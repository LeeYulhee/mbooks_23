package com.ll.mbooks.domain.postKeyword.repository;

import com.ll.mbooks.domain.postKeyword.entity.PostKeyword;

import java.util.List;

public interface PostKeywordRepositoryCustom {
    List<PostKeyword> getQslAllByAuthorId(Long authorId);
}
