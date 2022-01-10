package com.hospyboard.api.hospital.respository;

import com.hospyboard.api.hospital.entity.Tool;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRepository extends CrudRepository<Tool, Long> {
}
