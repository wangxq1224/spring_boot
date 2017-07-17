package com.demo.sboot.page;


/**
 * 
 * All rights Reserved, Designed By www.jovision.com
 *
 * Description:    TODO(页数据存放对象。请求数据中只包含请求第几个，即currentPage)
 *   
 * Created by wangxq on 2017年4月28日 下午1:48:55  
 *
 * Changelog:
 * 
 *
 */
public class Page {

	/**
	 * 每页显示数量
	 */
	private int pageSize = 2; 
	/**
	 * 当前记录位置
	 */
	private int currentRecord; 
	/**
	 * 当前页码
	 */
	private int currentPage; 
	/**
	 * 总记录数
	 */
	private int totalRecord; 
	/**
	 * 总页码
	 */
	private int totalPage; 
	private DataMap data = null;

	public Page() {
		data = new DataMap();
	}

	// 初始化每页显示数量
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	// 获取当前记录所在行数
	public int getCurrentRecord() {
		currentRecord = (getCurrentPage() - 1) * pageSize;
		if (currentRecord < 0) {
			currentRecord = 0;
		}
		return currentRecord;
	}

	public void setCurrentRecord(int currentRecord) {
		this.currentRecord = currentRecord;
	}

	// 获取当前页码，处理首页、尾页
	public int getCurrentPage() {
		if (currentPage < 0) {
			currentPage = 0;
		}
		if (currentPage > getTotalPage()) {
			currentPage = getTotalPage();
		}
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	// 获取总页数
	public int getTotalPage() {
		if (getTotalRecord() % pageSize == 0) {
			totalPage = getTotalRecord() / pageSize;
		} else if (getTotalRecord() % pageSize > 0) {
			totalPage = getTotalRecord() / pageSize + 1;
		}
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public DataMap getData() {
		return data;
	}

	public void setData(DataMap data) {
		this.data = data;
	}

}
