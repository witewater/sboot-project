package cn.wendong.admin.common.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 通用接口
 * @NoRepositoryBean表示该接口不会创建这个接口的实例
 * @author MB
 * @date 2018-12-02
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T,ID>, JpaSpecificationExecutor<T> {

    /**
     * 查找ID且状态正常
     *
     * @param id 主键ID
     * @param status 状态
     */
    public T findByIdAndStatus(Long id, Byte status);

    /**
     * 查找ID且排查状态
     *
     * @param id 主键ID
     * @param status 状态数组
     */
    public T findByIdAndStatusIn(Long id, Byte[] status);

    /**
     * 批量更新数据状态
     * #{#entityName} 实体类对象
     * @param status 状态
     * @param id ID列表
     * @return 更新数量
     */
    @Modifying
    @Transactional
    @Query("update #{#entityName} set status = ?1  where id in ?2")
    public Integer updateStatus(Byte status, List<Long> id);
}
