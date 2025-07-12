package com.desirArman.blog.repositories;

import com.desirArman.blog.domain.entities.Like;
import com.desirArman.blog.domain.entities.Post;
import com.desirArman.blog.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {

    int countByPost(Post post);

    Optional<Like> findByPostAndAuthor(Post post, User currentUser);
}
