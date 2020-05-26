package jp.co.illmatics.apps.shopping.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

import jp.co.illmatics.apps.shopping.model.Staff;

public interface StaffRepository extends CrudRepository<Staff, Long> {
  // 引数の id に一致するエンティティを取得。
  Optional<Staff> findById(long id);
}