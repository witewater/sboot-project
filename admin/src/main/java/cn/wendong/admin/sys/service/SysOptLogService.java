package cn.wendong.admin.sys.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import cn.wendong.admin.common.base.BaseSpecification;
import cn.wendong.admin.sys.entity.SysOptLog;

public interface SysOptLogService {

    Page<SysOptLog> getPageList(Example<SysOptLog> example, Integer pageIndex, Integer pageSize);

    SysOptLog getId(Long id);

    List<SysOptLog> getDataLogList(String model, Long recordId);

    @Transactional
    SysOptLog save(SysOptLog actionLog);

    @Transactional
    void deleteId(Long id);

	Page<SysOptLog> getPageList(BaseSpecification<SysOptLog> sysLog, PageRequest pageable);

    /*@Transactional
    void emptyLog();*/
}
