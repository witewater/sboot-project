package cn.wendong.admin.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.wendong.admin.common.base.BaseSpecification;
import cn.wendong.admin.core.enums.StatusEnum;
import cn.wendong.admin.sys.entity.SysDict;
import cn.wendong.admin.sys.repository.SysDictRepository;
import cn.wendong.admin.sys.service.SysDictService;

/**
 * 
 * @author MB
 * @date 2018-12-02
 */
@Service
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictRepository sysDictRepository;

    /**
     * 根据字典ID查询字典数据
     * @param id 字典ID
     */
    @Override
    public SysDict getId(Long id) {
        Byte[] status = {StatusEnum.NORMAL.getCode(), StatusEnum.FREEZED.getCode()};
        return sysDictRepository.findByIdAndStatusIn(id, status);
    }

    /**
     * 根据字典标识获取字典数据
     * @param name 字典标识
     */
    @Override
    public SysDict getName(String name){
        return sysDictRepository.findByNameAndStatus(name, StatusEnum.NORMAL.getCode());
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @param pageIndex 页码
     * @param pageSize 获取列表长度
     * @return 返回分页数据
     */
    @Override
    public Page<SysDict> getPageList(Example<SysDict> example, Integer pageIndex, Integer pageSize) {
        // 创建分页对象
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        PageRequest page = PageRequest.of(pageIndex-1, pageSize, sort);
        return sysDictRepository.findAll(example, page);
    }

    /**
     * 保存字典
     * @param dict 字典实体类
     */
    @Override
    @Transactional
    public SysDict save(SysDict dict){
        return sysDictRepository.save(dict);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Integer updateStatus(StatusEnum statusEnum, List<Long> idList){
        return sysDictRepository.updateStatus(statusEnum.getCode(),idList);
    }

	@Override
	public List<SysDict> findByType(String type) {
		Assert.hasLength(type, "type 不能为空");
		return sysDictRepository.findByType(type);
	}

	@Override
	public List<String> findTypes() {
		return sysDictRepository.findDistinctByType();
	}

	@Override
	public Page<SysDict> getPageList(BaseSpecification<SysDict> sysDict, Pageable pageable) {
		Page<SysDict> page = sysDictRepository.findAll(sysDict, pageable);
		return page;
	}

	
}
