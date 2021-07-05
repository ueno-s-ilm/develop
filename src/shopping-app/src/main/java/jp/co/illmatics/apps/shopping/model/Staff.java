package jp.co.illmatics.apps.shopping.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Table(name="staff")
@Entity
public class Staff {

  @Id 
  @Column(name="emp_id")
  private Long id;

  @Column(name="staff_name")
  private String name;

  public void setId(Long id){
    this.id = id;
  }
  public Long getId(){
    return this.id;
  }

  public void setName(String  name){
    this.name = name;
  }

  public String getName(){
    return this.name;
  }
}