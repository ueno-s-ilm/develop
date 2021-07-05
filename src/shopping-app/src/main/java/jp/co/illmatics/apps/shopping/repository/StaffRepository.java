package jp.co.illmatics.apps.shopping.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

import jp.co.illmatics.apps.shopping.model.Staff;

public interface StaffRepository extends CrudRepository<Staff, Long> {
  //
  Optional<Staff> findById(long id);
}