package com.nashs.daily_log.domain.post.repository;

import com.nashs.daily_log.domain.post.info.PostInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository {
    PostInfo findPostById(Long postId);
    Page<PostInfo> findAllPost(Pageable pageable);
    Page<PostInfo> findMyAllPost(Pageable pageable, String userSub);
    PostInfo savePost(PostInfo postInfo);
    boolean updatePostById(PostInfo postInfo);
    boolean deletePostById(Long postInfo);
}
