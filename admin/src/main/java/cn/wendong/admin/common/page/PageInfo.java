package cn.wendong.admin.common.page;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * 
 * @author mubai
 *
 *         2017年12月28日 上午10:41:07
 */
public class PageInfo<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 当前页
	private int pageNum = 1;
	// 每页的数量
	private int pageSize = 30;
	// 总记录数
	private long total;
	// 总页数
	private int pages;
	// 结果集
	private List<T> list;

	// 是否为第一页
	private boolean isFirstPage = false;
	// 是否为最后一页
	private boolean isLastPage = false;

	// 首页
	private int firstPage;
	// 上一页
	private int prevPage;
	// 下一页
	private int nextPage;
	// 尾页
	private int lastPage;

	private int showTag = 5;// 显示页面长度
	private int slideTag = 1;// 前后显示页面长度

	public PageInfo() {
	}

	/**
	 * 包装Page对象
	 *
	 * @param list
	 */
	public PageInfo(List<T> list) {
		if (list instanceof Collection) {
			this.pageNum = 1;
			this.pageSize = list.size();

			this.pages = 1;
			this.list = list;
			this.total = list.size();

		}
		// 初始化参数
		initialize();
		// 判断页面边界
		judgePageBoudary();
	}

	/**
	 * 初始化参数
	 */
	public void initialize() {

		this.firstPage = 1;

		this.lastPage = (int) (total / this.pageSize + firstPage - 1);

		if (this.total % this.pageSize != 0 || this.lastPage == 0) {
			this.lastPage++;
		}

		if (this.lastPage < this.firstPage) {
			this.lastPage = this.firstPage;
		}

		if (this.pageNum <= 1) {
			this.pageNum = this.firstPage;
			this.isFirstPage = true;
		}

		if (this.pageNum >= this.lastPage) {
			this.pageNum = this.lastPage;
			this.isLastPage = true;
		}

		if (this.pageNum < this.lastPage - 1) {
			this.nextPage = this.pageNum + 1;
		} else {
			this.nextPage = this.lastPage;
		}

		if (this.pageNum > 1) {
			this.prevPage = this.pageNum - 1;
		} else {
			this.prevPage = this.firstPage;
		}

		if (this.pageNum < this.firstPage) {// 如果当前页小于首页
			this.pageNum = this.firstPage;
		}

		if (this.pageNum > this.lastPage) {// 如果当前页大于尾页
			this.pageNum = this.lastPage;
		}
	}

	/**
	 * 判定页面边界
	 */
	private void judgePageBoudary() {
		this.isFirstPage = pageNum == 1;
		this.isLastPage = pageNum == pages;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public boolean isIsFirstPage() {
		return isFirstPage;
	}

	public void setIsFirstPage(boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public boolean isIsLastPage() {
		return isLastPage;
	}

	public void setIsLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// 当前页为首页
		if (pageNum == firstPage) {
			sb.append(
					"<li class=\"paginItem disabled\"><a href=\"javascript:;\" style=\"display:block;width:60px;\">&#171; 上一页</a></li>\n");
		} else {
			sb.append("<li class=\"paginItem\"><a href=\"javascript:;\" onclick=\"page(" + 1 + "," + pageSize
					+ ");\">首页</a></li>\n");
			sb.append(
					"<li class=\"paginItem\"><a href=\"javascript:;\" style=\"display:block;width:60px;\" onclick=\"page("
							+ prevPage + "," + pageSize + ");\">&#171; 上一页</a></li>\n");
		}

		int begin = pageNum - (showTag / 2);

		if (begin < firstPage) {
			begin = firstPage;
		}

		int end = begin + showTag - 1;

		if (end >= lastPage) {
			end = lastPage;
			begin = end - showTag + 1;
			if (begin < firstPage) {
				begin = firstPage;
			}
		}

		if (begin > firstPage) {
			int i = 0;
			for (i = firstPage; i < firstPage + slideTag && i < begin; i++) {
				sb.append("<li class=\"paginItem\"><a href=\"javascript:;\" onclick=\"page(" + i + "," + pageSize
						+ ");\">" + (i + 1 - firstPage) + "</a></li>\n");
			}
			if (i < begin) {
				sb.append("<li class=\"paginItem disabled more\"><a href=\"javascript:;\">...</a></li>\n");
			}
		}

		for (int i = begin; i <= end; i++) {
			if (pageNum == i) {
				sb.append("<li class=\"paginItem current\"><a href=\"javascript:;\">" + (i + 1 - firstPage)
						+ "</a></li>\n");
			} else {
				sb.append("<li class=\"paginItem\"><a href=\"javascript:;\" onclick=\"page(" + i + "," + pageSize
						+ ");\">" + (i + 1 - firstPage) + "</a></li>\n");
			}
		}

		if (lastPage - end > slideTag) {
			sb.append("<li class=\"paginItem disabled more\"><a href=\"javascript:;\">...</a></li>\n");
			end = lastPage - slideTag;
		}

		for (int i = end + 1; i <= lastPage; i++) {
			sb.append("<li class=\"paginItem\"><a href=\"javascript:;\" onclick=\"page(" + i + "," + pageSize + ");\">"
					+ (i + 1 - firstPage) + "</a></li>\n");
		}

		if (pageNum == lastPage) {
			sb.append(
					"<li class=\"paginItem disabled\"><a href=\"javascript:;\" style=\"display:block;width:60px;\">下一页 &#187;</a></li>\n");
		} else {
			sb.append(
					"<li class=\"paginItem\"><a href=\"javascript:;\" style=\"display:block;width:60px;\" onclick=\"page("
							+ nextPage + "," + pageSize + ");\">" + "下一页 &#187;</a></li>\n");
			sb.append("<li class=\"paginItem\"><a href=\"javascript:;\" onclick=\"page(" + pages + "," + pageSize
					+ ");\">" + "尾页</a></li>\n");
		}

		sb.insert(0, "<ul class=\"paginList\">\n").append("</ul>\n");

		sb.insert(0,
				"<div class=\"message\">共&nbsp;" + "<i class=\"blue\">" + total
						+ "</i>&nbsp;条记录，当前显示第&nbsp;<input type=\"number\" value=" + pageNum
						+ " style=\"width: 50px;text-align:center;\" id=\"toGoPage\"  onblur=\"toTZ();\" />"
						+ "</i>&nbsp;页，每页显示&nbsp;<input type=\"number\" value=" + pageSize
						+ " style=\"width: 50px;text-align:center;\" id=\"toPageSize\" onblur=\"toPageSize();\"/>&nbsp;条记录&nbsp;"
						+ "</div>\n");
		// 添加函数
		sb.append("<script type=\"text/javascript\">\n");
		// 总页码函数
		sb.append("function toPageSize(){");
		sb.append("var toPageSize = document.getElementById(\"toPageSize\").value;");
		sb.append("if(toPageSize == ''){document.getElementById(\"toPageSize\").value=10;return;}");
		sb.append("if(isNaN(Number(toPageSize))){document.getElementById(\"toPageSize\").value=10;return;}");
		sb.append("page(" + 1 + ",toPageSize);");
		sb.append("}\n");

		// 跳转函数
		sb.append("function toTZ(){");
		sb.append("var toPageVlue = document.getElementById(\"toGoPage\").value;");
		sb.append("if(toPageVlue == ''){document.getElementById(\"toGoPage\").value=1;return;}");
		sb.append("if(isNaN(Number(toPageVlue))){document.getElementById(\"toGoPage\").value=1;return;}");
		sb.append("page(toPageVlue," + pageSize + ");");
		sb.append("}\n");
		sb.append("</script>\n");

		return sb.toString();
	}

	/**
	 * 分页是否有效
	 * 
	 * @return this.pageSize==-1
	 */
	@JsonIgnore
	public boolean isDisabled() {
		return this.pageSize == -1;
	}
}
