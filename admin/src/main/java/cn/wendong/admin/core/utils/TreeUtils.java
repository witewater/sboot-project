package cn.wendong.admin.core.utils;

import java.util.ArrayList;
import java.util.List;

import cn.wendong.admin.common.base.TreeEntity;

/**
 * tree grid 的组件
 * @author MB yangtdo@qq.com
 * @date 2019-01-04
 * @param <T>
 */
public class TreeUtils<T extends TreeEntity<T>> {

	/**
	 * 默认跟节点
	 */
	public static final Long DEFAULT_PARENTID = 0L;
	
	public List<T> treeGridList(List<T> list) {
		List<T> result = this.nextLevelTreeList(list, DEFAULT_PARENTID);
		//没有匹配到的，如父节点不是0 的； 搜索场景
		for(T t :list) {
			if (!result.contains(t)) {
				result.add(t);
			}
		}
		return result;
	}
	
	private List<T> nextLevelTreeList(List<T> list, Long parentId){
		List<T> result = new ArrayList<T>();
		for (T t : list) {//
			if (parentId.equals(t.getPid())) {// 从跟节点开始
				result.add(t);
				result.addAll(this.nextLevelTreeList(list, t.getId()));
			}
		}
		return result;
	}
	
	public void setParent(T t,T parent) {
		if (t.getPid() == null) {
			t.setPid(DEFAULT_PARENTID);
			t.setPids(DEFAULT_PARENTID + ",");
		} else {
			if (null != parent) {
				t.setPid(parent.getId());
				t.setPids(parent.getPids() + parent.getId());
			}
		}
	}
	
}