package com.owenl.image.Repository;

import com.owenl.image.Model.TableModel.ObjectDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectRepository extends JpaRepository<ObjectDO, Integer> {



}
