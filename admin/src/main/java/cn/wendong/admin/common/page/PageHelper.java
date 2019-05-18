package cn.wendong.admin.common.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.domain.Page;

/**
 * bootstrap-table数据封装
 * 加入rows和total字段 必须是这两个
 * @author MB yangtdo@qq.com
 * @date 2019-01-31
 * @param <T>
 */
public class PageHelper<T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4388109661986605756L;
	// 实体类集合
	private List<T> rows = new ArrayList<T>();
	// 数据总条数
	private Long total;
    //总页码
	private int pages = 1;//默认一页
	//页面大小，每页显示多少条
	private int limit;
    //页码 从第几条开始查询
    private int offset ;
    //排序
    private String order ="asc";
	
	public PageHelper(Long total,List<T> rows) {
		this.total = total;
		this.rows.addAll(rows);
	}

	public PageHelper(Page<T> page){
		this.total = page.getTotalElements();//数据总数
		this.rows.addAll(page.getContent());
	}
	
	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	@Override
	public String toString() {
		 return ReflectionToStringBuilder.toString(this);
	}
	
}