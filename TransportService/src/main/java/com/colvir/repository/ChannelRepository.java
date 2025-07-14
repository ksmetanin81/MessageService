package com.colvir.repository;

import com.colvir.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    @Query(value = """
               select ch from Channel ch, MessageCode mc
                where ch = mc.channel
                  and mc.messageCode = :messageCode
                  and ch.processingCode = :processingCode
            """)
    List<Channel> findByMessageCodeAndProcessingCode(String messageCode, String processingCode);
}
