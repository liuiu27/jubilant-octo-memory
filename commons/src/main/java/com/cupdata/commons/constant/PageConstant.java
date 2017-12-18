package com.cupdata.commons.constant;

public class PageConstant {

	/** sql关键词 like * */
	public static final String LIKE_SQL = "like_";

	/** sql关键词 order by * */
	public static final String ORDER_BY = " order by ";

	/** sql关键词 group by * */
	public static final String GROUP_BY = " group by ";

	/** sql比较关系 大于 等于 * */
	public static final String GR_SQL = "gr_";

	/** sql比较关系 小于 等于 * */
	public static final String LE_SQL = "le_";

	/** sql比较关系 大于 * */
	public static final String GREAT_SQL = "great_";

	/** sql比较关系 小于 * */
	public static final String LESS_SQL = "less_";

	/** sql比较关系 不等于 * */
	public static final String NEQ_SQL = "neq_";

	/** sql比较关系 等于 * */
	public static final String EQ_SQL = "eq_";

	/** sql 过滤条件 日期 * */
	public static final String PREFIX_DATE = "f_date_";

	/** sql 过滤条件 int或者Integer * */
	public static final String PREFIX_INT = "f_int_";

	/** sql 过滤条件 long 或者 Long * */
	public static final String PREFIX_LONG = "f_long_";

	/** sql 过滤条件 double 或者 Double * */
	public static final String PREFIX_DOUBLE = "f_double_";

	/** sql 排序前缀 s_ * */
	public static final String PREFIX_SORT = "s_";

	/** sql 过滤条件前缀 f_ * */
	public static final String PREFIX_FILTER = "f_";
	
	/**
	 * 每页的记录数
	 */
	public final static int DEFAULT_PAGE_SIZE = 20;
	
	/**
	 * 默认当前页
	 */
	public static final Integer DEFAULT_CURRENT_PAGE = 1;
	
	/**
	 * 默认分页查询位移
	 */
	public static final Integer DEFAULT_OFFSET = 0;
	
	
	/**
	 * 起始行号（从0开始）
	 */
	public static final String OFFSET = "OFFSET";
	
	/**
	 * 页面记录数
	 */
	public static final String PAGE_SIZE = "PAGE_SIZE";
	
	/**
	 * 总记录数
	 */
	public static final String TOTAL_RECORDS = "TOTAL_RECORDS";
	
	/**
	 * 当前页
	 */
	public static final String CURRENT_PAGE = "CURRENT_PAGE";
	
	/**
	 * 总页数
	 */
	public static final String TOTAL_PAGES = "TOTAL_PAGES";
}
