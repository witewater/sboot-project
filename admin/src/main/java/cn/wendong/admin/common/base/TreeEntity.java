package cn.wendong.admin.common.base;

import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * 树形结构基础类
 * @author MB yangtdo@qq.com
 * @date 2019-02-17
 * @param <T>
 */
@Getter
@Setter
@MappedSuperclass
public abstract class TreeEntity<T> extends BaseEntity<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1415841235811156677L;

	/**
	 * 父部门
	 */
	private Long pid;
	/**
	 * 父ids，按层级逗号分隔
	 */
	private String pids;
	/**
	 * 排序
	 */
	private Integer sort;
	
}
