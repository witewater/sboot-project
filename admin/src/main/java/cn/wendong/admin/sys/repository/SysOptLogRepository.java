package cn.wendong.admin.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.wendong.admin.sys.entity.SysOptLog;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
public interface SysOptLogRepository extends JpaRepository<SysOptLog, Long>,JpaSpecificationExecutor<SysOptLog> {

    /**
     * 根据模型和数据ID查询日志列表
     * @param model 模型（表名）
     * @param recordId 数据ID
     */
    public List<SysOptLog> findByModelAndRecordId(String model, Long recordId);
}
