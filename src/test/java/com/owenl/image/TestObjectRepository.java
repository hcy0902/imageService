package com.owenl.image;

import com.owenl.image.Model.TableModel.ImageDO;
import com.owenl.image.Model.TableModel.ObjectDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestObjectRepository extends JpaRepository<ObjectDO, Integer> {

}
