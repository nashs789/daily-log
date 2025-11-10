package com.nashs.daily_log.domain.post.service;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.exception.PostDomainException;
import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nashs.daily_log.domain.post.exception.PostDomainException.PostDomainExceptionCode.FAIL_UPDATE_POST;
import static com.nashs.daily_log.domain.post.exception.PostDomainException.PostDomainExceptionCode.NOT_POST_OWNER;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostInfo findPostById(Long postId) {
        return postRepository.findPostById(postId);
    }

    public Page<PostInfo> findAllPost(Pageable pageable) {
        return postRepository.findAllPost(pageable);
    }

    public Page<PostInfo> findMyAllPost(LifeLogUser lifeLogUser, Pageable pageable) {
        return postRepository.findMyAllPost(pageable, lifeLogUser.sub());
    }

    public PostInfo savePost(PostInfo postInfo) {
        return postRepository.savePost(postInfo);
    }

    public void updatePostById(LifeLogUser lifeLogUser, PostInfo postInfo) {
        checkIsOwner(lifeLogUser, postInfo.getId());

        if (!postRepository.updatePostById(postInfo)) {
            throw new PostDomainException(FAIL_UPDATE_POST);
        }
    }

    public boolean deletePostById(LifeLogUser lifeLogUser, Long postId) {
        checkIsOwner(lifeLogUser, postId);

        return postRepository.deletePostById(postId);
    }

    /**
     * 요청을 보낸 주체가 게시글의 소유주인지 확인
     * @param lifeLogUser 현재 로그인 유저
     * @param postId 게시글 아이디
     * @return 게시글 Info 객체
     */
    private void checkIsOwner(LifeLogUser lifeLogUser, Long postId) {
        PostInfo postInfo = findPostById(postId);
        String postOwner = postInfo.getUserInfo().getSub();

        if (!postOwner.equals(lifeLogUser.sub())) {
            throw new PostDomainException(NOT_POST_OWNER);
        }
    }
}
