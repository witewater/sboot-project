package cn.wendong.admin.sys.service.impl;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wendong.admin.common.base.BaseSpecification;
import cn.wendong.admin.sys.entity.SysOptLog;
import cn.wendong.admin.sys.repository.SysOptLogRepository;
import cn.wendong.admin.sys.service.SysOptLogService;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
@Service
public class SysOptLogServiceImpl implements SysOptLogService {

    @Autowired
    private SysOptLogRepository sysOptLogRepository;

    /**
     * 根据日志ID查询日志数据
     * @param id 日志ID
     */
    @Override
    public SysOptLog getId(Long id) {
        return sysOptLogRepository.findById(id).get();
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @param pageIndex 页码
     * @param pageSize 获取列表长度
     * @return 返回分页数据
     */
    @Override
    public Page<SysOptLog> getPageList(Example<SysOptLog> example, Integer pageIndex, Integer pageSize) {
        // 创建分页对象
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        PageRequest page = PageRequest.of(pageIndex-1, pageSize, sort);
        return sysOptLogRepository.findAll(example, page);
    }

    /**
     * 获取数据的日志列表
     * @param model 模型（表名）
     * @param recordId 数据ID
     */
    @Override
    public List<SysOptLog> getDataLogList(String model, Long recordId){
        return sysOptLogRepository.findByModelAndRecordId(model, recordId);
    }

    /**
     * 保存日志
     * @param actionLog 日志实体类
     */
    @Override
    @Transactional
    public SysOptLog save(SysOptLog actionLog){
        return sysOptLogRepository.save(actionLog);
    }

    /**
     * 删除指指定ID日志
     */
    @Override
    @Transactional
    public void deleteId(Long id){
        sysOptLogRepository.deleteById(id);
    }

	@Override
	public Page<SysOptLog> getPageList(BaseSpecification<SysOptLog> sysLog, PageRequest pageable) {
		Page<SysOptLog> page = sysOptLogRepository.findAll(sysLog, pageable);
		return page;
	}

    
//    /**
//     * 清空日志
//     */
//    @Override
//    @Transactional
//    public void emptyLog(){
//        sysOptLogRepository.deleteAll();
//    }
    
    
    
}
