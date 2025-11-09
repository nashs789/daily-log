package com.nashs.daily_log.domain.post.repository;

import com.nashs.daily_log.domain.post.info.PostInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository {
    PostInfo findPostById(Long postId);
    Page<PostInfo> findAllPost(Pageable pageable);
    List<PostInfo> findMyAllPost(String userSub);
    PostInfo savePost(PostInfo postInfo);
    boolean updatePostById(PostInfo postInfo);
    boolean deletePostById(PostInfo postInfo);
}
