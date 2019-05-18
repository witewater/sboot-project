package cn.wendong.admin.common.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树形节点类型
 * 
 * @author MB yangtdo@qq.com
 * @date 2018-12-06
 */
public class TreeNode {

	private String id;

	private Integer dataId;// 数据id

	private Integer parentId;// 父id

	private String parentIds;//父ids，按层级逗号分隔
	
	private String text;//文本

	private String url;//地址

	private String state;// 判断节点是否打开 closed,open

	private boolean checked; // 判断节点是否选中

	private Map<String, Object> attributes = new HashMap<String, Object>();

	private List<TreeNode> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
}
