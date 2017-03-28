package com.manev.quislisting.repository.post;

import com.manev.quislisting.domain.post.discriminator.Attachment;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AttachmentRepository extends PostRepository<Attachment> {
}
