package com.image.Repository;

import com.image.Model.TableModel.ImageDO;
import com.image.Model.TableModel.ObjectDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectRepository extends JpaRepository<ObjectDO, Integer> {
}
