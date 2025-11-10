package com.nashs.daily_log.infra.post.repository.impl;

import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.domain.post.repository.PostRepository;
import com.nashs.daily_log.infra.post.entity.Post;
import com.nashs.daily_log.infra.post.exception.PostInfraException;
import com.nashs.daily_log.infra.post.repository.PostJpaRepository;
import com.nashs.daily_log.infra.template.entity.Template;
import com.nashs.daily_log.infra.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.nashs.daily_log.infra.post.entity.Post.PostStatus.DELETED;
import static com.nashs.daily_log.infra.post.entity.Post.PostStatus.NORMAL;
import static com.nashs.daily_log.infra.post.exception.PostInfraException.PostInfraExceptionCode.NO_SUCH_POST;

@Repository
@RequiredArgsConstructor
@Transactional
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public PostInfo findPostById(Long postId) {
        return postJpaRepository.findPostById(postId)
                                .orElseThrow(() -> new PostInfraException(NO_SUCH_POST))
                                .toInfo();
    }

    @Override
    public Page<PostInfo> findAllPost(Pageable pageable) {
        return postJpaRepository.findAllByStatusOrderByIdDesc(NORMAL, pageable)
                                .map(Post::toInfo);
    }

    @Override
    public Page<PostInfo> findMyAllPost(Pageable pageable, String userSub) {
        return postJpaRepository.findByUserAndStatusOrderByIdDesc(User.builder()
                                                                      .sub(userSub)
                                                                      .build(), NORMAL, pageable)
                                .map(Post::toInfo);
    }

    @Override
    public PostInfo savePost(PostInfo postInfo) {
        return postJpaRepository.save(Post.fromInfo(postInfo))
                                .toInfo();
    }

    @Override
    public boolean updatePostById(PostInfo postInfo) {
        return postJpaRepository.updatePostById(
                postInfo.getId(),
                Template.fromInfo(postInfo.getTemplateInfo()),
                postInfo.getTitle(),
                postInfo.getContent()
        ) > 0;
    }

    @Override
    public boolean deletePostById(Long postId) {
        return postJpaRepository.deletePostById(postId, DELETED) > 0;
    }
}
