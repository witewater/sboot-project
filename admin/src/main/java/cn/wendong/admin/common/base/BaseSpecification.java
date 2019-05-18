package cn.wendong.admin.common.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

/**
 * 简化Specification写法 这里对字符串进行模糊查询
 * 
 * @author MB yangtdo@qq.com
 * @date 2019-02-01
 * @param <T>
 */
public class BaseSpecification<T> implements Specification<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 638199679052632075L;

	private Map<String, Object> param = new HashMap<String, Object>(1);

	private Class<T> clazz;

	public BaseSpecification(Map<String, Object> param, Class<T> clazz) {
		this.param = param;
		this.clazz = clazz;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		Field[] fields = clazz.getDeclaredFields();
		List<Predicate> predicates = new ArrayList<>();
		try {
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				String name = fields[i].getName();
				if (param.containsKey(name)) {
					if (ObjectUtils.isEmpty(param.get(name))) {
						continue;
					}
					// 对于字符串类型进行模糊查询，其他值精确匹配
					if (fields[i].getGenericType().toString().equals("class java.lang.String")) {
						predicates.add(builder.like(root.get(name), "%" + param.get(name) + "%"));
					} else {
						predicates.add(builder.equal(root.get(name), param.get(name)));
					}
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("获取数据错误，请检查实体类Bean格式是否规范！", e);
		}

		// 方法一：
		// return builder.and(predicates.toArray(new
		// Predicate[predicates.size()]));
		Predicate[] pres = new Predicate[predicates.size()];
		// 方法二：
		// 把Predicate应用到CriteriaQuery中去,因为还可以给CriteriaQuery添加其他的功能，比如排序、分组等
		query.where(predicates.toArray(pres));
		// 可以添加排序功能
		query.orderBy(builder.desc(root.get("id").as(Long.class)));
		return query.getRestriction();
	}

}
