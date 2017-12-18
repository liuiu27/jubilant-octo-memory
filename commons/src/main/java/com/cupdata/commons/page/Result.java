package com.cupdata.commons.page;

import java.util.List;

/**
 * 
* @ClassName: Result 
* @Description: 分页查询结果
* @author LinYong 
* @date 2017年3月21日 上午8:35:46 
*
 */
public class Result {
	/**
	 * 分页信息
	 */
	private Page page;

	/**
	 * 查询结果
	 */
	private List content;

	/**
	 * The default constructor
	 */
	public Result() {
		super();
	}

	/**
	 * The constructor using fields
	 * 
	 * @param pPage
	 *            pPage
	 * @param pContent
	 *            pContent
	 */
	public Result(Page pPage, List pContent) {
		this.page = pPage;
		this.content = pContent;
	}

	/**
	 * @return Returns the content.
	 */
	public List getContent() {
		return content;
	}

	/**
	 * @return Returns the page.
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * @param content
	 *            The content to set.
	 */
	public void setContent(List content) {
		this.content = content;
	}

	/**
	 * @param page
	 *            The page to set.
	 */
	public void setPage(Page page) {
		this.page = page;
	}
}