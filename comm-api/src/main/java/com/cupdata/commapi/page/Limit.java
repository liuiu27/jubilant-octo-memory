package com.cupdata.commapi.page;

import com.cupdata.commapi.constant.PageConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * 
* @ClassName: Limit 
* @Description: 该类用来在构造查询时使用，包含过滤条件和排序条件
* @author LinYong 
* @date 2017年3月21日 上午8:45:05 
*
 */
public class Limit{
	/**
	 * 过滤条件 以<key，value>保存，key值以前缀开头如:GR_列名，表示条件是该列值要大于value值
	 * key值的形式有like_;LE_;NEQ_;EQ_;等
	 */
	private Map<String, Object> filter;

	/**
	 * 排序条件 以<key，value>保存,key--排序的列名如 s_ware_name， 例如，value--排序的顺序,升序或降序
	 */
	private Map<String, String> sort;

	/** groupBy 字段名称 * */
	private String groupBy;

	/**
	 * 默认构造函数
	 */
	public Limit()
	{
		filter = new HashMap<String, Object>();
		sort = new HashMap<String, String>();
	}

	/**
	 * @return Returns the filter.
	 */
	public Map getFilter()
	{
		return filter;
	}

	/**
	 * @param filter
	 *            The filter to set.
	 */
	public void setFilter(Map<String, Object> filter)
	{
		this.filter = filter;
	}

	/**
	 * @return Returns the sort.
	 */
	public Map getSort()
	{
		return sort;
	}

	/**
	 * @param sort
	 *            The sort to set.
	 */
	public void setSort(Map<String, String> sort)
	{
		this.sort = sort;
	}

	/**
	 * 添加排序顺序
	 * 
	 * @param columnName
	 *            排序列名
	 * @param orderKind
	 *            排序的次序 ASC 或者DESC
	 */
	public void addSort(String columnName, String orderKind)
	{
		// this.sort.clear();
		this.sort.put(columnName, orderKind);
	}

	/**
	 * 添加过滤条件
	 * 
	 * @param filterName
	 *            过滤字段名称
	 * @param value
	 *            过滤值
	 */
	public void addFilter(String filterName, Object value)
	{
		this.filter.put(filterName, value);
	}

	/**
	 * 分析map得到过滤条件的sql形式语句
	 * 
	 * @return String
	 */
	public String getFilterString()
	{
		if (filter == null || filter.isEmpty())
		{
			return null;
		}
		StringBuffer sb = new StringBuffer();
		Iterator iterator = filter.entrySet().iterator();
		String key = null;
		while (iterator.hasNext())
		{
			if (StringUtils.isNotEmpty(key))
			{
				sb.append(" AND ");
			}
			Entry entry = (Entry) iterator.next();
			key = (String) entry.getKey();
			if (key.startsWith(PageConstant.GR_SQL))
			{
				sb.append(removePrefix(key, PageConstant.GR_SQL)).append(
						" >=? ");
			} else if (key.startsWith(PageConstant.LE_SQL))
			{
				sb.append(removePrefix(key, PageConstant.LE_SQL)).append(
						" <=? ");
			} else if (key.startsWith(PageConstant.GREAT_SQL))
			{
				sb.append(removePrefix(key, PageConstant.GREAT_SQL)).append(
						" >? ");
			} else if (key.startsWith(PageConstant.LESS_SQL))
			{
				sb.append(removePrefix(key, PageConstant.LESS_SQL)).append(
						" <? ");
			} else if (key.startsWith(PageConstant.LIKE_SQL))
			{
				sb.append(removePrefix(key, PageConstant.LIKE_SQL)).append(
						" LIKE ? ");
			} else if (key.startsWith(PageConstant.NEQ_SQL))
			{
				sb.append(removePrefix(key, PageConstant.NEQ_SQL)).append(
						" !=? ");
			} else if (key.startsWith(PageConstant.EQ_SQL))
			{
				sb.append(removePrefix(key, PageConstant.EQ_SQL))
						.append(" =? ");
			}
		}
		return sb.toString();
	}

	/**
	 * 去前缀
	 * 
	 * @param value
	 *            要去前缀的字符串
	 * @param prefix
	 *            前缀
	 * @return String
	 */
	public String removePrefix(String value, String prefix)
	{
		return value.substring(prefix.length());
	}

	/**
	 * 分析map得到排序条件的sql语句
	 * 
	 * @return String
	 */
	public String getSortString()
	{
		if (sort == null || sort.isEmpty())
		{
			return null;
		}
		Iterator iterator = sort.entrySet().iterator();
		StringBuffer sb = new StringBuffer();
		sb.append(PageConstant.ORDER_BY);
		String key = null;
		while (iterator.hasNext())
		{
			if (StringUtils.isNotEmpty(key))
			{
				sb.append(", ");
			}
			Entry entry = (Entry) iterator.next();
			key = (String) entry.getKey();
			sb
					.append(
							(key).substring(PageConstant.PREFIX_SORT
									.length())).append(" ");
			sb.append((String) entry.getValue());
		}
		return sb.toString();
	}

	/**
	 * @return Returns the groupBy.
	 */
	public String getGroupBy()
	{
		return groupBy;
	}

	/**
	 * @param groupBy
	 *            The groupBy to set.
	 */
	public void setGroupBy(String groupBy)
	{
		this.groupBy = groupBy;
	}
}

