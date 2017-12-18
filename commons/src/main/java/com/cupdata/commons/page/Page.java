package com.cupdata.commons.page;

import com.cupdata.commons.constant.PageConstant;

/**
 * 
* @ClassName: Page 
* @Description:分页查询page 
* @author LinYong 
* @date 2017年3月20日 下午10:07:31 
*
 */
public class Page {
	/**
	 * 是否有前一页
	 */
    private boolean hasPrePage;

    /**
     * 是否有下一页
     */
    private boolean hasNextPage;
    
    /**
     * 总记录数
     */
    private long totalRecords;

    /**
     * 总页数
     */
    private int totalPages;
    
    /**
     * 每页记录数
     */
    private int pageSize = PageConstant.DEFAULT_PAGE_SIZE;
    
    /**
     * 开始记录编号。
     * 从0开始
     */
    private int offset;

    /**
     * 当前页。
     * 从1开始
     */
    private int currentPage;

    /** The default constructor */
    public Page() {
        setCurrentPage(1);
    }

    /**
     * 
     * @param currentPage
     * @param totalRecords
     */
    public void setPage(Integer currentPage, Integer totalRecords, Integer pageSize){
    	//设置默认值
    	setHasPrePage(false);
    	setHasNextPage(false);
    	
    	//设置默认pageSize
    	if(null == pageSize || pageSize <= 0){
    		pageSize = PageConstant.DEFAULT_PAGE_SIZE;
    	}
    	
    	//计算totalPages
    	if (totalRecords % pageSize == 0){
            totalPages = totalRecords / pageSize;
        }else{
            totalPages = totalRecords / pageSize + 1;
        }
    	
    	//计算currentPage
    	if (totalPages == 0) {
            currentPage = 0;
        } else if (currentPage <= 0) {
            currentPage = 1;
        } else if (currentPage > totalPages && totalPages != 0) {
            currentPage = totalPages;
        }
    	
    	//计算hasPrePage，hasNextPage
    	 if (currentPage > 1) {
             hasPrePage = true;
         } else {
             hasPrePage = false;
         }
         if (currentPage < this.totalPages) {
             hasNextPage = true;
         } else {
             hasNextPage = false;
         }
    	
    	setHasPrePage(hasPrePage);
    	setHasNextPage(hasNextPage);
    	setCurrentPage(currentPage);
    	setTotalRecords(totalRecords);
    	setTotalPages(totalPages);
    	setPageSize(pageSize);
    }
    
    /**
     *
     * @param pCurrentPage pCurrentPage
     * @param pTotalPage pTotalPage
     * @param pTotalRecords pTotalRecords
     */
   /*
    public void setPage(int pCurrentPage, int pTotalPage, int pTotalRecords)
    {
        if (pTotalPage == 0)
        {
            this.currentPage = 0;
        }
        else if (pCurrentPage <= 0)
        {
            this.currentPage = 1;
        }
        else if (pCurrentPage > pTotalPage && pTotalPage != 0)
        {
            this.currentPage = pTotalPage;
        }
        else
        {
            this.currentPage = pCurrentPage;
        }
        this.totalPages = pTotalPage;
        this.totalRecords = pTotalRecords;
        setCurrentPage(this.currentPage);
    }
*/
    /**
     * @param currentPage The currentPage to set.
     */
    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
        if (currentPage == 0) {
            this.offset = 0;
        } else {
            this.offset = (currentPage - 1) * pageSize;
        }
        
        if (currentPage > 1) {
            this.hasPrePage = true;
        } else {
            this.hasPrePage = false;
        }
        
        if (currentPage < this.totalPages) {
            this.hasNextPage = true;
        } else {
            this.hasNextPage = false;
        }
    }

    /**
     * Use the origin page to create a new page.
     *
     * @param trs totalRecords
     */
    public void setPage(int trs)
    {
        if (trs % pageSize == 0)
        {
            this.totalPages = trs / pageSize;
        }
        else
        {
            this.totalPages = trs / pageSize + 1;
        }
        this.totalRecords = trs;
        if (this.totalPages == 0)
        {
            this.currentPage = 0;
        }
        else if (this.currentPage <= 0)
        {
            this.currentPage = 1;
        }
        else if (this.currentPage > this.totalPages && this.totalPages != 0)
        {
            this.currentPage = this.totalPages;
        }
        setCurrentPage(this.currentPage);
    }

    /**
     * @return Returns the offset.
     */
    public int getOffset()
    {
        return offset;
    }

    /**
     * @param offset The offset to set.
     */
    public void setOffset(int offset)
    {
        this.offset = offset;
    }

    /**
     * @return Returns the currentPage.
     */
    public int getCurrentPage()
    {
        return currentPage;
    }

    public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
     * @return Returns the hasNextPage.
     */
    public boolean getHasNextPage()
    {
        return hasNextPage;
    }

    /**
     * @param hasNextPage The hasNextPage to set.
     */
    public void setHasNextPage(boolean hasNextPage)
    {
        this.hasNextPage = hasNextPage;
    }

    /**
     * @return Returns the hasPrePage.
     */
    public boolean getHasPrePage()
    {
        return hasPrePage;
    }

    /**
     * @param hasPrePage The hasPrePage to set.
     */
    public void setHasPrePage(boolean hasPrePage)
    {
        this.hasPrePage = hasPrePage;
    }

    /**
     * @return Returns the totalPage.
     */
    public int getTotalPages()
    {
        return totalPages;
    }

    /**
     * @param totalPage The totalPage to set.
     */
    public void setTotalPages(int totalPages)
    {
        this.totalPages = totalPages;
    }

    /**
     * @return totalRecords
     */
    public long getTotalRecords()
    {
        return this.totalRecords;
    }

    /**
     * @param pTotalRecords pTotalRecords
     */
    public void setTotalRecords(long pTotalRecords)
    {
        this.totalRecords = pTotalRecords;
    }
}