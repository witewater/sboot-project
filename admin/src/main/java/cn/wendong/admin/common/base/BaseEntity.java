package cn.wendong.admin.common.base;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 将父类属性继承到子类 并非是一个完整的实体类
 * 此类不能再标注@Entity或@Table注解，也无需实现序列化接口
 * @author MB
 * @date 2018-12-02
 * @param <T>
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -777685537264987328L;

	/**
	 *  //JPA提供的四种标准用法为TABLE,SEQUENCE,IDENTITY,AUTO.
	 *  //TABLE：使用一个特定的数据库表格来保存主键。
	 *  //SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。
	 *  //IDENTITY：主键由数据库自动生成（主要是自动增长型）
	 *  //AUTO：主键由程序控制。
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//mysql可以用IDENTITY主键生成策略, oracle就得用SEQUENCE主键生成策略
	protected Long id;
	
	@CreatedDate
	@Column(name = "create_date", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)//生成yyyy-MM-dd HH:mm:ss类型的日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//出参时间格式化
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//入参时，请求报文只需要传入yyyymmddhhmmss字符串进来，则自动转换为Date类型数据
	private Date createDate;
	
	@LastModifiedDate
	@Column(name = "update_date")
	@Temporal(TemporalType.TIMESTAMP)//生成yyyy-MM-dd HH:mm:ss类型的日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //出参时间格式化
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//入参时，请求报文只需要传入yyyymmddhhmmss字符串进来，则自动转换为Date类型数据
	private Date updateDate;
	
	private String remark;//备注信息
	
	public BaseEntity() {
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!getClass().equals(obj.getClass())) {
			return false;
		}
		BaseEntity<?> that = (BaseEntity<?>) obj;
		return null == this.getId() ? false : this.getId().equals(that.getId());
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
