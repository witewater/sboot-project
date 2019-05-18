package cn.wendong.admin.common.base;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service基类
 * 
 * @author MB
 * @date 2018-12-02
 */
@Transactional(rollbackFor = Exception.class)
public abstract class BaseService<T, ID extends Serializable> {

	@PersistenceContext
    protected EntityManager em;
	
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
}
